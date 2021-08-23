package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Exercice;
import com.mycompany.myapp.repository.ExerciceRepository;
import com.mycompany.myapp.service.dto.ExerciceDTO;
import com.mycompany.myapp.service.mapper.ExerciceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exercice}.
 */
@Service
@Transactional
public class ExerciceService {

    private final Logger log = LoggerFactory.getLogger(ExerciceService.class);

    private final ExerciceRepository exerciceRepository;

    private final ExerciceMapper exerciceMapper;

    public ExerciceService(ExerciceRepository exerciceRepository, ExerciceMapper exerciceMapper) {
        this.exerciceRepository = exerciceRepository;
        this.exerciceMapper = exerciceMapper;
    }

    /**
     * Save a exercice.
     *
     * @param exerciceDTO the entity to save.
     * @return the persisted entity.
     */
    public ExerciceDTO save(ExerciceDTO exerciceDTO) {
        log.debug("Request to save Exercice : {}", exerciceDTO);
        Exercice exercice = exerciceMapper.toEntity(exerciceDTO);
        exercice = exerciceRepository.save(exercice);
        return exerciceMapper.toDto(exercice);
    }

    /**
     * Partially update a exercice.
     *
     * @param exerciceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExerciceDTO> partialUpdate(ExerciceDTO exerciceDTO) {
        log.debug("Request to partially update Exercice : {}", exerciceDTO);

        return exerciceRepository
            .findById(exerciceDTO.getId())
            .map(
                existingExercice -> {
                    exerciceMapper.partialUpdate(existingExercice, exerciceDTO);

                    return existingExercice;
                }
            )
            .map(exerciceRepository::save)
            .map(exerciceMapper::toDto);
    }

    /**
     * Get all the exercices.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciceDTO> findAll() {
        log.debug("Request to get all Exercices");
        return exerciceRepository.findAll().stream().map(exerciceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one exercice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExerciceDTO> findOne(Long id) {
        log.debug("Request to get Exercice : {}", id);
        return exerciceRepository.findById(id).map(exerciceMapper::toDto);
    }

    /**
     * Delete the exercice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Exercice : {}", id);
        exerciceRepository.deleteById(id);
    }

         /**
     * Delete all exercices.
     *
     */
   public List<ExerciceDTO> deleteAll(List<ExerciceDTO> exerciceDTOS) {
        exerciceDTOS.forEach(
            exerciceDTO -> {
                exerciceDTO.setDeleted(true);
            }
        );
        exerciceRepository.saveAll(exerciceMapper.toEntity(exerciceDTOS));
        return exerciceDTOS;
    }
}
