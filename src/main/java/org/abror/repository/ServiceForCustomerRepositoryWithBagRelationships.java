package org.abror.repository;

import java.util.List;
import java.util.Optional;
import org.abror.domain.ServiceForCustomer;
import org.springframework.data.domain.Page;

public interface ServiceForCustomerRepositoryWithBagRelationships {
    Optional<ServiceForCustomer> fetchBagRelationships(Optional<ServiceForCustomer> serviceForCustomer);

    List<ServiceForCustomer> fetchBagRelationships(List<ServiceForCustomer> serviceForCustomers);

    Page<ServiceForCustomer> fetchBagRelationships(Page<ServiceForCustomer> serviceForCustomers);
}
