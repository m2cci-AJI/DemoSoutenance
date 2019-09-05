package io.github.chargeplan.application.web.rest;
import io.github.chargeplan.application.domain.Collaborators;
import io.github.chargeplan.application.service.CollaboratorsService;
import io.github.chargeplan.application.web.rest.errors.BadRequestAlertException;
import io.github.chargeplan.application.web.rest.util.HeaderUtil;
import io.github.chargeplan.application.web.rest.util.PaginationUtil;
import io.github.chargeplan.application.service.dto.CollaboratorsCriteria;
import io.github.chargeplan.application.service.CollaboratorsQueryService;
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
 * REST controller for managing Collaborators.
 */
@RestController
@RequestMapping("/api")
public class CollaboratorsResource {

    private final Logger log = LoggerFactory.getLogger(CollaboratorsResource.class);

    private static final String ENTITY_NAME = "collaborators";

    private final CollaboratorsService collaboratorsService;

    private final CollaboratorsQueryService collaboratorsQueryService;

    public CollaboratorsResource(CollaboratorsService collaboratorsService, CollaboratorsQueryService collaboratorsQueryService) {
        this.collaboratorsService = collaboratorsService;
        this.collaboratorsQueryService = collaboratorsQueryService;
    }

    /**
     * POST  /collaborators : Create a new collaborators.
     *
     * @param collaborators the collaborators to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collaborators, or with status 400 (Bad Request) if the collaborators has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/collaborators")
    public ResponseEntity<Collaborators> createCollaborators(@Valid @RequestBody Collaborators collaborators) throws URISyntaxException {
        log.debug("REST request to save Collaborators : {}", collaborators);
        if (collaborators.getId() != null) {
            throw new BadRequestAlertException("A new collaborators cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Collaborators result = collaboratorsService.save(collaborators);
        return ResponseEntity.created(new URI("/api/collaborators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collaborators : Updates an existing collaborators.
     *
     * @param collaborators the collaborators to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collaborators,
     * or with status 400 (Bad Request) if the collaborators is not valid,
     * or with status 500 (Internal Server Error) if the collaborators couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/collaborators")
    public ResponseEntity<Collaborators> updateCollaborators(@Valid @RequestBody Collaborators collaborators) throws URISyntaxException {
        log.debug("REST request to update Collaborators : {}", collaborators);
        if (collaborators.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Collaborators result = collaboratorsService.save(collaborators);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, collaborators.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collaborators : get all the collaborators.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of collaborators in body
     */
    @GetMapping("/collaborators")
    public ResponseEntity<List<Collaborators>> getAllCollaborators(CollaboratorsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Collaborators by criteria: {}", criteria);
        Page<Collaborators> page = collaboratorsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/collaborators");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /collaborators/count : count all the collaborators.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/collaborators/count")
    public ResponseEntity<Long> countCollaborators(CollaboratorsCriteria criteria) {
        log.debug("REST request to count Collaborators by criteria: {}", criteria);
        return ResponseEntity.ok().body(collaboratorsQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /collaborators/:id : get the "id" collaborators.
     *
     * @param id the id of the collaborators to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collaborators, or with status 404 (Not Found)
     */
    @GetMapping("/collaborators/{id}")
    public ResponseEntity<Collaborators> getCollaborators(@PathVariable Long id) {
        log.debug("REST request to get Collaborators : {}", id);
        Optional<Collaborators> collaborators = collaboratorsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collaborators);
    }

    /**
     * DELETE  /collaborators/:id : delete the "id" collaborators.
     *
     * @param id the id of the collaborators to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/collaborators/{id}")
    public ResponseEntity<Void> deleteCollaborators(@PathVariable Long id) {
        log.debug("REST request to delete Collaborators : {}", id);
        collaboratorsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/collaborators?query=:query : search for the collaborators corresponding
     * to the query.
     *
     * @param query the query of the collaborators search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/collaborators")
    public ResponseEntity<List<Collaborators>> searchCollaborators(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Collaborators for query {}", query);
        Page<Collaborators> page = collaboratorsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/collaborators");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
