package org.abror.service;

import java.util.Optional;
import org.abror.service.dto.RoomDTO;

/**
 * Service Interface for managing {@link org.abror.domain.Room}.
 */
public interface RoomService {
    /**
     * Save a room.
     *
     * @param roomDTO the entity to save.
     * @return the persisted entity.
     */
    RoomDTO save(RoomDTO roomDTO);

    /**
     * Updates a room.
     *
     * @param roomDTO the entity to update.
     * @return the persisted entity.
     */
    RoomDTO update(RoomDTO roomDTO);

    /**
     * Partially updates a room.
     *
     * @param roomDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoomDTO> partialUpdate(RoomDTO roomDTO);

    /**
     * Get the "id" room.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomDTO> findOne(Long id);

    /**
     * Delete the "id" room.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
