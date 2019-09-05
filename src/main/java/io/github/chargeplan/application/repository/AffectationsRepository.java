package io.github.chargeplan.application.repository;

import io.github.chargeplan.application.domain.Affectations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Affectations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationsRepository extends JpaRepository<Affectations, Long>, JpaSpecificationExecutor<Affectations> {

}
