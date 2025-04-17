package org.abror.service.mapper;

import org.abror.domain.ServiceForCustomer;
import org.abror.service.dto.ServiceForCustomerDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ServiceForCustomer} and its DTO {@link ServiceForCustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceForCustomerMapper extends EntityMapper<ServiceForCustomerDTO, ServiceForCustomer> {
}
