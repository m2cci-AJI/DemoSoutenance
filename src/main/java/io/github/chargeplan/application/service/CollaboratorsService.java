package io.github.chargeplan.application.service;

import io.github.chargeplan.application.domain.Collaborators;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Collaborators.
 */
public interface CollaboratorsService {

    /**
     * Save a collaborators.
     *
     * @param collaborators the entity to save
     * @return the persisted entity
     */
    Collaborators save(Collaborators collaborators);

    /**
     * Get all the collaborators.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Collaborators> findAll(Pageable pageable);


    /**
     * Get the "id" collaborators.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Collaborators> findOne(Long id);

    /**
     * Delete the "id" collaborators.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the collaborators corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Collaborators> search(String query, Pageable pageable);
}
