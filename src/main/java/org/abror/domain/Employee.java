package org.abror.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "specialization", length = 255, nullable = false)
    private String specialization;

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceProvider serviceProvider;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "employees")
    @JsonIgnoreProperties(value = { "serviceProvider", "employees" }, allowSetters = true)
    private Set<ServiceForCustomer> servicesForCustomers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Employee name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public Employee specialization(String specialization) {
        this.setSpecialization(specialization);
        return this;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public ServiceProvider getServiceProvider() {
        return this.serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Employee serviceProvider(ServiceProvider serviceProvider) {
        this.setServiceProvider(serviceProvider);
        return this;
    }

    public Set<ServiceForCustomer> getServicesForCustomers() {
        return this.servicesForCustomers;
    }

    public void setServicesForCustomers(Set<ServiceForCustomer> serviceForCustomers) {
        if (this.servicesForCustomers != null) {
            this.servicesForCustomers.forEach(i -> i.removeEmployees(this));
        }
        if (serviceForCustomers != null) {
            serviceForCustomers.forEach(i -> i.addEmployees(this));
        }
        this.servicesForCustomers = serviceForCustomers;
    }

    public Employee servicesForCustomers(Set<ServiceForCustomer> serviceForCustomers) {
        this.setServicesForCustomers(serviceForCustomers);
        return this;
    }

    public Employee addServicesForCustomer(ServiceForCustomer serviceForCustomer) {
        this.servicesForCustomers.add(serviceForCustomer);
        serviceForCustomer.getEmployees().add(this);
        return this;
    }

    public Employee removeServicesForCustomer(ServiceForCustomer serviceForCustomer) {
        this.servicesForCustomers.remove(serviceForCustomer);
        serviceForCustomer.getEmployees().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            "}";
    }
}
