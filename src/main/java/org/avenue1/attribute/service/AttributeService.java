package org.avenue1.attribute.service;

import org.avenue1.attribute.domain.Attribute;
import org.avenue1.attribute.repository.AttributeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing Attribute.
 */
@Service
public class AttributeService {

    private final Logger log = LoggerFactory.getLogger(AttributeService.class);

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    /**
     * Save a attribute.
     *
     * @param attribute the entity to save
     * @return the persisted entity
     */
    public Attribute save(Attribute attribute) {
        log.debug("Request to save Attribute : {}", attribute);
        return attributeRepository.save(attribute);
    }

    /**
     * Get all the attributes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Attribute> findAll(Pageable pageable) {
        log.debug("Request to get all Attributes");
        return attributeRepository.findAll(pageable);
    }


    /**
     * Get one attribute by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Attribute> findOne(String id) {
        log.debug("Request to get Attribute : {}", id);
        return attributeRepository.findById(id);
    }

    /**
     * Delete the attribute by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Attribute : {}", id);
        attributeRepository.deleteById(id);
    }
}
