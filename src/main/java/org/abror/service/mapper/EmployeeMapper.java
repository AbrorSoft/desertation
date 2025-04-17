package org.abror.service.mapper;

import org.abror.domain.Employee;
import org.abror.service.dto.EmployeeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
}
