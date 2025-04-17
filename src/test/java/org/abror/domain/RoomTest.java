package org.abror.domain;

import static org.abror.domain.RoomTestSamples.*;
import static org.abror.domain.ServiceProviderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.abror.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Room.class);
        Room room1 = getRoomSample1();
        Room room2 = new Room();
        assertThat(room1).isNotEqualTo(room2);

        room2.setId(room1.getId());
        assertThat(room1).isEqualTo(room2);

        room2 = getRoomSample2();
        assertThat(room1).isNotEqualTo(room2);
    }

    @Test
    void serviceProviderTest() throws Exception {
        Room room = getRoomRandomSampleGenerator();
        ServiceProvider serviceProviderBack = getServiceProviderRandomSampleGenerator();

        room.setServiceProvider(serviceProviderBack);
        assertThat(room.getServiceProvider()).isEqualTo(serviceProviderBack);

        room.serviceProvider(null);
        assertThat(room.getServiceProvider()).isNull();
    }
}
