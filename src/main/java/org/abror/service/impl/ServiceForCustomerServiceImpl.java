package org.abror.service.impl;

import java.util.Optional;
import org.abror.domain.ServiceForCustomer;
import org.abror.repository.ServiceForCustomerRepository;
import org.abror.service.ServiceForCustomerService;
import org.abror.service.dto.ServiceForCustomerDTO;
import org.abror.service.mapper.ServiceForCustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.abror.domain.ServiceForCustomer}.
 */
@Service
@Transactional
public class ServiceForCustomerServiceImpl implements ServiceForCustomerService {

    private final Logger log = LoggerFactory.getLogger(ServiceForCustomerServiceImpl.class);

    private final ServiceForCustomerRepository serviceForCustomerRepository;

    private final ServiceForCustomerMapper serviceForCustomerMapper;

    public ServiceForCustomerServiceImpl(
        ServiceForCustomerRepository serviceForCustomerRepository,
        ServiceForCustomerMapper serviceForCustomerMapper
    ) {
        this.serviceForCustomerRepository = serviceForCustomerRepository;
        this.serviceForCustomerMapper = serviceForCustomerMapper;
    }

    @Override
    public ServiceForCustomerDTO save(ServiceForCustomerDTO serviceForCustomerDTO) {
        log.debug("Request to save ServiceForCustomer : {}", serviceForCustomerDTO);
        ServiceForCustomer serviceForCustomer = serviceForCustomerMapper.toEntity(serviceForCustomerDTO);
        serviceForCustomer = serviceForCustomerRepository.save(serviceForCustomer);
        return serviceForCustomerMapper.toDto(serviceForCustomer);
    }

    @Override
    public ServiceForCustomerDTO update(ServiceForCustomerDTO serviceForCustomerDTO) {
        log.debug("Request to update ServiceForCustomer : {}", serviceForCustomerDTO);
        ServiceForCustomer serviceForCustomer = serviceForCustomerMapper.toEntity(serviceForCustomerDTO);
        serviceForCustomer = serviceForCustomerRepository.save(serviceForCustomer);
        return serviceForCustomerMapper.toDto(serviceForCustomer);
    }

    @Override
    public Optional<ServiceForCustomerDTO> partialUpdate(ServiceForCustomerDTO serviceForCustomerDTO) {
        log.debug("Request to partially update ServiceForCustomer : {}", serviceForCustomerDTO);

        return serviceForCustomerRepository
            .findById(serviceForCustomerDTO.getId())
            .map(existingServiceForCustomer -> {
                serviceForCustomerMapper.partialUpdate(existingServiceForCustomer, serviceForCustomerDTO);

                return existingServiceForCustomer;
            })
            .map(serviceForCustomerRepository::save)
            .map(serviceForCustomerMapper::toDto);
    }

    public Page<ServiceForCustomerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return serviceForCustomerRepository.findAllWithEagerRelationships(pageable).map(serviceForCustomerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceForCustomerDTO> findOne(Long id) {
        log.debug("Request to get ServiceForCustomer : {}", id);
        return serviceForCustomerRepository.findOneWithEagerRelationships(id).map(serviceForCustomerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceForCustomer : {}", id);
        serviceForCustomerRepository.deleteById(id);
    }
}
