package org.abror.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.abror.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceForCustomerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceForCustomerDTO.class);
        ServiceForCustomerDTO serviceForCustomerDTO1 = new ServiceForCustomerDTO();
        serviceForCustomerDTO1.setId(1L);
        ServiceForCustomerDTO serviceForCustomerDTO2 = new ServiceForCustomerDTO();
        assertThat(serviceForCustomerDTO1).isNotEqualTo(serviceForCustomerDTO2);
        serviceForCustomerDTO2.setId(serviceForCustomerDTO1.getId());
        assertThat(serviceForCustomerDTO1).isEqualTo(serviceForCustomerDTO2);
        serviceForCustomerDTO2.setId(2L);
        assertThat(serviceForCustomerDTO1).isNotEqualTo(serviceForCustomerDTO2);
        serviceForCustomerDTO1.setId(null);
        assertThat(serviceForCustomerDTO1).isNotEqualTo(serviceForCustomerDTO2);
    }
}
