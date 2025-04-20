package org.abror.web.rest;

import static org.abror.domain.ServiceProviderAsserts.*;
import static org.abror.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.abror.IntegrationTest;
import org.abror.domain.ServiceProvider;
import org.abror.domain.enumeration.ServiceProviderType;
import org.abror.repository.ServiceProviderRepository;
import org.abror.service.dto.ServiceProviderDTO;
import org.abror.service.mapper.ServiceProviderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceProviderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ServiceProviderType DEFAULT_TYPE = ServiceProviderType.BARBERSHOP;
    private static final ServiceProviderType UPDATED_TYPE = ServiceProviderType.SALON;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_INFO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ServiceProviderMapper serviceProviderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceProviderMockMvc;

    private ServiceProvider serviceProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceProvider createEntity(EntityManager em) {
        ServiceProvider serviceProvider = new ServiceProvider()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .address(DEFAULT_ADDRESS)
            .contactInfo(DEFAULT_CONTACT_INFO);
        return serviceProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceProvider createUpdatedEntity(EntityManager em) {
        ServiceProvider serviceProvider = new ServiceProvider()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .address(UPDATED_ADDRESS)
            .contactInfo(UPDATED_CONTACT_INFO);
        return serviceProvider;
    }

    @BeforeEach
    public void initTest() {
        serviceProvider = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceProvider() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);
        var returnedServiceProviderDTO = om.readValue(
            restServiceProviderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceProviderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceProviderDTO.class
        );

        // Validate the ServiceProvider in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceProvider = serviceProviderMapper.toEntity(returnedServiceProviderDTO);
        assertServiceProviderUpdatableFieldsEquals(returnedServiceProvider, getPersistedServiceProvider(returnedServiceProvider));
    }

    @Test
    @Transactional
    void createServiceProviderWithExistingId() throws Exception {
        // Create the ServiceProvider with an existing ID
        serviceProvider.setId(1L);
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceProviderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceProvider.setName(null);

        // Create the ServiceProvider, which fails.
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        restServiceProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceProviderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceProvider.setType(null);

        // Create the ServiceProvider, which fails.
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        restServiceProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceProviderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceProviders() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactInfo").value(hasItem(DEFAULT_CONTACT_INFO)));
    }

    @Test
    @Transactional
    void getServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get the serviceProvider
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceProvider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.contactInfo").value(DEFAULT_CONTACT_INFO));
    }

    @Test
    @Transactional
    void getServiceProvidersByIdFiltering() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        Long id = serviceProvider.getId();

        defaultServiceProviderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServiceProviderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServiceProviderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where name equals to
        defaultServiceProviderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where name in
        defaultServiceProviderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where name is not null
        defaultServiceProviderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceProvidersByNameContainsSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where name contains
        defaultServiceProviderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where name does not contain
        defaultServiceProviderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where type equals to
        defaultServiceProviderFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where type in
        defaultServiceProviderFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where type is not null
        defaultServiceProviderFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the roomPricingList where price equals to
        defaultServiceProviderFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the roomPricingList where price in
        defaultServiceProviderFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the roomPricingList where amount is not null
        defaultServiceProviderFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the roomPricingList where amount is greater than or equal to
        defaultServiceProviderFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the roomPricingList where amount is less than or equal to
        defaultServiceProviderFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the roomPricingList where amount is less than
        defaultServiceProviderFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the roomPricingList where amount is greater than
        defaultServiceProviderFiltering("amount.greaterThan=" + SMALLER_PRICE, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where address equals to
        defaultServiceProviderFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where address in
        defaultServiceProviderFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where address is not null
        defaultServiceProviderFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAddressContainsSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where address contains
        defaultServiceProviderFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where address does not contain
        defaultServiceProviderFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByContactInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where contactInfo equals to
        defaultServiceProviderFiltering("contactInfo.equals=" + DEFAULT_CONTACT_INFO, "contactInfo.equals=" + UPDATED_CONTACT_INFO);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByContactInfoIsInShouldWork() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where contactInfo in
        defaultServiceProviderFiltering(
            "contactInfo.in=" + DEFAULT_CONTACT_INFO + "," + UPDATED_CONTACT_INFO,
            "contactInfo.in=" + UPDATED_CONTACT_INFO
        );
    }

    @Test
    @Transactional
    void getAllServiceProvidersByContactInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where contactInfo is not null
        defaultServiceProviderFiltering("contactInfo.specified=true", "contactInfo.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceProvidersByContactInfoContainsSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where contactInfo contains
        defaultServiceProviderFiltering("contactInfo.contains=" + DEFAULT_CONTACT_INFO, "contactInfo.contains=" + UPDATED_CONTACT_INFO);
    }

    @Test
    @Transactional
    void getAllServiceProvidersByContactInfoNotContainsSomething() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList where contactInfo does not contain
        defaultServiceProviderFiltering(
            "contactInfo.doesNotContain=" + UPDATED_CONTACT_INFO,
            "contactInfo.doesNotContain=" + DEFAULT_CONTACT_INFO
        );
    }

    private void defaultServiceProviderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServiceProviderShouldBeFound(shouldBeFound);
        defaultServiceProviderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceProviderShouldBeFound(String filter) throws Exception {
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactInfo").value(hasItem(DEFAULT_CONTACT_INFO)));

        // Check, that the count call also returns 1
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceProviderShouldNotBeFound(String filter) throws Exception {
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceProvider() throws Exception {
        // Get the serviceProvider
        restServiceProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceProvider
        ServiceProvider updatedServiceProvider = serviceProviderRepository.findById(serviceProvider.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceProvider are not directly saved in db
        em.detach(updatedServiceProvider);
        updatedServiceProvider.name(UPDATED_NAME).type(UPDATED_TYPE).address(UPDATED_ADDRESS).contactInfo(UPDATED_CONTACT_INFO);
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(updatedServiceProvider);

        restServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceProviderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceProviderToMatchAllProperties(updatedServiceProvider);
    }

    @Test
    @Transactional
    void putNonExistingServiceProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceProvider.setId(longCount.incrementAndGet());

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceProvider.setId(longCount.incrementAndGet());

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceProvider.setId(longCount.incrementAndGet());

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceProviderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceProviderWithPatch() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceProvider using partial update
        ServiceProvider partialUpdatedServiceProvider = new ServiceProvider();
        partialUpdatedServiceProvider.setId(serviceProvider.getId());

        partialUpdatedServiceProvider.type(UPDATED_TYPE).address(UPDATED_ADDRESS);

        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the ServiceProvider in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceProviderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceProvider, serviceProvider),
            getPersistedServiceProvider(serviceProvider)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceProviderWithPatch() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceProvider using partial update
        ServiceProvider partialUpdatedServiceProvider = new ServiceProvider();
        partialUpdatedServiceProvider.setId(serviceProvider.getId());

        partialUpdatedServiceProvider.name(UPDATED_NAME).type(UPDATED_TYPE).address(UPDATED_ADDRESS).contactInfo(UPDATED_CONTACT_INFO);

        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the ServiceProvider in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceProviderUpdatableFieldsEquals(
            partialUpdatedServiceProvider,
            getPersistedServiceProvider(partialUpdatedServiceProvider)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceProvider.setId(longCount.incrementAndGet());

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceProviderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceProvider.setId(longCount.incrementAndGet());

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceProvider.setId(longCount.incrementAndGet());

        // Create the ServiceProvider
        ServiceProviderDTO serviceProviderDTO = serviceProviderMapper.toDto(serviceProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceProviderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceProvider
        restServiceProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceProviderRepository.count();
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

    protected ServiceProvider getPersistedServiceProvider(ServiceProvider serviceProvider) {
        return serviceProviderRepository.findById(serviceProvider.getId()).orElseThrow();
    }

    protected void assertPersistedServiceProviderToMatchAllProperties(ServiceProvider expectedServiceProvider) {
        assertServiceProviderAllPropertiesEquals(expectedServiceProvider, getPersistedServiceProvider(expectedServiceProvider));
    }

    protected void assertPersistedServiceProviderToMatchUpdatableProperties(ServiceProvider expectedServiceProvider) {
        assertServiceProviderAllUpdatablePropertiesEquals(expectedServiceProvider, getPersistedServiceProvider(expectedServiceProvider));
    }
}
