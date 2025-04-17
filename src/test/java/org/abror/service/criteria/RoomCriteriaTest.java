package org.abror.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RoomCriteriaTest {

    @Test
    void newRoomCriteriaHasAllFiltersNullTest() {
        var roomCriteria = new RoomCriteria();
        assertThat(roomCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void roomCriteriaFluentMethodsCreatesFiltersTest() {
        var roomCriteria = new RoomCriteria();

        setAllFilters(roomCriteria);

        assertThat(roomCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void roomCriteriaCopyCreatesNullFilterTest() {
        var roomCriteria = new RoomCriteria();
        var copy = roomCriteria.copy();

        assertThat(roomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(roomCriteria)
        );
    }

    @Test
    void roomCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var roomCriteria = new RoomCriteria();
        setAllFilters(roomCriteria);

        var copy = roomCriteria.copy();

        assertThat(roomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(roomCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var roomCriteria = new RoomCriteria();

        assertThat(roomCriteria).hasToString("RoomCriteria{}");
    }

    private static void setAllFilters(RoomCriteria roomCriteria) {
        roomCriteria.id();
        roomCriteria.roomNumber();
        roomCriteria.description();
        roomCriteria.serviceProviderId();
        roomCriteria.distinct();
    }

    private static Condition<RoomCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRoomNumber()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getServiceProviderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RoomCriteria> copyFiltersAre(RoomCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRoomNumber(), copy.getRoomNumber()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getServiceProviderId(), copy.getServiceProviderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
