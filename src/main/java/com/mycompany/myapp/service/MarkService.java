package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Mark;
import com.mycompany.myapp.repository.MarkRepository;
import com.mycompany.myapp.service.dto.CategoryDTO;
import com.mycompany.myapp.service.dto.MarkDTO;
import com.mycompany.myapp.service.mapper.MarkMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mark}.
 */
@Service
@Transactional
public class MarkService {

    private final Logger log = LoggerFactory.getLogger(MarkService.class);

    private final MarkRepository markRepository;

    private final MarkMapper markMapper;

    public MarkService(MarkRepository markRepository, MarkMapper markMapper) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
    }

    /**
     * Save a mark.
     *
     * @param markDTO the entity to save.
     * @return the persisted entity.
     */
    public MarkDTO save(MarkDTO markDTO) {
        log.debug("Request to save Mark : {}", markDTO);
        Mark mark = markMapper.toEntity(markDTO);
        mark = markRepository.save(mark);
        return markMapper.toDto(mark);
    }

    /**
     * Partially update a mark.
     *
     * @param markDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MarkDTO> partialUpdate(MarkDTO markDTO) {
        log.debug("Request to partially update Mark : {}", markDTO);

        return markRepository
            .findById(markDTO.getId())
            .map(
                existingMark -> {
                    markMapper.partialUpdate(existingMark, markDTO);

                    return existingMark;
                }
            )
            .map(markRepository::save)
            .map(markMapper::toDto);
    }

    /**
     * Get all the marks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MarkDTO> findAll() {
        log.debug("Request to get all Marks");
        return markRepository.findAll().stream().map(markMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one mark by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MarkDTO> findOne(Long id) {
        log.debug("Request to get Mark : {}", id);
        return markRepository.findById(id).map(markMapper::toDto);
    }

    /**
     * Delete the mark by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mark : {}", id);
        markRepository.deleteById(id);
    }

    /**
     * Delete all Marks.
     *
     */
    public List<MarkDTO> deleteAll(List<MarkDTO> markDTOS) {
        markDTOS.forEach(
            markDTOO -> {
                markDTOO.setDeleted(true);
            }
        );
        markRepository.saveAll(markMapper.toEntity(markDTOS));
        return markDTOS;
    }
}
