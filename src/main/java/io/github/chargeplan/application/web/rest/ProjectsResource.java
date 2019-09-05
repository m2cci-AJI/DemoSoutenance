package io.github.chargeplan.application.web.rest;
import io.github.chargeplan.application.domain.Projects;
import io.github.chargeplan.application.service.ProjectsService;
import io.github.chargeplan.application.web.rest.errors.BadRequestAlertException;
import io.github.chargeplan.application.web.rest.util.HeaderUtil;
import io.github.chargeplan.application.web.rest.util.PaginationUtil;
import io.github.chargeplan.application.service.dto.ProjectsCriteria;
import io.github.chargeplan.application.service.ProjectsQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Projects.
 */
@RestController
@RequestMapping("/api")
public class ProjectsResource {

    private final Logger log = LoggerFactory.getLogger(ProjectsResource.class);

    private static final String ENTITY_NAME = "projects";

    private final ProjectsService projectsService;

    private final ProjectsQueryService projectsQueryService;

    public ProjectsResource(ProjectsService projectsService, ProjectsQueryService projectsQueryService) {
        this.projectsService = projectsService;
        this.projectsQueryService = projectsQueryService;
    }

    /**
     * POST  /projects : Create a new projects.
     *
     * @param projects the projects to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projects, or with status 400 (Bad Request) if the projects has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    public ResponseEntity<Projects> createProjects(@Valid @RequestBody Projects projects) throws URISyntaxException {
        log.debug("REST request to save Projects : {}", projects);
        if (projects.getId() != null) {
            throw new BadRequestAlertException("A new projects cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Projects result = projectsService.save(projects);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing projects.
     *
     * @param projects the projects to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projects,
     * or with status 400 (Bad Request) if the projects is not valid,
     * or with status 500 (Internal Server Error) if the projects couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    public ResponseEntity<Projects> updateProjects(@Valid @RequestBody Projects projects) throws URISyntaxException {
        log.debug("REST request to update Projects : {}", projects);
        if (projects.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Projects result = projectsService.save(projects);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projects.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects")
    public ResponseEntity<List<Projects>> getAllProjects(ProjectsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Projects by criteria: {}", criteria);
        Page<Projects> page = projectsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /projects/count : count all the projects.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/projects/count")
    public ResponseEntity<Long> countProjects(ProjectsCriteria criteria) {
        log.debug("REST request to count Projects by criteria: {}", criteria);
        return ResponseEntity.ok().body(projectsQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /projects/:id : get the "id" projects.
     *
     * @param id the id of the projects to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projects, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<Projects> getProjects(@PathVariable Long id) {
        log.debug("REST request to get Projects : {}", id);
        Optional<Projects> projects = projectsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projects);
    }

    /**
     * DELETE  /projects/:id : delete the "id" projects.
     *
     * @param id the id of the projects to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProjects(@PathVariable Long id) {
        log.debug("REST request to delete Projects : {}", id);
        projectsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/projects?query=:query : search for the projects corresponding
     * to the query.
     *
     * @param query the query of the projects search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/projects")
    public ResponseEntity<List<Projects>> searchProjects(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Projects for query {}", query);
        Page<Projects> page = projectsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/projects");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
