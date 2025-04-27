package org.abror.service.impl;

import java.util.Optional;
import org.abror.domain.ServiceProvider;
import org.abror.repository.ServiceProviderRepository;
import org.abror.service.FileUploadService;
import org.abror.service.ServiceProviderService;
import org.abror.service.dto.ServiceProviderDTO;
import org.abror.service.mapper.ServiceProviderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.abror.domain.ServiceProvider}.
 */
@Service
@Transactional
public class ServiceProviderServiceImpl implements ServiceProviderService {

    private final Logger log = LoggerFactory.getLogger(ServiceProviderServiceImpl.class);

    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceProviderMapper serviceProviderMapper;
    private final FileUploadService fileUploadService;

    public ServiceProviderServiceImpl(
        ServiceProviderRepository serviceProviderRepository,
        FileUploadService fileUploadService,
        ServiceProviderMapper serviceProviderMapper
    ) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.fileUploadService = fileUploadService;
        this.serviceProviderMapper = serviceProviderMapper;
    }

    @Override
    public ServiceProviderDTO save(ServiceProviderDTO serviceProviderDTO) {
        log.debug("Request to save ServiceProvider : {}", serviceProviderDTO);
        saveImageFile(serviceProviderDTO);

        ServiceProvider serviceProvider = serviceProviderMapper.toEntity(serviceProviderDTO);
        serviceProvider = serviceProviderRepository.save(serviceProvider);
        return serviceProviderMapper.toDto(serviceProvider);
    }

    @Override
    public ServiceProviderDTO update(ServiceProviderDTO serviceProviderDTO) {
        log.debug("Request to update ServiceProvider : {}", serviceProviderDTO);
        saveImageFile(serviceProviderDTO);

        ServiceProvider serviceProvider = serviceProviderMapper.toEntity(serviceProviderDTO);
        serviceProvider = serviceProviderRepository.save(serviceProvider);
        return serviceProviderMapper.toDto(serviceProvider);
    }

    @Override
    public Optional<ServiceProviderDTO> partialUpdate(ServiceProviderDTO serviceProviderDTO) {
        log.debug("Request to partially update ServiceProvider : {}", serviceProviderDTO);

        return serviceProviderRepository
            .findById(serviceProviderDTO.getId())
            .map(existingServiceProvider -> {
                serviceProviderMapper.partialUpdate(existingServiceProvider, serviceProviderDTO);
                if (serviceProviderDTO.getImageFile() != null) {
                    saveImageFile(serviceProviderDTO);
                }
                return existingServiceProvider;
            })
            .map(serviceProviderRepository::save)
            .map(serviceProviderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceProviderDTO> findOne(Long id) {
        log.debug("Request to get ServiceProvider : {}", id);
        return serviceProviderRepository.findById(id).map(serviceProviderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceProvider : {}", id);
        serviceProviderRepository.deleteById(id);
    }

    private void saveImageFile(ServiceProviderDTO serviceProviderDTO) {
        if (serviceProviderDTO.getImageFile() != null && serviceProviderDTO.getImageKey() != null) {
            fileUploadService.uploadFile(serviceProviderDTO.getImageKey(), serviceProviderDTO.getImageFile());
        }
    }
}
