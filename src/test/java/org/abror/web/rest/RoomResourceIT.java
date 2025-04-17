package org.abror.web.rest;

import static org.abror.domain.RoomAsserts.*;
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
import org.abror.domain.Room;
import org.abror.domain.ServiceProvider;
import org.abror.repository.RoomRepository;
import org.abror.service.dto.RoomDTO;
import org.abror.service.mapper.RoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomResourceIT {

    private static final String DEFAULT_ROOM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room().roomNumber(DEFAULT_ROOM_NUMBER).description(DEFAULT_DESCRIPTION);
        return room;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room().roomNumber(UPDATED_ROOM_NUMBER).description(UPDATED_DESCRIPTION);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @Test
    @Transactional
    void createRoom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);
        var returnedRoomDTO = om.readValue(
            restRoomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoomDTO.class
        );

        // Validate the Room in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoom = roomMapper.toEntity(returnedRoomDTO);
        assertRoomUpdatableFieldsEquals(returnedRoom, getPersistedRoom(returnedRoom));
    }

    @Test
    @Transactional
    void createRoomWithExistingId() throws Exception {
        // Create the Room with an existing ID
        room.setId(1L);
        RoomDTO roomDTO = roomMapper.toDto(room);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoomNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        room.setRoomNumber(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.roomNumber").value(DEFAULT_ROOM_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getRoomsByIdFiltering() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        Long id = room.getId();

        defaultRoomFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRoomFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRoomFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber equals to
        defaultRoomFiltering("roomNumber.equals=" + DEFAULT_ROOM_NUMBER, "roomNumber.equals=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberIsInShouldWork() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber in
        defaultRoomFiltering("roomNumber.in=" + DEFAULT_ROOM_NUMBER + "," + UPDATED_ROOM_NUMBER, "roomNumber.in=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber is not null
        defaultRoomFiltering("roomNumber.specified=true", "roomNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber contains
        defaultRoomFiltering("roomNumber.contains=" + DEFAULT_ROOM_NUMBER, "roomNumber.contains=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberNotContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber does not contain
        defaultRoomFiltering("roomNumber.doesNotContain=" + UPDATED_ROOM_NUMBER, "roomNumber.doesNotContain=" + DEFAULT_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description equals to
        defaultRoomFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description in
        defaultRoomFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description is not null
        defaultRoomFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description contains
        defaultRoomFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description does not contain
        defaultRoomFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByServiceProviderIsEqualToSomething() throws Exception {
        ServiceProvider serviceProvider;
        if (TestUtil.findAll(em, ServiceProvider.class).isEmpty()) {
            roomRepository.saveAndFlush(room);
            serviceProvider = ServiceProviderResourceIT.createEntity(em);
        } else {
            serviceProvider = TestUtil.findAll(em, ServiceProvider.class).get(0);
        }
        em.persist(serviceProvider);
        em.flush();
        room.setServiceProvider(serviceProvider);
        roomRepository.saveAndFlush(room);
        Long serviceProviderId = serviceProvider.getId();
        // Get all the roomList where serviceProvider equals to serviceProviderId
        defaultRoomShouldBeFound("serviceProviderId.equals=" + serviceProviderId);

        // Get all the roomList where serviceProvider equals to (serviceProviderId + 1)
        defaultRoomShouldNotBeFound("serviceProviderId.equals=" + (serviceProviderId + 1));
    }

    private void defaultRoomFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRoomShouldBeFound(shouldBeFound);
        defaultRoomShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoomShouldBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoomShouldNotBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom.roomNumber(UPDATED_ROOM_NUMBER).description(UPDATED_DESCRIPTION);
        RoomDTO roomDTO = roomMapper.toDto(updatedRoom);

        restRoomMockMvc
            .perform(put(ENTITY_API_URL_ID, roomDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isOk());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoomToMatchAllProperties(updatedRoom);
    }

    @Test
    @Transactional
    void putNonExistingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL_ID, roomDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom.description(UPDATED_DESCRIPTION);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRoom, room), getPersistedRoom(room));
    }

    @Test
    @Transactional
    void fullUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom.roomNumber(UPDATED_ROOM_NUMBER).description(UPDATED_DESCRIPTION);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomUpdatableFieldsEquals(partialUpdatedRoom, getPersistedRoom(partialUpdatedRoom));
    }

    @Test
    @Transactional
    void patchNonExistingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the room
        restRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, room.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roomRepository.count();
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

    protected Room getPersistedRoom(Room room) {
        return roomRepository.findById(room.getId()).orElseThrow();
    }

    protected void assertPersistedRoomToMatchAllProperties(Room expectedRoom) {
        assertRoomAllPropertiesEquals(expectedRoom, getPersistedRoom(expectedRoom));
    }

    protected void assertPersistedRoomToMatchUpdatableProperties(Room expectedRoom) {
        assertRoomAllUpdatablePropertiesEquals(expectedRoom, getPersistedRoom(expectedRoom));
    }
}
