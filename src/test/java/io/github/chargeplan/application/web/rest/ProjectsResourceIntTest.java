package io.github.chargeplan.application.web.rest;

import io.github.chargeplan.application.ChargeplanApplicationApp;

import io.github.chargeplan.application.domain.Projects;
import io.github.chargeplan.application.domain.Affectations;
import io.github.chargeplan.application.repository.ProjectsRepository;
import io.github.chargeplan.application.repository.search.ProjectsSearchRepository;
import io.github.chargeplan.application.service.ProjectsService;
import io.github.chargeplan.application.web.rest.errors.ExceptionTranslator;
import io.github.chargeplan.application.service.dto.ProjectsCriteria;
import io.github.chargeplan.application.service.ProjectsQueryService;

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

/**
 * Test class for the ProjectsResource REST controller.
 *
 * @see ProjectsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChargeplanApplicationApp.class)
public class ProjectsResourceIntTest {

    private static final String DEFAULT_NAME_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_PROJECT = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_D_P = "AAAAAAAAAA";
    private static final String UPDATED_D_P = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private ProjectsService projectsService;

    /**
     * This repository is mocked in the io.github.chargeplan.application.repository.search test package.
     *
     * @see io.github.chargeplan.application.repository.search.ProjectsSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProjectsSearchRepository mockProjectsSearchRepository;

    @Autowired
    private ProjectsQueryService projectsQueryService;

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

    private MockMvc restProjectsMockMvc;

    private Projects projects;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectsResource projectsResource = new ProjectsResource(projectsService, projectsQueryService);
        this.restProjectsMockMvc = MockMvcBuilders.standaloneSetup(projectsResource)
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
    public static Projects createEntity(EntityManager em) {
        Projects projects = new Projects()
            .nameProject(DEFAULT_NAME_PROJECT)
            .projectCode(DEFAULT_PROJECT_CODE)
            .client(DEFAULT_CLIENT)
            .dP(DEFAULT_D_P)
            .description(DEFAULT_DESCRIPTION);
        return projects;
    }

    @Before
    public void initTest() {
        projects = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjects() throws Exception {
        int databaseSizeBeforeCreate = projectsRepository.findAll().size();

        // Create the Projects
        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isCreated());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate + 1);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getNameProject()).isEqualTo(DEFAULT_NAME_PROJECT);
        assertThat(testProjects.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testProjects.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testProjects.getdP()).isEqualTo(DEFAULT_D_P);
        assertThat(testProjects.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Projects in Elasticsearch
        verify(mockProjectsSearchRepository, times(1)).save(testProjects);
    }

    @Test
    @Transactional
    public void createProjectsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectsRepository.findAll().size();

        // Create the Projects with an existing ID
        projects.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Projects in Elasticsearch
        verify(mockProjectsSearchRepository, times(0)).save(projects);
    }

    @Test
    @Transactional
    public void checkNameProjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectsRepository.findAll().size();
        // set the field null
        projects.setNameProject(null);

        // Create the Projects, which fails.

        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isBadRequest());

        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectsRepository.findAll().size();
        // set the field null
        projects.setProjectCode(null);

        // Create the Projects, which fails.

        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isBadRequest());

        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList
        restProjectsMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameProject").value(hasItem(DEFAULT_NAME_PROJECT.toString())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].dP").value(hasItem(DEFAULT_D_P.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get the projects
        restProjectsMockMvc.perform(get("/api/projects/{id}", projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projects.getId().intValue()))
            .andExpect(jsonPath("$.nameProject").value(DEFAULT_NAME_PROJECT.toString()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.client").value(DEFAULT_CLIENT.toString()))
            .andExpect(jsonPath("$.dP").value(DEFAULT_D_P.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectsByNameProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where nameProject equals to DEFAULT_NAME_PROJECT
        defaultProjectsShouldBeFound("nameProject.equals=" + DEFAULT_NAME_PROJECT);

        // Get all the projectsList where nameProject equals to UPDATED_NAME_PROJECT
        defaultProjectsShouldNotBeFound("nameProject.equals=" + UPDATED_NAME_PROJECT);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameProjectIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where nameProject in DEFAULT_NAME_PROJECT or UPDATED_NAME_PROJECT
        defaultProjectsShouldBeFound("nameProject.in=" + DEFAULT_NAME_PROJECT + "," + UPDATED_NAME_PROJECT);

        // Get all the projectsList where nameProject equals to UPDATED_NAME_PROJECT
        defaultProjectsShouldNotBeFound("nameProject.in=" + UPDATED_NAME_PROJECT);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameProjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where nameProject is not null
        defaultProjectsShouldBeFound("nameProject.specified=true");

        // Get all the projectsList where nameProject is null
        defaultProjectsShouldNotBeFound("nameProject.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectCode equals to DEFAULT_PROJECT_CODE
        defaultProjectsShouldBeFound("projectCode.equals=" + DEFAULT_PROJECT_CODE);

        // Get all the projectsList where projectCode equals to UPDATED_PROJECT_CODE
        defaultProjectsShouldNotBeFound("projectCode.equals=" + UPDATED_PROJECT_CODE);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectCodeIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectCode in DEFAULT_PROJECT_CODE or UPDATED_PROJECT_CODE
        defaultProjectsShouldBeFound("projectCode.in=" + DEFAULT_PROJECT_CODE + "," + UPDATED_PROJECT_CODE);

        // Get all the projectsList where projectCode equals to UPDATED_PROJECT_CODE
        defaultProjectsShouldNotBeFound("projectCode.in=" + UPDATED_PROJECT_CODE);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectCode is not null
        defaultProjectsShouldBeFound("projectCode.specified=true");

        // Get all the projectsList where projectCode is null
        defaultProjectsShouldNotBeFound("projectCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where client equals to DEFAULT_CLIENT
        defaultProjectsShouldBeFound("client.equals=" + DEFAULT_CLIENT);

        // Get all the projectsList where client equals to UPDATED_CLIENT
        defaultProjectsShouldNotBeFound("client.equals=" + UPDATED_CLIENT);
    }

    @Test
    @Transactional
    public void getAllProjectsByClientIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where client in DEFAULT_CLIENT or UPDATED_CLIENT
        defaultProjectsShouldBeFound("client.in=" + DEFAULT_CLIENT + "," + UPDATED_CLIENT);

        // Get all the projectsList where client equals to UPDATED_CLIENT
        defaultProjectsShouldNotBeFound("client.in=" + UPDATED_CLIENT);
    }

    @Test
    @Transactional
    public void getAllProjectsByClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where client is not null
        defaultProjectsShouldBeFound("client.specified=true");

        // Get all the projectsList where client is null
        defaultProjectsShouldNotBeFound("client.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsBydPIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where dP equals to DEFAULT_D_P
        defaultProjectsShouldBeFound("dP.equals=" + DEFAULT_D_P);

        // Get all the projectsList where dP equals to UPDATED_D_P
        defaultProjectsShouldNotBeFound("dP.equals=" + UPDATED_D_P);
    }

    @Test
    @Transactional
    public void getAllProjectsBydPIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where dP in DEFAULT_D_P or UPDATED_D_P
        defaultProjectsShouldBeFound("dP.in=" + DEFAULT_D_P + "," + UPDATED_D_P);

        // Get all the projectsList where dP equals to UPDATED_D_P
        defaultProjectsShouldNotBeFound("dP.in=" + UPDATED_D_P);
    }

    @Test
    @Transactional
    public void getAllProjectsBydPIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where dP is not null
        defaultProjectsShouldBeFound("dP.specified=true");

        // Get all the projectsList where dP is null
        defaultProjectsShouldNotBeFound("dP.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where description equals to DEFAULT_DESCRIPTION
        defaultProjectsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the projectsList where description equals to UPDATED_DESCRIPTION
        defaultProjectsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProjectsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the projectsList where description equals to UPDATED_DESCRIPTION
        defaultProjectsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where description is not null
        defaultProjectsShouldBeFound("description.specified=true");

        // Get all the projectsList where description is null
        defaultProjectsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByAffectationIsEqualToSomething() throws Exception {
        // Initialize the database
        Affectations affectation = AffectationsResourceIntTest.createEntity(em);
        em.persist(affectation);
        em.flush();
        projects.addAffectation(affectation);
        projectsRepository.saveAndFlush(projects);
        Long affectationId = affectation.getId();

        // Get all the projectsList where affectation equals to affectationId
        defaultProjectsShouldBeFound("affectationId.equals=" + affectationId);

        // Get all the projectsList where affectation equals to affectationId + 1
        defaultProjectsShouldNotBeFound("affectationId.equals=" + (affectationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectsShouldBeFound(String filter) throws Exception {
        restProjectsMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameProject").value(hasItem(DEFAULT_NAME_PROJECT)))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE)))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT)))
            .andExpect(jsonPath("$.[*].dP").value(hasItem(DEFAULT_D_P)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProjectsMockMvc.perform(get("/api/projects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectsShouldNotBeFound(String filter) throws Exception {
        restProjectsMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectsMockMvc.perform(get("/api/projects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProjects() throws Exception {
        // Get the projects
        restProjectsMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjects() throws Exception {
        // Initialize the database
        projectsService.save(projects);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockProjectsSearchRepository);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects
        Projects updatedProjects = projectsRepository.findById(projects.getId()).get();
        // Disconnect from session so that the updates on updatedProjects are not directly saved in db
        em.detach(updatedProjects);
        updatedProjects
            .nameProject(UPDATED_NAME_PROJECT)
            .projectCode(UPDATED_PROJECT_CODE)
            .client(UPDATED_CLIENT)
            .dP(UPDATED_D_P)
            .description(UPDATED_DESCRIPTION);

        restProjectsMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjects)))
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getNameProject()).isEqualTo(UPDATED_NAME_PROJECT);
        assertThat(testProjects.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testProjects.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testProjects.getdP()).isEqualTo(UPDATED_D_P);
        assertThat(testProjects.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Projects in Elasticsearch
        verify(mockProjectsSearchRepository, times(1)).save(testProjects);
    }

    @Test
    @Transactional
    public void updateNonExistingProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Create the Projects

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectsMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Projects in Elasticsearch
        verify(mockProjectsSearchRepository, times(0)).save(projects);
    }

    @Test
    @Transactional
    public void deleteProjects() throws Exception {
        // Initialize the database
        projectsService.save(projects);

        int databaseSizeBeforeDelete = projectsRepository.findAll().size();

        // Delete the projects
        restProjectsMockMvc.perform(delete("/api/projects/{id}", projects.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Projects in Elasticsearch
        verify(mockProjectsSearchRepository, times(1)).deleteById(projects.getId());
    }

    @Test
    @Transactional
    public void searchProjects() throws Exception {
        // Initialize the database
        projectsService.save(projects);
        when(mockProjectsSearchRepository.search(queryStringQuery("id:" + projects.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(projects), PageRequest.of(0, 1), 1));
        // Search the projects
        restProjectsMockMvc.perform(get("/api/_search/projects?query=id:" + projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameProject").value(hasItem(DEFAULT_NAME_PROJECT)))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE)))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT)))
            .andExpect(jsonPath("$.[*].dP").value(hasItem(DEFAULT_D_P)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Projects.class);
        Projects projects1 = new Projects();
        projects1.setId(1L);
        Projects projects2 = new Projects();
        projects2.setId(projects1.getId());
        assertThat(projects1).isEqualTo(projects2);
        projects2.setId(2L);
        assertThat(projects1).isNotEqualTo(projects2);
        projects1.setId(null);
        assertThat(projects1).isNotEqualTo(projects2);
    }
}
