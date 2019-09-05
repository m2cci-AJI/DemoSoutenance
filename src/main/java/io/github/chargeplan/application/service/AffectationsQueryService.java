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

import io.github.chargeplan.application.domain.Affectations;
import io.github.chargeplan.application.domain.*; // for static metamodels
import io.github.chargeplan.application.repository.AffectationsRepository;
import io.github.chargeplan.application.repository.search.AffectationsSearchRepository;
import io.github.chargeplan.application.service.dto.AffectationsCriteria;

/**
 * Service for executing complex queries for Affectations entities in the database.
 * The main input is a {@link AffectationsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Affectations} or a {@link Page} of {@link Affectations} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AffectationsQueryService extends QueryService<Affectations> {

    private final Logger log = LoggerFactory.getLogger(AffectationsQueryService.class);

    private final AffectationsRepository affectationsRepository;

    private final AffectationsSearchRepository affectationsSearchRepository;

    public AffectationsQueryService(AffectationsRepository affectationsRepository, AffectationsSearchRepository affectationsSearchRepository) {
        this.affectationsRepository = affectationsRepository;
        this.affectationsSearchRepository = affectationsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Affectations} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Affectations> findByCriteria(AffectationsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Affectations> specification = createSpecification(criteria);
        return affectationsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Affectations} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Affectations> findByCriteria(AffectationsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Affectations> specification = createSpecification(criteria);
        return affectationsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AffectationsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Affectations> specification = createSpecification(criteria);
        return affectationsRepository.count(specification);
    }

    /**
     * Function to convert AffectationsCriteria to a {@link Specification}
     */
    private Specification<Affectations> createSpecification(AffectationsCriteria criteria) {
        Specification<Affectations> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Affectations_.id));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Affectations_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Affectations_.dateFin));
            }
            if (criteria.getCharge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCharge(), Affectations_.charge));
            }
            if (criteria.getCommentaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommentaire(), Affectations_.commentaire));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildSpecification(criteria.getColor(), Affectations_.color));
            }
            if (criteria.getCollaboratorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCollaboratorId(),
                    root -> root.join(Affectations_.collaborator, JoinType.LEFT).get(Collaborators_.id)));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectId(),
                    root -> root.join(Affectations_.project, JoinType.LEFT).get(Projects_.id)));
            }
        }
        return specification;
    }
}
