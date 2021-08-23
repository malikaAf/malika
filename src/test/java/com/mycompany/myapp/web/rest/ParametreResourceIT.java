package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Parametre;
import com.mycompany.myapp.repository.ParametreRepository;
import com.mycompany.myapp.service.dto.ParametreDTO;
import com.mycompany.myapp.service.mapper.ParametreMapper;
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
 * Integration tests for the {@link ParametreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParametreResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_TVA = 1D;
    private static final Double UPDATED_TVA = 2D;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/parametres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParametreRepository parametreRepository;

    @Autowired
    private ParametreMapper parametreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParametreMockMvc;

    private Parametre parametre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametre createEntity(EntityManager em) {
        Parametre parametre = new Parametre().libelle(DEFAULT_LIBELLE).tva(DEFAULT_TVA).deleted(DEFAULT_DELETED);
        return parametre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametre createUpdatedEntity(EntityManager em) {
        Parametre parametre = new Parametre().libelle(UPDATED_LIBELLE).tva(UPDATED_TVA).deleted(UPDATED_DELETED);
        return parametre;
    }

    @BeforeEach
    public void initTest() {
        parametre = createEntity(em);
    }

    @Test
    @Transactional
    void createParametre() throws Exception {
        int databaseSizeBeforeCreate = parametreRepository.findAll().size();
        // Create the Parametre
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);
        restParametreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreDTO)))
            .andExpect(status().isCreated());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeCreate + 1);
        Parametre testParametre = parametreList.get(parametreList.size() - 1);
        assertThat(testParametre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testParametre.getTva()).isEqualTo(DEFAULT_TVA);
        assertThat(testParametre.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createParametreWithExistingId() throws Exception {
        // Create the Parametre with an existing ID
        parametre.setId(1L);
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        int databaseSizeBeforeCreate = parametreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametreRepository.findAll().size();
        // set the field null
        parametre.setLibelle(null);

        // Create the Parametre, which fails.
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        restParametreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreDTO)))
            .andExpect(status().isBadRequest());

        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametreRepository.findAll().size();
        // set the field null
        parametre.setDeleted(null);

        // Create the Parametre, which fails.
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        restParametreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreDTO)))
            .andExpect(status().isBadRequest());

        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParametres() throws Exception {
        // Initialize the database
        parametreRepository.saveAndFlush(parametre);

        // Get all the parametreList
        restParametreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(DEFAULT_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getParametre() throws Exception {
        // Initialize the database
        parametreRepository.saveAndFlush(parametre);

        // Get the parametre
        restParametreMockMvc
            .perform(get(ENTITY_API_URL_ID, parametre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parametre.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.tva").value(DEFAULT_TVA.doubleValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingParametre() throws Exception {
        // Get the parametre
        restParametreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParametre() throws Exception {
        // Initialize the database
        parametreRepository.saveAndFlush(parametre);

        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();

        // Update the parametre
        Parametre updatedParametre = parametreRepository.findById(parametre.getId()).get();
        // Disconnect from session so that the updates on updatedParametre are not directly saved in db
        em.detach(updatedParametre);
        updatedParametre.libelle(UPDATED_LIBELLE).tva(UPDATED_TVA).deleted(UPDATED_DELETED);
        ParametreDTO parametreDTO = parametreMapper.toDto(updatedParametre);

        restParametreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
        Parametre testParametre = parametreList.get(parametreList.size() - 1);
        assertThat(testParametre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testParametre.getTva()).isEqualTo(UPDATED_TVA);
        assertThat(testParametre.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingParametre() throws Exception {
        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();
        parametre.setId(count.incrementAndGet());

        // Create the Parametre
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParametre() throws Exception {
        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();
        parametre.setId(count.incrementAndGet());

        // Create the Parametre
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParametre() throws Exception {
        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();
        parametre.setId(count.incrementAndGet());

        // Create the Parametre
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParametreWithPatch() throws Exception {
        // Initialize the database
        parametreRepository.saveAndFlush(parametre);

        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();

        // Update the parametre using partial update
        Parametre partialUpdatedParametre = new Parametre();
        partialUpdatedParametre.setId(parametre.getId());

        partialUpdatedParametre.libelle(UPDATED_LIBELLE);

        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametre))
            )
            .andExpect(status().isOk());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
        Parametre testParametre = parametreList.get(parametreList.size() - 1);
        assertThat(testParametre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testParametre.getTva()).isEqualTo(DEFAULT_TVA);
        assertThat(testParametre.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateParametreWithPatch() throws Exception {
        // Initialize the database
        parametreRepository.saveAndFlush(parametre);

        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();

        // Update the parametre using partial update
        Parametre partialUpdatedParametre = new Parametre();
        partialUpdatedParametre.setId(parametre.getId());

        partialUpdatedParametre.libelle(UPDATED_LIBELLE).tva(UPDATED_TVA).deleted(UPDATED_DELETED);

        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametre))
            )
            .andExpect(status().isOk());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
        Parametre testParametre = parametreList.get(parametreList.size() - 1);
        assertThat(testParametre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testParametre.getTva()).isEqualTo(UPDATED_TVA);
        assertThat(testParametre.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingParametre() throws Exception {
        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();
        parametre.setId(count.incrementAndGet());

        // Create the Parametre
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parametreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParametre() throws Exception {
        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();
        parametre.setId(count.incrementAndGet());

        // Create the Parametre
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParametre() throws Exception {
        int databaseSizeBeforeUpdate = parametreRepository.findAll().size();
        parametre.setId(count.incrementAndGet());

        // Create the Parametre
        ParametreDTO parametreDTO = parametreMapper.toDto(parametre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parametreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parametre in the database
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParametre() throws Exception {
        // Initialize the database
        parametreRepository.saveAndFlush(parametre);

        int databaseSizeBeforeDelete = parametreRepository.findAll().size();

        // Delete the parametre
        restParametreMockMvc
            .perform(delete(ENTITY_API_URL_ID, parametre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parametre> parametreList = parametreRepository.findAll();
        assertThat(parametreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
