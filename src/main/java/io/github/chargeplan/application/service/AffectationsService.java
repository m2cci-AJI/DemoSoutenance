package io.github.chargeplan.application.service;

import io.github.chargeplan.application.domain.Affectations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Affectations.
 */
public interface AffectationsService {

    /**
     * Save a affectations.
     *
     * @param affectations the entity to save
     * @return the persisted entity
     */
    Affectations save(Affectations affectations);

    /**
     * Get all the affectations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Affectations> findAll(Pageable pageable);


    /**
     * Get the "id" affectations.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Affectations> findOne(Long id);

    /**
     * Delete the "id" affectations.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the affectations corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Affectations> search(String query, Pageable pageable);
}
