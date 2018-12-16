package org.avenue1.attribute.repository;

import org.avenue1.attribute.domain.Attribute;
import org.avenue1.attribute.domain.AttributeValue;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the AttributeValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeValueRepository extends MongoRepository<AttributeValue, String> {
    Optional<AttributeValue> findByAttributeAndValue(Attribute attribute, String value);
}
