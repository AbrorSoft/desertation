package org.abror.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServiceForCustomerCriteriaTest {

    @Test
    void newServiceForCustomerCriteriaHasAllFiltersNullTest() {
        var serviceForCustomerCriteria = new ServiceForCustomerCriteria();
        assertThat(serviceForCustomerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void serviceForCustomerCriteriaFluentMethodsCreatesFiltersTest() {
        var serviceForCustomerCriteria = new ServiceForCustomerCriteria();

        setAllFilters(serviceForCustomerCriteria);

        assertThat(serviceForCustomerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void serviceForCustomerCriteriaCopyCreatesNullFilterTest() {
        var serviceForCustomerCriteria = new ServiceForCustomerCriteria();
        var copy = serviceForCustomerCriteria.copy();

        assertThat(serviceForCustomerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(serviceForCustomerCriteria)
        );
    }

    @Test
    void serviceForCustomerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var serviceForCustomerCriteria = new ServiceForCustomerCriteria();
        setAllFilters(serviceForCustomerCriteria);

        var copy = serviceForCustomerCriteria.copy();

        assertThat(serviceForCustomerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(serviceForCustomerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var serviceForCustomerCriteria = new ServiceForCustomerCriteria();

        assertThat(serviceForCustomerCriteria).hasToString("ServiceForCustomerCriteria{}");
    }

    private static void setAllFilters(ServiceForCustomerCriteria serviceForCustomerCriteria) {
        serviceForCustomerCriteria.id();
        serviceForCustomerCriteria.name();
        serviceForCustomerCriteria.description();
        serviceForCustomerCriteria.duration();
        serviceForCustomerCriteria.serviceProviderId();
        serviceForCustomerCriteria.employeesId();
        serviceForCustomerCriteria.distinct();
    }

    private static Condition<ServiceForCustomerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDuration()) &&
                condition.apply(criteria.getServiceProviderId()) &&
                condition.apply(criteria.getEmployeesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServiceForCustomerCriteria> copyFiltersAre(
        ServiceForCustomerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDuration(), copy.getDuration()) &&
                condition.apply(criteria.getServiceProviderId(), copy.getServiceProviderId()) &&
                condition.apply(criteria.getEmployeesId(), copy.getEmployeesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
