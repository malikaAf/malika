package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ParametreRepository;
import com.mycompany.myapp.service.ParametreService;
import com.mycompany.myapp.service.dto.ParametreDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Parametre}.
 */
@RestController
@RequestMapping("/api")
public class ParametreResource {

    private final Logger log = LoggerFactory.getLogger(ParametreResource.class);

    private static final String ENTITY_NAME = "parametre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametreService parametreService;

    private final ParametreRepository parametreRepository;

    public ParametreResource(ParametreService parametreService, ParametreRepository parametreRepository) {
        this.parametreService = parametreService;
        this.parametreRepository = parametreRepository;
    }

    /**
     * {@code POST  /parametres} : Create a new parametre.
     *
     * @param parametreDTO the parametreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametreDTO, or with status {@code 400 (Bad Request)} if the parametre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parametres")
    public ResponseEntity<ParametreDTO> createParametre(@Valid @RequestBody ParametreDTO parametreDTO) throws URISyntaxException {
        log.debug("REST request to save Parametre : {}", parametreDTO);
        if (parametreDTO.getId() != null) {
            throw new BadRequestAlertException("A new parametre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParametreDTO result = parametreService.save(parametreDTO);
        return ResponseEntity
            .created(new URI("/api/parametres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parametres/:id} : Updates an existing parametre.
     *
     * @param id the id of the parametreDTO to save.
     * @param parametreDTO the parametreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametreDTO,
     * or with status {@code 400 (Bad Request)} if the parametreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parametres/{id}")
    public ResponseEntity<ParametreDTO> updateParametre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParametreDTO parametreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Parametre : {}, {}", id, parametreDTO);
        if (parametreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParametreDTO result = parametreService.save(parametreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parametres/:id} : Partial updates given fields of an existing parametre, field will ignore if it is null
     *
     * @param id the id of the parametreDTO to save.
     * @param parametreDTO the parametreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametreDTO,
     * or with status {@code 400 (Bad Request)} if the parametreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parametreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parametres/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ParametreDTO> partialUpdateParametre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParametreDTO parametreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parametre partially : {}, {}", id, parametreDTO);
        if (parametreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParametreDTO> result = parametreService.partialUpdate(parametreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parametres} : get all the parametres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametres in body.
     */
    @GetMapping("/parametres")
    public List<ParametreDTO> getAllParametres() {
        log.debug("REST request to get all Parametres");
        return parametreService.findAll().stream().filter(parametreDTO -> !parametreDTO.getDeleted()).collect(Collectors.toList());
    }

    /**
     * {@code GET  /parametres/:id} : get the "id" parametre.
     *
     * @param id the id of the parametreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parametres/{id}")
    public ResponseEntity<ParametreDTO> getParametre(@PathVariable Long id) {
        log.debug("REST request to get Parametre : {}", id);
        Optional<ParametreDTO> parametreDTO = parametreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parametreDTO);
    }

    /**
     * {@code DELETE  /parametres/:id} : delete the "id" parametre.
     *
     * @param id the id of the parametreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parametres/{id}")
    public ResponseEntity<Void> deleteParametre(@PathVariable Long id) {
        log.debug("REST request to delete Parametre : {}", id);
        parametreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/parametres/delleteAll")
    public ResponseEntity<List<ParametreDTO>> deleteAll(@RequestBody @Valid List<ParametreDTO> parametreDTO) {
        return ResponseEntity.ok(parametreService.deleteAll(parametreDTO));
    }
}
