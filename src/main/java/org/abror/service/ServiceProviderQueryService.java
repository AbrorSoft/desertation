package org.abror.service;

import org.abror.domain.*; // for static metamodels
import org.abror.domain.ServiceProvider;
import org.abror.repository.ServiceProviderRepository;
import org.abror.service.criteria.ServiceProviderCriteria;
import org.abror.service.dto.ServiceProviderDTO;
import org.abror.service.mapper.ServiceProviderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServiceProvider} entities in the database.
 * The main input is a {@link ServiceProviderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServiceProviderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceProviderQueryService extends QueryService<ServiceProvider> {

    private final Logger log = LoggerFactory.getLogger(ServiceProviderQueryService.class);

    private final ServiceProviderRepository serviceProviderRepository;

    private final ServiceProviderMapper serviceProviderMapper;

    public ServiceProviderQueryService(ServiceProviderRepository serviceProviderRepository, ServiceProviderMapper serviceProviderMapper) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceProviderMapper = serviceProviderMapper;
    }

    /**
     * Return a {@link Page} of {@link ServiceProviderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceProviderDTO> findByCriteria(ServiceProviderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceProvider> specification = createSpecification(criteria);
        return serviceProviderRepository.findAll(specification, page).map(serviceProviderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceProviderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ServiceProvider> specification = createSpecification(criteria);
        return serviceProviderRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceProviderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceProvider> createSpecification(ServiceProviderCriteria criteria) {
        Specification<ServiceProvider> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServiceProvider_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ServiceProvider_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ServiceProvider_.type));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), ServiceProvider_.address));
            }
            if (criteria.getContactInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactInfo(), ServiceProvider_.contactInfo));
            }
        }
        return specification;
    }
}
