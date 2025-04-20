package org.abror.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.abror.domain.enumeration.ServiceProviderType;

/**
 * A ServiceProvider.
 */
@Entity
@Table(name = "service_provider")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceProvider implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ServiceProviderType type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_info")
    private String contactInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceProvider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ServiceProvider name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceProviderType getType() {
        return this.type;
    }

    public ServiceProvider type(ServiceProviderType type) {
        this.setType(type);
        return this;
    }

    public void setType(ServiceProviderType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ServiceProvider amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public ServiceProvider address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public ServiceProvider contactInfo(String contactInfo) {
        this.setContactInfo(contactInfo);
        return this;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceProvider)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceProvider) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceProvider{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", address='" + getAddress() + "'" +
            ", contactInfo='" + getContactInfo() + "'" +
            "}";
    }
}
