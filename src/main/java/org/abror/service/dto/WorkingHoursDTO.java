package org.abror.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import org.abror.domain.enumeration.DayOfWeek;

/**
 * A DTO for the {@link org.abror.domain.WorkingHours} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkingHoursDTO implements Serializable {

    private Long id;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkingHoursDTO)) {
            return false;
        }

        WorkingHoursDTO workingHoursDTO = (WorkingHoursDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workingHoursDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingHoursDTO{" +
            "id=" + getId() +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
