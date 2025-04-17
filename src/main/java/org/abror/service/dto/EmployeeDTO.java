package org.abror.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link org.abror.domain.Employee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String specialization;

    private ServiceProviderDTO serviceProvider;

    private Set<ServiceForCustomerDTO> servicesForCustomers = new HashSet<>();

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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public ServiceProviderDTO getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProviderDTO serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Set<ServiceForCustomerDTO> getServicesForCustomers() {
        return servicesForCustomers;
    }

    public void setServicesForCustomers(Set<ServiceForCustomerDTO> servicesForCustomers) {
        this.servicesForCustomers = servicesForCustomers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", serviceProvider=" + getServiceProvider() +
            ", servicesForCustomers=" + getServicesForCustomers() +
            "}";
    }
}
