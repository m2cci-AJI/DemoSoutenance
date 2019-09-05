package io.github.chargeplan.application.repository;

import io.github.chargeplan.application.domain.Collaborators;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Collaborators entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollaboratorsRepository extends JpaRepository<Collaborators, Long>, JpaSpecificationExecutor<Collaborators> {

}
