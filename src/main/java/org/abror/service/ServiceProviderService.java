package org.abror.service;

import java.util.Optional;
import org.abror.service.dto.ServiceProviderDTO;

/**
 * Service Interface for managing {@link org.abror.domain.ServiceProvider}.
 */
public interface ServiceProviderService {
    /**
     * Save a serviceProvider.
     *
     * @param serviceProviderDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceProviderDTO save(ServiceProviderDTO serviceProviderDTO);

    /**
     * Updates a serviceProvider.
     *
     * @param serviceProviderDTO the entity to update.
     * @return the persisted entity.
     */
    ServiceProviderDTO update(ServiceProviderDTO serviceProviderDTO);

    /**
     * Partially updates a serviceProvider.
     *
     * @param serviceProviderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceProviderDTO> partialUpdate(ServiceProviderDTO serviceProviderDTO);

    /**
     * Get the "id" serviceProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceProviderDTO> findOne(Long id);

    /**
     * Delete the "id" serviceProvider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
