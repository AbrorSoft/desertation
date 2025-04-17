package org.abror.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.abror.domain.ServiceForCustomer} entity. This class is used
 * in {@link org.abror.web.rest.ServiceForCustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-for-customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceForCustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter duration;

    private LongFilter serviceProviderId;

    private LongFilter employeesId;

    private Boolean distinct;

    public ServiceForCustomerCriteria() {}

    public ServiceForCustomerCriteria(ServiceForCustomerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.duration = other.optionalDuration().map(IntegerFilter::copy).orElse(null);
        this.serviceProviderId = other.optionalServiceProviderId().map(LongFilter::copy).orElse(null);
        this.employeesId = other.optionalEmployeesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ServiceForCustomerCriteria copy() {
        return new ServiceForCustomerCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public Optional<IntegerFilter> optionalDuration() {
        return Optional.ofNullable(duration);
    }

    public IntegerFilter duration() {
        if (duration == null) {
            setDuration(new IntegerFilter());
        }
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
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

    public LongFilter getEmployeesId() {
        return employeesId;
    }

    public Optional<LongFilter> optionalEmployeesId() {
        return Optional.ofNullable(employeesId);
    }

    public LongFilter employeesId() {
        if (employeesId == null) {
            setEmployeesId(new LongFilter());
        }
        return employeesId;
    }

    public void setEmployeesId(LongFilter employeesId) {
        this.employeesId = employeesId;
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
        final ServiceForCustomerCriteria that = (ServiceForCustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(serviceProviderId, that.serviceProviderId) &&
            Objects.equals(employeesId, that.employeesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, duration, serviceProviderId, employeesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceForCustomerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDuration().map(f -> "duration=" + f + ", ").orElse("") +
            optionalServiceProviderId().map(f -> "serviceProviderId=" + f + ", ").orElse("") +
            optionalEmployeesId().map(f -> "employeesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
