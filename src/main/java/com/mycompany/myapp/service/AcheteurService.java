package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Acheteur;
import com.mycompany.myapp.repository.AcheteurRepository;
import com.mycompany.myapp.service.dto.AcheteurDTO;
import com.mycompany.myapp.service.mapper.AcheteurMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Acheteur}.
 */
@Service
@Transactional
public class AcheteurService {

    private final Logger log = LoggerFactory.getLogger(AcheteurService.class);

    private final AcheteurRepository acheteurRepository;

    private final AcheteurMapper acheteurMapper;

    public AcheteurService(AcheteurRepository acheteurRepository, AcheteurMapper acheteurMapper) {
        this.acheteurRepository = acheteurRepository;
        this.acheteurMapper = acheteurMapper;
    }

    /**
     * Save a acheteur.
     *
     * @param acheteurDTO the entity to save.
     * @return the persisted entity.
     */
    public AcheteurDTO save(AcheteurDTO acheteurDTO) {
        log.debug("Request to save Acheteur : {}", acheteurDTO);
        Acheteur acheteur = acheteurMapper.toEntity(acheteurDTO);
        acheteur = acheteurRepository.save(acheteur);
        return acheteurMapper.toDto(acheteur);
    }

    /**
     * Partially update a acheteur.
     *
     * @param acheteurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AcheteurDTO> partialUpdate(AcheteurDTO acheteurDTO) {
        log.debug("Request to partially update Acheteur : {}", acheteurDTO);

        return acheteurRepository
            .findById(acheteurDTO.getId())
            .map(
                existingAcheteur -> {
                    acheteurMapper.partialUpdate(existingAcheteur, acheteurDTO);

                    return existingAcheteur;
                }
            )
            .map(acheteurRepository::save)
            .map(acheteurMapper::toDto);
    }

    /**
     * Get all the acheteurs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AcheteurDTO> findAll() {
        log.debug("Request to get all Acheteurs");
        return acheteurRepository.findAll().stream().map(acheteurMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one acheteur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AcheteurDTO> findOne(Long id) {
        log.debug("Request to get Acheteur : {}", id);
        return acheteurRepository.findById(id).map(acheteurMapper::toDto);
    }

    /**
     * Delete the acheteur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Acheteur : {}", id);
        acheteurRepository.deleteById(id);
    }

    /**
     * Delete all acheteurs.
     *
     */
    public List<AcheteurDTO> deleteAll(List<AcheteurDTO> acheteurDTOS) {
        acheteurDTOS.forEach(
            acheteurDTO -> {
                acheteurDTO.setDeleted(true);
            }
        );
        acheteurRepository.saveAll(acheteurMapper.toEntity(acheteurDTOS));
        return acheteurDTOS;
    }
}
