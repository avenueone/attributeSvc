package org.avenue1.attribute.service;

import org.avenue1.attribute.domain.AttributeValue;
import org.avenue1.attribute.repository.AttributeValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing AttributeValue.
 */
@Service
public class AttributeValueService {

    private final Logger log = LoggerFactory.getLogger(AttributeValueService.class);

    private final AttributeValueRepository attributeValueRepository;

    public AttributeValueService(AttributeValueRepository attributeValueRepository) {
        this.attributeValueRepository = attributeValueRepository;
    }

    /**
     * Save a attributeValue.
     *
     * @param attributeValue the entity to save
     * @return the persisted entity
     */
    public AttributeValue save(AttributeValue attributeValue) {
        log.debug("Request to save AttributeValue : {}", attributeValue);
        return attributeValueRepository.save(attributeValue);
    }

    /**
     * Get all the attributeValues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<AttributeValue> findAll(Pageable pageable) {
        log.debug("Request to get all AttributeValues");
        return attributeValueRepository.findAll(pageable);
    }


    /**
     * Get one attributeValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<AttributeValue> findOne(String id) {
        log.debug("Request to get AttributeValue : {}", id);
        return attributeValueRepository.findById(id);
    }

    /**
     * Delete the attributeValue by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete AttributeValue : {}", id);
        attributeValueRepository.deleteById(id);
    }
}
