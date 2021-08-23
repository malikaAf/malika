package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SortieRepository;
import com.mycompany.myapp.service.SortieService;
import com.mycompany.myapp.service.dto.SortieDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Sortie}.
 */
@RestController
@RequestMapping("/api")
public class SortieResource {

    private final Logger log = LoggerFactory.getLogger(SortieResource.class);

    private static final String ENTITY_NAME = "sortie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SortieService sortieService;

    private final SortieRepository sortieRepository;

    public SortieResource(SortieService sortieService, SortieRepository sortieRepository) {
        this.sortieService = sortieService;
        this.sortieRepository = sortieRepository;
    }

    /**
     * {@code POST  /sorties} : Create a new sortie.
     *
     * @param sortieDTO the sortieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sortieDTO, or with status {@code 400 (Bad Request)} if the sortie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sorties")
    public ResponseEntity<SortieDTO> createSortie(@Valid @RequestBody SortieDTO sortieDTO) throws URISyntaxException {
        log.debug("REST request to save Sortie : {}", sortieDTO);
        if (sortieDTO.getId() != null) {
            throw new BadRequestAlertException("A new sortie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SortieDTO result = sortieService.save(sortieDTO);
        return ResponseEntity
            .created(new URI("/api/sorties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sorties/:id} : Updates an existing sortie.
     *
     * @param id the id of the sortieDTO to save.
     * @param sortieDTO the sortieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sortieDTO,
     * or with status {@code 400 (Bad Request)} if the sortieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sortieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sorties/{id}")
    public ResponseEntity<SortieDTO> updateSortie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SortieDTO sortieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sortie : {}, {}", id, sortieDTO);
        if (sortieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sortieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sortieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SortieDTO result = sortieService.save(sortieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sortieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sorties/:id} : Partial updates given fields of an existing sortie, field will ignore if it is null
     *
     * @param id the id of the sortieDTO to save.
     * @param sortieDTO the sortieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sortieDTO,
     * or with status {@code 400 (Bad Request)} if the sortieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sortieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sortieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sorties/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SortieDTO> partialUpdateSortie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SortieDTO sortieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sortie partially : {}, {}", id, sortieDTO);
        if (sortieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sortieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sortieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SortieDTO> result = sortieService.partialUpdate(sortieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sortieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sorties} : get all the sorties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sorties in body.
     */
    @GetMapping("/sorties")
    public List<SortieDTO> getAllSorties() {
        log.debug("REST request to get all Sorties");
        return sortieService.findAll().stream().filter(sortieDTO -> !sortieDTO.getDeleted()).collect(Collectors.toList());
    }

    /**
     * {@code GET  /sorties} : get all the sorties deleted.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sorties in body.
     */
    @GetMapping("/sorties/allsortiesdeleted")
    public List<SortieDTO> getAllDeletedSorties() {
        log.debug("REST request to get all Sorties deleted");
        return sortieService.findAll().stream().filter(sortieDTO -> sortieDTO.getDeleted() == true).collect(Collectors.toList());
    }

    /**
     * {@code GET  /sorties/:id} : get the "id" sortie.
     *
     * @param id the id of the sortieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sortieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sorties/{id}")
    public ResponseEntity<SortieDTO> getSortie(@PathVariable Long id) {
        log.debug("REST request to get Sortie : {}", id);
        Optional<SortieDTO> sortieDTO = sortieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sortieDTO);
    }

    /**
     * {@code DELETE  /sorties/:id} : delete the "id" sortie.
     *
     * @param id the id of the sortieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sorties/{id}")
    public ResponseEntity<Void> deleteSortie(@PathVariable Long id) {
        log.debug("REST request to delete Sortie : {}", id);
        sortieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/sorties/delleteAll")
    public ResponseEntity<List<SortieDTO>> deleteAll(@RequestBody @Valid List<SortieDTO> sortieDTOS) {
        return ResponseEntity.ok(sortieService.deleteAll(sortieDTOS));
    }
}
