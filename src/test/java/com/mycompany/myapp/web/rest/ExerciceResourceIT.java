package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Exercice;
import com.mycompany.myapp.repository.ExerciceRepository;
import com.mycompany.myapp.service.dto.ExerciceDTO;
import com.mycompany.myapp.service.mapper.ExerciceMapper;
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
 * Integration tests for the {@link ExerciceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciceResourceIT {

    private static final Integer DEFAULT_ANNEE = 2;
    private static final Integer UPDATED_ANNEE = 3;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/exercices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciceRepository exerciceRepository;

    @Autowired
    private ExerciceMapper exerciceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciceMockMvc;

    private Exercice exercice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercice createEntity(EntityManager em) {
        Exercice exercice = new Exercice().annee(DEFAULT_ANNEE).deleted(DEFAULT_DELETED);
        return exercice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercice createUpdatedEntity(EntityManager em) {
        Exercice exercice = new Exercice().annee(UPDATED_ANNEE).deleted(UPDATED_DELETED);
        return exercice;
    }

    @BeforeEach
    public void initTest() {
        exercice = createEntity(em);
    }

    @Test
    @Transactional
    void createExercice() throws Exception {
        int databaseSizeBeforeCreate = exerciceRepository.findAll().size();
        // Create the Exercice
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);
        restExerciceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciceDTO)))
            .andExpect(status().isCreated());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeCreate + 1);
        Exercice testExercice = exerciceList.get(exerciceList.size() - 1);
        assertThat(testExercice.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testExercice.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createExerciceWithExistingId() throws Exception {
        // Create the Exercice with an existing ID
        exercice.setId(1L);
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        int databaseSizeBeforeCreate = exerciceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciceRepository.findAll().size();
        // set the field null
        exercice.setDeleted(null);

        // Create the Exercice, which fails.
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        restExerciceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciceDTO)))
            .andExpect(status().isBadRequest());

        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExercices() throws Exception {
        // Initialize the database
        exerciceRepository.saveAndFlush(exercice);

        // Get all the exerciceList
        restExerciceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercice.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getExercice() throws Exception {
        // Initialize the database
        exerciceRepository.saveAndFlush(exercice);

        // Get the exercice
        restExerciceMockMvc
            .perform(get(ENTITY_API_URL_ID, exercice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exercice.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingExercice() throws Exception {
        // Get the exercice
        restExerciceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExercice() throws Exception {
        // Initialize the database
        exerciceRepository.saveAndFlush(exercice);

        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();

        // Update the exercice
        Exercice updatedExercice = exerciceRepository.findById(exercice.getId()).get();
        // Disconnect from session so that the updates on updatedExercice are not directly saved in db
        em.detach(updatedExercice);
        updatedExercice.annee(UPDATED_ANNEE).deleted(UPDATED_DELETED);
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(updatedExercice);

        restExerciceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
        Exercice testExercice = exerciceList.get(exerciceList.size() - 1);
        assertThat(testExercice.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testExercice.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingExercice() throws Exception {
        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();
        exercice.setId(count.incrementAndGet());

        // Create the Exercice
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExercice() throws Exception {
        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();
        exercice.setId(count.incrementAndGet());

        // Create the Exercice
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExercice() throws Exception {
        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();
        exercice.setId(count.incrementAndGet());

        // Create the Exercice
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciceWithPatch() throws Exception {
        // Initialize the database
        exerciceRepository.saveAndFlush(exercice);

        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();

        // Update the exercice using partial update
        Exercice partialUpdatedExercice = new Exercice();
        partialUpdatedExercice.setId(exercice.getId());

        restExerciceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercice))
            )
            .andExpect(status().isOk());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
        Exercice testExercice = exerciceList.get(exerciceList.size() - 1);
        assertThat(testExercice.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testExercice.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateExerciceWithPatch() throws Exception {
        // Initialize the database
        exerciceRepository.saveAndFlush(exercice);

        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();

        // Update the exercice using partial update
        Exercice partialUpdatedExercice = new Exercice();
        partialUpdatedExercice.setId(exercice.getId());

        partialUpdatedExercice.annee(UPDATED_ANNEE).deleted(UPDATED_DELETED);

        restExerciceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercice))
            )
            .andExpect(status().isOk());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
        Exercice testExercice = exerciceList.get(exerciceList.size() - 1);
        assertThat(testExercice.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testExercice.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingExercice() throws Exception {
        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();
        exercice.setId(count.incrementAndGet());

        // Create the Exercice
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExercice() throws Exception {
        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();
        exercice.setId(count.incrementAndGet());

        // Create the Exercice
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExercice() throws Exception {
        int databaseSizeBeforeUpdate = exerciceRepository.findAll().size();
        exercice.setId(count.incrementAndGet());

        // Create the Exercice
        ExerciceDTO exerciceDTO = exerciceMapper.toDto(exercice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exerciceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercice in the database
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExercice() throws Exception {
        // Initialize the database
        exerciceRepository.saveAndFlush(exercice);

        int databaseSizeBeforeDelete = exerciceRepository.findAll().size();

        // Delete the exercice
        restExerciceMockMvc
            .perform(delete(ENTITY_API_URL_ID, exercice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exercice> exerciceList = exerciceRepository.findAll();
        assertThat(exerciceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
