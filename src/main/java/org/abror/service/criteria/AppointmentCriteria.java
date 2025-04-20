package org.abror.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.abror.domain.enumeration.AppointmentStatus;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.abror.domain.Appointment} entity. This class is used
 * in {@link org.abror.web.rest.AppointmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appointments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AppointmentStatus
     */
    public static class AppointmentStatusFilter extends Filter<AppointmentStatus> {

        public AppointmentStatusFilter() {}

        public AppointmentStatusFilter(AppointmentStatusFilter filter) {
            super(filter);
        }

        @Override
        public AppointmentStatusFilter copy() {
            return new AppointmentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private AppointmentStatusFilter status;

    private LongFilter serviceProviderId;

    private ServiceProviderCriteria.ServiceProviderTypeFilter serviceProviderType;

    private LongFilter employeeId;

    private LongFilter serviceForCustomerId;

    private LongFilter roomId;

    private LongFilter userId;

    private Boolean distinct;

    public AppointmentCriteria() {}

    public AppointmentCriteria(AppointmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(InstantFilter::copy).orElse(null);
        this.endTime = other.optionalEndTime().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AppointmentStatusFilter::copy).orElse(null);
        this.serviceProviderId = other.optionalServiceProviderId().map(LongFilter::copy).orElse(null);
        this.serviceProviderType = other
            .optionalServiceProviderType()
            .map(ServiceProviderCriteria.ServiceProviderTypeFilter::copy)
            .orElse(null);
        this.employeeId = other.optionalEmployeeId().map(LongFilter::copy).orElse(null);
        this.serviceForCustomerId = other.optionalServiceForCustomerId().map(LongFilter::copy).orElse(null);
        this.roomId = other.optionalRoomId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppointmentCriteria copy() {
        return new AppointmentCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public Optional<LocalDateFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public LocalDateFilter date() {
        if (date == null) {
            setDate(new LocalDateFilter());
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public Optional<InstantFilter> optionalStartTime() {
        return Optional.ofNullable(startTime);
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            setStartTime(new InstantFilter());
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public Optional<InstantFilter> optionalEndTime() {
        return Optional.ofNullable(endTime);
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            setEndTime(new InstantFilter());
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public AppointmentStatusFilter getStatus() {
        return status;
    }

    public Optional<AppointmentStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AppointmentStatusFilter status() {
        if (status == null) {
            setStatus(new AppointmentStatusFilter());
        }
        return status;
    }

    public void setStatus(AppointmentStatusFilter status) {
        this.status = status;
    }

    public Optional<ServiceProviderCriteria.ServiceProviderTypeFilter> optionalServiceProviderType() {
        return Optional.ofNullable(serviceProviderType);
    }

    public ServiceProviderCriteria.ServiceProviderTypeFilter getServiceProviderType() {
        return serviceProviderType;
    }

    public void setServiceProviderType(ServiceProviderCriteria.ServiceProviderTypeFilter serviceProviderType) {
        this.serviceProviderType = serviceProviderType;
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

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public Optional<LongFilter> optionalEmployeeId() {
        return Optional.ofNullable(employeeId);
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            setEmployeeId(new LongFilter());
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getServiceForCustomerId() {
        return serviceForCustomerId;
    }

    public Optional<LongFilter> optionalServiceForCustomerId() {
        return Optional.ofNullable(serviceForCustomerId);
    }

    public LongFilter serviceForCustomerId() {
        if (serviceForCustomerId == null) {
            setServiceForCustomerId(new LongFilter());
        }
        return serviceForCustomerId;
    }

    public void setServiceForCustomerId(LongFilter serviceForCustomerId) {
        this.serviceForCustomerId = serviceForCustomerId;
    }

    public LongFilter getRoomId() {
        return roomId;
    }

    public Optional<LongFilter> optionalRoomId() {
        return Optional.ofNullable(roomId);
    }

    public LongFilter roomId() {
        if (roomId == null) {
            setRoomId(new LongFilter());
        }
        return roomId;
    }

    public void setRoomId(LongFilter roomId) {
        this.roomId = roomId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(this.userId);
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
        final AppointmentCriteria that = (AppointmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(status, that.status) &&
            Objects.equals(serviceProviderId, that.serviceProviderId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(serviceForCustomerId, that.serviceForCustomerId) &&
            Objects.equals(roomId, that.roomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, startTime, endTime, status, serviceProviderId, employeeId, serviceForCustomerId, roomId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalEndTime().map(f -> "endTime=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalServiceProviderId().map(f -> "serviceProviderId=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalServiceForCustomerId().map(f -> "serviceForCustomerId=" + f + ", ").orElse("") +
            optionalRoomId().map(f -> "roomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
