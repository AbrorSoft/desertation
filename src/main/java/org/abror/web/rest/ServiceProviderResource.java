package org.abror.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.abror.repository.ServiceProviderRepository;
import org.abror.service.ServiceProviderQueryService;
import org.abror.service.ServiceProviderService;
import org.abror.service.criteria.ServiceProviderCriteria;
import org.abror.service.dto.ServiceProviderDTO;
import org.abror.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.abror.domain.ServiceProvider}.
 */
@RestController
@RequestMapping("/api/service-providers")
public class ServiceProviderResource {

    private final Logger log = LoggerFactory.getLogger(ServiceProviderResource.class);

    private static final String ENTITY_NAME = "serviceProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceProviderService serviceProviderService;

    private final ServiceProviderRepository serviceProviderRepository;

    private final ServiceProviderQueryService serviceProviderQueryService;

    public ServiceProviderResource(
        ServiceProviderService serviceProviderService,
        ServiceProviderRepository serviceProviderRepository,
        ServiceProviderQueryService serviceProviderQueryService
    ) {
        this.serviceProviderService = serviceProviderService;
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceProviderQueryService = serviceProviderQueryService;
    }

    /**
     * {@code POST  /service-providers} : Create a new serviceProvider.
     *
     * @param serviceProviderDTO the serviceProviderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceProviderDTO, or with status {@code 400 (Bad Request)} if the serviceProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceProviderDTO> createServiceProvider(@Valid @RequestBody ServiceProviderDTO serviceProviderDTO)
        throws URISyntaxException {
        log.debug("REST request to save ServiceProvider : {}", serviceProviderDTO);
        if (serviceProviderDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceProviderDTO = serviceProviderService.save(serviceProviderDTO);
        return ResponseEntity.created(new URI("/api/service-providers/" + serviceProviderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceProviderDTO.getId().toString()))
            .body(serviceProviderDTO);
    }

    /**
     * {@code PUT  /service-providers/:id} : Updates an existing serviceProvider.
     *
     * @param id the id of the serviceProviderDTO to save.
     * @param serviceProviderDTO the serviceProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceProviderDTO,
     * or with status {@code 400 (Bad Request)} if the serviceProviderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceProviderDTO> updateServiceProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceProviderDTO serviceProviderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceProvider : {}, {}", id, serviceProviderDTO);
        if (serviceProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceProviderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceProviderDTO = serviceProviderService.update(serviceProviderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceProviderDTO.getId().toString()))
            .body(serviceProviderDTO);
    }

    /**
     * {@code PATCH  /service-providers/:id} : Partial updates given fields of an existing serviceProvider, field will ignore if it is null
     *
     * @param id the id of the serviceProviderDTO to save.
     * @param serviceProviderDTO the serviceProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceProviderDTO,
     * or with status {@code 400 (Bad Request)} if the serviceProviderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceProviderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceProviderDTO> partialUpdateServiceProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceProviderDTO serviceProviderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceProvider partially : {}, {}", id, serviceProviderDTO);
        if (serviceProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceProviderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceProviderDTO> result = serviceProviderService.partialUpdate(serviceProviderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceProviderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-providers} : get all the serviceProviders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceProviders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceProviderDTO>> getAllServiceProviders(
        ServiceProviderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ServiceProviders by criteria: {}", criteria);

        Page<ServiceProviderDTO> page = serviceProviderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-providers/count} : count all the serviceProviders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countServiceProviders(ServiceProviderCriteria criteria) {
        log.debug("REST request to count ServiceProviders by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceProviderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-providers/:id} : get the "id" serviceProvider.
     *
     * @param id the id of the serviceProviderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceProviderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderDTO> getServiceProvider(@PathVariable("id") Long id) {
        log.debug("REST request to get ServiceProvider : {}", id);
        Optional<ServiceProviderDTO> serviceProviderDTO = serviceProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceProviderDTO);
    }

    /**
     * {@code DELETE  /service-providers/:id} : delete the "id" serviceProvider.
     *
     * @param id the id of the serviceProviderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable("id") Long id) {
        log.debug("REST request to delete ServiceProvider : {}", id);
        serviceProviderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
