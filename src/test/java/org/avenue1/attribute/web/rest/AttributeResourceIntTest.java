package org.avenue1.attribute.web.rest;

import org.avenue1.attribute.AttributeSvcApp;

import org.avenue1.attribute.domain.Attribute;
import org.avenue1.attribute.domain.EntityType;
import org.avenue1.attribute.enums.EntityTypeEnum;
import org.avenue1.attribute.repository.AttributeRepository;
import org.avenue1.attribute.repository.EntityTypeRepository;
import org.avenue1.attribute.service.AttributeService;
import org.avenue1.attribute.web.rest.errors.ExceptionTranslator;

import org.checkerframework.checker.units.qual.A;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.avenue1.attribute.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.avenue1.attribute.domain.enumeration.DataTypeEnum;
/**
 * Test class for the AttributeResource REST controller.
 *
 * @see AttributeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AttributeSvcApp.class)
public class AttributeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";
    private static List<EntityType> DEFAULT_ENTITY_TYPES = new ArrayList();
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DataTypeEnum DEFAULT_DATA_TYPE = DataTypeEnum.STRING;
    private static final DataTypeEnum UPDATED_DATA_TYPE = DataTypeEnum.INTEGER;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_HAS_VALID_VALUES = false;
    private static final Boolean UPDATED_HAS_VALID_VALUES = true;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MANDATORY = false;
    private static final Boolean UPDATED_MANDATORY = true;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private EntityTypeRepository entityTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAttributeMockMvc;

    private Attribute attribute;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final AttributeResource attributeResource = new AttributeResource(attributeService);
        this.restAttributeMockMvc = MockMvcBuilders.standaloneSetup(attributeResource)
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
    public static Attribute createEntity() {

        Attribute attribute = new Attribute()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .dataType(DEFAULT_DATA_TYPE)
            .active(DEFAULT_ACTIVE)
            .hasValidValues(DEFAULT_HAS_VALID_VALUES)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .mandatory(DEFAULT_MANDATORY);
        return attribute;
    }

    @Before
    public void initTest() {
        attributeRepository.deleteAll();
        EntityType mockEntityType = new EntityType().type(EntityTypeEnum.CALENDAR).description("Calendar");
        entityTypeRepository.save(mockEntityType);
        attribute = createEntity();
        DEFAULT_ENTITY_TYPES = entityTypeRepository.findAll();
        attribute.getEntityTypes().addAll(DEFAULT_ENTITY_TYPES);
    }



    @Test
    public void createAttribute() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isCreated());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate + 1);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttribute.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAttribute.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testAttribute.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testAttribute.isHasValidValues()).isEqualTo(DEFAULT_HAS_VALID_VALUES);
        assertThat(testAttribute.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAttribute.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAttribute.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAttribute.isMandatory()).isEqualTo(DEFAULT_MANDATORY);
    }

    @Test
    public void createAttributeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute with an existing ID
        attribute.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setName(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkEntityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.getEntityTypes().clear();

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setDescription(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkMandatoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setMandatory(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAttributes() throws Exception {
        // Initialize the database
        attributeRepository.save(attribute);

        // Get all the attributeList
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].hasValidValues").value(hasItem(DEFAULT_HAS_VALID_VALUES.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY.booleanValue())));
    }
    
    @Test
    public void getAttribute() throws Exception {
        // Initialize the database
        attributeRepository.save(attribute);

        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", attribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attribute.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.hasValidValues").value(DEFAULT_HAS_VALID_VALUES.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.mandatory").value(DEFAULT_MANDATORY.booleanValue()));
    }

    @Test
    public void getNonExistingAttribute() throws Exception {
        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Update the attribute
        Attribute updatedAttribute = attributeRepository.findById(attribute.getId()).get();
        updatedAttribute
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .dataType(UPDATED_DATA_TYPE)
            .active(UPDATED_ACTIVE)
            .hasValidValues(UPDATED_HAS_VALID_VALUES)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .mandatory(UPDATED_MANDATORY);

        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttribute)))
            .andExpect(status().isOk());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttribute.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAttribute.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testAttribute.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAttribute.isHasValidValues()).isEqualTo(UPDATED_HAS_VALID_VALUES);
        assertThat(testAttribute.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAttribute.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAttribute.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAttribute.isMandatory()).isEqualTo(UPDATED_MANDATORY);
    }

    @Test
    public void updateNonExistingAttribute() throws Exception {
        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Create the Attribute

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeDelete = attributeRepository.findAll().size();

        // Get the attribute
        restAttributeMockMvc.perform(delete("/api/attributes/{id}", attribute.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribute.class);
        Attribute attribute1 = new Attribute();
        attribute1.setId("id1");
        Attribute attribute2 = new Attribute();
        attribute2.setId(attribute1.getId());
        assertThat(attribute1).isEqualTo(attribute2);
        attribute2.setId("id2");
        assertThat(attribute1).isNotEqualTo(attribute2);
        attribute1.setId(null);
        assertThat(attribute1).isNotEqualTo(attribute2);
    }
}
