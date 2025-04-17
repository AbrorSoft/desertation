package org.abror.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.abror.repository.ServiceForCustomerRepository;
import org.abror.service.ServiceForCustomerQueryService;
import org.abror.service.ServiceForCustomerService;
import org.abror.service.criteria.ServiceForCustomerCriteria;
import org.abror.service.dto.ServiceForCustomerDTO;
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
 * REST controller for managing {@link org.abror.domain.ServiceForCustomer}.
 */
@RestController
@RequestMapping("/api/service-for-customers")
public class ServiceForCustomerResource {

    private final Logger log = LoggerFactory.getLogger(ServiceForCustomerResource.class);

    private static final String ENTITY_NAME = "serviceForCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceForCustomerService serviceForCustomerService;

    private final ServiceForCustomerRepository serviceForCustomerRepository;

    private final ServiceForCustomerQueryService serviceForCustomerQueryService;

    public ServiceForCustomerResource(
        ServiceForCustomerService serviceForCustomerService,
        ServiceForCustomerRepository serviceForCustomerRepository,
        ServiceForCustomerQueryService serviceForCustomerQueryService
    ) {
        this.serviceForCustomerService = serviceForCustomerService;
        this.serviceForCustomerRepository = serviceForCustomerRepository;
        this.serviceForCustomerQueryService = serviceForCustomerQueryService;
    }

    /**
     * {@code POST  /service-for-customers} : Create a new serviceForCustomer.
     *
     * @param serviceForCustomerDTO the serviceForCustomerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceForCustomerDTO, or with status {@code 400 (Bad Request)} if the serviceForCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceForCustomerDTO> createServiceForCustomer(@Valid @RequestBody ServiceForCustomerDTO serviceForCustomerDTO)
        throws URISyntaxException {
        log.debug("REST request to save ServiceForCustomer : {}", serviceForCustomerDTO);
        if (serviceForCustomerDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceForCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceForCustomerDTO = serviceForCustomerService.save(serviceForCustomerDTO);
        return ResponseEntity.created(new URI("/api/service-for-customers/" + serviceForCustomerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceForCustomerDTO.getId().toString()))
            .body(serviceForCustomerDTO);
    }

    /**
     * {@code PUT  /service-for-customers/:id} : Updates an existing serviceForCustomer.
     *
     * @param id the id of the serviceForCustomerDTO to save.
     * @param serviceForCustomerDTO the serviceForCustomerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceForCustomerDTO,
     * or with status {@code 400 (Bad Request)} if the serviceForCustomerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceForCustomerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceForCustomerDTO> updateServiceForCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceForCustomerDTO serviceForCustomerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceForCustomer : {}, {}", id, serviceForCustomerDTO);
        if (serviceForCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceForCustomerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceForCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceForCustomerDTO = serviceForCustomerService.update(serviceForCustomerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceForCustomerDTO.getId().toString()))
            .body(serviceForCustomerDTO);
    }

    /**
     * {@code PATCH  /service-for-customers/:id} : Partial updates given fields of an existing serviceForCustomer, field will ignore if it is null
     *
     * @param id the id of the serviceForCustomerDTO to save.
     * @param serviceForCustomerDTO the serviceForCustomerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceForCustomerDTO,
     * or with status {@code 400 (Bad Request)} if the serviceForCustomerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceForCustomerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceForCustomerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceForCustomerDTO> partialUpdateServiceForCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceForCustomerDTO serviceForCustomerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceForCustomer partially : {}, {}", id, serviceForCustomerDTO);
        if (serviceForCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceForCustomerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceForCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceForCustomerDTO> result = serviceForCustomerService.partialUpdate(serviceForCustomerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceForCustomerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-for-customers} : get all the serviceForCustomers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceForCustomers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceForCustomerDTO>> getAllServiceForCustomers(
        ServiceForCustomerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ServiceForCustomers by criteria: {}", criteria);

        Page<ServiceForCustomerDTO> page = serviceForCustomerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-for-customers/count} : count all the serviceForCustomers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countServiceForCustomers(ServiceForCustomerCriteria criteria) {
        log.debug("REST request to count ServiceForCustomers by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceForCustomerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-for-customers/:id} : get the "id" serviceForCustomer.
     *
     * @param id the id of the serviceForCustomerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceForCustomerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceForCustomerDTO> getServiceForCustomer(@PathVariable("id") Long id) {
        log.debug("REST request to get ServiceForCustomer : {}", id);
        Optional<ServiceForCustomerDTO> serviceForCustomerDTO = serviceForCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceForCustomerDTO);
    }

    /**
     * {@code DELETE  /service-for-customers/:id} : delete the "id" serviceForCustomer.
     *
     * @param id the id of the serviceForCustomerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceForCustomer(@PathVariable("id") Long id) {
        log.debug("REST request to delete ServiceForCustomer : {}", id);
        serviceForCustomerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
