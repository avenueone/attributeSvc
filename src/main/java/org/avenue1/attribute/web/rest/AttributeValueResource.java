package org.avenue1.attribute.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.avenue1.attribute.domain.AttributeValue;
import org.avenue1.attribute.service.AttributeValueService;
import org.avenue1.attribute.web.rest.errors.BadRequestAlertException;
import org.avenue1.attribute.web.rest.util.HeaderUtil;
import org.avenue1.attribute.web.rest.util.PaginationUtil;
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

/**
 * REST controller for managing AttributeValue.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AttributeValueResource {

    private final Logger log = LoggerFactory.getLogger(AttributeValueResource.class);

    private static final String ENTITY_NAME = "attributeSvcAttributeValue";

    private final AttributeValueService attributeValueService;

    public AttributeValueResource(AttributeValueService attributeValueService) {
        this.attributeValueService = attributeValueService;
    }

    /**
     * POST  /attribute-values : Create a new attributeValue.
     *
     * @param attributeValue the attributeValue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attributeValue, or with status 400 (Bad Request) if the attributeValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attribute-values")
    @Timed
    public ResponseEntity<AttributeValue> createAttributeValue(@Valid @RequestBody AttributeValue attributeValue) throws URISyntaxException {
        log.debug("REST request to save AttributeValue : {}", attributeValue);
        if (attributeValue.getId() != null) {
            throw new BadRequestAlertException("A new attributeValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributeValue result = attributeValueService.save(attributeValue);
        return ResponseEntity.created(new URI("/api/attribute-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attribute-values : Updates an existing attributeValue.
     *
     * @param attributeValue the attributeValue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attributeValue,
     * or with status 400 (Bad Request) if the attributeValue is not valid,
     * or with status 500 (Internal Server Error) if the attributeValue couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attribute-values")
    @Timed
    public ResponseEntity<AttributeValue> updateAttributeValue(@Valid @RequestBody AttributeValue attributeValue) throws URISyntaxException {
        log.debug("REST request to update AttributeValue : {}", attributeValue);
        if (attributeValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttributeValue result = attributeValueService.save(attributeValue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attributeValue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attribute-values : get all the attributeValues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attributeValues in body
     */
    @GetMapping("/attribute-values")
    @Timed
    public ResponseEntity<List<AttributeValue>> getAllAttributeValues(Pageable pageable) {
        log.debug("REST request to get a page of AttributeValues");
        Page<AttributeValue> page = attributeValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attribute-values");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /attribute-values/:id : get the "id" attributeValue.
     *
     * @param id the id of the attributeValue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attributeValue, or with status 404 (Not Found)
     */
    @GetMapping("/attribute-values/{id}")
    @Timed
    public ResponseEntity<AttributeValue> getAttributeValue(@PathVariable String id) {
        log.debug("REST request to get AttributeValue : {}", id);
        Optional<AttributeValue> attributeValue = attributeValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attributeValue);
    }

    /**
     * DELETE  /attribute-values/:id : delete the "id" attributeValue.
     *
     * @param id the id of the attributeValue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attribute-values/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable String id) {
        log.debug("REST request to delete AttributeValue : {}", id);
        attributeValueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
