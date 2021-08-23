package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Entree;
import com.mycompany.myapp.repository.EntreeRepository;
import com.mycompany.myapp.service.dto.EntreeDTO;
import com.mycompany.myapp.service.mapper.EntreeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Entree}.
 */
@Service
@Transactional
public class EntreeService {

    private final Logger log = LoggerFactory.getLogger(EntreeService.class);

    private final EntreeRepository entreeRepository;

    private final EntreeMapper entreeMapper;

    public EntreeService(EntreeRepository entreeRepository, EntreeMapper entreeMapper) {
        this.entreeRepository = entreeRepository;
        this.entreeMapper = entreeMapper;
    }

    /**
     * Save a entree.
     *
     * @param entreeDTO the entity to save.
     * @return the persisted entity.
     */
    public EntreeDTO save(EntreeDTO entreeDTO) {
        log.debug("Request to save Entree : {}", entreeDTO);
        Entree entree = entreeMapper.toEntity(entreeDTO);
        entree = entreeRepository.save(entree);
        return entreeMapper.toDto(entree);
    }

    /**
     * Partially update a entree.
     *
     * @param entreeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EntreeDTO> partialUpdate(EntreeDTO entreeDTO) {
        log.debug("Request to partially update Entree : {}", entreeDTO);

        return entreeRepository
            .findById(entreeDTO.getId())
            .map(
                existingEntree -> {
                    entreeMapper.partialUpdate(existingEntree, entreeDTO);

                    return existingEntree;
                }
            )
            .map(entreeRepository::save)
            .map(entreeMapper::toDto);
    }

    /**
     * Get all the entrees.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EntreeDTO> findAll() {
        log.debug("Request to get all Entrees");
        return entreeRepository.findAll().stream().map(entreeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one entree by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntreeDTO> findOne(Long id) {
        log.debug("Request to get Entree : {}", id);
        return entreeRepository.findById(id).map(entreeMapper::toDto);
    }

    /**
     * Delete the entree by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Entree : {}", id);
        entreeRepository.deleteById(id);
    }

    /**
     * Delete all entrees.
     *
     */
    public List<EntreeDTO> deleteAll(List<EntreeDTO> entreeDTOS) {
        entreeDTOS.forEach(
            entreDTO -> {
                entreDTO.setDeleted(true);
            }
        );
        entreeRepository.saveAll(entreeMapper.toEntity(entreeDTOS));
        return entreeDTOS;
    }
}
