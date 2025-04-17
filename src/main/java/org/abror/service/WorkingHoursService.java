package org.abror.service;

import java.util.Optional;
import org.abror.service.dto.WorkingHoursDTO;

/**
 * Service Interface for managing {@link org.abror.domain.WorkingHours}.
 */
public interface WorkingHoursService {
    /**
     * Save a workingHours.
     *
     * @param workingHoursDTO the entity to save.
     * @return the persisted entity.
     */
    WorkingHoursDTO save(WorkingHoursDTO workingHoursDTO);

    /**
     * Updates a workingHours.
     *
     * @param workingHoursDTO the entity to update.
     * @return the persisted entity.
     */
    WorkingHoursDTO update(WorkingHoursDTO workingHoursDTO);

    /**
     * Partially updates a workingHours.
     *
     * @param workingHoursDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkingHoursDTO> partialUpdate(WorkingHoursDTO workingHoursDTO);

    /**
     * Get the "id" workingHours.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkingHoursDTO> findOne(Long id);

    /**
     * Delete the "id" workingHours.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
