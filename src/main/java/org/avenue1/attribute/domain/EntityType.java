package org.avenue1.attribute.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.avenue1.attribute.domain.enumeration.DataTypeEnum;
import org.avenue1.attribute.enums.EntityTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.*;

/**
 * A EntityType.
 */
@Document(collection = "entity_type")
public class EntityType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("type")
    private EntityTypeEnum type;

    @Field("description")
    private String description;

    @Field("service")
    private String service;

    @DBRef
    @Field("attributes")
    @JsonIgnore
    private Set<Attribute> attributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EntityTypeEnum getType() {
        return type;
    }

    public EntityType type(EntityTypeEnum type) {
        this.type = type;
        return this;
    }



    public void setType(EntityTypeEnum type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public EntityType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getService() {
        return service;
    }

    public EntityType service(String service) {
        this.service = service;
        return this;
    }

    public void setService(String service) {
        this.service = service;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityType entityType = (EntityType) o;
        if (entityType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EntityType.class.getSimpleName() + "[", "]")
            .add("id='" + id + "'")
            .add("type=" + type)
            .add("description='" + description + "'")
            .add("service='" + service + "'")
            .add("attributes=" + attributes)
            .toString();
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }
}
