package org.abror.service;

import jakarta.persistence.criteria.JoinType;
import org.abror.domain.*; // for static metamodels
import org.abror.domain.ServiceForCustomer;
import org.abror.repository.ServiceForCustomerRepository;
import org.abror.service.criteria.ServiceForCustomerCriteria;
import org.abror.service.dto.ServiceForCustomerDTO;
import org.abror.service.mapper.ServiceForCustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServiceForCustomer} entities in the database.
 * The main input is a {@link ServiceForCustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServiceForCustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceForCustomerQueryService extends QueryService<ServiceForCustomer> {

    private final Logger log = LoggerFactory.getLogger(ServiceForCustomerQueryService.class);

    private final ServiceForCustomerRepository serviceForCustomerRepository;

    private final ServiceForCustomerMapper serviceForCustomerMapper;

    public ServiceForCustomerQueryService(
        ServiceForCustomerRepository serviceForCustomerRepository,
        ServiceForCustomerMapper serviceForCustomerMapper
    ) {
        this.serviceForCustomerRepository = serviceForCustomerRepository;
        this.serviceForCustomerMapper = serviceForCustomerMapper;
    }

    /**
     * Return a {@link Page} of {@link ServiceForCustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceForCustomerDTO> findByCriteria(ServiceForCustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceForCustomer> specification = createSpecification(criteria);
        return serviceForCustomerRepository
            .fetchBagRelationships(serviceForCustomerRepository.findAll(specification, page))
            .map(serviceForCustomerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceForCustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ServiceForCustomer> specification = createSpecification(criteria);
        return serviceForCustomerRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceForCustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceForCustomer> createSpecification(ServiceForCustomerCriteria criteria) {
        Specification<ServiceForCustomer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServiceForCustomer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ServiceForCustomer_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ServiceForCustomer_.description));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), ServiceForCustomer_.duration));
            }
            if (criteria.getServiceProviderId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getServiceProviderId(),
                        root -> root.join(ServiceForCustomer_.serviceProvider, JoinType.LEFT).get(ServiceProvider_.id)
                    )
                );
            }
            if (criteria.getEmployeesId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getEmployeesId(),
                        root -> root.join(ServiceForCustomer_.employees, JoinType.LEFT).get(Employee_.id)
                    )
                );
            }
        }
        return specification;
    }
}
