package org.abror.service;

import jakarta.persistence.criteria.JoinType;
import java.util.Objects;
import java.util.Optional;
import org.abror.domain.*;
import org.abror.repository.AppointmentRepository;
import org.abror.repository.UserRepository;
import org.abror.security.SecurityUtils;
import org.abror.service.criteria.AppointmentCriteria;
import org.abror.service.dto.AppointmentDTO;
import org.abror.service.mapper.AppointmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Appointment} entities in the database.
 * The main input is a {@link AppointmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AppointmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppointmentQueryService extends QueryService<Appointment> {

    private final Logger log = LoggerFactory.getLogger(AppointmentQueryService.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    private final UserRepository userRepository;

    public AppointmentQueryService(
        AppointmentRepository appointmentRepository,
        AppointmentMapper appointmentMapper,
        UserRepository userRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.userRepository = userRepository;
    }

    /**
     * Return a {@link Page} of {@link AppointmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findByCriteria(AppointmentCriteria criteria, Pageable page, Boolean getIsNullApps) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Appointment> specification = createSpecificationByRole(criteria, getIsNullApps);
        return appointmentRepository.findAll(specification, page).map(appointmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppointmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Appointment> specification = createSpecification(criteria);
        return appointmentRepository.count(specification);
    }

    protected Specification<Appointment> createSpecificationByRole(AppointmentCriteria criteria, Boolean getIsNullApps) {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow();

        Optional<User> userOptional = userRepository.findOneWithAuthoritiesByLogin(username);
        if (userOptional.isPresent()) {
            User user = userOptional.orElseThrow();

            boolean roleAdmin = user.getAuthorities().stream().anyMatch(authority -> Objects.equals(authority.getName(), "ROLE_ADMIN"));

            if (!roleAdmin) {
                Specification<Appointment> specification = createSpecification(criteria);
                if (getIsNullApps) {
                    return specification.and(createdBySpecificationIsNullApps());
                }
                return specification.and(createdBySpecification(user.getId()));
            }
        }

        return createSpecification(criteria);
    }

    protected Specification<Appointment> createdBySpecificationIsNullApps() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(Appointment_.user));
    }

    protected Specification<Appointment> createdBySpecification(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(Appointment_.user, JoinType.LEFT).get(User_.id), userId);
    }

    /**
     * Function to convert {@link AppointmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Appointment> createSpecification(AppointmentCriteria criteria) {
        Specification<Appointment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Appointment_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Appointment_.date));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Appointment_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Appointment_.endTime));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Appointment_.status));
            }
            if (criteria.getServiceProviderId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getServiceProviderId(),
                        root -> root.join(Appointment_.serviceProvider, JoinType.LEFT).get(ServiceProvider_.id)
                    )
                );
            }
            if (criteria.getServiceProviderType() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getServiceProviderType(),
                        root -> root.join(Appointment_.serviceProvider, JoinType.LEFT).get(ServiceProvider_.type)
                    )
                );
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmployeeId(), root -> root.join(Appointment_.employee, JoinType.LEFT).get(Employee_.id))
                );
            }
            if (criteria.getServiceForCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getServiceForCustomerId(),
                        root -> root.join(Appointment_.serviceForCustomer, JoinType.LEFT).get(ServiceForCustomer_.id)
                    )
                );
            }
            if (criteria.getRoomId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoomId(), root -> root.join(Appointment_.room, JoinType.LEFT).get(Room_.id))
                );
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(Appointment_.user, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
