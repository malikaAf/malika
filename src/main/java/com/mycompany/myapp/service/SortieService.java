package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Sortie;
import com.mycompany.myapp.repository.EntreeRepository;
import com.mycompany.myapp.repository.SortieRepository;
import com.mycompany.myapp.service.dto.EntreeDTO;
import com.mycompany.myapp.service.dto.SortieDTO;
import com.mycompany.myapp.service.mapper.EntreeMapper;
import com.mycompany.myapp.service.mapper.SortieMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sortie}.
 */
@Service
@Transactional
public class SortieService {

    private final Logger log = LoggerFactory.getLogger(SortieService.class);

    private final SortieRepository sortieRepository;

    private final SortieMapper sortieMapper;

    @Autowired
    EntreeService entreeService;

    @Autowired
    EntreeRepository entreeRepository;

    @Autowired
    EntreeMapper entreeMapper;

    @Autowired
    SortieMapper getSortieMapper;

    public SortieService(SortieRepository sortieRepository, SortieMapper sortieMapper) {
        this.sortieRepository = sortieRepository;
        this.sortieMapper = sortieMapper;
    }

    /**
     * Save a sortie.
     *
     * @param sortieDTO the entity to save.
     * @return the persisted entity.
     */
    public SortieDTO save(SortieDTO sortieDTO) {
        log.debug("Request to save Sortie : {}", sortieDTO);

        if (sortieDTO.getEntree().getRestant() == null) {
            sortieDTO.getEntree().setRestant(sortieDTO.getEntree().getQuantite() - sortieDTO.getQuantite());
        } else {
            sortieDTO.getEntree().setRestant(sortieDTO.getEntree().getRestant() - sortieDTO.getQuantite());
        }

        System.out.println("=======================");
        System.out.println(sortieDTO.getEntree().getRestant());
        System.out.println("=======================");
        if (sortieDTO.getEntree().getRestant() >= 0) {
            entreeRepository.save(entreeMapper.toEntity(sortieDTO.getEntree()));
            System.out.println("======bien=================");
            System.out.println(sortieDTO.getEntree().getRestant());
            System.out.println("=======================");
            Sortie sortie = sortieMapper.toEntity(sortieDTO);
            sortie = sortieRepository.save(sortie);
            return sortieMapper.toDto(sortie);
        }

        return null;
    }

    /**
     * Partially update a sortie.
     *
     * @param sortieDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SortieDTO> partialUpdate(SortieDTO sortieDTO) {
        log.debug("Request to partially update Sortie : {}", sortieDTO);

        return sortieRepository
            .findById(sortieDTO.getId())
            .map(
                existingSortie -> {
                    sortieMapper.partialUpdate(existingSortie, sortieDTO);

                    return existingSortie;
                }
            )
            .map(sortieRepository::save)
            .map(sortieMapper::toDto);
    }

    /**
     * Get all the sorties.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SortieDTO> findAll() {
        log.debug("Request to get all Sorties");
        return sortieRepository.findAll().stream().map(sortieMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sortie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SortieDTO> findOne(Long id) {
        log.debug("Request to get Sortie : {}", id);
        return sortieRepository.findById(id).map(sortieMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Sortie : {}", id);
        sortieRepository.deleteById(id);
    }

    /**
     * Delete all sorties.
     *
     */
    @Transactional(readOnly = true)
    public List<SortieDTO> deleteAll(List<SortieDTO> sortieDTOS) {
        sortieDTOS.forEach(
            sortiDTO -> {
                sortiDTO.setDeleted(true);
            }
        );
        sortieRepository.saveAll(sortieMapper.toEntity(sortieDTOS));
        return sortieDTOS;
    }
}
