package org.abror.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ServiceForCustomer.
 */
@Entity
@Table(name = "service_for_customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceForCustomer implements Serializable {

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

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceProvider serviceProvider;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_service_for_customer__employees",
        joinColumns = @JoinColumn(name = "service_for_customer_id"),
        inverseJoinColumns = @JoinColumn(name = "employees_id")
    )
    @JsonIgnoreProperties(value = { "serviceProvider", "servicesForCustomers", "services" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceForCustomer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ServiceForCustomer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ServiceForCustomer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public ServiceForCustomer duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ServiceProvider getServiceProvider() {
        return this.serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ServiceForCustomer serviceProvider(ServiceProvider serviceProvider) {
        this.setServiceProvider(serviceProvider);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public ServiceForCustomer employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public ServiceForCustomer addEmployees(Employee employee) {
        this.employees.add(employee);
        return this;
    }

    public ServiceForCustomer removeEmployees(Employee employee) {
        this.employees.remove(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceForCustomer)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceForCustomer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceForCustomer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", duration=" + getDuration() +
            "}";
    }
}
