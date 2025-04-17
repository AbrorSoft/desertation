package org.abror.web.rest;

import static org.abror.domain.WorkingHoursAsserts.*;
import static org.abror.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.abror.IntegrationTest;
import org.abror.domain.Employee;
import org.abror.domain.WorkingHours;
import org.abror.domain.enumeration.DayOfWeek;
import org.abror.repository.WorkingHoursRepository;
import org.abror.service.dto.WorkingHoursDTO;
import org.abror.service.mapper.WorkingHoursMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkingHoursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkingHoursResourceIT {

    private static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final DayOfWeek UPDATED_DAY_OF_WEEK = DayOfWeek.TUESDAY;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/working-hours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkingHoursRepository workingHoursRepository;

    @Autowired
    private WorkingHoursMapper workingHoursMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkingHoursMockMvc;

    private WorkingHours workingHours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingHours createEntity(EntityManager em) {
        WorkingHours workingHours = new WorkingHours()
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return workingHours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingHours createUpdatedEntity(EntityManager em) {
        WorkingHours workingHours = new WorkingHours()
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        return workingHours;
    }

    @BeforeEach
    public void initTest() {
        workingHours = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkingHours() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);
        var returnedWorkingHoursDTO = om.readValue(
            restWorkingHoursMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workingHoursDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkingHoursDTO.class
        );

        // Validate the WorkingHours in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkingHours = workingHoursMapper.toEntity(returnedWorkingHoursDTO);
        assertWorkingHoursUpdatableFieldsEquals(returnedWorkingHours, getPersistedWorkingHours(returnedWorkingHours));
    }

    @Test
    @Transactional
    void createWorkingHoursWithExistingId() throws Exception {
        // Create the WorkingHours with an existing ID
        workingHours.setId(1L);
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkingHoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workingHoursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDayOfWeekIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workingHours.setDayOfWeek(null);

        // Create the WorkingHours, which fails.
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        restWorkingHoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workingHoursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workingHours.setStartTime(null);

        // Create the WorkingHours, which fails.
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        restWorkingHoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workingHoursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workingHours.setEndTime(null);

        // Create the WorkingHours, which fails.
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        restWorkingHoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workingHoursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    void getWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get the workingHours
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL_ID, workingHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workingHours.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    void getWorkingHoursByIdFiltering() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        Long id = workingHours.getId();

        defaultWorkingHoursFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWorkingHoursFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWorkingHoursFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkingHoursByDayOfWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where dayOfWeek equals to
        defaultWorkingHoursFiltering("dayOfWeek.equals=" + DEFAULT_DAY_OF_WEEK, "dayOfWeek.equals=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllWorkingHoursByDayOfWeekIsInShouldWork() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where dayOfWeek in
        defaultWorkingHoursFiltering(
            "dayOfWeek.in=" + DEFAULT_DAY_OF_WEEK + "," + UPDATED_DAY_OF_WEEK,
            "dayOfWeek.in=" + UPDATED_DAY_OF_WEEK
        );
    }

    @Test
    @Transactional
    void getAllWorkingHoursByDayOfWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where dayOfWeek is not null
        defaultWorkingHoursFiltering("dayOfWeek.specified=true", "dayOfWeek.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkingHoursByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where startTime equals to
        defaultWorkingHoursFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllWorkingHoursByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where startTime in
        defaultWorkingHoursFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllWorkingHoursByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where startTime is not null
        defaultWorkingHoursFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkingHoursByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where endTime equals to
        defaultWorkingHoursFiltering("endTime.equals=" + DEFAULT_END_TIME, "endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllWorkingHoursByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where endTime in
        defaultWorkingHoursFiltering("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME, "endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllWorkingHoursByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList where endTime is not null
        defaultWorkingHoursFiltering("endTime.specified=true", "endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkingHoursByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            workingHoursRepository.saveAndFlush(workingHours);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        workingHours.setEmployee(employee);
        workingHoursRepository.saveAndFlush(workingHours);
        Long employeeId = employee.getId();
        // Get all the workingHoursList where employee equals to employeeId
        defaultWorkingHoursShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the workingHoursList where employee equals to (employeeId + 1)
        defaultWorkingHoursShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    private void defaultWorkingHoursFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWorkingHoursShouldBeFound(shouldBeFound);
        defaultWorkingHoursShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkingHoursShouldBeFound(String filter) throws Exception {
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));

        // Check, that the count call also returns 1
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkingHoursShouldNotBeFound(String filter) throws Exception {
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkingHours() throws Exception {
        // Get the workingHours
        restWorkingHoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workingHours
        WorkingHours updatedWorkingHours = workingHoursRepository.findById(workingHours.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkingHours are not directly saved in db
        em.detach(updatedWorkingHours);
        updatedWorkingHours.dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(updatedWorkingHours);

        restWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingHoursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workingHoursDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkingHoursToMatchAllProperties(updatedWorkingHours);
    }

    @Test
    @Transactional
    void putNonExistingWorkingHours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workingHours.setId(longCount.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingHoursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkingHours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workingHours.setId(longCount.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkingHours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workingHours.setId(longCount.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workingHoursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkingHoursWithPatch() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workingHours using partial update
        WorkingHours partialUpdatedWorkingHours = new WorkingHours();
        partialUpdatedWorkingHours.setId(workingHours.getId());

        partialUpdatedWorkingHours.dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME);

        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkingHours))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkingHoursUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkingHours, workingHours),
            getPersistedWorkingHours(workingHours)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkingHoursWithPatch() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workingHours using partial update
        WorkingHours partialUpdatedWorkingHours = new WorkingHours();
        partialUpdatedWorkingHours.setId(workingHours.getId());

        partialUpdatedWorkingHours.dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkingHours))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkingHoursUpdatableFieldsEquals(partialUpdatedWorkingHours, getPersistedWorkingHours(partialUpdatedWorkingHours));
    }

    @Test
    @Transactional
    void patchNonExistingWorkingHours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workingHours.setId(longCount.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workingHoursDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkingHours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workingHours.setId(longCount.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkingHours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workingHours.setId(longCount.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workingHoursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingHours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the workingHours
        restWorkingHoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, workingHours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return workingHoursRepository.count();
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

    protected WorkingHours getPersistedWorkingHours(WorkingHours workingHours) {
        return workingHoursRepository.findById(workingHours.getId()).orElseThrow();
    }

    protected void assertPersistedWorkingHoursToMatchAllProperties(WorkingHours expectedWorkingHours) {
        assertWorkingHoursAllPropertiesEquals(expectedWorkingHours, getPersistedWorkingHours(expectedWorkingHours));
    }

    protected void assertPersistedWorkingHoursToMatchUpdatableProperties(WorkingHours expectedWorkingHours) {
        assertWorkingHoursAllUpdatablePropertiesEquals(expectedWorkingHours, getPersistedWorkingHours(expectedWorkingHours));
    }
}
