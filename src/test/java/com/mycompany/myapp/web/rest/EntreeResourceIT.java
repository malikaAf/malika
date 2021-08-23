package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Entree;
import com.mycompany.myapp.repository.EntreeRepository;
import com.mycompany.myapp.service.dto.EntreeDTO;
import com.mycompany.myapp.service.mapper.EntreeMapper;
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
 * Integration tests for the {@link EntreeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntreeResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_QUANTITE = 1D;
    private static final Double UPDATED_QUANTITE = 2D;

    private static final Double DEFAULT_PRIX_UNITAIRE_TTC = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE_TTC = 2D;

    private static final String DEFAULT_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SERIE = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_CARACT_SUPPLEMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_CARACT_SUPPLEMENTAIRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ENTREE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ENTREE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_BORDEREAU = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BORDEREAU = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BORDEREAU_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BORDEREAU_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EN_STOCK = false;
    private static final Boolean UPDATED_EN_STOCK = true;

    private static final Boolean DEFAULT_EN_COMMANDE = false;
    private static final Boolean UPDATED_EN_COMMANDE = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/entrees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EntreeRepository entreeRepository;

    @Autowired
    private EntreeMapper entreeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntreeMockMvc;

    private Entree entree;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entree createEntity(EntityManager em) {
        Entree entree = new Entree()
            .libelle(DEFAULT_LIBELLE)
            .quantite(DEFAULT_QUANTITE)
            .prixUnitaireTTC(DEFAULT_PRIX_UNITAIRE_TTC)
            .serie(DEFAULT_SERIE)
            .model(DEFAULT_MODEL)
            .caractSupplementaire(DEFAULT_CARACT_SUPPLEMENTAIRE)
            .dateEntree(DEFAULT_DATE_ENTREE)
            .bordereau(DEFAULT_BORDEREAU)
            .bordereauContentType(DEFAULT_BORDEREAU_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION)
            .enStock(DEFAULT_EN_STOCK)
            .enCommande(DEFAULT_EN_COMMANDE)
            .deleted(DEFAULT_DELETED);
        return entree;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entree createUpdatedEntity(EntityManager em) {
        Entree entree = new Entree()
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .prixUnitaireTTC(UPDATED_PRIX_UNITAIRE_TTC)
            .serie(UPDATED_SERIE)
            .model(UPDATED_MODEL)
            .caractSupplementaire(UPDATED_CARACT_SUPPLEMENTAIRE)
            .dateEntree(UPDATED_DATE_ENTREE)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION)
            .enStock(UPDATED_EN_STOCK)
            .enCommande(UPDATED_EN_COMMANDE)
            .deleted(UPDATED_DELETED);
        return entree;
    }

    @BeforeEach
    public void initTest() {
        entree = createEntity(em);
    }

    @Test
    @Transactional
    void createEntree() throws Exception {
        int databaseSizeBeforeCreate = entreeRepository.findAll().size();
        // Create the Entree
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);
        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isCreated());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeCreate + 1);
        Entree testEntree = entreeList.get(entreeList.size() - 1);
        assertThat(testEntree.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testEntree.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testEntree.getPrixUnitaireTTC()).isEqualTo(DEFAULT_PRIX_UNITAIRE_TTC);
        assertThat(testEntree.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testEntree.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testEntree.getCaractSupplementaire()).isEqualTo(DEFAULT_CARACT_SUPPLEMENTAIRE);
        assertThat(testEntree.getDateEntree()).isEqualTo(DEFAULT_DATE_ENTREE);
        assertThat(testEntree.getBordereau()).isEqualTo(DEFAULT_BORDEREAU);
        assertThat(testEntree.getBordereauContentType()).isEqualTo(DEFAULT_BORDEREAU_CONTENT_TYPE);
        assertThat(testEntree.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testEntree.getEnStock()).isEqualTo(DEFAULT_EN_STOCK);
        assertThat(testEntree.getEnCommande()).isEqualTo(DEFAULT_EN_COMMANDE);
        assertThat(testEntree.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createEntreeWithExistingId() throws Exception {
        // Create the Entree with an existing ID
        entree.setId(1L);
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        int databaseSizeBeforeCreate = entreeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeRepository.findAll().size();
        // set the field null
        entree.setQuantite(null);

        // Create the Entree, which fails.
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isBadRequest());

        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixUnitaireTTCIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeRepository.findAll().size();
        // set the field null
        entree.setPrixUnitaireTTC(null);

        // Create the Entree, which fails.
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isBadRequest());

        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSerieIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeRepository.findAll().size();
        // set the field null
        entree.setSerie(null);

        // Create the Entree, which fails.
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isBadRequest());

        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeRepository.findAll().size();
        // set the field null
        entree.setModel(null);

        // Create the Entree, which fails.
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isBadRequest());

        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObservationIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeRepository.findAll().size();
        // set the field null
        entree.setObservation(null);

        // Create the Entree, which fails.
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isBadRequest());

        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeRepository.findAll().size();
        // set the field null
        entree.setDeleted(null);

        // Create the Entree, which fails.
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        restEntreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isBadRequest());

        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEntrees() throws Exception {
        // Initialize the database
        entreeRepository.saveAndFlush(entree);

        // Get all the entreeList
        restEntreeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entree.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.doubleValue())))
            .andExpect(jsonPath("$.[*].prixUnitaireTTC").value(hasItem(DEFAULT_PRIX_UNITAIRE_TTC.doubleValue())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].caractSupplementaire").value(hasItem(DEFAULT_CARACT_SUPPLEMENTAIRE)))
            .andExpect(jsonPath("$.[*].dateEntree").value(hasItem(DEFAULT_DATE_ENTREE.toString())))
            .andExpect(jsonPath("$.[*].bordereauContentType").value(hasItem(DEFAULT_BORDEREAU_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bordereau").value(hasItem(Base64Utils.encodeToString(DEFAULT_BORDEREAU))))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].enStock").value(hasItem(DEFAULT_EN_STOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].enCommande").value(hasItem(DEFAULT_EN_COMMANDE.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getEntree() throws Exception {
        // Initialize the database
        entreeRepository.saveAndFlush(entree);

        // Get the entree
        restEntreeMockMvc
            .perform(get(ENTITY_API_URL_ID, entree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entree.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.doubleValue()))
            .andExpect(jsonPath("$.prixUnitaireTTC").value(DEFAULT_PRIX_UNITAIRE_TTC.doubleValue()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.caractSupplementaire").value(DEFAULT_CARACT_SUPPLEMENTAIRE))
            .andExpect(jsonPath("$.dateEntree").value(DEFAULT_DATE_ENTREE.toString()))
            .andExpect(jsonPath("$.bordereauContentType").value(DEFAULT_BORDEREAU_CONTENT_TYPE))
            .andExpect(jsonPath("$.bordereau").value(Base64Utils.encodeToString(DEFAULT_BORDEREAU)))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.enStock").value(DEFAULT_EN_STOCK.booleanValue()))
            .andExpect(jsonPath("$.enCommande").value(DEFAULT_EN_COMMANDE.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEntree() throws Exception {
        // Get the entree
        restEntreeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEntree() throws Exception {
        // Initialize the database
        entreeRepository.saveAndFlush(entree);

        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();

        // Update the entree
        Entree updatedEntree = entreeRepository.findById(entree.getId()).get();
        // Disconnect from session so that the updates on updatedEntree are not directly saved in db
        em.detach(updatedEntree);
        updatedEntree
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .prixUnitaireTTC(UPDATED_PRIX_UNITAIRE_TTC)
            .serie(UPDATED_SERIE)
            .model(UPDATED_MODEL)
            .caractSupplementaire(UPDATED_CARACT_SUPPLEMENTAIRE)
            .dateEntree(UPDATED_DATE_ENTREE)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION)
            .enStock(UPDATED_EN_STOCK)
            .enCommande(UPDATED_EN_COMMANDE)
            .deleted(UPDATED_DELETED);
        EntreeDTO entreeDTO = entreeMapper.toDto(updatedEntree);

        restEntreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entreeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entreeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
        Entree testEntree = entreeList.get(entreeList.size() - 1);
        assertThat(testEntree.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEntree.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testEntree.getPrixUnitaireTTC()).isEqualTo(UPDATED_PRIX_UNITAIRE_TTC);
        assertThat(testEntree.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testEntree.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testEntree.getCaractSupplementaire()).isEqualTo(UPDATED_CARACT_SUPPLEMENTAIRE);
        assertThat(testEntree.getDateEntree()).isEqualTo(UPDATED_DATE_ENTREE);
        assertThat(testEntree.getBordereau()).isEqualTo(UPDATED_BORDEREAU);
        assertThat(testEntree.getBordereauContentType()).isEqualTo(UPDATED_BORDEREAU_CONTENT_TYPE);
        assertThat(testEntree.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testEntree.getEnStock()).isEqualTo(UPDATED_EN_STOCK);
        assertThat(testEntree.getEnCommande()).isEqualTo(UPDATED_EN_COMMANDE);
        assertThat(testEntree.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingEntree() throws Exception {
        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();
        entree.setId(count.incrementAndGet());

        // Create the Entree
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entreeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entreeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntree() throws Exception {
        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();
        entree.setId(count.incrementAndGet());

        // Create the Entree
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entreeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntree() throws Exception {
        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();
        entree.setId(count.incrementAndGet());

        // Create the Entree
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntreeWithPatch() throws Exception {
        // Initialize the database
        entreeRepository.saveAndFlush(entree);

        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();

        // Update the entree using partial update
        Entree partialUpdatedEntree = new Entree();
        partialUpdatedEntree.setId(entree.getId());

        partialUpdatedEntree
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .prixUnitaireTTC(UPDATED_PRIX_UNITAIRE_TTC)
            .model(UPDATED_MODEL)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .enStock(UPDATED_EN_STOCK)
            .enCommande(UPDATED_EN_COMMANDE);

        restEntreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntree))
            )
            .andExpect(status().isOk());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
        Entree testEntree = entreeList.get(entreeList.size() - 1);
        assertThat(testEntree.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEntree.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testEntree.getPrixUnitaireTTC()).isEqualTo(UPDATED_PRIX_UNITAIRE_TTC);
        assertThat(testEntree.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testEntree.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testEntree.getCaractSupplementaire()).isEqualTo(DEFAULT_CARACT_SUPPLEMENTAIRE);
        assertThat(testEntree.getDateEntree()).isEqualTo(DEFAULT_DATE_ENTREE);
        assertThat(testEntree.getBordereau()).isEqualTo(UPDATED_BORDEREAU);
        assertThat(testEntree.getBordereauContentType()).isEqualTo(UPDATED_BORDEREAU_CONTENT_TYPE);
        assertThat(testEntree.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testEntree.getEnStock()).isEqualTo(UPDATED_EN_STOCK);
        assertThat(testEntree.getEnCommande()).isEqualTo(UPDATED_EN_COMMANDE);
        assertThat(testEntree.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateEntreeWithPatch() throws Exception {
        // Initialize the database
        entreeRepository.saveAndFlush(entree);

        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();

        // Update the entree using partial update
        Entree partialUpdatedEntree = new Entree();
        partialUpdatedEntree.setId(entree.getId());

        partialUpdatedEntree
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .prixUnitaireTTC(UPDATED_PRIX_UNITAIRE_TTC)
            .serie(UPDATED_SERIE)
            .model(UPDATED_MODEL)
            .caractSupplementaire(UPDATED_CARACT_SUPPLEMENTAIRE)
            .dateEntree(UPDATED_DATE_ENTREE)
            .bordereau(UPDATED_BORDEREAU)
            .bordereauContentType(UPDATED_BORDEREAU_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION)
            .enStock(UPDATED_EN_STOCK)
            .enCommande(UPDATED_EN_COMMANDE)
            .deleted(UPDATED_DELETED);

        restEntreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntree))
            )
            .andExpect(status().isOk());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
        Entree testEntree = entreeList.get(entreeList.size() - 1);
        assertThat(testEntree.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEntree.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testEntree.getPrixUnitaireTTC()).isEqualTo(UPDATED_PRIX_UNITAIRE_TTC);
        assertThat(testEntree.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testEntree.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testEntree.getCaractSupplementaire()).isEqualTo(UPDATED_CARACT_SUPPLEMENTAIRE);
        assertThat(testEntree.getDateEntree()).isEqualTo(UPDATED_DATE_ENTREE);
        assertThat(testEntree.getBordereau()).isEqualTo(UPDATED_BORDEREAU);
        assertThat(testEntree.getBordereauContentType()).isEqualTo(UPDATED_BORDEREAU_CONTENT_TYPE);
        assertThat(testEntree.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testEntree.getEnStock()).isEqualTo(UPDATED_EN_STOCK);
        assertThat(testEntree.getEnCommande()).isEqualTo(UPDATED_EN_COMMANDE);
        assertThat(testEntree.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingEntree() throws Exception {
        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();
        entree.setId(count.incrementAndGet());

        // Create the Entree
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entreeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entreeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntree() throws Exception {
        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();
        entree.setId(count.incrementAndGet());

        // Create the Entree
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entreeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntree() throws Exception {
        int databaseSizeBeforeUpdate = entreeRepository.findAll().size();
        entree.setId(count.incrementAndGet());

        // Create the Entree
        EntreeDTO entreeDTO = entreeMapper.toDto(entree);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(entreeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entree in the database
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntree() throws Exception {
        // Initialize the database
        entreeRepository.saveAndFlush(entree);

        int databaseSizeBeforeDelete = entreeRepository.findAll().size();

        // Delete the entree
        restEntreeMockMvc
            .perform(delete(ENTITY_API_URL_ID, entree.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entree> entreeList = entreeRepository.findAll();
        assertThat(entreeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
