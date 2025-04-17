package org.abror.web.rest;

import static org.abror.domain.ServiceForCustomerAsserts.*;
import static org.abror.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.abror.IntegrationTest;
import org.abror.domain.Employee;
import org.abror.domain.ServiceForCustomer;
import org.abror.domain.ServiceProvider;
import org.abror.repository.ServiceForCustomerRepository;
import org.abror.service.ServiceForCustomerService;
import org.abror.service.dto.ServiceForCustomerDTO;
import org.abror.service.mapper.ServiceForCustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceForCustomerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceForCustomerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;
    private static final Integer SMALLER_DURATION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/service-for-customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceForCustomerRepository serviceForCustomerRepository;

    @Mock
    private ServiceForCustomerRepository serviceForCustomerRepositoryMock;

    @Autowired
    private ServiceForCustomerMapper serviceForCustomerMapper;

    @Mock
    private ServiceForCustomerService serviceForCustomerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceForCustomerMockMvc;

    private ServiceForCustomer serviceForCustomer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceForCustomer createEntity(EntityManager em) {
        ServiceForCustomer serviceForCustomer = new ServiceForCustomer()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .duration(DEFAULT_DURATION);
        return serviceForCustomer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceForCustomer createUpdatedEntity(EntityManager em) {
        ServiceForCustomer serviceForCustomer = new ServiceForCustomer()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .duration(UPDATED_DURATION);
        return serviceForCustomer;
    }

    @BeforeEach
    public void initTest() {
        serviceForCustomer = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceForCustomer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceForCustomer
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);
        var returnedServiceForCustomerDTO = om.readValue(
            restServiceForCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceForCustomerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceForCustomerDTO.class
        );

        // Validate the ServiceForCustomer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceForCustomer = serviceForCustomerMapper.toEntity(returnedServiceForCustomerDTO);
        assertServiceForCustomerUpdatableFieldsEquals(
            returnedServiceForCustomer,
            getPersistedServiceForCustomer(returnedServiceForCustomer)
        );
    }

    @Test
    @Transactional
    void createServiceForCustomerWithExistingId() throws Exception {
        // Create the ServiceForCustomer with an existing ID
        serviceForCustomer.setId(1L);
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceForCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceForCustomerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceForCustomer.setName(null);

        // Create the ServiceForCustomer, which fails.
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        restServiceForCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceForCustomerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceForCustomer.setDuration(null);

        // Create the ServiceForCustomer, which fails.
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        restServiceForCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceForCustomerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceForCustomers() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList
        restServiceForCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceForCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceForCustomersWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceForCustomerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceForCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceForCustomerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceForCustomersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceForCustomerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceForCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(serviceForCustomerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServiceForCustomer() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get the serviceForCustomer
        restServiceForCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceForCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceForCustomer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    void getServiceForCustomersByIdFiltering() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        Long id = serviceForCustomer.getId();

        defaultServiceForCustomerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServiceForCustomerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServiceForCustomerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where name equals to
        defaultServiceForCustomerFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where name in
        defaultServiceForCustomerFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where name is not null
        defaultServiceForCustomerFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByNameContainsSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where name contains
        defaultServiceForCustomerFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where name does not contain
        defaultServiceForCustomerFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where description equals to
        defaultServiceForCustomerFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where description in
        defaultServiceForCustomerFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where description is not null
        defaultServiceForCustomerFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where description contains
        defaultServiceForCustomerFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where description does not contain
        defaultServiceForCustomerFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where duration equals to
        defaultServiceForCustomerFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where duration in
        defaultServiceForCustomerFiltering("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION, "duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where duration is not null
        defaultServiceForCustomerFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where duration is greater than or equal to
        defaultServiceForCustomerFiltering(
            "duration.greaterThanOrEqual=" + DEFAULT_DURATION,
            "duration.greaterThanOrEqual=" + UPDATED_DURATION
        );
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where duration is less than or equal to
        defaultServiceForCustomerFiltering("duration.lessThanOrEqual=" + DEFAULT_DURATION, "duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where duration is less than
        defaultServiceForCustomerFiltering("duration.lessThan=" + UPDATED_DURATION, "duration.lessThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        // Get all the serviceForCustomerList where duration is greater than
        defaultServiceForCustomerFiltering("duration.greaterThan=" + SMALLER_DURATION, "duration.greaterThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByServiceProviderIsEqualToSomething() throws Exception {
        ServiceProvider serviceProvider;
        if (TestUtil.findAll(em, ServiceProvider.class).isEmpty()) {
            serviceForCustomerRepository.saveAndFlush(serviceForCustomer);
            serviceProvider = ServiceProviderResourceIT.createEntity(em);
        } else {
            serviceProvider = TestUtil.findAll(em, ServiceProvider.class).get(0);
        }
        em.persist(serviceProvider);
        em.flush();
        serviceForCustomer.setServiceProvider(serviceProvider);
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);
        Long serviceProviderId = serviceProvider.getId();
        // Get all the serviceForCustomerList where serviceProvider equals to serviceProviderId
        defaultServiceForCustomerShouldBeFound("serviceProviderId.equals=" + serviceProviderId);

        // Get all the serviceForCustomerList where serviceProvider equals to (serviceProviderId + 1)
        defaultServiceForCustomerShouldNotBeFound("serviceProviderId.equals=" + (serviceProviderId + 1));
    }

    @Test
    @Transactional
    void getAllServiceForCustomersByEmployeesIsEqualToSomething() throws Exception {
        Employee employees;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            serviceForCustomerRepository.saveAndFlush(serviceForCustomer);
            employees = EmployeeResourceIT.createEntity(em);
        } else {
            employees = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employees);
        em.flush();
        serviceForCustomer.addEmployees(employees);
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);
        Long employeesId = employees.getId();
        // Get all the serviceForCustomerList where employees equals to employeesId
        defaultServiceForCustomerShouldBeFound("employeesId.equals=" + employeesId);

        // Get all the serviceForCustomerList where employees equals to (employeesId + 1)
        defaultServiceForCustomerShouldNotBeFound("employeesId.equals=" + (employeesId + 1));
    }

    private void defaultServiceForCustomerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServiceForCustomerShouldBeFound(shouldBeFound);
        defaultServiceForCustomerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceForCustomerShouldBeFound(String filter) throws Exception {
        restServiceForCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceForCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));

        // Check, that the count call also returns 1
        restServiceForCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceForCustomerShouldNotBeFound(String filter) throws Exception {
        restServiceForCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceForCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceForCustomer() throws Exception {
        // Get the serviceForCustomer
        restServiceForCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceForCustomer() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceForCustomer
        ServiceForCustomer updatedServiceForCustomer = serviceForCustomerRepository.findById(serviceForCustomer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceForCustomer are not directly saved in db
        em.detach(updatedServiceForCustomer);
        updatedServiceForCustomer.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).duration(UPDATED_DURATION);
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(updatedServiceForCustomer);

        restServiceForCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceForCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceForCustomerDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceForCustomerToMatchAllProperties(updatedServiceForCustomer);
    }

    @Test
    @Transactional
    void putNonExistingServiceForCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceForCustomer.setId(longCount.incrementAndGet());

        // Create the ServiceForCustomer
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceForCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceForCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceForCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceForCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceForCustomer.setId(longCount.incrementAndGet());

        // Create the ServiceForCustomer
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceForCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceForCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceForCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceForCustomer.setId(longCount.incrementAndGet());

        // Create the ServiceForCustomer
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceForCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceForCustomerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceForCustomerWithPatch() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceForCustomer using partial update
        ServiceForCustomer partialUpdatedServiceForCustomer = new ServiceForCustomer();
        partialUpdatedServiceForCustomer.setId(serviceForCustomer.getId());

        partialUpdatedServiceForCustomer.description(UPDATED_DESCRIPTION);

        restServiceForCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceForCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceForCustomer))
            )
            .andExpect(status().isOk());

        // Validate the ServiceForCustomer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceForCustomerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceForCustomer, serviceForCustomer),
            getPersistedServiceForCustomer(serviceForCustomer)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceForCustomerWithPatch() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceForCustomer using partial update
        ServiceForCustomer partialUpdatedServiceForCustomer = new ServiceForCustomer();
        partialUpdatedServiceForCustomer.setId(serviceForCustomer.getId());

        partialUpdatedServiceForCustomer.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).duration(UPDATED_DURATION);

        restServiceForCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceForCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceForCustomer))
            )
            .andExpect(status().isOk());

        // Validate the ServiceForCustomer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceForCustomerUpdatableFieldsEquals(
            partialUpdatedServiceForCustomer,
            getPersistedServiceForCustomer(partialUpdatedServiceForCustomer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceForCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceForCustomer.setId(longCount.incrementAndGet());

        // Create the ServiceForCustomer
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceForCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceForCustomerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceForCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceForCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceForCustomer.setId(longCount.incrementAndGet());

        // Create the ServiceForCustomer
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceForCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceForCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceForCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceForCustomer.setId(longCount.incrementAndGet());

        // Create the ServiceForCustomer
        ServiceForCustomerDTO serviceForCustomerDTO = serviceForCustomerMapper.toDto(serviceForCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceForCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceForCustomerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceForCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceForCustomer() throws Exception {
        // Initialize the database
        serviceForCustomerRepository.saveAndFlush(serviceForCustomer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceForCustomer
        restServiceForCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceForCustomer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceForCustomerRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ServiceForCustomer getPersistedServiceForCustomer(ServiceForCustomer serviceForCustomer) {
        return serviceForCustomerRepository.findById(serviceForCustomer.getId()).orElseThrow();
    }

    protected void assertPersistedServiceForCustomerToMatchAllProperties(ServiceForCustomer expectedServiceForCustomer) {
        assertServiceForCustomerAllPropertiesEquals(expectedServiceForCustomer, getPersistedServiceForCustomer(expectedServiceForCustomer));
    }

    protected void assertPersistedServiceForCustomerToMatchUpdatableProperties(ServiceForCustomer expectedServiceForCustomer) {
        assertServiceForCustomerAllUpdatablePropertiesEquals(
            expectedServiceForCustomer,
            getPersistedServiceForCustomer(expectedServiceForCustomer)
        );
    }
}
