package org.abror.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomAllPropertiesEquals(Room expected, Room actual) {
        assertRoomAutoGeneratedPropertiesEquals(expected, actual);
        assertRoomAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomAllUpdatablePropertiesEquals(Room expected, Room actual) {
        assertRoomUpdatableFieldsEquals(expected, actual);
        assertRoomUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomAutoGeneratedPropertiesEquals(Room expected, Room actual) {
        assertThat(expected)
            .as("Verify Room auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomUpdatableFieldsEquals(Room expected, Room actual) {
        assertThat(expected)
            .as("Verify Room relevant properties")
            .satisfies(e -> assertThat(e.getRoomNumber()).as("check roomNumber").isEqualTo(actual.getRoomNumber()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoomUpdatableRelationshipsEquals(Room expected, Room actual) {
        assertThat(expected)
            .as("Verify Room relationships")
            .satisfies(e -> assertThat(e.getServiceProvider()).as("check serviceProvider").isEqualTo(actual.getServiceProvider()));
    }
}
