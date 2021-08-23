package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Mark;
import com.mycompany.myapp.repository.MarkRepository;
import com.mycompany.myapp.service.dto.MarkDTO;
import com.mycompany.myapp.service.mapper.MarkMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarkResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private MarkMapper markMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarkMockMvc;

    private Mark mark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mark createEntity(EntityManager em) {
        Mark mark = new Mark().libelle(DEFAULT_LIBELLE).deleted(DEFAULT_DELETED);
        return mark;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mark createUpdatedEntity(EntityManager em) {
        Mark mark = new Mark().libelle(UPDATED_LIBELLE).deleted(UPDATED_DELETED);
        return mark;
    }

    @BeforeEach
    public void initTest() {
        mark = createEntity(em);
    }

    @Test
    @Transactional
    void createMark() throws Exception {
        int databaseSizeBeforeCreate = markRepository.findAll().size();
        // Create the Mark
        MarkDTO markDTO = markMapper.toDto(mark);
        restMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(markDTO)))
            .andExpect(status().isCreated());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeCreate + 1);
        Mark testMark = markList.get(markList.size() - 1);
        assertThat(testMark.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testMark.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createMarkWithExistingId() throws Exception {
        // Create the Mark with an existing ID
        mark.setId(1L);
        MarkDTO markDTO = markMapper.toDto(mark);

        int databaseSizeBeforeCreate = markRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(markDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = markRepository.findAll().size();
        // set the field null
        mark.setLibelle(null);

        // Create the Mark, which fails.
        MarkDTO markDTO = markMapper.toDto(mark);

        restMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(markDTO)))
            .andExpect(status().isBadRequest());

        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = markRepository.findAll().size();
        // set the field null
        mark.setDeleted(null);

        // Create the Mark, which fails.
        MarkDTO markDTO = markMapper.toDto(mark);

        restMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(markDTO)))
            .andExpect(status().isBadRequest());

        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarks() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        // Get all the markList
        restMarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mark.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getMark() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        // Get the mark
        restMarkMockMvc
            .perform(get(ENTITY_API_URL_ID, mark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mark.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMark() throws Exception {
        // Get the mark
        restMarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMark() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        int databaseSizeBeforeUpdate = markRepository.findAll().size();

        // Update the mark
        Mark updatedMark = markRepository.findById(mark.getId()).get();
        // Disconnect from session so that the updates on updatedMark are not directly saved in db
        em.detach(updatedMark);
        updatedMark.libelle(UPDATED_LIBELLE).deleted(UPDATED_DELETED);
        MarkDTO markDTO = markMapper.toDto(updatedMark);

        restMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, markDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(markDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
        Mark testMark = markList.get(markList.size() - 1);
        assertThat(testMark.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testMark.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingMark() throws Exception {
        int databaseSizeBeforeUpdate = markRepository.findAll().size();
        mark.setId(count.incrementAndGet());

        // Create the Mark
        MarkDTO markDTO = markMapper.toDto(mark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, markDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(markDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMark() throws Exception {
        int databaseSizeBeforeUpdate = markRepository.findAll().size();
        mark.setId(count.incrementAndGet());

        // Create the Mark
        MarkDTO markDTO = markMapper.toDto(mark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(markDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMark() throws Exception {
        int databaseSizeBeforeUpdate = markRepository.findAll().size();
        mark.setId(count.incrementAndGet());

        // Create the Mark
        MarkDTO markDTO = markMapper.toDto(mark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(markDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarkWithPatch() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        int databaseSizeBeforeUpdate = markRepository.findAll().size();

        // Update the mark using partial update
        Mark partialUpdatedMark = new Mark();
        partialUpdatedMark.setId(mark.getId());

        partialUpdatedMark.libelle(UPDATED_LIBELLE);

        restMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMark))
            )
            .andExpect(status().isOk());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
        Mark testMark = markList.get(markList.size() - 1);
        assertThat(testMark.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testMark.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateMarkWithPatch() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        int databaseSizeBeforeUpdate = markRepository.findAll().size();

        // Update the mark using partial update
        Mark partialUpdatedMark = new Mark();
        partialUpdatedMark.setId(mark.getId());

        partialUpdatedMark.libelle(UPDATED_LIBELLE).deleted(UPDATED_DELETED);

        restMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMark))
            )
            .andExpect(status().isOk());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
        Mark testMark = markList.get(markList.size() - 1);
        assertThat(testMark.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testMark.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingMark() throws Exception {
        int databaseSizeBeforeUpdate = markRepository.findAll().size();
        mark.setId(count.incrementAndGet());

        // Create the Mark
        MarkDTO markDTO = markMapper.toDto(mark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, markDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(markDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMark() throws Exception {
        int databaseSizeBeforeUpdate = markRepository.findAll().size();
        mark.setId(count.incrementAndGet());

        // Create the Mark
        MarkDTO markDTO = markMapper.toDto(mark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(markDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMark() throws Exception {
        int databaseSizeBeforeUpdate = markRepository.findAll().size();
        mark.setId(count.incrementAndGet());

        // Create the Mark
        MarkDTO markDTO = markMapper.toDto(mark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(markDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMark() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        int databaseSizeBeforeDelete = markRepository.findAll().size();

        // Delete the mark
        restMarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, mark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
