package org.abror.service;

import java.util.Optional;
import org.abror.service.dto.ServiceForCustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.abror.domain.ServiceForCustomer}.
 */
public interface ServiceForCustomerService {
    /**
     * Save a serviceForCustomer.
     *
     * @param serviceForCustomerDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceForCustomerDTO save(ServiceForCustomerDTO serviceForCustomerDTO);

    /**
     * Updates a serviceForCustomer.
     *
     * @param serviceForCustomerDTO the entity to update.
     * @return the persisted entity.
     */
    ServiceForCustomerDTO update(ServiceForCustomerDTO serviceForCustomerDTO);

    /**
     * Partially updates a serviceForCustomer.
     *
     * @param serviceForCustomerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceForCustomerDTO> partialUpdate(ServiceForCustomerDTO serviceForCustomerDTO);

    /**
     * Get all the serviceForCustomers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceForCustomerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" serviceForCustomer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceForCustomerDTO> findOne(Long id);

    /**
     * Delete the "id" serviceForCustomer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
