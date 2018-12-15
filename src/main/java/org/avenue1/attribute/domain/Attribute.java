package org.avenue1.attribute.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.avenue1.attribute.domain.enumeration.DataTypeEnum;

/**
 * A Attribute.
 */
@Document(collection = "attribute")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("description")
    private String description;

    @Field("data_type")
    private DataTypeEnum dataType;

    @Field("active")
    private Boolean active;

    @Field("has_valid_values")
    private Boolean hasValidValues;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("status")
    private String status;

    @NotNull
    @Field("mandatory")
    private Boolean mandatory;

    @DBRef
    @Field("entityTypes")
    @JsonIgnore
    private Set<EntityType> entityTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Attribute name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Attribute description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataTypeEnum getDataType() {
        return dataType;
    }

    public Attribute dataType(DataTypeEnum dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }

    public Boolean isActive() {
        return active;
    }

    public Attribute active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isHasValidValues() {
        return hasValidValues;
    }

    public Attribute hasValidValues(Boolean hasValidValues) {
        this.hasValidValues = hasValidValues;
        return this;
    }

    public void setHasValidValues(Boolean hasValidValues) {
        this.hasValidValues = hasValidValues;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Attribute startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Attribute endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public Attribute status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isMandatory() {
        return mandatory;
    }

    public Attribute mandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Set<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public Attribute entityTypes(Set<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
        return this;
    }

    public Attribute addEntityType(EntityType entityType) {
        this.entityTypes.add(entityType);
        entityType.getAttributes().add(this);
        return this;
    }

    public Attribute removeEntityType(EntityType entityType) {
        this.entityTypes.remove(entityType);
        entityType.getAttributes().remove(this);
        return this;
    }

    public void setEntityTypes(Set<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
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
        Attribute attribute = (Attribute) o;
        if (attribute.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attribute.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", active='" + isActive() + "'" +
            ", hasValidValues='" + isHasValidValues() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", mandatory='" + isMandatory() + "'" +
            "}";
    }
}
