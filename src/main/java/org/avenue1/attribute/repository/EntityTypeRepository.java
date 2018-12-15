package org.avenue1.attribute.repository;

import org.avenue1.attribute.domain.EntityType;
import org.avenue1.attribute.enums.EntityTypeEnum;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the EntityType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityTypeRepository extends MongoRepository<EntityType, String> {
    Optional<EntityType> findByType(EntityTypeEnum type);
}
