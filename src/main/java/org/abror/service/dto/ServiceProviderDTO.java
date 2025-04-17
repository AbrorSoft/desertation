package org.abror.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.abror.domain.enumeration.ServiceProviderType;

/**
 * A DTO for the {@link org.abror.domain.ServiceProvider} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceProviderDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private ServiceProviderType type;

    private String address;

    private String contactInfo;

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

    public ServiceProviderType getType() {
        return type;
    }

    public void setType(ServiceProviderType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceProviderDTO)) {
            return false;
        }

        ServiceProviderDTO serviceProviderDTO = (ServiceProviderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceProviderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceProviderDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", address='" + getAddress() + "'" +
            ", contactInfo='" + getContactInfo() + "'" +
            "}";
    }
}
