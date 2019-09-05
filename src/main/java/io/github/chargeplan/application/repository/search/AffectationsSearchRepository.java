package io.github.chargeplan.application.repository.search;

import io.github.chargeplan.application.domain.Affectations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Affectations entity.
 */
public interface AffectationsSearchRepository extends ElasticsearchRepository<Affectations, Long> {
}
