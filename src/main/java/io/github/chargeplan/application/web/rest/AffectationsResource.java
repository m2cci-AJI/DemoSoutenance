package io.github.chargeplan.application.web.rest;
import io.github.chargeplan.application.domain.Affectations;
import io.github.chargeplan.application.service.AffectationsService;
import io.github.chargeplan.application.web.rest.errors.BadRequestAlertException;
import io.github.chargeplan.application.web.rest.util.HeaderUtil;
import io.github.chargeplan.application.web.rest.util.PaginationUtil;
import io.github.chargeplan.application.service.dto.AffectationsCriteria;
import io.github.chargeplan.application.service.AffectationsQueryService;
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
 * REST controller for managing Affectations.
 */
@RestController
@RequestMapping("/api")
public class AffectationsResource {

    private final Logger log = LoggerFactory.getLogger(AffectationsResource.class);

    private static final String ENTITY_NAME = "affectations";

    private final AffectationsService affectationsService;

    private final AffectationsQueryService affectationsQueryService;

    public AffectationsResource(AffectationsService affectationsService, AffectationsQueryService affectationsQueryService) {
        this.affectationsService = affectationsService;
        this.affectationsQueryService = affectationsQueryService;
    }

    /**
     * POST  /affectations : Create a new affectations.
     *
     * @param affectations the affectations to create
     * @return the ResponseEntity with status 201 (Created) and with body the new affectations, or with status 400 (Bad Request) if the affectations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/affectations")
    public ResponseEntity<Affectations> createAffectations(@Valid @RequestBody Affectations affectations) throws URISyntaxException {
        log.debug("REST request to save Affectations : {}", affectations);
        if (affectations.getId() != null) {
            throw new BadRequestAlertException("A new affectations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Affectations result = affectationsService.save(affectations);
        return ResponseEntity.created(new URI("/api/affectations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /affectations : Updates an existing affectations.
     *
     * @param affectations the affectations to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated affectations,
     * or with status 400 (Bad Request) if the affectations is not valid,
     * or with status 500 (Internal Server Error) if the affectations couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/affectations")
    public ResponseEntity<Affectations> updateAffectations(@Valid @RequestBody Affectations affectations) throws URISyntaxException {
        log.debug("REST request to update Affectations : {}", affectations);
        if (affectations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Affectations result = affectationsService.save(affectations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, affectations.getId().toString()))
            .body(result);
    }

    /**
     * GET  /affectations : get all the affectations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of affectations in body
     */
    @GetMapping("/affectations")
    public ResponseEntity<List<Affectations>> getAllAffectations(AffectationsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Affectations by criteria: {}", criteria);
        Page<Affectations> page = affectationsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/affectations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /affectations/count : count all the affectations.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/affectations/count")
    public ResponseEntity<Long> countAffectations(AffectationsCriteria criteria) {
        log.debug("REST request to count Affectations by criteria: {}", criteria);
        return ResponseEntity.ok().body(affectationsQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /affectations/:id : get the "id" affectations.
     *
     * @param id the id of the affectations to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the affectations, or with status 404 (Not Found)
     */
    @GetMapping("/affectations/{id}")
    public ResponseEntity<Affectations> getAffectations(@PathVariable Long id) {
        log.debug("REST request to get Affectations : {}", id);
        Optional<Affectations> affectations = affectationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affectations);
    }

    /**
     * DELETE  /affectations/:id : delete the "id" affectations.
     *
     * @param id the id of the affectations to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/affectations/{id}")
    public ResponseEntity<Void> deleteAffectations(@PathVariable Long id) {
        log.debug("REST request to delete Affectations : {}", id);
        affectationsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/affectations?query=:query : search for the affectations corresponding
     * to the query.
     *
     * @param query the query of the affectations search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/affectations")
    public ResponseEntity<List<Affectations>> searchAffectations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Affectations for query {}", query);
        Page<Affectations> page = affectationsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/affectations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
