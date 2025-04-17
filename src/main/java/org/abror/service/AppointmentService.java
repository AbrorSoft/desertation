package org.abror.service;

import java.util.Optional;
import org.abror.service.dto.AppointmentDTO;

/**
 * Service Interface for managing {@link org.abror.domain.Appointment}.
 */
public interface AppointmentService {
    /**
     * Save a appointment.
     *
     * @param appointmentDTO the entity to save.
     * @return the persisted entity.
     */
    AppointmentDTO save(AppointmentDTO appointmentDTO);

    /**
     * Updates a appointment.
     *
     * @param appointmentDTO the entity to update.
     * @return the persisted entity.
     */
    AppointmentDTO update(AppointmentDTO appointmentDTO);

    /**
     * Partially updates a appointment.
     *
     * @param appointmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppointmentDTO> partialUpdate(AppointmentDTO appointmentDTO);

    /**
     * Get the "id" appointment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppointmentDTO> findOne(Long id);

    /**
     * Delete the "id" appointment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
