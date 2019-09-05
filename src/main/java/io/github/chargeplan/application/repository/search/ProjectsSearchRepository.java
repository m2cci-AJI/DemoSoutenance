package io.github.chargeplan.application.repository.search;

import io.github.chargeplan.application.domain.Projects;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Projects entity.
 */
public interface ProjectsSearchRepository extends ElasticsearchRepository<Projects, Long> {
}
