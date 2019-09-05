package io.github.chargeplan.application.service.impl;

import io.github.chargeplan.application.service.ProjectsService;
import io.github.chargeplan.application.domain.Projects;
import io.github.chargeplan.application.repository.ProjectsRepository;
import io.github.chargeplan.application.repository.search.ProjectsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Projects.
 */
@Service
@Transactional
public class ProjectsServiceImpl implements ProjectsService {

    private final Logger log = LoggerFactory.getLogger(ProjectsServiceImpl.class);

    private final ProjectsRepository projectsRepository;

    private final ProjectsSearchRepository projectsSearchRepository;

    public ProjectsServiceImpl(ProjectsRepository projectsRepository, ProjectsSearchRepository projectsSearchRepository) {
        this.projectsRepository = projectsRepository;
        this.projectsSearchRepository = projectsSearchRepository;
    }

    /**
     * Save a projects.
     *
     * @param projects the entity to save
     * @return the persisted entity
     */
    @Override
    public Projects save(Projects projects) {
        log.debug("Request to save Projects : {}", projects);
        Projects result = projectsRepository.save(projects);
        projectsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Projects> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectsRepository.findAll(pageable);
    }


    /**
     * Get one projects by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Projects> findOne(Long id) {
        log.debug("Request to get Projects : {}", id);
        return projectsRepository.findById(id);
    }

    /**
     * Delete the projects by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Projects : {}", id);
        projectsRepository.deleteById(id);
        projectsSearchRepository.deleteById(id);
    }

    /**
     * Search for the projects corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Projects> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Projects for query {}", query);
        return projectsSearchRepository.search(queryStringQuery(query), pageable);    }
}
