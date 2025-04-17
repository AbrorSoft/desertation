package org.abror.service;

import jakarta.persistence.criteria.JoinType;
import org.abror.domain.*; // for static metamodels
import org.abror.domain.WorkingHours;
import org.abror.repository.WorkingHoursRepository;
import org.abror.service.criteria.WorkingHoursCriteria;
import org.abror.service.dto.WorkingHoursDTO;
import org.abror.service.mapper.WorkingHoursMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WorkingHours} entities in the database.
 * The main input is a {@link WorkingHoursCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WorkingHoursDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkingHoursQueryService extends QueryService<WorkingHours> {

    private final Logger log = LoggerFactory.getLogger(WorkingHoursQueryService.class);

    private final WorkingHoursRepository workingHoursRepository;

    private final WorkingHoursMapper workingHoursMapper;

    public WorkingHoursQueryService(WorkingHoursRepository workingHoursRepository, WorkingHoursMapper workingHoursMapper) {
        this.workingHoursRepository = workingHoursRepository;
        this.workingHoursMapper = workingHoursMapper;
    }

    /**
     * Return a {@link Page} of {@link WorkingHoursDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkingHoursDTO> findByCriteria(WorkingHoursCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkingHours> specification = createSpecification(criteria);
        return workingHoursRepository.findAll(specification, page).map(workingHoursMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkingHoursCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkingHours> specification = createSpecification(criteria);
        return workingHoursRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkingHoursCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkingHours> createSpecification(WorkingHoursCriteria criteria) {
        Specification<WorkingHours> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkingHours_.id));
            }
            if (criteria.getDayOfWeek() != null) {
                specification = specification.and(buildSpecification(criteria.getDayOfWeek(), WorkingHours_.dayOfWeek));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), WorkingHours_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), WorkingHours_.endTime));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmployeeId(), root -> root.join(WorkingHours_.employee, JoinType.LEFT).get(Employee_.id))
                );
            }
        }
        return specification;
    }
}
