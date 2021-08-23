package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Acheteur;
import com.mycompany.myapp.domain.enumeration.TypeAcheteur;
import com.mycompany.myapp.repository.AcheteurRepository;
import com.mycompany.myapp.service.dto.AcheteurDTO;
import com.mycompany.myapp.service.mapper.AcheteurMapper;
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
 * Integration tests for the {@link AcheteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcheteurResourceIT {

    private static final TypeAcheteur DEFAULT_TYPE_CLIENT = TypeAcheteur.ACHETEUR;
    private static final TypeAcheteur UPDATED_TYPE_CLIENT = TypeAcheteur.ACHETEUR;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_CNIB = "AAAAAAAAAA";
    private static final String UPDATED_CNIB = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMRO_BANQUAIRE = "AAAAAAAAAA";
    private static final String UPDATED_NUMRO_BANQUAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/acheteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcheteurRepository acheteurRepository;

    @Autowired
    private AcheteurMapper acheteurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcheteurMockMvc;

    private Acheteur acheteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acheteur createEntity(EntityManager em) {
        Acheteur acheteur = new Acheteur()
            .typeClient(DEFAULT_TYPE_CLIENT)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .tel(DEFAULT_TEL)
            .cnib(DEFAULT_CNIB)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .numroBanquaire(DEFAULT_NUMRO_BANQUAIRE)
            .deleted(DEFAULT_DELETED);
        return acheteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acheteur createUpdatedEntity(EntityManager em) {
        Acheteur acheteur = new Acheteur()
            .typeClient(UPDATED_TYPE_CLIENT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .tel(UPDATED_TEL)
            .cnib(UPDATED_CNIB)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .numroBanquaire(UPDATED_NUMRO_BANQUAIRE)
            .deleted(UPDATED_DELETED);
        return acheteur;
    }

    @BeforeEach
    public void initTest() {
        acheteur = createEntity(em);
    }

    @Test
    @Transactional
    void createAcheteur() throws Exception {
        int databaseSizeBeforeCreate = acheteurRepository.findAll().size();
        // Create the Acheteur
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);
        restAcheteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isCreated());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeCreate + 1);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getTypeClient()).isEqualTo(DEFAULT_TYPE_CLIENT);
        assertThat(testAcheteur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAcheteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAcheteur.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testAcheteur.getCnib()).isEqualTo(DEFAULT_CNIB);
        assertThat(testAcheteur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAcheteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAcheteur.getNumroBanquaire()).isEqualTo(DEFAULT_NUMRO_BANQUAIRE);
        assertThat(testAcheteur.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createAcheteurWithExistingId() throws Exception {
        // Create the Acheteur with an existing ID
        acheteur.setId(1L);
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        int databaseSizeBeforeCreate = acheteurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcheteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = acheteurRepository.findAll().size();
        // set the field null
        acheteur.setNom(null);

        // Create the Acheteur, which fails.
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        restAcheteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isBadRequest());

        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = acheteurRepository.findAll().size();
        // set the field null
        acheteur.setTel(null);

        // Create the Acheteur, which fails.
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        restAcheteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isBadRequest());

        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCnibIsRequired() throws Exception {
        int databaseSizeBeforeTest = acheteurRepository.findAll().size();
        // set the field null
        acheteur.setCnib(null);

        // Create the Acheteur, which fails.
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        restAcheteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isBadRequest());

        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = acheteurRepository.findAll().size();
        // set the field null
        acheteur.setEmail(null);

        // Create the Acheteur, which fails.
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        restAcheteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isBadRequest());

        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = acheteurRepository.findAll().size();
        // set the field null
        acheteur.setDeleted(null);

        // Create the Acheteur, which fails.
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        restAcheteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isBadRequest());

        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcheteurs() throws Exception {
        // Initialize the database
        acheteurRepository.saveAndFlush(acheteur);

        // Get all the acheteurList
        restAcheteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acheteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeClient").value(hasItem(DEFAULT_TYPE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].cnib").value(hasItem(DEFAULT_CNIB)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].numroBanquaire").value(hasItem(DEFAULT_NUMRO_BANQUAIRE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getAcheteur() throws Exception {
        // Initialize the database
        acheteurRepository.saveAndFlush(acheteur);

        // Get the acheteur
        restAcheteurMockMvc
            .perform(get(ENTITY_API_URL_ID, acheteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acheteur.getId().intValue()))
            .andExpect(jsonPath("$.typeClient").value(DEFAULT_TYPE_CLIENT.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.cnib").value(DEFAULT_CNIB))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.numroBanquaire").value(DEFAULT_NUMRO_BANQUAIRE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAcheteur() throws Exception {
        // Get the acheteur
        restAcheteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcheteur() throws Exception {
        // Initialize the database
        acheteurRepository.saveAndFlush(acheteur);

        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();

        // Update the acheteur
        Acheteur updatedAcheteur = acheteurRepository.findById(acheteur.getId()).get();
        // Disconnect from session so that the updates on updatedAcheteur are not directly saved in db
        em.detach(updatedAcheteur);
        updatedAcheteur
            .typeClient(UPDATED_TYPE_CLIENT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .tel(UPDATED_TEL)
            .cnib(UPDATED_CNIB)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .numroBanquaire(UPDATED_NUMRO_BANQUAIRE)
            .deleted(UPDATED_DELETED);
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(updatedAcheteur);

        restAcheteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acheteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acheteurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getTypeClient()).isEqualTo(UPDATED_TYPE_CLIENT);
        assertThat(testAcheteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAcheteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAcheteur.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testAcheteur.getCnib()).isEqualTo(UPDATED_CNIB);
        assertThat(testAcheteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAcheteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAcheteur.getNumroBanquaire()).isEqualTo(UPDATED_NUMRO_BANQUAIRE);
        assertThat(testAcheteur.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
        acheteur.setId(count.incrementAndGet());

        // Create the Acheteur
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcheteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acheteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acheteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
        acheteur.setId(count.incrementAndGet());

        // Create the Acheteur
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcheteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acheteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
        acheteur.setId(count.incrementAndGet());

        // Create the Acheteur
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcheteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcheteurWithPatch() throws Exception {
        // Initialize the database
        acheteurRepository.saveAndFlush(acheteur);

        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();

        // Update the acheteur using partial update
        Acheteur partialUpdatedAcheteur = new Acheteur();
        partialUpdatedAcheteur.setId(acheteur.getId());

        partialUpdatedAcheteur.tel(UPDATED_TEL).cnib(UPDATED_CNIB).email(UPDATED_EMAIL).numroBanquaire(UPDATED_NUMRO_BANQUAIRE);

        restAcheteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcheteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcheteur))
            )
            .andExpect(status().isOk());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getTypeClient()).isEqualTo(DEFAULT_TYPE_CLIENT);
        assertThat(testAcheteur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAcheteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAcheteur.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testAcheteur.getCnib()).isEqualTo(UPDATED_CNIB);
        assertThat(testAcheteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAcheteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAcheteur.getNumroBanquaire()).isEqualTo(UPDATED_NUMRO_BANQUAIRE);
        assertThat(testAcheteur.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateAcheteurWithPatch() throws Exception {
        // Initialize the database
        acheteurRepository.saveAndFlush(acheteur);

        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();

        // Update the acheteur using partial update
        Acheteur partialUpdatedAcheteur = new Acheteur();
        partialUpdatedAcheteur.setId(acheteur.getId());

        partialUpdatedAcheteur
            .typeClient(UPDATED_TYPE_CLIENT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .tel(UPDATED_TEL)
            .cnib(UPDATED_CNIB)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .numroBanquaire(UPDATED_NUMRO_BANQUAIRE)
            .deleted(UPDATED_DELETED);

        restAcheteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcheteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcheteur))
            )
            .andExpect(status().isOk());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getTypeClient()).isEqualTo(UPDATED_TYPE_CLIENT);
        assertThat(testAcheteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAcheteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAcheteur.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testAcheteur.getCnib()).isEqualTo(UPDATED_CNIB);
        assertThat(testAcheteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAcheteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAcheteur.getNumroBanquaire()).isEqualTo(UPDATED_NUMRO_BANQUAIRE);
        assertThat(testAcheteur.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
        acheteur.setId(count.incrementAndGet());

        // Create the Acheteur
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcheteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acheteurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acheteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
        acheteur.setId(count.incrementAndGet());

        // Create the Acheteur
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcheteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acheteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
        acheteur.setId(count.incrementAndGet());

        // Create the Acheteur
        AcheteurDTO acheteurDTO = acheteurMapper.toDto(acheteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcheteurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(acheteurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcheteur() throws Exception {
        // Initialize the database
        acheteurRepository.saveAndFlush(acheteur);

        int databaseSizeBeforeDelete = acheteurRepository.findAll().size();

        // Delete the acheteur
        restAcheteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, acheteur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Acheteur> acheteurList = acheteurRepository.findAll();
        assertThat(acheteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
