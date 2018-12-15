package org.avenue1.attribute.web.rest;

import org.avenue1.attribute.AttributeSvcApp;

import org.avenue1.attribute.domain.EntityType;
import org.avenue1.attribute.repository.EntityTypeRepository;
import org.avenue1.attribute.service.EntityTypeService;
import org.avenue1.attribute.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;


import static org.avenue1.attribute.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EntityTypeResource REST controller.
 *
 * @see EntityTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AttributeSvcApp.class)
public class EntityTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE = "BBBBBBBBBB";

    @Autowired
    private EntityTypeRepository entityTypeRepository;

    @Autowired
    private EntityTypeService entityTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restEntityTypeMockMvc;

    private EntityType entityType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntityTypeResource entityTypeResource = new EntityTypeResource(entityTypeService);
        this.restEntityTypeMockMvc = MockMvcBuilders.standaloneSetup(entityTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityType createEntity() {
        EntityType entityType = new EntityType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .service(DEFAULT_SERVICE);
        return entityType;
    }

    @Before
    public void initTest() {
        entityTypeRepository.deleteAll();
        entityType = createEntity();
    }

    @Test
    public void createEntityType() throws Exception {
        int databaseSizeBeforeCreate = entityTypeRepository.findAll().size();

        // Create the EntityType
        restEntityTypeMockMvc.perform(post("/api/entity-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityType)))
            .andExpect(status().isCreated());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EntityType testEntityType = entityTypeList.get(entityTypeList.size() - 1);
        assertThat(testEntityType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEntityType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEntityType.getService()).isEqualTo(DEFAULT_SERVICE);
    }

    @Test
    public void createEntityTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityTypeRepository.findAll().size();

        // Create the EntityType with an existing ID
        entityType.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityTypeMockMvc.perform(post("/api/entity-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityType)))
            .andExpect(status().isBadRequest());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = entityTypeRepository.findAll().size();
        // set the field null
        entityType.setName(null);

        // Create the EntityType, which fails.

        restEntityTypeMockMvc.perform(post("/api/entity-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityType)))
            .andExpect(status().isBadRequest());

        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEntityTypes() throws Exception {
        // Initialize the database
        entityTypeRepository.save(entityType);

        // Get all the entityTypeList
        restEntityTypeMockMvc.perform(get("/api/entity-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityType.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].service").value(hasItem(DEFAULT_SERVICE.toString())));
    }
    
    @Test
    public void getEntityType() throws Exception {
        // Initialize the database
        entityTypeRepository.save(entityType);

        // Get the entityType
        restEntityTypeMockMvc.perform(get("/api/entity-types/{id}", entityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityType.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.service").value(DEFAULT_SERVICE.toString()));
    }

    @Test
    public void getNonExistingEntityType() throws Exception {
        // Get the entityType
        restEntityTypeMockMvc.perform(get("/api/entity-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEntityType() throws Exception {
        // Initialize the database
        entityTypeService.save(entityType);

        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();

        // Update the entityType
        EntityType updatedEntityType = entityTypeRepository.findById(entityType.getId()).get();
        updatedEntityType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .service(UPDATED_SERVICE);

        restEntityTypeMockMvc.perform(put("/api/entity-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityType)))
            .andExpect(status().isOk());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
        EntityType testEntityType = entityTypeList.get(entityTypeList.size() - 1);
        assertThat(testEntityType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntityType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEntityType.getService()).isEqualTo(UPDATED_SERVICE);
    }

    @Test
    public void updateNonExistingEntityType() throws Exception {
        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();

        // Create the EntityType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntityTypeMockMvc.perform(put("/api/entity-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityType)))
            .andExpect(status().isBadRequest());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEntityType() throws Exception {
        // Initialize the database
        entityTypeService.save(entityType);

        int databaseSizeBeforeDelete = entityTypeRepository.findAll().size();

        // Get the entityType
        restEntityTypeMockMvc.perform(delete("/api/entity-types/{id}", entityType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityType.class);
        EntityType entityType1 = new EntityType();
        entityType1.setId("id1");
        EntityType entityType2 = new EntityType();
        entityType2.setId(entityType1.getId());
        assertThat(entityType1).isEqualTo(entityType2);
        entityType2.setId("id2");
        assertThat(entityType1).isNotEqualTo(entityType2);
        entityType1.setId(null);
        assertThat(entityType1).isNotEqualTo(entityType2);
    }
}
