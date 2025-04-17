package org.abror.service.mapper;

import org.abror.domain.Room;
import org.abror.service.dto.RoomDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
}
