package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Parametre;
import com.mycompany.myapp.repository.ParametreRepository;
import com.mycompany.myapp.service.dto.ParametreDTO;
import com.mycompany.myapp.service.mapper.ParametreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parametre}.
 */
@Service
@Transactional
public class ParametreService {

    private final Logger log = LoggerFactory.getLogger(ParametreService.class);

    private final ParametreRepository parametreRepository;

    private final ParametreMapper parametreMapper;

    public ParametreService(ParametreRepository parametreRepository, ParametreMapper parametreMapper) {
        this.parametreRepository = parametreRepository;
        this.parametreMapper = parametreMapper;
    }

    /**
     * Save a parametre.
     *
     * @param parametreDTO the entity to save.
     * @return the persisted entity.
     */
    public ParametreDTO save(ParametreDTO parametreDTO) {
        log.debug("Request to save Parametre : {}", parametreDTO);
        Parametre parametre = parametreMapper.toEntity(parametreDTO);
        parametre = parametreRepository.save(parametre);
        return parametreMapper.toDto(parametre);
    }

    /**
     * Partially update a parametre.
     *
     * @param parametreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParametreDTO> partialUpdate(ParametreDTO parametreDTO) {
        log.debug("Request to partially update Parametre : {}", parametreDTO);

        return parametreRepository
            .findById(parametreDTO.getId())
            .map(
                existingParametre -> {
                    parametreMapper.partialUpdate(existingParametre, parametreDTO);

                    return existingParametre;
                }
            )
            .map(parametreRepository::save)
            .map(parametreMapper::toDto);
    }

    /**
     * Get all the parametres.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParametreDTO> findAll() {
        log.debug("Request to get all Parametres");
        return parametreRepository.findAll().stream().map(parametreMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one parametre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParametreDTO> findOne(Long id) {
        log.debug("Request to get Parametre : {}", id);
        return parametreRepository.findById(id).map(parametreMapper::toDto);
    }

    /**
     * Delete the parametre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parametre : {}", id);
        parametreRepository.deleteById(id);
    }


    /**
     * Delete all parametres.
     *
     */
    public List<ParametreDTO> deleteAll(List<ParametreDTO> parametreDTOS) {
        parametreDTOS.forEach(
            parametreDTO -> {
                parametreDTO.setDeleted(true);
            }
        );
        parametreRepository.saveAll(parametreMapper.toEntity(parametreDTOS));
        return parametreDTOS;
    }




}
