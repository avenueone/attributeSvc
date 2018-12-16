package org.avenue1.attribute.repository;

import org.avenue1.attribute.domain.Attribute;
import org.avenue1.attribute.enums.EntityTypeEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * Spring Data MongoDB repository for the Attribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeRepository extends MongoRepository<Attribute, String> {

    List<Attribute> findAllByEntityTypes(Collection<EntityTypeEnum>  types);
}
