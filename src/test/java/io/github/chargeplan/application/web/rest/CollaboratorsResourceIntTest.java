package io.github.chargeplan.application.web.rest;

import io.github.chargeplan.application.ChargeplanApplicationApp;

import io.github.chargeplan.application.domain.Collaborators;
import io.github.chargeplan.application.domain.Affectations;
import io.github.chargeplan.application.repository.CollaboratorsRepository;
import io.github.chargeplan.application.repository.search.CollaboratorsSearchRepository;
import io.github.chargeplan.application.service.CollaboratorsService;
import io.github.chargeplan.application.web.rest.errors.ExceptionTranslator;
import io.github.chargeplan.application.service.dto.CollaboratorsCriteria;
import io.github.chargeplan.application.service.CollaboratorsQueryService;

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
import java.util.Collections;
import java.util.List;


import static io.github.chargeplan.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.chargeplan.application.domain.enumeration.Skill;
/**
 * Test class for the CollaboratorsResource REST controller.
 *
 * @see CollaboratorsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChargeplanApplicationApp.class)
public class CollaboratorsResourceIntTest {

    private static final String DEFAULT_NOM_COLLABORATOR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COLLABORATOR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_COLLABORATOR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_COLLABORATOR = "BBBBBBBBBB";

    private static final String DEFAULT_TRIGRAMME = "AAAAAAAAAA";
    private static final String UPDATED_TRIGRAMME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Skill DEFAULT_COMPETENCIES = Skill.JAVA;
    private static final Skill UPDATED_COMPETENCIES = Skill.C;

    @Autowired
    private CollaboratorsRepository collaboratorsRepository;

    @Autowired
    private CollaboratorsService collaboratorsService;

    /**
     * This repository is mocked in the io.github.chargeplan.application.repository.search test package.
     *
     * @see io.github.chargeplan.application.repository.search.CollaboratorsSearchRepositoryMockConfiguration
     */
    @Autowired
    private CollaboratorsSearchRepository mockCollaboratorsSearchRepository;

    @Autowired
    private CollaboratorsQueryService collaboratorsQueryService;

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

    private MockMvc restCollaboratorsMockMvc;

    private Collaborators collaborators;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollaboratorsResource collaboratorsResource = new CollaboratorsResource(collaboratorsService, collaboratorsQueryService);
        this.restCollaboratorsMockMvc = MockMvcBuilders.standaloneSetup(collaboratorsResource)
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
    public static Collaborators createEntity(EntityManager em) {
        Collaborators collaborators = new Collaborators()
            .nomCollaborator(DEFAULT_NOM_COLLABORATOR)
            .prenomCollaborator(DEFAULT_PRENOM_COLLABORATOR)
            .trigramme(DEFAULT_TRIGRAMME)
            .email(DEFAULT_EMAIL)
            .competencies(DEFAULT_COMPETENCIES);
        return collaborators;
    }

    @Before
    public void initTest() {
        collaborators = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollaborators() throws Exception {
        int databaseSizeBeforeCreate = collaboratorsRepository.findAll().size();

        // Create the Collaborators
        restCollaboratorsMockMvc.perform(post("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaborators)))
            .andExpect(status().isCreated());

        // Validate the Collaborators in the database
        List<Collaborators> collaboratorsList = collaboratorsRepository.findAll();
        assertThat(collaboratorsList).hasSize(databaseSizeBeforeCreate + 1);
        Collaborators testCollaborators = collaboratorsList.get(collaboratorsList.size() - 1);
        assertThat(testCollaborators.getNomCollaborator()).isEqualTo(DEFAULT_NOM_COLLABORATOR);
        assertThat(testCollaborators.getPrenomCollaborator()).isEqualTo(DEFAULT_PRENOM_COLLABORATOR);
        assertThat(testCollaborators.getTrigramme()).isEqualTo(DEFAULT_TRIGRAMME);
        assertThat(testCollaborators.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCollaborators.getCompetencies()).isEqualTo(DEFAULT_COMPETENCIES);

        // Validate the Collaborators in Elasticsearch
        verify(mockCollaboratorsSearchRepository, times(1)).save(testCollaborators);
    }

    @Test
    @Transactional
    public void createCollaboratorsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collaboratorsRepository.findAll().size();

        // Create the Collaborators with an existing ID
        collaborators.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollaboratorsMockMvc.perform(post("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaborators)))
            .andExpect(status().isBadRequest());

        // Validate the Collaborators in the database
        List<Collaborators> collaboratorsList = collaboratorsRepository.findAll();
        assertThat(collaboratorsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Collaborators in Elasticsearch
        verify(mockCollaboratorsSearchRepository, times(0)).save(collaborators);
    }

    @Test
    @Transactional
    public void checkNomCollaboratorIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaboratorsRepository.findAll().size();
        // set the field null
        collaborators.setNomCollaborator(null);

        // Create the Collaborators, which fails.

        restCollaboratorsMockMvc.perform(post("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaborators)))
            .andExpect(status().isBadRequest());

        List<Collaborators> collaboratorsList = collaboratorsRepository.findAll();
        assertThat(collaboratorsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCollaborators() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList
        restCollaboratorsMockMvc.perform(get("/api/collaborators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaborators.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCollaborator").value(hasItem(DEFAULT_NOM_COLLABORATOR.toString())))
            .andExpect(jsonPath("$.[*].prenomCollaborator").value(hasItem(DEFAULT_PRENOM_COLLABORATOR.toString())))
            .andExpect(jsonPath("$.[*].trigramme").value(hasItem(DEFAULT_TRIGRAMME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].competencies").value(hasItem(DEFAULT_COMPETENCIES.toString())));
    }
    
    @Test
    @Transactional
    public void getCollaborators() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get the collaborators
        restCollaboratorsMockMvc.perform(get("/api/collaborators/{id}", collaborators.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collaborators.getId().intValue()))
            .andExpect(jsonPath("$.nomCollaborator").value(DEFAULT_NOM_COLLABORATOR.toString()))
            .andExpect(jsonPath("$.prenomCollaborator").value(DEFAULT_PRENOM_COLLABORATOR.toString()))
            .andExpect(jsonPath("$.trigramme").value(DEFAULT_TRIGRAMME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.competencies").value(DEFAULT_COMPETENCIES.toString()));
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByNomCollaboratorIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where nomCollaborator equals to DEFAULT_NOM_COLLABORATOR
        defaultCollaboratorsShouldBeFound("nomCollaborator.equals=" + DEFAULT_NOM_COLLABORATOR);

        // Get all the collaboratorsList where nomCollaborator equals to UPDATED_NOM_COLLABORATOR
        defaultCollaboratorsShouldNotBeFound("nomCollaborator.equals=" + UPDATED_NOM_COLLABORATOR);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByNomCollaboratorIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where nomCollaborator in DEFAULT_NOM_COLLABORATOR or UPDATED_NOM_COLLABORATOR
        defaultCollaboratorsShouldBeFound("nomCollaborator.in=" + DEFAULT_NOM_COLLABORATOR + "," + UPDATED_NOM_COLLABORATOR);

        // Get all the collaboratorsList where nomCollaborator equals to UPDATED_NOM_COLLABORATOR
        defaultCollaboratorsShouldNotBeFound("nomCollaborator.in=" + UPDATED_NOM_COLLABORATOR);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByNomCollaboratorIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where nomCollaborator is not null
        defaultCollaboratorsShouldBeFound("nomCollaborator.specified=true");

        // Get all the collaboratorsList where nomCollaborator is null
        defaultCollaboratorsShouldNotBeFound("nomCollaborator.specified=false");
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByPrenomCollaboratorIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where prenomCollaborator equals to DEFAULT_PRENOM_COLLABORATOR
        defaultCollaboratorsShouldBeFound("prenomCollaborator.equals=" + DEFAULT_PRENOM_COLLABORATOR);

        // Get all the collaboratorsList where prenomCollaborator equals to UPDATED_PRENOM_COLLABORATOR
        defaultCollaboratorsShouldNotBeFound("prenomCollaborator.equals=" + UPDATED_PRENOM_COLLABORATOR);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByPrenomCollaboratorIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where prenomCollaborator in DEFAULT_PRENOM_COLLABORATOR or UPDATED_PRENOM_COLLABORATOR
        defaultCollaboratorsShouldBeFound("prenomCollaborator.in=" + DEFAULT_PRENOM_COLLABORATOR + "," + UPDATED_PRENOM_COLLABORATOR);

        // Get all the collaboratorsList where prenomCollaborator equals to UPDATED_PRENOM_COLLABORATOR
        defaultCollaboratorsShouldNotBeFound("prenomCollaborator.in=" + UPDATED_PRENOM_COLLABORATOR);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByPrenomCollaboratorIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where prenomCollaborator is not null
        defaultCollaboratorsShouldBeFound("prenomCollaborator.specified=true");

        // Get all the collaboratorsList where prenomCollaborator is null
        defaultCollaboratorsShouldNotBeFound("prenomCollaborator.specified=false");
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByTrigrammeIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where trigramme equals to DEFAULT_TRIGRAMME
        defaultCollaboratorsShouldBeFound("trigramme.equals=" + DEFAULT_TRIGRAMME);

        // Get all the collaboratorsList where trigramme equals to UPDATED_TRIGRAMME
        defaultCollaboratorsShouldNotBeFound("trigramme.equals=" + UPDATED_TRIGRAMME);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByTrigrammeIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where trigramme in DEFAULT_TRIGRAMME or UPDATED_TRIGRAMME
        defaultCollaboratorsShouldBeFound("trigramme.in=" + DEFAULT_TRIGRAMME + "," + UPDATED_TRIGRAMME);

        // Get all the collaboratorsList where trigramme equals to UPDATED_TRIGRAMME
        defaultCollaboratorsShouldNotBeFound("trigramme.in=" + UPDATED_TRIGRAMME);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByTrigrammeIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where trigramme is not null
        defaultCollaboratorsShouldBeFound("trigramme.specified=true");

        // Get all the collaboratorsList where trigramme is null
        defaultCollaboratorsShouldNotBeFound("trigramme.specified=false");
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where email equals to DEFAULT_EMAIL
        defaultCollaboratorsShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the collaboratorsList where email equals to UPDATED_EMAIL
        defaultCollaboratorsShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCollaboratorsShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the collaboratorsList where email equals to UPDATED_EMAIL
        defaultCollaboratorsShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where email is not null
        defaultCollaboratorsShouldBeFound("email.specified=true");

        // Get all the collaboratorsList where email is null
        defaultCollaboratorsShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByCompetenciesIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where competencies equals to DEFAULT_COMPETENCIES
        defaultCollaboratorsShouldBeFound("competencies.equals=" + DEFAULT_COMPETENCIES);

        // Get all the collaboratorsList where competencies equals to UPDATED_COMPETENCIES
        defaultCollaboratorsShouldNotBeFound("competencies.equals=" + UPDATED_COMPETENCIES);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByCompetenciesIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where competencies in DEFAULT_COMPETENCIES or UPDATED_COMPETENCIES
        defaultCollaboratorsShouldBeFound("competencies.in=" + DEFAULT_COMPETENCIES + "," + UPDATED_COMPETENCIES);

        // Get all the collaboratorsList where competencies equals to UPDATED_COMPETENCIES
        defaultCollaboratorsShouldNotBeFound("competencies.in=" + UPDATED_COMPETENCIES);
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByCompetenciesIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorsRepository.saveAndFlush(collaborators);

        // Get all the collaboratorsList where competencies is not null
        defaultCollaboratorsShouldBeFound("competencies.specified=true");

        // Get all the collaboratorsList where competencies is null
        defaultCollaboratorsShouldNotBeFound("competencies.specified=false");
    }

    @Test
    @Transactional
    public void getAllCollaboratorsByAffectationIsEqualToSomething() throws Exception {
        // Initialize the database
        Affectations affectation = AffectationsResourceIntTest.createEntity(em);
        em.persist(affectation);
        em.flush();
        collaborators.addAffectation(affectation);
        collaboratorsRepository.saveAndFlush(collaborators);
        Long affectationId = affectation.getId();

        // Get all the collaboratorsList where affectation equals to affectationId
        defaultCollaboratorsShouldBeFound("affectationId.equals=" + affectationId);

        // Get all the collaboratorsList where affectation equals to affectationId + 1
        defaultCollaboratorsShouldNotBeFound("affectationId.equals=" + (affectationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCollaboratorsShouldBeFound(String filter) throws Exception {
        restCollaboratorsMockMvc.perform(get("/api/collaborators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaborators.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCollaborator").value(hasItem(DEFAULT_NOM_COLLABORATOR)))
            .andExpect(jsonPath("$.[*].prenomCollaborator").value(hasItem(DEFAULT_PRENOM_COLLABORATOR)))
            .andExpect(jsonPath("$.[*].trigramme").value(hasItem(DEFAULT_TRIGRAMME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].competencies").value(hasItem(DEFAULT_COMPETENCIES.toString())));

        // Check, that the count call also returns 1
        restCollaboratorsMockMvc.perform(get("/api/collaborators/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCollaboratorsShouldNotBeFound(String filter) throws Exception {
        restCollaboratorsMockMvc.perform(get("/api/collaborators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollaboratorsMockMvc.perform(get("/api/collaborators/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCollaborators() throws Exception {
        // Get the collaborators
        restCollaboratorsMockMvc.perform(get("/api/collaborators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollaborators() throws Exception {
        // Initialize the database
        collaboratorsService.save(collaborators);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCollaboratorsSearchRepository);

        int databaseSizeBeforeUpdate = collaboratorsRepository.findAll().size();

        // Update the collaborators
        Collaborators updatedCollaborators = collaboratorsRepository.findById(collaborators.getId()).get();
        // Disconnect from session so that the updates on updatedCollaborators are not directly saved in db
        em.detach(updatedCollaborators);
        updatedCollaborators
            .nomCollaborator(UPDATED_NOM_COLLABORATOR)
            .prenomCollaborator(UPDATED_PRENOM_COLLABORATOR)
            .trigramme(UPDATED_TRIGRAMME)
            .email(UPDATED_EMAIL)
            .competencies(UPDATED_COMPETENCIES);

        restCollaboratorsMockMvc.perform(put("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCollaborators)))
            .andExpect(status().isOk());

        // Validate the Collaborators in the database
        List<Collaborators> collaboratorsList = collaboratorsRepository.findAll();
        assertThat(collaboratorsList).hasSize(databaseSizeBeforeUpdate);
        Collaborators testCollaborators = collaboratorsList.get(collaboratorsList.size() - 1);
        assertThat(testCollaborators.getNomCollaborator()).isEqualTo(UPDATED_NOM_COLLABORATOR);
        assertThat(testCollaborators.getPrenomCollaborator()).isEqualTo(UPDATED_PRENOM_COLLABORATOR);
        assertThat(testCollaborators.getTrigramme()).isEqualTo(UPDATED_TRIGRAMME);
        assertThat(testCollaborators.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCollaborators.getCompetencies()).isEqualTo(UPDATED_COMPETENCIES);

        // Validate the Collaborators in Elasticsearch
        verify(mockCollaboratorsSearchRepository, times(1)).save(testCollaborators);
    }

    @Test
    @Transactional
    public void updateNonExistingCollaborators() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorsRepository.findAll().size();

        // Create the Collaborators

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollaboratorsMockMvc.perform(put("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaborators)))
            .andExpect(status().isBadRequest());

        // Validate the Collaborators in the database
        List<Collaborators> collaboratorsList = collaboratorsRepository.findAll();
        assertThat(collaboratorsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Collaborators in Elasticsearch
        verify(mockCollaboratorsSearchRepository, times(0)).save(collaborators);
    }

    @Test
    @Transactional
    public void deleteCollaborators() throws Exception {
        // Initialize the database
        collaboratorsService.save(collaborators);

        int databaseSizeBeforeDelete = collaboratorsRepository.findAll().size();

        // Delete the collaborators
        restCollaboratorsMockMvc.perform(delete("/api/collaborators/{id}", collaborators.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Collaborators> collaboratorsList = collaboratorsRepository.findAll();
        assertThat(collaboratorsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Collaborators in Elasticsearch
        verify(mockCollaboratorsSearchRepository, times(1)).deleteById(collaborators.getId());
    }

    @Test
    @Transactional
    public void searchCollaborators() throws Exception {
        // Initialize the database
        collaboratorsService.save(collaborators);
        when(mockCollaboratorsSearchRepository.search(queryStringQuery("id:" + collaborators.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(collaborators), PageRequest.of(0, 1), 1));
        // Search the collaborators
        restCollaboratorsMockMvc.perform(get("/api/_search/collaborators?query=id:" + collaborators.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaborators.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCollaborator").value(hasItem(DEFAULT_NOM_COLLABORATOR)))
            .andExpect(jsonPath("$.[*].prenomCollaborator").value(hasItem(DEFAULT_PRENOM_COLLABORATOR)))
            .andExpect(jsonPath("$.[*].trigramme").value(hasItem(DEFAULT_TRIGRAMME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].competencies").value(hasItem(DEFAULT_COMPETENCIES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collaborators.class);
        Collaborators collaborators1 = new Collaborators();
        collaborators1.setId(1L);
        Collaborators collaborators2 = new Collaborators();
        collaborators2.setId(collaborators1.getId());
        assertThat(collaborators1).isEqualTo(collaborators2);
        collaborators2.setId(2L);
        assertThat(collaborators1).isNotEqualTo(collaborators2);
        collaborators1.setId(null);
        assertThat(collaborators1).isNotEqualTo(collaborators2);
    }
}
