package org.abror.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.abror.domain.ServiceForCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ServiceForCustomerRepositoryWithBagRelationshipsImpl implements ServiceForCustomerRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SERVICEFORCUSTOMERS_PARAMETER = "serviceForCustomers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ServiceForCustomer> fetchBagRelationships(Optional<ServiceForCustomer> serviceForCustomer) {
        return serviceForCustomer.map(this::fetchEmployees);
    }

    @Override
    public Page<ServiceForCustomer> fetchBagRelationships(Page<ServiceForCustomer> serviceForCustomers) {
        return new PageImpl<>(
            fetchBagRelationships(serviceForCustomers.getContent()),
            serviceForCustomers.getPageable(),
            serviceForCustomers.getTotalElements()
        );
    }

    @Override
    public List<ServiceForCustomer> fetchBagRelationships(List<ServiceForCustomer> serviceForCustomers) {
        return Optional.of(serviceForCustomers).map(this::fetchEmployees).orElse(Collections.emptyList());
    }

    ServiceForCustomer fetchEmployees(ServiceForCustomer result) {
        return entityManager
            .createQuery(
                "select serviceForCustomer from ServiceForCustomer serviceForCustomer left join fetch serviceForCustomer.employees where serviceForCustomer.id = :id",
                ServiceForCustomer.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ServiceForCustomer> fetchEmployees(List<ServiceForCustomer> serviceForCustomers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, serviceForCustomers.size()).forEach(index -> order.put(serviceForCustomers.get(index).getId(), index));
        List<ServiceForCustomer> result = entityManager
            .createQuery(
                "select serviceForCustomer from ServiceForCustomer serviceForCustomer left join fetch serviceForCustomer.employees where serviceForCustomer in :serviceForCustomers",
                ServiceForCustomer.class
            )
            .setParameter(SERVICEFORCUSTOMERS_PARAMETER, serviceForCustomers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
