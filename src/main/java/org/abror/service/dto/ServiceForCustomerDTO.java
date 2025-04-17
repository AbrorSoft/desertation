package org.abror.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link org.abror.domain.ServiceForCustomer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceForCustomerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    private Integer duration;

    private ServiceProviderDTO serviceProvider;

    private Set<EmployeeDTO> employees = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ServiceProviderDTO getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProviderDTO serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Set<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeDTO> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceForCustomerDTO)) {
            return false;
        }

        ServiceForCustomerDTO serviceForCustomerDTO = (ServiceForCustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceForCustomerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceForCustomerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", duration=" + getDuration() +
            ", serviceProvider=" + getServiceProvider() +
            ", employees=" + getEmployees() +
            "}";
    }
}
