package org.avenue1.attribute.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AttributeValue.
 */
@Document(collection = "attribute_value")
public class AttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("value")
    private String value;

    @Field("active")
    private Boolean active;

    @DBRef
    @Field("attribute")
    private Set<Attribute> attributes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public AttributeValue value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean isActive() {
        return active;
    }

    public AttributeValue active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public AttributeValue attributes(Set<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }



    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
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
        AttributeValue attributeValue = (AttributeValue) o;
        if (attributeValue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attributeValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
