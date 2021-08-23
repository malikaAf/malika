package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EntreeExportPDF;
import com.mycompany.myapp.repository.EntreeRepository;
import com.mycompany.myapp.service.EntreeService;
import com.mycompany.myapp.service.dto.EntreeDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Entree}.
 */
@RestController
@RequestMapping("/api")
public class EntreeResource {

    private final Logger log = LoggerFactory.getLogger(EntreeResource.class);

    private static final String ENTITY_NAME = "entree";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntreeService entreeService;

    private final EntreeRepository entreeRepository;

    public EntreeResource(EntreeService entreeService, EntreeRepository entreeRepository) {
        this.entreeService = entreeService;
        this.entreeRepository = entreeRepository;
    }

    /**
     * {@code POST  /entrees} : Create a new entree.
     *
     * @param entreeDTO the entreeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entreeDTO, or with status {@code 400 (Bad Request)} if the entree has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entrees")
    public ResponseEntity<EntreeDTO> createEntree(@Valid @RequestBody EntreeDTO entreeDTO) throws URISyntaxException {
        log.debug("REST request to save Entree : {}", entreeDTO);
        if (entreeDTO.getId() != null) {
            throw new BadRequestAlertException("A new entree cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntreeDTO result = entreeService.save(entreeDTO);
        return ResponseEntity
            .created(new URI("/api/entrees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entrees/:id} : Updates an existing entree.
     *
     * @param id the id of the entreeDTO to save.
     * @param entreeDTO the entreeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entreeDTO,
     * or with status {@code 400 (Bad Request)} if the entreeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entreeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entrees/{id}")
    public ResponseEntity<EntreeDTO> updateEntree(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EntreeDTO entreeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Entree : {}, {}", id, entreeDTO);
        if (entreeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entreeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EntreeDTO result = entreeService.save(entreeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entreeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /entrees/:id} : Partial updates given fields of an existing entree, field will ignore if it is null
     *
     * @param id the id of the entreeDTO to save.
     * @param entreeDTO the entreeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entreeDTO,
     * or with status {@code 400 (Bad Request)} if the entreeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the entreeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entreeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/entrees/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EntreeDTO> partialUpdateEntree(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EntreeDTO entreeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Entree partially : {}, {}", id, entreeDTO);
        if (entreeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entreeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EntreeDTO> result = entreeService.partialUpdate(entreeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entreeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /entrees} : get all the entrees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entrees in body.
     */
    @GetMapping("/entrees")
    public List<EntreeDTO> getAllEntrees() {
        log.debug("REST request to get all Entrees");
        return entreeService.findAll().stream().filter(entreeDTO -> !entreeDTO.getDeleted()).collect(Collectors.toList());
    }

    /**
     * {@code GET  /entrees} : get all the entrees deleted.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entrees in body.
     */
    @GetMapping("/entrees/allDeletedList")
    public List<EntreeDTO> getAllDeletedEntrees() {
        log.debug("REST request to get all Entrees deleted");
        return entreeService.findAll().stream().filter(entreeDTO -> entreeDTO.getDeleted() == true).collect(Collectors.toList());
    }

    /**
     * {@code GET  /entrees/:id} : get the "id" entree.
     *
     * @param id the id of the entreeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entreeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entrees/{id}")
    public ResponseEntity<EntreeDTO> getEntree(@PathVariable Long id) {
        log.debug("REST request to get Entree : {}", id);
        Optional<EntreeDTO> entreeDTO = entreeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entreeDTO);
    }

    /**
     * {@code DELETE  /entrees/:id} : delete the "id" entree.
     *
     * @param id the id of the entreeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entrees/{id}")
    public ResponseEntity<Void> deleteEntree(@PathVariable Long id) {
        log.debug("REST request to delete Entree : {}", id);
        entreeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/entrees/delleteAll")
    public ResponseEntity<List<EntreeDTO>> deleteAll(@RequestBody @Valid List<EntreeDTO> entreeDTO) {
        return ResponseEntity.ok(entreeService.deleteAll(entreeDTO));
    }

    @GetMapping("/entrees/export")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; filename=entree.pdf";

        response.setHeader(headerKey, headerValue);

        List<EntreeDTO> listEntree = entreeService.findAll();

        EntreeExportPDF exporter = new EntreeExportPDF(listEntree);
        exporter.export(response);
    }
}
