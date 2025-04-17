package org.abror.repository;

import java.util.List;
import java.util.Optional;
import org.abror.domain.ServiceForCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceForCustomer entity.
 *
 * When extending this class, extend ServiceForCustomerRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ServiceForCustomerRepository
    extends
        ServiceForCustomerRepositoryWithBagRelationships,
        JpaRepository<ServiceForCustomer, Long>,
        JpaSpecificationExecutor<ServiceForCustomer> {
    default Optional<ServiceForCustomer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<ServiceForCustomer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<ServiceForCustomer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
