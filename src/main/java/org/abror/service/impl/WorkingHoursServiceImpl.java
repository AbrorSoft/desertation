package org.abror.service.impl;

import java.util.Optional;
import org.abror.domain.WorkingHours;
import org.abror.repository.WorkingHoursRepository;
import org.abror.service.WorkingHoursService;
import org.abror.service.dto.WorkingHoursDTO;
import org.abror.service.mapper.WorkingHoursMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.abror.domain.WorkingHours}.
 */
@Service
@Transactional
public class WorkingHoursServiceImpl implements WorkingHoursService {

    private final Logger log = LoggerFactory.getLogger(WorkingHoursServiceImpl.class);

    private final WorkingHoursRepository workingHoursRepository;

    private final WorkingHoursMapper workingHoursMapper;

    public WorkingHoursServiceImpl(WorkingHoursRepository workingHoursRepository, WorkingHoursMapper workingHoursMapper) {
        this.workingHoursRepository = workingHoursRepository;
        this.workingHoursMapper = workingHoursMapper;
    }

    @Override
    public WorkingHoursDTO save(WorkingHoursDTO workingHoursDTO) {
        log.debug("Request to save WorkingHours : {}", workingHoursDTO);
        WorkingHours workingHours = workingHoursMapper.toEntity(workingHoursDTO);
        workingHours = workingHoursRepository.save(workingHours);
        return workingHoursMapper.toDto(workingHours);
    }

    @Override
    public WorkingHoursDTO update(WorkingHoursDTO workingHoursDTO) {
        log.debug("Request to update WorkingHours : {}", workingHoursDTO);
        WorkingHours workingHours = workingHoursMapper.toEntity(workingHoursDTO);
        workingHours = workingHoursRepository.save(workingHours);
        return workingHoursMapper.toDto(workingHours);
    }

    @Override
    public Optional<WorkingHoursDTO> partialUpdate(WorkingHoursDTO workingHoursDTO) {
        log.debug("Request to partially update WorkingHours : {}", workingHoursDTO);

        return workingHoursRepository
            .findById(workingHoursDTO.getId())
            .map(existingWorkingHours -> {
                workingHoursMapper.partialUpdate(existingWorkingHours, workingHoursDTO);

                return existingWorkingHours;
            })
            .map(workingHoursRepository::save)
            .map(workingHoursMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkingHoursDTO> findOne(Long id) {
        log.debug("Request to get WorkingHours : {}", id);
        return workingHoursRepository.findById(id).map(workingHoursMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkingHours : {}", id);
        workingHoursRepository.deleteById(id);
    }
}
