package org.abror.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServiceProviderCriteriaTest {

    @Test
    void newServiceProviderCriteriaHasAllFiltersNullTest() {
        var serviceProviderCriteria = new ServiceProviderCriteria();
        assertThat(serviceProviderCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void serviceProviderCriteriaFluentMethodsCreatesFiltersTest() {
        var serviceProviderCriteria = new ServiceProviderCriteria();

        setAllFilters(serviceProviderCriteria);

        assertThat(serviceProviderCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void serviceProviderCriteriaCopyCreatesNullFilterTest() {
        var serviceProviderCriteria = new ServiceProviderCriteria();
        var copy = serviceProviderCriteria.copy();

        assertThat(serviceProviderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(serviceProviderCriteria)
        );
    }

    @Test
    void serviceProviderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var serviceProviderCriteria = new ServiceProviderCriteria();
        setAllFilters(serviceProviderCriteria);

        var copy = serviceProviderCriteria.copy();

        assertThat(serviceProviderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(serviceProviderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var serviceProviderCriteria = new ServiceProviderCriteria();

        assertThat(serviceProviderCriteria).hasToString("ServiceProviderCriteria{}");
    }

    private static void setAllFilters(ServiceProviderCriteria serviceProviderCriteria) {
        serviceProviderCriteria.id();
        serviceProviderCriteria.name();
        serviceProviderCriteria.type();
        serviceProviderCriteria.address();
        serviceProviderCriteria.contactInfo();
        serviceProviderCriteria.distinct();
    }

    private static Condition<ServiceProviderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getContactInfo()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServiceProviderCriteria> copyFiltersAre(
        ServiceProviderCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getContactInfo(), copy.getContactInfo()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
