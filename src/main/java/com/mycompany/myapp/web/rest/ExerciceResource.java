package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ExerciceRepository;
import com.mycompany.myapp.service.ExerciceService;
import com.mycompany.myapp.service.dto.ExerciceDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Exercice}.
 */
@RestController
@RequestMapping("/api")
public class ExerciceResource {

    private final Logger log = LoggerFactory.getLogger(ExerciceResource.class);

    private static final String ENTITY_NAME = "exercice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciceService exerciceService;

    private final ExerciceRepository exerciceRepository;

    public ExerciceResource(ExerciceService exerciceService, ExerciceRepository exerciceRepository) {
        this.exerciceService = exerciceService;
        this.exerciceRepository = exerciceRepository;
    }

    /**
     * {@code POST  /exercices} : Create a new exercice.
     *
     * @param exerciceDTO the exerciceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciceDTO, or with status {@code 400 (Bad Request)} if the exercice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercices")
    public ResponseEntity<ExerciceDTO> createExercice(@Valid @RequestBody ExerciceDTO exerciceDTO) throws URISyntaxException {
        log.debug("REST request to save Exercice : {}", exerciceDTO);
        if (exerciceDTO.getId() != null) {
            throw new BadRequestAlertException("A new exercice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciceDTO result = exerciceService.save(exerciceDTO);
        return ResponseEntity
            .created(new URI("/api/exercices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercices/:id} : Updates an existing exercice.
     *
     * @param id the id of the exerciceDTO to save.
     * @param exerciceDTO the exerciceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciceDTO,
     * or with status {@code 400 (Bad Request)} if the exerciceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercices/{id}")
    public ResponseEntity<ExerciceDTO> updateExercice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciceDTO exerciceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Exercice : {}, {}", id, exerciceDTO);
        if (exerciceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciceDTO result = exerciceService.save(exerciceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercices/:id} : Partial updates given fields of an existing exercice, field will ignore if it is null
     *
     * @param id the id of the exerciceDTO to save.
     * @param exerciceDTO the exerciceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciceDTO,
     * or with status {@code 400 (Bad Request)} if the exerciceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exerciceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PatchMapping(value = "/exercices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExerciceDTO> partialUpdateExercice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciceDTO exerciceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exercice partially : {}, {}", id, exerciceDTO);
        if (exerciceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciceDTO> result = exerciceService.partialUpdate(exerciceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exercices} : get all the exercices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exercices in body.
     */
    @GetMapping("/exercices")
    public List<ExerciceDTO> getAllExercices() {
        log.debug("REST request to get all Exercices");
        return exerciceService.findAll().stream().filter(exerciceDTO -> !exerciceDTO.getDeleted()).collect(Collectors.toList());
    }

    /**
     * {@code GET  /exercices/:id} : get the "id" exercice.
     *
     * @param id the id of the exerciceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercices/{id}")
    public ResponseEntity<ExerciceDTO> getExercice(@PathVariable Long id) {
        log.debug("REST request to get Exercice : {}", id);
        Optional<ExerciceDTO> exerciceDTO = exerciceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciceDTO);
    }

    /**
     * {@code DELETE  /exercices/:id} : delete the "id" exercice.
     *
     * @param id the id of the exerciceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercices/{id}")
    public ResponseEntity<Void> deleteExercice(@PathVariable Long id) {
        log.debug("REST request to delete Exercice : {}", id);
        exerciceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

@PutMapping("/exercices/delleteAll")
    public ResponseEntity<List<ExerciceDTO>> deleteAll(@RequestBody @Valid List<ExerciceDTO> exerciceDTOS) {
        return ResponseEntity.ok(exerciceService.deleteAll(exerciceDTOS));
    }

}
