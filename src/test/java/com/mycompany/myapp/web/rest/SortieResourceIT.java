package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Sortie;
import com.mycompany.myapp.repository.SortieRepository;
import com.mycompany.myapp.service.dto.SortieDTO;
import com.mycompany.myapp.service.mapper.SortieMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SortieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SortieResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_QUANTITE = 1D;
    private static final Double UPDATED_QUANTITE = 2D;

    private static final Double DEFAULT_PRIX_UNITAIRE_TTC = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE_TTC = 2D;

    private static final LocalDate DEFAULT_DATE_SORTIE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SORTIE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_BORDEREAU = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BORDEREAU = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BORDEREAU_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BORDEREAU_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/sorties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SortieRepository sortieRepository;

    @Autowired
    private SortieMapper sortieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSortieMockMvc;

    private Sortie sortie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sortie createEntity(EntityManager em) {
        Sortie sortie = new Sortie()
            .libelle(DEFAULT_LIBELLE)
            .quantite(DEFAULT_QUANTITE)
            .prixUnitaireTTC(DEFAULT_PRIX_UNITAIRE_TTC)
            .dateSortie(DEFAULT_DATE_SORTIE)
            .bordereau(DEFAULT_BORDEREAU)
            .bordereauContentType(DEFAULT_BORDEREAU_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION)
            .deleted(DEFAULT_DELETED);
        return sortie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sortie createUpdatedEntity(EntityManager em) {
        Sortie sortie = new Sortie()
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .prixUnitaireTTC(UPDATED_PRIX_UNITAIRE_TTC)
            .dateSortie(UPDATED_DATE_SORTIE)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION)
            .deleted(UPDATED_DELETED);
        return sortie;
    }

    @BeforeEach
    public void initTest() {
        sortie = createEntity(em);
    }

    @Test
    @Transactional
    void createSortie() throws Exception {
        int databaseSizeBeforeCreate = sortieRepository.findAll().size();
        // Create the Sortie
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);
        restSortieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sortieDTO)))
            .andExpect(status().isCreated());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeCreate + 1);
        Sortie testSortie = sortieList.get(sortieList.size() - 1);
        assertThat(testSortie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSortie.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testSortie.getPrixUnitaireTTC()).isEqualTo(DEFAULT_PRIX_UNITAIRE_TTC);
        assertThat(testSortie.getDateSortie()).isEqualTo(DEFAULT_DATE_SORTIE);
        assertThat(testSortie.getBordereau()).isEqualTo(DEFAULT_BORDEREAU);
        assertThat(testSortie.getBordereauContentType()).isEqualTo(DEFAULT_BORDEREAU_CONTENT_TYPE);
        assertThat(testSortie.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testSortie.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createSortieWithExistingId() throws Exception {
        // Create the Sortie with an existing ID
        sortie.setId(1L);
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        int databaseSizeBeforeCreate = sortieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSortieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sortieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = sortieRepository.findAll().size();
        // set the field null
        sortie.setQuantite(null);

        // Create the Sortie, which fails.
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        restSortieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sortieDTO)))
            .andExpect(status().isBadRequest());

        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixUnitaireTTCIsRequired() throws Exception {
        int databaseSizeBeforeTest = sortieRepository.findAll().size();
        // set the field null
        sortie.setPrixUnitaireTTC(null);

        // Create the Sortie, which fails.
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        restSortieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sortieDTO)))
            .andExpect(status().isBadRequest());

        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObservationIsRequired() throws Exception {
        int databaseSizeBeforeTest = sortieRepository.findAll().size();
        // set the field null
        sortie.setObservation(null);

        // Create the Sortie, which fails.
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        restSortieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sortieDTO)))
            .andExpect(status().isBadRequest());

        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = sortieRepository.findAll().size();
        // set the field null
        sortie.setDeleted(null);

        // Create the Sortie, which fails.
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        restSortieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sortieDTO)))
            .andExpect(status().isBadRequest());

        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSorties() throws Exception {
        // Initialize the database
        sortieRepository.saveAndFlush(sortie);

        // Get all the sortieList
        restSortieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sortie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.doubleValue())))
            .andExpect(jsonPath("$.[*].prixUnitaireTTC").value(hasItem(DEFAULT_PRIX_UNITAIRE_TTC.doubleValue())))
            .andExpect(jsonPath("$.[*].dateSortie").value(hasItem(DEFAULT_DATE_SORTIE.toString())))
            .andExpect(jsonPath("$.[*].bordereauContentType").value(hasItem(DEFAULT_BORDEREAU_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bordereau").value(hasItem(Base64Utils.encodeToString(DEFAULT_BORDEREAU))))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getSortie() throws Exception {
        // Initialize the database
        sortieRepository.saveAndFlush(sortie);

        // Get the sortie
        restSortieMockMvc
            .perform(get(ENTITY_API_URL_ID, sortie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sortie.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.doubleValue()))
            .andExpect(jsonPath("$.prixUnitaireTTC").value(DEFAULT_PRIX_UNITAIRE_TTC.doubleValue()))
            .andExpect(jsonPath("$.dateSortie").value(DEFAULT_DATE_SORTIE.toString()))
            .andExpect(jsonPath("$.bordereauContentType").value(DEFAULT_BORDEREAU_CONTENT_TYPE))
            .andExpect(jsonPath("$.bordereau").value(Base64Utils.encodeToString(DEFAULT_BORDEREAU)))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSortie() throws Exception {
        // Get the sortie
        restSortieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSortie() throws Exception {
        // Initialize the database
        sortieRepository.saveAndFlush(sortie);

        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();

        // Update the sortie
        Sortie updatedSortie = sortieRepository.findById(sortie.getId()).get();
        // Disconnect from session so that the updates on updatedSortie are not directly saved in db
        em.detach(updatedSortie);
        updatedSortie
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .prixUnitaireTTC(UPDATED_PRIX_UNITAIRE_TTC)
            .dateSortie(UPDATED_DATE_SORTIE)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION)
            .deleted(UPDATED_DELETED);
        SortieDTO sortieDTO = sortieMapper.toDto(updatedSortie);

        restSortieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sortieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sortieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
        Sortie testSortie = sortieList.get(sortieList.size() - 1);
        assertThat(testSortie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSortie.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testSortie.getPrixUnitaireTTC()).isEqualTo(UPDATED_PRIX_UNITAIRE_TTC);
        assertThat(testSortie.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
        assertThat(testSortie.getBordereau()).isEqualTo(UPDATED_BORDEREAU);
        assertThat(testSortie.getBordereauContentType()).isEqualTo(UPDATED_BORDEREAU_CONTENT_TYPE);
        assertThat(testSortie.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testSortie.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingSortie() throws Exception {
        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();
        sortie.setId(count.incrementAndGet());

        // Create the Sortie
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSortieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sortieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sortieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSortie() throws Exception {
        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();
        sortie.setId(count.incrementAndGet());

        // Create the Sortie
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSortieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sortieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSortie() throws Exception {
        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();
        sortie.setId(count.incrementAndGet());

        // Create the Sortie
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSortieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sortieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSortieWithPatch() throws Exception {
        // Initialize the database
        sortieRepository.saveAndFlush(sortie);

        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();

        // Update the sortie using partial update
        Sortie partialUpdatedSortie = new Sortie();
        partialUpdatedSortie.setId(sortie.getId());

        partialUpdatedSortie
            .libelle(UPDATED_LIBELLE)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION)
            .deleted(UPDATED_DELETED);

        restSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSortie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSortie))
            )
            .andExpect(status().isOk());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
        Sortie testSortie = sortieList.get(sortieList.size() - 1);
        assertThat(testSortie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSortie.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testSortie.getPrixUnitaireTTC()).isEqualTo(DEFAULT_PRIX_UNITAIRE_TTC);
        assertThat(testSortie.getDateSortie()).isEqualTo(DEFAULT_DATE_SORTIE);
        assertThat(testSortie.getBordereau()).isEqualTo(UPDATED_BORDEREAU);
        assertThat(testSortie.getBordereauContentType()).isEqualTo(UPDATED_BORDEREAU_CONTENT_TYPE);
        assertThat(testSortie.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testSortie.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSortieWithPatch() throws Exception {
        // Initialize the database
        sortieRepository.saveAndFlush(sortie);

        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();

        // Update the sortie using partial update
        Sortie partialUpdatedSortie = new Sortie();
        partialUpdatedSortie.setId(sortie.getId());

        partialUpdatedSortie
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .prixUnitaireTTC(UPDATED_PRIX_UNITAIRE_TTC)
            .dateSortie(UPDATED_DATE_SORTIE)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION)
            .deleted(UPDATED_DELETED);

        restSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSortie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSortie))
            )
            .andExpect(status().isOk());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
        Sortie testSortie = sortieList.get(sortieList.size() - 1);
        assertThat(testSortie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSortie.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testSortie.getPrixUnitaireTTC()).isEqualTo(UPDATED_PRIX_UNITAIRE_TTC);
        assertThat(testSortie.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
        assertThat(testSortie.getBordereau()).isEqualTo(UPDATED_BORDEREAU);
        assertThat(testSortie.getBordereauContentType()).isEqualTo(UPDATED_BORDEREAU_CONTENT_TYPE);
        assertThat(testSortie.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testSortie.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSortie() throws Exception {
        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();
        sortie.setId(count.incrementAndGet());

        // Create the Sortie
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sortieDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sortieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSortie() throws Exception {
        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();
        sortie.setId(count.incrementAndGet());

        // Create the Sortie
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSortieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sortieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSortie() throws Exception {
        int databaseSizeBeforeUpdate = sortieRepository.findAll().size();
        sortie.setId(count.incrementAndGet());

        // Create the Sortie
        SortieDTO sortieDTO = sortieMapper.toDto(sortie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSortieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sortieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sortie in the database
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSortie() throws Exception {
        // Initialize the database
        sortieRepository.saveAndFlush(sortie);

        int databaseSizeBeforeDelete = sortieRepository.findAll().size();

        // Delete the sortie
        restSortieMockMvc
            .perform(delete(ENTITY_API_URL_ID, sortie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sortie> sortieList = sortieRepository.findAll();
        assertThat(sortieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
