package org.abror.web.rest;

import static org.abror.domain.AppointmentAsserts.*;
import static org.abror.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.abror.IntegrationTest;
import org.abror.domain.Appointment;
import org.abror.domain.Employee;
import org.abror.domain.Room;
import org.abror.domain.ServiceForCustomer;
import org.abror.domain.ServiceProvider;
import org.abror.domain.enumeration.AppointmentStatus;
import org.abror.repository.AppointmentRepository;
import org.abror.service.dto.AppointmentDTO;
import org.abror.service.mapper.AppointmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppointmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AppointmentStatus DEFAULT_STATUS = AppointmentStatus.BOOKED;
    private static final AppointmentStatus UPDATED_STATUS = AppointmentStatus.COMPLETED;

    private static final String ENTITY_API_URL = "/api/appointments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .date(DEFAULT_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .status(DEFAULT_STATUS);
        return appointment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createUpdatedEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS);
        return appointment;
    }

    @BeforeEach
    public void initTest() {
        appointment = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        var returnedAppointmentDTO = om.readValue(
            restAppointmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appointmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppointmentDTO.class
        );

        // Validate the Appointment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppointment = appointmentMapper.toEntity(returnedAppointmentDTO);
        assertAppointmentUpdatableFieldsEquals(returnedAppointment, getPersistedAppointment(returnedAppointment));
    }

    @Test
    @Transactional
    void createAppointmentWithExistingId() throws Exception {
        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appointment.setDate(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appointment.setStartTime(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appointment.setEndTime(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appointment.setStatus(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL_ID, appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getAppointmentsByIdFiltering() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        Long id = appointment.getId();

        defaultAppointmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppointmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppointmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppointmentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where date equals to
        defaultAppointmentFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where date in
        defaultAppointmentFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where date is not null
        defaultAppointmentFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointmentsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where date is greater than or equal to
        defaultAppointmentFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where date is less than or equal to
        defaultAppointmentFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where date is less than
        defaultAppointmentFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where date is greater than
        defaultAppointmentFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where startTime equals to
        defaultAppointmentFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAppointmentsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where startTime in
        defaultAppointmentFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAppointmentsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where startTime is not null
        defaultAppointmentFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointmentsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where endTime equals to
        defaultAppointmentFiltering("endTime.equals=" + DEFAULT_END_TIME, "endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAppointmentsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where endTime in
        defaultAppointmentFiltering("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME, "endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAppointmentsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where endTime is not null
        defaultAppointmentFiltering("endTime.specified=true", "endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointmentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where status equals to
        defaultAppointmentFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAppointmentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where status in
        defaultAppointmentFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAppointmentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where status is not null
        defaultAppointmentFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointmentsByServiceProviderIsEqualToSomething() throws Exception {
        ServiceProvider serviceProvider;
        if (TestUtil.findAll(em, ServiceProvider.class).isEmpty()) {
            appointmentRepository.saveAndFlush(appointment);
            serviceProvider = ServiceProviderResourceIT.createEntity(em);
        } else {
            serviceProvider = TestUtil.findAll(em, ServiceProvider.class).get(0);
        }
        em.persist(serviceProvider);
        em.flush();
        appointment.setServiceProvider(serviceProvider);
        appointmentRepository.saveAndFlush(appointment);
        Long serviceProviderId = serviceProvider.getId();
        // Get all the appointmentList where serviceProvider equals to serviceProviderId
        defaultAppointmentShouldBeFound("serviceProviderId.equals=" + serviceProviderId);

        // Get all the appointmentList where serviceProvider equals to (serviceProviderId + 1)
        defaultAppointmentShouldNotBeFound("serviceProviderId.equals=" + (serviceProviderId + 1));
    }

    @Test
    @Transactional
    void getAllAppointmentsByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            appointmentRepository.saveAndFlush(appointment);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        appointment.setEmployee(employee);
        appointmentRepository.saveAndFlush(appointment);
        Long employeeId = employee.getId();
        // Get all the appointmentList where employee equals to employeeId
        defaultAppointmentShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the appointmentList where employee equals to (employeeId + 1)
        defaultAppointmentShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllAppointmentsByServiceForCustomerIsEqualToSomething() throws Exception {
        ServiceForCustomer serviceForCustomer;
        if (TestUtil.findAll(em, ServiceForCustomer.class).isEmpty()) {
            appointmentRepository.saveAndFlush(appointment);
            serviceForCustomer = ServiceForCustomerResourceIT.createEntity(em);
        } else {
            serviceForCustomer = TestUtil.findAll(em, ServiceForCustomer.class).get(0);
        }
        em.persist(serviceForCustomer);
        em.flush();
        appointment.setServiceForCustomer(serviceForCustomer);
        appointmentRepository.saveAndFlush(appointment);
        Long serviceForCustomerId = serviceForCustomer.getId();
        // Get all the appointmentList where serviceForCustomer equals to serviceForCustomerId
        defaultAppointmentShouldBeFound("serviceForCustomerId.equals=" + serviceForCustomerId);

        // Get all the appointmentList where serviceForCustomer equals to (serviceForCustomerId + 1)
        defaultAppointmentShouldNotBeFound("serviceForCustomerId.equals=" + (serviceForCustomerId + 1));
    }

    @Test
    @Transactional
    void getAllAppointmentsByRoomIsEqualToSomething() throws Exception {
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            appointmentRepository.saveAndFlush(appointment);
            room = RoomResourceIT.createEntity(em);
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        em.persist(room);
        em.flush();
        appointment.setRoom(room);
        appointmentRepository.saveAndFlush(appointment);
        Long roomId = room.getId();
        // Get all the appointmentList where room equals to roomId
        defaultAppointmentShouldBeFound("roomId.equals=" + roomId);

        // Get all the appointmentList where room equals to (roomId + 1)
        defaultAppointmentShouldNotBeFound("roomId.equals=" + (roomId + 1));
    }

    private void defaultAppointmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppointmentShouldBeFound(shouldBeFound);
        defaultAppointmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppointmentShouldBeFound(String filter) throws Exception {
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppointmentShouldNotBeFound(String filter) throws Exception {
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findById(appointment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppointment are not directly saved in db
        em.detach(updatedAppointment);
        updatedAppointment.date(UPDATED_DATE).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).status(UPDATED_STATUS);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appointmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppointmentToMatchAllProperties(updatedAppointment);
    }

    @Test
    @Transactional
    void putNonExistingAppointment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appointment.setId(longCount.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appointment.setId(longCount.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appointment.setId(longCount.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appointmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppointmentWithPatch() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appointment using partial update
        Appointment partialUpdatedAppointment = new Appointment();
        partialUpdatedAppointment.setId(appointment.getId());

        partialUpdatedAppointment.date(UPDATED_DATE);

        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppointment))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppointmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppointment, appointment),
            getPersistedAppointment(appointment)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppointmentWithPatch() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appointment using partial update
        Appointment partialUpdatedAppointment = new Appointment();
        partialUpdatedAppointment.setId(appointment.getId());

        partialUpdatedAppointment.date(UPDATED_DATE).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).status(UPDATED_STATUS);

        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppointment))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppointmentUpdatableFieldsEquals(partialUpdatedAppointment, getPersistedAppointment(partialUpdatedAppointment));
    }

    @Test
    @Transactional
    void patchNonExistingAppointment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appointment.setId(longCount.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appointment.setId(longCount.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appointment.setId(longCount.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appointmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appointment
        restAppointmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appointmentRepository.count();
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

    protected Appointment getPersistedAppointment(Appointment appointment) {
        return appointmentRepository.findById(appointment.getId()).orElseThrow();
    }

    protected void assertPersistedAppointmentToMatchAllProperties(Appointment expectedAppointment) {
        assertAppointmentAllPropertiesEquals(expectedAppointment, getPersistedAppointment(expectedAppointment));
    }

    protected void assertPersistedAppointmentToMatchUpdatableProperties(Appointment expectedAppointment) {
        assertAppointmentAllUpdatablePropertiesEquals(expectedAppointment, getPersistedAppointment(expectedAppointment));
    }
}
