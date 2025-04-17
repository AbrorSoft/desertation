package org.abror.service.mapper;

import org.abror.domain.ServiceProvider;
import org.abror.service.dto.ServiceProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceProvider} and its DTO {@link ServiceProviderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceProviderMapper extends EntityMapper<ServiceProviderDTO, ServiceProvider> {}
