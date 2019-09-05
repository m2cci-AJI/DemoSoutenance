package io.github.chargeplan.application.repository.search;

import io.github.chargeplan.application.domain.Collaborators;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Collaborators entity.
 */
public interface CollaboratorsSearchRepository extends ElasticsearchRepository<Collaborators, Long> {
}
