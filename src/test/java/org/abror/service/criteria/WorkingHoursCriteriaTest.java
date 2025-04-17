package org.abror.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WorkingHoursCriteriaTest {

    @Test
    void newWorkingHoursCriteriaHasAllFiltersNullTest() {
        var workingHoursCriteria = new WorkingHoursCriteria();
        assertThat(workingHoursCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void workingHoursCriteriaFluentMethodsCreatesFiltersTest() {
        var workingHoursCriteria = new WorkingHoursCriteria();

        setAllFilters(workingHoursCriteria);

        assertThat(workingHoursCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void workingHoursCriteriaCopyCreatesNullFilterTest() {
        var workingHoursCriteria = new WorkingHoursCriteria();
        var copy = workingHoursCriteria.copy();

        assertThat(workingHoursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(workingHoursCriteria)
        );
    }

    @Test
    void workingHoursCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var workingHoursCriteria = new WorkingHoursCriteria();
        setAllFilters(workingHoursCriteria);

        var copy = workingHoursCriteria.copy();

        assertThat(workingHoursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(workingHoursCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var workingHoursCriteria = new WorkingHoursCriteria();

        assertThat(workingHoursCriteria).hasToString("WorkingHoursCriteria{}");
    }

    private static void setAllFilters(WorkingHoursCriteria workingHoursCriteria) {
        workingHoursCriteria.id();
        workingHoursCriteria.dayOfWeek();
        workingHoursCriteria.startTime();
        workingHoursCriteria.endTime();
        workingHoursCriteria.employeeId();
        workingHoursCriteria.distinct();
    }

    private static Condition<WorkingHoursCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDayOfWeek()) &&
                condition.apply(criteria.getStartTime()) &&
                condition.apply(criteria.getEndTime()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WorkingHoursCriteria> copyFiltersAre(
        WorkingHoursCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDayOfWeek(), copy.getDayOfWeek()) &&
                condition.apply(criteria.getStartTime(), copy.getStartTime()) &&
                condition.apply(criteria.getEndTime(), copy.getEndTime()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
