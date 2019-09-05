package io.github.chargeplan.application.service;

import io.github.chargeplan.application.domain.Projects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Projects.
 */
public interface ProjectsService {

    /**
     * Save a projects.
     *
     * @param projects the entity to save
     * @return the persisted entity
     */
    Projects save(Projects projects);

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Projects> findAll(Pageable pageable);


    /**
     * Get the "id" projects.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Projects> findOne(Long id);

    /**
     * Delete the "id" projects.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the projects corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Projects> search(String query, Pageable pageable);
}
