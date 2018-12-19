package org.avenue1.attribute.service;

import org.avenue1.attribute.domain.EntityType;
import org.avenue1.attribute.enums.EntityTypeEnum;
import org.avenue1.attribute.repository.EntityTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing EntityType.
 */
@Service
public class EntityTypeService {

    private final Logger log = LoggerFactory.getLogger(EntityTypeService.class);

    private final EntityTypeRepository entityTypeRepository;

    public EntityTypeService(EntityTypeRepository entityTypeRepository) {
        this.entityTypeRepository = entityTypeRepository;
    }

    /**
     * Save a entityType.
     *
     * @param entityType the entity to save
     * @return the persisted entity
     */
    public EntityType save(EntityType entityType) {
        log.debug("Request to save EntityType : {}", entityType);
        return entityTypeRepository.save(entityType);
    }

    public Optional<EntityType> findByEntityTypeEnum(EntityTypeEnum entityTypeEnum) {
        return entityTypeRepository.findByType(entityTypeEnum);
    }

    /**
     * Get all the entityTypes.
     *
     * @return the list of entities
     */
    public List<EntityType> findAll() {
        log.debug("Request to get all EntityTypes");
        return entityTypeRepository.findAll();
    }


    /**
     * Get one entityType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<EntityType> findOne(String id) {
        log.debug("Request to get EntityType : {}", id);
        return entityTypeRepository.findById(id);
    }

    /**
     * Delete the entityType by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete EntityType : {}", id);
        entityTypeRepository.deleteById(id);
    }
}
