package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Article;
import com.mycompany.myapp.repository.ArticleRepository;
import com.mycompany.myapp.service.dto.ArticleDTO;
import com.mycompany.myapp.service.mapper.ArticleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    /**
     * Save a article.
     *
     * @param articleDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    /**
     * Partially update a article.
     *
     * @param articleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArticleDTO> partialUpdate(ArticleDTO articleDTO) {
        log.debug("Request to partially update Article : {}", articleDTO);

        return articleRepository
            .findById(articleDTO.getId())
            .map(
                existingArticle -> {
                    articleMapper.partialUpdate(existingArticle, articleDTO);

                    return existingArticle;
                }
            )
            .map(articleRepository::save)
            .map(articleMapper::toDto);
    }

    /**
     * Get all the articles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> findAll() {
        log.debug("Request to get all Articles");
        return articleRepository.findAll().stream().map(articleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one article by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id).map(articleMapper::toDto);
    }

    /**
     * Delete the article by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
    }

    /**
     * Delete all articles.
     *
     */
    public List<ArticleDTO> deleteAll(List<ArticleDTO> articleDTOS) {
        articleDTOS.forEach(
            articleDTO -> {
                articleDTO.setDeleted(true);
            }
        );
        articleRepository.saveAll(articleMapper.toEntity(articleDTOS));
        return articleDTOS;
    }
}
