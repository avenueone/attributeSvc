package org.avenue1.attribute.web.rest;

import org.avenue1.attribute.AttributeSvcApp;

import org.avenue1.attribute.domain.AttributeValue;
import org.avenue1.attribute.repository.AttributeValueRepository;
import org.avenue1.attribute.service.AttributeValueService;
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
 * Test class for the AttributeValueResource REST controller.
 *
 * @see AttributeValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AttributeSvcApp.class)
public class AttributeValueResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private AttributeValueService attributeValueService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAttributeValueMockMvc;

    private AttributeValue attributeValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributeValueResource attributeValueResource = new AttributeValueResource(attributeValueService);
        this.restAttributeValueMockMvc = MockMvcBuilders.standaloneSetup(attributeValueResource)
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
    public static AttributeValue createEntity() {
        AttributeValue attributeValue = new AttributeValue()
            .value(DEFAULT_VALUE)
            .active(DEFAULT_ACTIVE);
        return attributeValue;
    }

    @Before
    public void initTest() {
        attributeValueRepository.deleteAll();
        attributeValue = createEntity();
    }

    @Test
    public void createAttributeValue() throws Exception {
        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();

        // Create the AttributeValue
        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isCreated());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate + 1);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testAttributeValue.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    public void createAttributeValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();

        // Create the AttributeValue with an existing ID
        attributeValue.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeValueRepository.findAll().size();
        // set the field null
        attributeValue.setValue(null);

        // Create the AttributeValue, which fails.

        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAttributeValues() throws Exception {
        // Initialize the database
        attributeValueRepository.save(attributeValue);

        // Get all the attributeValueList
        restAttributeValueMockMvc.perform(get("/api/attribute-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    public void getAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.save(attributeValue);

        // Get the attributeValue
        restAttributeValueMockMvc.perform(get("/api/attribute-values/{id}", attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attributeValue.getId()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    public void getNonExistingAttributeValue() throws Exception {
        // Get the attributeValue
        restAttributeValueMockMvc.perform(get("/api/attribute-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAttributeValue() throws Exception {
        // Initialize the database
        attributeValueService.save(attributeValue);

        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Update the attributeValue
        AttributeValue updatedAttributeValue = attributeValueRepository.findById(attributeValue.getId()).get();
        updatedAttributeValue
            .value(UPDATED_VALUE)
            .active(UPDATED_ACTIVE);

        restAttributeValueMockMvc.perform(put("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttributeValue)))
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAttributeValue.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    public void updateNonExistingAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Create the AttributeValue

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc.perform(put("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAttributeValue() throws Exception {
        // Initialize the database
        attributeValueService.save(attributeValue);

        int databaseSizeBeforeDelete = attributeValueRepository.findAll().size();

        // Get the attributeValue
        restAttributeValueMockMvc.perform(delete("/api/attribute-values/{id}", attributeValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeValue.class);
        AttributeValue attributeValue1 = new AttributeValue();
        attributeValue1.setId("id1");
        AttributeValue attributeValue2 = new AttributeValue();
        attributeValue2.setId(attributeValue1.getId());
        assertThat(attributeValue1).isEqualTo(attributeValue2);
        attributeValue2.setId("id2");
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
        attributeValue1.setId(null);
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
    }
}
