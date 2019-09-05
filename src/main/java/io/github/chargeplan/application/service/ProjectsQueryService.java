package io.github.chargeplan.application.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.chargeplan.application.domain.Projects;
import io.github.chargeplan.application.domain.*; // for static metamodels
import io.github.chargeplan.application.repository.ProjectsRepository;
import io.github.chargeplan.application.repository.search.ProjectsSearchRepository;
import io.github.chargeplan.application.service.dto.ProjectsCriteria;

/**
 * Service for executing complex queries for Projects entities in the database.
 * The main input is a {@link ProjectsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Projects} or a {@link Page} of {@link Projects} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectsQueryService extends QueryService<Projects> {

    private final Logger log = LoggerFactory.getLogger(ProjectsQueryService.class);

    private final ProjectsRepository projectsRepository;

    private final ProjectsSearchRepository projectsSearchRepository;

    public ProjectsQueryService(ProjectsRepository projectsRepository, ProjectsSearchRepository projectsSearchRepository) {
        this.projectsRepository = projectsRepository;
        this.projectsSearchRepository = projectsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Projects} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Projects> findByCriteria(ProjectsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Projects> specification = createSpecification(criteria);
        return projectsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Projects} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Projects> findByCriteria(ProjectsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Projects> specification = createSpecification(criteria);
        return projectsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProjectsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Projects> specification = createSpecification(criteria);
        return projectsRepository.count(specification);
    }

    /**
     * Function to convert ProjectsCriteria to a {@link Specification}
     */
    private Specification<Projects> createSpecification(ProjectsCriteria criteria) {
        Specification<Projects> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Projects_.id));
            }
            if (criteria.getNameProject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameProject(), Projects_.nameProject));
            }
            if (criteria.getProjectCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectCode(), Projects_.projectCode));
            }
            if (criteria.getClient() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClient(), Projects_.client));
            }
            if (criteria.getdP() != null) {
                specification = specification.and(buildStringSpecification(criteria.getdP(), Projects_.dP));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Projects_.description));
            }
            if (criteria.getAffectationId() != null) {
                specification = specification.and(buildSpecification(criteria.getAffectationId(),
                    root -> root.join(Projects_.affectations, JoinType.LEFT).get(Affectations_.id)));
            }
        }
        return specification;
    }
}
