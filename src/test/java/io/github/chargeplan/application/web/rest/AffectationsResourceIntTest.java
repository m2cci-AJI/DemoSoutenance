package io.github.chargeplan.application.web.rest;

import io.github.chargeplan.application.ChargeplanApplicationApp;

import io.github.chargeplan.application.domain.Affectations;
import io.github.chargeplan.application.domain.Collaborators;
import io.github.chargeplan.application.domain.Projects;
import io.github.chargeplan.application.repository.AffectationsRepository;
import io.github.chargeplan.application.repository.search.AffectationsSearchRepository;
import io.github.chargeplan.application.service.AffectationsService;
import io.github.chargeplan.application.web.rest.errors.ExceptionTranslator;
import io.github.chargeplan.application.service.dto.AffectationsCriteria;
import io.github.chargeplan.application.service.AffectationsQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static io.github.chargeplan.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.chargeplan.application.domain.enumeration.Colors;
/**
 * Test class for the AffectationsResource REST controller.
 *
 * @see AffectationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChargeplanApplicationApp.class)
public class AffectationsResourceIntTest {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CHARGE = 1;
    private static final Integer UPDATED_CHARGE = 2;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Colors DEFAULT_COLOR = Colors.rouge;
    private static final Colors UPDATED_COLOR = Colors.gris;

    @Autowired
    private AffectationsRepository affectationsRepository;

    @Autowired
    private AffectationsService affectationsService;

    /**
     * This repository is mocked in the io.github.chargeplan.application.repository.search test package.
     *
     * @see io.github.chargeplan.application.repository.search.AffectationsSearchRepositoryMockConfiguration
     */
    @Autowired
    private AffectationsSearchRepository mockAffectationsSearchRepository;

    @Autowired
    private AffectationsQueryService affectationsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAffectationsMockMvc;

    private Affectations affectations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AffectationsResource affectationsResource = new AffectationsResource(affectationsService, affectationsQueryService);
        this.restAffectationsMockMvc = MockMvcBuilders.standaloneSetup(affectationsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectations createEntity(EntityManager em) {
        Affectations affectations = new Affectations()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .charge(DEFAULT_CHARGE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .color(DEFAULT_COLOR);
        return affectations;
    }

    @Before
    public void initTest() {
        affectations = createEntity(em);
    }

    @Test
    @Transactional
    public void createAffectations() throws Exception {
        int databaseSizeBeforeCreate = affectationsRepository.findAll().size();

        // Create the Affectations
        restAffectationsMockMvc.perform(post("/api/affectations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(affectations)))
            .andExpect(status().isCreated());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeCreate + 1);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAffectations.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAffectations.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testAffectations.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testAffectations.getColor()).isEqualTo(DEFAULT_COLOR);

        // Validate the Affectations in Elasticsearch
        verify(mockAffectationsSearchRepository, times(1)).save(testAffectations);
    }

    @Test
    @Transactional
    public void createAffectationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = affectationsRepository.findAll().size();

        // Create the Affectations with an existing ID
        affectations.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffectationsMockMvc.perform(post("/api/affectations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(affectations)))
            .andExpect(status().isBadRequest());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Affectations in Elasticsearch
        verify(mockAffectationsSearchRepository, times(0)).save(affectations);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationsRepository.findAll().size();
        // set the field null
        affectations.setDateDebut(null);

        // Create the Affectations, which fails.

        restAffectationsMockMvc.perform(post("/api/affectations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(affectations)))
            .andExpect(status().isBadRequest());

        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAffectations() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList
        restAffectationsMockMvc.perform(get("/api/affectations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectations.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }
    
    @Test
    @Transactional
    public void getAffectations() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get the affectations
        restAffectationsMockMvc.perform(get("/api/affectations/{id}", affectations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(affectations.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultAffectationsShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the affectationsList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultAffectationsShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultAffectationsShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the affectationsList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultAffectationsShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateDebut is not null
        defaultAffectationsShouldBeFound("dateDebut.specified=true");

        // Get all the affectationsList where dateDebut is null
        defaultAffectationsShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateDebut greater than or equals to DEFAULT_DATE_DEBUT
        defaultAffectationsShouldBeFound("dateDebut.greaterOrEqualThan=" + DEFAULT_DATE_DEBUT);

        // Get all the affectationsList where dateDebut greater than or equals to UPDATED_DATE_DEBUT
        defaultAffectationsShouldNotBeFound("dateDebut.greaterOrEqualThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateDebut less than or equals to DEFAULT_DATE_DEBUT
        defaultAffectationsShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the affectationsList where dateDebut less than or equals to UPDATED_DATE_DEBUT
        defaultAffectationsShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllAffectationsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateFin equals to DEFAULT_DATE_FIN
        defaultAffectationsShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the affectationsList where dateFin equals to UPDATED_DATE_FIN
        defaultAffectationsShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultAffectationsShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the affectationsList where dateFin equals to UPDATED_DATE_FIN
        defaultAffectationsShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateFin is not null
        defaultAffectationsShouldBeFound("dateFin.specified=true");

        // Get all the affectationsList where dateFin is null
        defaultAffectationsShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateFin greater than or equals to DEFAULT_DATE_FIN
        defaultAffectationsShouldBeFound("dateFin.greaterOrEqualThan=" + DEFAULT_DATE_FIN);

        // Get all the affectationsList where dateFin greater than or equals to UPDATED_DATE_FIN
        defaultAffectationsShouldNotBeFound("dateFin.greaterOrEqualThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAffectationsByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where dateFin less than or equals to DEFAULT_DATE_FIN
        defaultAffectationsShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the affectationsList where dateFin less than or equals to UPDATED_DATE_FIN
        defaultAffectationsShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }


    @Test
    @Transactional
    public void getAllAffectationsByChargeIsEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where charge equals to DEFAULT_CHARGE
        defaultAffectationsShouldBeFound("charge.equals=" + DEFAULT_CHARGE);

        // Get all the affectationsList where charge equals to UPDATED_CHARGE
        defaultAffectationsShouldNotBeFound("charge.equals=" + UPDATED_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAffectationsByChargeIsInShouldWork() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where charge in DEFAULT_CHARGE or UPDATED_CHARGE
        defaultAffectationsShouldBeFound("charge.in=" + DEFAULT_CHARGE + "," + UPDATED_CHARGE);

        // Get all the affectationsList where charge equals to UPDATED_CHARGE
        defaultAffectationsShouldNotBeFound("charge.in=" + UPDATED_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAffectationsByChargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where charge is not null
        defaultAffectationsShouldBeFound("charge.specified=true");

        // Get all the affectationsList where charge is null
        defaultAffectationsShouldNotBeFound("charge.specified=false");
    }

    @Test
    @Transactional
    public void getAllAffectationsByChargeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where charge greater than or equals to DEFAULT_CHARGE
        defaultAffectationsShouldBeFound("charge.greaterOrEqualThan=" + DEFAULT_CHARGE);

        // Get all the affectationsList where charge greater than or equals to UPDATED_CHARGE
        defaultAffectationsShouldNotBeFound("charge.greaterOrEqualThan=" + UPDATED_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAffectationsByChargeIsLessThanSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where charge less than or equals to DEFAULT_CHARGE
        defaultAffectationsShouldNotBeFound("charge.lessThan=" + DEFAULT_CHARGE);

        // Get all the affectationsList where charge less than or equals to UPDATED_CHARGE
        defaultAffectationsShouldBeFound("charge.lessThan=" + UPDATED_CHARGE);
    }


    @Test
    @Transactional
    public void getAllAffectationsByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where commentaire equals to DEFAULT_COMMENTAIRE
        defaultAffectationsShouldBeFound("commentaire.equals=" + DEFAULT_COMMENTAIRE);

        // Get all the affectationsList where commentaire equals to UPDATED_COMMENTAIRE
        defaultAffectationsShouldNotBeFound("commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void getAllAffectationsByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where commentaire in DEFAULT_COMMENTAIRE or UPDATED_COMMENTAIRE
        defaultAffectationsShouldBeFound("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE);

        // Get all the affectationsList where commentaire equals to UPDATED_COMMENTAIRE
        defaultAffectationsShouldNotBeFound("commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void getAllAffectationsByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where commentaire is not null
        defaultAffectationsShouldBeFound("commentaire.specified=true");

        // Get all the affectationsList where commentaire is null
        defaultAffectationsShouldNotBeFound("commentaire.specified=false");
    }

    @Test
    @Transactional
    public void getAllAffectationsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where color equals to DEFAULT_COLOR
        defaultAffectationsShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the affectationsList where color equals to UPDATED_COLOR
        defaultAffectationsShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllAffectationsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultAffectationsShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the affectationsList where color equals to UPDATED_COLOR
        defaultAffectationsShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllAffectationsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        affectationsRepository.saveAndFlush(affectations);

        // Get all the affectationsList where color is not null
        defaultAffectationsShouldBeFound("color.specified=true");

        // Get all the affectationsList where color is null
        defaultAffectationsShouldNotBeFound("color.specified=false");
    }

    @Test
    @Transactional
    public void getAllAffectationsByCollaboratorIsEqualToSomething() throws Exception {
        // Initialize the database
        Collaborators collaborator = CollaboratorsResourceIntTest.createEntity(em);
        em.persist(collaborator);
        em.flush();
        affectations.setCollaborator(collaborator);
        affectationsRepository.saveAndFlush(affectations);
        Long collaboratorId = collaborator.getId();

        // Get all the affectationsList where collaborator equals to collaboratorId
        defaultAffectationsShouldBeFound("collaboratorId.equals=" + collaboratorId);

        // Get all the affectationsList where collaborator equals to collaboratorId + 1
        defaultAffectationsShouldNotBeFound("collaboratorId.equals=" + (collaboratorId + 1));
    }


    @Test
    @Transactional
    public void getAllAffectationsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Projects project = ProjectsResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        affectations.setProject(project);
        affectationsRepository.saveAndFlush(affectations);
        Long projectId = project.getId();

        // Get all the affectationsList where project equals to projectId
        defaultAffectationsShouldBeFound("projectId.equals=" + projectId);

        // Get all the affectationsList where project equals to projectId + 1
        defaultAffectationsShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAffectationsShouldBeFound(String filter) throws Exception {
        restAffectationsMockMvc.perform(get("/api/affectations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectations.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));

        // Check, that the count call also returns 1
        restAffectationsMockMvc.perform(get("/api/affectations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAffectationsShouldNotBeFound(String filter) throws Exception {
        restAffectationsMockMvc.perform(get("/api/affectations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAffectationsMockMvc.perform(get("/api/affectations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAffectations() throws Exception {
        // Get the affectations
        restAffectationsMockMvc.perform(get("/api/affectations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAffectations() throws Exception {
        // Initialize the database
        affectationsService.save(affectations);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAffectationsSearchRepository);

        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();

        // Update the affectations
        Affectations updatedAffectations = affectationsRepository.findById(affectations.getId()).get();
        // Disconnect from session so that the updates on updatedAffectations are not directly saved in db
        em.detach(updatedAffectations);
        updatedAffectations
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .charge(UPDATED_CHARGE)
            .commentaire(UPDATED_COMMENTAIRE)
            .color(UPDATED_COLOR);

        restAffectationsMockMvc.perform(put("/api/affectations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAffectations)))
            .andExpect(status().isOk());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);
        Affectations testAffectations = affectationsList.get(affectationsList.size() - 1);
        assertThat(testAffectations.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAffectations.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAffectations.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testAffectations.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testAffectations.getColor()).isEqualTo(UPDATED_COLOR);

        // Validate the Affectations in Elasticsearch
        verify(mockAffectationsSearchRepository, times(1)).save(testAffectations);
    }

    @Test
    @Transactional
    public void updateNonExistingAffectations() throws Exception {
        int databaseSizeBeforeUpdate = affectationsRepository.findAll().size();

        // Create the Affectations

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationsMockMvc.perform(put("/api/affectations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(affectations)))
            .andExpect(status().isBadRequest());

        // Validate the Affectations in the database
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Affectations in Elasticsearch
        verify(mockAffectationsSearchRepository, times(0)).save(affectations);
    }

    @Test
    @Transactional
    public void deleteAffectations() throws Exception {
        // Initialize the database
        affectationsService.save(affectations);

        int databaseSizeBeforeDelete = affectationsRepository.findAll().size();

        // Delete the affectations
        restAffectationsMockMvc.perform(delete("/api/affectations/{id}", affectations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Affectations> affectationsList = affectationsRepository.findAll();
        assertThat(affectationsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Affectations in Elasticsearch
        verify(mockAffectationsSearchRepository, times(1)).deleteById(affectations.getId());
    }

    @Test
    @Transactional
    public void searchAffectations() throws Exception {
        // Initialize the database
        affectationsService.save(affectations);
        when(mockAffectationsSearchRepository.search(queryStringQuery("id:" + affectations.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(affectations), PageRequest.of(0, 1), 1));
        // Search the affectations
        restAffectationsMockMvc.perform(get("/api/_search/affectations?query=id:" + affectations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectations.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Affectations.class);
        Affectations affectations1 = new Affectations();
        affectations1.setId(1L);
        Affectations affectations2 = new Affectations();
        affectations2.setId(affectations1.getId());
        assertThat(affectations1).isEqualTo(affectations2);
        affectations2.setId(2L);
        assertThat(affectations1).isNotEqualTo(affectations2);
        affectations1.setId(null);
        assertThat(affectations1).isNotEqualTo(affectations2);
    }
}
