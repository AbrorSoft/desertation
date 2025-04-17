package org.abror.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.abror.domain.enumeration.DayOfWeek;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.abror.domain.WorkingHours} entity. This class is used
 * in {@link org.abror.web.rest.WorkingHoursResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /working-hours?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkingHoursCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DayOfWeek
     */
    public static class DayOfWeekFilter extends Filter<DayOfWeek> {

        public DayOfWeekFilter() {}

        public DayOfWeekFilter(DayOfWeekFilter filter) {
            super(filter);
        }

        @Override
        public DayOfWeekFilter copy() {
            return new DayOfWeekFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DayOfWeekFilter dayOfWeek;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private LongFilter employeeId;

    private Boolean distinct;

    public WorkingHoursCriteria() {}

    public WorkingHoursCriteria(WorkingHoursCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dayOfWeek = other.optionalDayOfWeek().map(DayOfWeekFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(InstantFilter::copy).orElse(null);
        this.endTime = other.optionalEndTime().map(InstantFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WorkingHoursCriteria copy() {
        return new WorkingHoursCriteria(this);
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

    public DayOfWeekFilter getDayOfWeek() {
        return dayOfWeek;
    }

    public Optional<DayOfWeekFilter> optionalDayOfWeek() {
        return Optional.ofNullable(dayOfWeek);
    }

    public DayOfWeekFilter dayOfWeek() {
        if (dayOfWeek == null) {
            setDayOfWeek(new DayOfWeekFilter());
        }
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekFilter dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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
        final WorkingHoursCriteria that = (WorkingHoursCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dayOfWeek, that.dayOfWeek) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dayOfWeek, startTime, endTime, employeeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingHoursCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDayOfWeek().map(f -> "dayOfWeek=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalEndTime().map(f -> "endTime=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
