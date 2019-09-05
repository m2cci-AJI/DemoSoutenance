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

import io.github.chargeplan.application.domain.Collaborators;
import io.github.chargeplan.application.domain.*; // for static metamodels
import io.github.chargeplan.application.repository.CollaboratorsRepository;
import io.github.chargeplan.application.repository.search.CollaboratorsSearchRepository;
import io.github.chargeplan.application.service.dto.CollaboratorsCriteria;

/**
 * Service for executing complex queries for Collaborators entities in the database.
 * The main input is a {@link CollaboratorsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Collaborators} or a {@link Page} of {@link Collaborators} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollaboratorsQueryService extends QueryService<Collaborators> {

    private final Logger log = LoggerFactory.getLogger(CollaboratorsQueryService.class);

    private final CollaboratorsRepository collaboratorsRepository;

    private final CollaboratorsSearchRepository collaboratorsSearchRepository;

    public CollaboratorsQueryService(CollaboratorsRepository collaboratorsRepository, CollaboratorsSearchRepository collaboratorsSearchRepository) {
        this.collaboratorsRepository = collaboratorsRepository;
        this.collaboratorsSearchRepository = collaboratorsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Collaborators} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Collaborators> findByCriteria(CollaboratorsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Collaborators> specification = createSpecification(criteria);
        return collaboratorsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Collaborators} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Collaborators> findByCriteria(CollaboratorsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Collaborators> specification = createSpecification(criteria);
        return collaboratorsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollaboratorsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Collaborators> specification = createSpecification(criteria);
        return collaboratorsRepository.count(specification);
    }

    /**
     * Function to convert CollaboratorsCriteria to a {@link Specification}
     */
    private Specification<Collaborators> createSpecification(CollaboratorsCriteria criteria) {
        Specification<Collaborators> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Collaborators_.id));
            }
            if (criteria.getNomCollaborator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomCollaborator(), Collaborators_.nomCollaborator));
            }
            if (criteria.getPrenomCollaborator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenomCollaborator(), Collaborators_.prenomCollaborator));
            }
            if (criteria.getTrigramme() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrigramme(), Collaborators_.trigramme));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Collaborators_.email));
            }
            if (criteria.getCompetencies() != null) {
                specification = specification.and(buildSpecification(criteria.getCompetencies(), Collaborators_.competencies));
            }
            if (criteria.getAffectationId() != null) {
                specification = specification.and(buildSpecification(criteria.getAffectationId(),
                    root -> root.join(Collaborators_.affectations, JoinType.LEFT).get(Affectations_.id)));
            }
        }
        return specification;
    }
}
