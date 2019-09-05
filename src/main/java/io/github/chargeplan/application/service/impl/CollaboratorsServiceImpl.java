package io.github.chargeplan.application.service.impl;

import io.github.chargeplan.application.service.CollaboratorsService;
import io.github.chargeplan.application.domain.Collaborators;
import io.github.chargeplan.application.repository.CollaboratorsRepository;
import io.github.chargeplan.application.repository.search.CollaboratorsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Collaborators.
 */
@Service
@Transactional
public class CollaboratorsServiceImpl implements CollaboratorsService {

    private final Logger log = LoggerFactory.getLogger(CollaboratorsServiceImpl.class);

    private final CollaboratorsRepository collaboratorsRepository;

    private final CollaboratorsSearchRepository collaboratorsSearchRepository;

    public CollaboratorsServiceImpl(CollaboratorsRepository collaboratorsRepository, CollaboratorsSearchRepository collaboratorsSearchRepository) {
        this.collaboratorsRepository = collaboratorsRepository;
        this.collaboratorsSearchRepository = collaboratorsSearchRepository;
    }

    /**
     * Save a collaborators.
     *
     * @param collaborators the entity to save
     * @return the persisted entity
     */
    @Override
    public Collaborators save(Collaborators collaborators) {
        log.debug("Request to save Collaborators : {}", collaborators);
        Collaborators result = collaboratorsRepository.save(collaborators);
        collaboratorsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the collaborators.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Collaborators> findAll(Pageable pageable) {
        log.debug("Request to get all Collaborators");
        return collaboratorsRepository.findAll(pageable);
    }


    /**
     * Get one collaborators by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Collaborators> findOne(Long id) {
        log.debug("Request to get Collaborators : {}", id);
        return collaboratorsRepository.findById(id);
    }

    /**
     * Delete the collaborators by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Collaborators : {}", id);
        collaboratorsRepository.deleteById(id);
        collaboratorsSearchRepository.deleteById(id);
    }

    /**
     * Search for the collaborators corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Collaborators> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Collaborators for query {}", query);
        return collaboratorsSearchRepository.search(queryStringQuery(query), pageable);    }
}
