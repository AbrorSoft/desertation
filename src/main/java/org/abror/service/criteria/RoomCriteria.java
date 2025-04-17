package org.abror.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.abror.domain.Room} entity. This class is used
 * in {@link org.abror.web.rest.RoomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rooms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter roomNumber;

    private StringFilter description;

    private LongFilter serviceProviderId;

    private Boolean distinct;

    public RoomCriteria() {}

    public RoomCriteria(RoomCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.roomNumber = other.optionalRoomNumber().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.serviceProviderId = other.optionalServiceProviderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RoomCriteria copy() {
        return new RoomCriteria(this);
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

    public StringFilter getRoomNumber() {
        return roomNumber;
    }

    public Optional<StringFilter> optionalRoomNumber() {
        return Optional.ofNullable(roomNumber);
    }

    public StringFilter roomNumber() {
        if (roomNumber == null) {
            setRoomNumber(new StringFilter());
        }
        return roomNumber;
    }

    public void setRoomNumber(StringFilter roomNumber) {
        this.roomNumber = roomNumber;
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
        final RoomCriteria that = (RoomCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(roomNumber, that.roomNumber) &&
            Objects.equals(description, that.description) &&
            Objects.equals(serviceProviderId, that.serviceProviderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, description, serviceProviderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRoomNumber().map(f -> "roomNumber=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalServiceProviderId().map(f -> "serviceProviderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
