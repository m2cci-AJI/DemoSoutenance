package io.github.chargeplan.application.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ProjectsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProjectsSearchRepositoryMockConfiguration {

    @MockBean
    private ProjectsSearchRepository mockProjectsSearchRepository;

}
