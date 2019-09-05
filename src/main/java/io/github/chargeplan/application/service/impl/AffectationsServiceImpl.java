package io.github.chargeplan.application.service.impl;

import io.github.chargeplan.application.service.AffectationsService;
import io.github.chargeplan.application.domain.Affectations;
import io.github.chargeplan.application.repository.AffectationsRepository;
import io.github.chargeplan.application.repository.search.AffectationsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Affectations.
 */
@Service
@Transactional
public class AffectationsServiceImpl implements AffectationsService {

    private final Logger log = LoggerFactory.getLogger(AffectationsServiceImpl.class);

    private final AffectationsRepository affectationsRepository;

    private final AffectationsSearchRepository affectationsSearchRepository;

    public AffectationsServiceImpl(AffectationsRepository affectationsRepository, AffectationsSearchRepository affectationsSearchRepository) {
        this.affectationsRepository = affectationsRepository;
        this.affectationsSearchRepository = affectationsSearchRepository;
    }

    /**
     * Save a affectations.
     *
     * @param affectations the entity to save
     * @return the persisted entity
     */
    @Override
    public Affectations save(Affectations affectations) {
        log.debug("Request to save Affectations : {}", affectations);
        Affectations result = affectationsRepository.save(affectations);
        affectationsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the affectations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Affectations> findAll(Pageable pageable) {
        log.debug("Request to get all Affectations");
        return affectationsRepository.findAll(pageable);
    }


    /**
     * Get one affectations by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Affectations> findOne(Long id) {
        log.debug("Request to get Affectations : {}", id);
        return affectationsRepository.findById(id);
    }

    /**
     * Delete the affectations by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Affectations : {}", id);
        affectationsRepository.deleteById(id);
        affectationsSearchRepository.deleteById(id);
    }

    /**
     * Search for the affectations corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Affectations> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Affectations for query {}", query);
        return affectationsSearchRepository.search(queryStringQuery(query), pageable);    }
}
