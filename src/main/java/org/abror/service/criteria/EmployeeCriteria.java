package org.abror.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.abror.domain.Employee} entity. This class is used
 * in {@link org.abror.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter specialization;

    private LongFilter serviceProviderId;

    private LongFilter servicesForCustomerId;

    private LongFilter servicesId;

    private Boolean distinct;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.specialization = other.optionalSpecialization().map(StringFilter::copy).orElse(null);
        this.serviceProviderId = other.optionalServiceProviderId().map(LongFilter::copy).orElse(null);
        this.servicesForCustomerId = other.optionalServicesForCustomerId().map(LongFilter::copy).orElse(null);
        this.servicesId = other.optionalServicesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSpecialization() {
        return specialization;
    }

    public Optional<StringFilter> optionalSpecialization() {
        return Optional.ofNullable(specialization);
    }

    public StringFilter specialization() {
        if (specialization == null) {
            setSpecialization(new StringFilter());
        }
        return specialization;
    }

    public void setSpecialization(StringFilter specialization) {
        this.specialization = specialization;
    }

    public LongFilter getServiceProviderId() {
        return serviceProviderId;
    }

    public Optional<LongFilter> optionalServiceProviderId() {
        return Optional.ofNullable(serviceProviderId);
    }

    public LongFilter serviceProviderId() {
        if (serviceProviderId == null) {
            setServiceProviderId(new LongFilter());
        }
        return serviceProviderId;
    }

    public void setServiceProviderId(LongFilter serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public LongFilter getServicesForCustomerId() {
        return servicesForCustomerId;
    }

    public Optional<LongFilter> optionalServicesForCustomerId() {
        return Optional.ofNullable(servicesForCustomerId);
    }

    public LongFilter servicesForCustomerId() {
        if (servicesForCustomerId == null) {
            setServicesForCustomerId(new LongFilter());
        }
        return servicesForCustomerId;
    }

    public void setServicesForCustomerId(LongFilter servicesForCustomerId) {
        this.servicesForCustomerId = servicesForCustomerId;
    }

    public LongFilter getServicesId() {
        return servicesId;
    }

    public Optional<LongFilter> optionalServicesId() {
        return Optional.ofNullable(servicesId);
    }

    public LongFilter servicesId() {
        if (servicesId == null) {
            setServicesId(new LongFilter());
        }
        return servicesId;
    }

    public void setServicesId(LongFilter servicesId) {
        this.servicesId = servicesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(specialization, that.specialization) &&
            Objects.equals(serviceProviderId, that.serviceProviderId) &&
            Objects.equals(servicesForCustomerId, that.servicesForCustomerId) &&
            Objects.equals(servicesId, that.servicesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialization, serviceProviderId, servicesForCustomerId, servicesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSpecialization().map(f -> "specialization=" + f + ", ").orElse("") +
            optionalServiceProviderId().map(f -> "serviceProviderId=" + f + ", ").orElse("") +
            optionalServicesForCustomerId().map(f -> "servicesForCustomerId=" + f + ", ").orElse("") +
            optionalServicesId().map(f -> "servicesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
