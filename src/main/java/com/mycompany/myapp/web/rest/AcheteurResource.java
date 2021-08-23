package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AcheteurRepository;
import com.mycompany.myapp.service.AcheteurService;
import com.mycompany.myapp.service.dto.AcheteurDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Acheteur}.
 */
@RestController
@RequestMapping("/api")
public class AcheteurResource {

    private final Logger log = LoggerFactory.getLogger(AcheteurResource.class);

    private static final String ENTITY_NAME = "acheteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcheteurService acheteurService;

    private final AcheteurRepository acheteurRepository;

    public AcheteurResource(AcheteurService acheteurService, AcheteurRepository acheteurRepository) {
        this.acheteurService = acheteurService;
        this.acheteurRepository = acheteurRepository;
    }

    /**
     * {@code POST  /acheteurs} : Create a new acheteur.
     *
     * @param acheteurDTO the acheteurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acheteurDTO, or with status {@code 400 (Bad Request)} if the acheteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acheteurs")
    public ResponseEntity<AcheteurDTO> createAcheteur(@Valid @RequestBody AcheteurDTO acheteurDTO) throws URISyntaxException {
        log.debug("REST request to save Acheteur : {}", acheteurDTO);
        if (acheteurDTO.getId() != null) {
            throw new BadRequestAlertException("A new acheteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcheteurDTO result = acheteurService.save(acheteurDTO);
        return ResponseEntity
            .created(new URI("/api/acheteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acheteurs/:id} : Updates an existing acheteur.
     *
     * @param id the id of the acheteurDTO to save.
     * @param acheteurDTO the acheteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acheteurDTO,
     * or with status {@code 400 (Bad Request)} if the acheteurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acheteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acheteurs/{id}")
    public ResponseEntity<AcheteurDTO> updateAcheteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcheteurDTO acheteurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Acheteur : {}, {}", id, acheteurDTO);
        if (acheteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acheteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acheteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcheteurDTO result = acheteurService.save(acheteurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acheteurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acheteurs/:id} : Partial updates given fields of an existing acheteur, field will ignore if it is null
     *
     * @param id the id of the acheteurDTO to save.
     * @param acheteurDTO the acheteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acheteurDTO,
     * or with status {@code 400 (Bad Request)} if the acheteurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the acheteurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the acheteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acheteurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AcheteurDTO> partialUpdateAcheteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcheteurDTO acheteurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Acheteur partially : {}, {}", id, acheteurDTO);
        if (acheteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acheteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acheteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcheteurDTO> result = acheteurService.partialUpdate(acheteurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acheteurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acheteurs} : get all the acheteurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acheteurs in body.
     */
    @GetMapping("/acheteurs")
    public List<AcheteurDTO> getAllAcheteurs() {
        log.debug("REST request to get all Acheteurs");
        return acheteurService.findAll().stream().filter(acheteurDTO -> !acheteurDTO.getDeleted()).collect(Collectors.toList());
    }

    /**
     * {@code GET  /acheteurs/:id} : get the "id" acheteur.
     *
     * @param id the id of the acheteurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acheteurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acheteurs/{id}")
    public ResponseEntity<AcheteurDTO> getAcheteur(@PathVariable Long id) {
        log.debug("REST request to get Acheteur : {}", id);
        Optional<AcheteurDTO> acheteurDTO = acheteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acheteurDTO);
    }

    /**
     * {@code DELETE  /acheteurs/:id} : delete the "id" acheteur.
     *
     * @param id the id of the acheteurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acheteurs/{id}")
    public ResponseEntity<Void> deleteAcheteur(@PathVariable Long id) {
        log.debug("REST request to delete Acheteur : {}", id);
        acheteurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/acheteurs/delleteAll")
    public ResponseEntity<List<AcheteurDTO>> deleteAll(@RequestBody @Valid List<AcheteurDTO> acheteurDTO) {
        return ResponseEntity.ok(acheteurService.deleteAll(acheteurDTO));
    }
}
