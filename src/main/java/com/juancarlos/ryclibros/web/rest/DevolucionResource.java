package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.DevolucionRepository;
import com.juancarlos.ryclibros.service.DevolucionService;
import com.juancarlos.ryclibros.service.dto.DevolucionDTO;
import com.juancarlos.ryclibros.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Devolucion}.
 */
@RestController
@RequestMapping("/api")
public class DevolucionResource {

    private final Logger log = LoggerFactory.getLogger(DevolucionResource.class);

    private static final String ENTITY_NAME = "devolucion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DevolucionService devolucionService;

    private final DevolucionRepository devolucionRepository;

    public DevolucionResource(DevolucionService devolucionService, DevolucionRepository devolucionRepository) {
        this.devolucionService = devolucionService;
        this.devolucionRepository = devolucionRepository;
    }

    /**
     * {@code POST  /devolucions} : Create a new devolucion.
     *
     * @param devolucionDTO the devolucionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new devolucionDTO, or with status {@code 400 (Bad Request)} if the devolucion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devolucions")
    public ResponseEntity<DevolucionDTO> createDevolucion(@Valid @RequestBody DevolucionDTO devolucionDTO) throws URISyntaxException {
        log.debug("REST request to save Devolucion : {}", devolucionDTO);
        if (devolucionDTO.getId() != null) {
            throw new BadRequestAlertException("A new devolucion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DevolucionDTO result = devolucionService.save(devolucionDTO);
        return ResponseEntity
            .created(new URI("/api/devolucions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/devolucions/prestamo")
    public ResponseEntity<DevolucionDTO> createDevolucionFromPrestamo(@Valid @RequestBody DevolucionDTO devolucionDTO)
        throws URISyntaxException {
        log.debug("REST request to save Devolucion : {}", devolucionDTO);
        if (devolucionDTO.getId() != null) {
            throw new BadRequestAlertException("La devolucion no puede tener un identificador", ENTITY_NAME, "idexists");
        }

        DevolucionDTO result = devolucionService.setStatusDevueltoToTrue(devolucionDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", "Se devolvio el libro correctamente");

        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code PUT  /devolucions/:id} : Updates an existing devolucion.
     *
     * @param id            the id of the devolucionDTO to save.
     * @param devolucionDTO the devolucionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated devolucionDTO,
     * or with status {@code 400 (Bad Request)} if the devolucionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the devolucionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devolucions/{id}")
    public ResponseEntity<DevolucionDTO> updateDevolucion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DevolucionDTO devolucionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Devolucion : {}, {}", id, devolucionDTO);
        if (devolucionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, devolucionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!devolucionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DevolucionDTO result = devolucionService.save(devolucionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, devolucionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /devolucions/:id} : Partial updates given fields of an existing devolucion, field will ignore if it is null
     *
     * @param id            the id of the devolucionDTO to save.
     * @param devolucionDTO the devolucionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated devolucionDTO,
     * or with status {@code 400 (Bad Request)} if the devolucionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the devolucionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the devolucionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/devolucions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DevolucionDTO> partialUpdateDevolucion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DevolucionDTO devolucionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Devolucion partially : {}, {}", id, devolucionDTO);
        if (devolucionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, devolucionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!devolucionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DevolucionDTO> result = devolucionService.partialUpdate(devolucionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, devolucionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /devolucions} : get all the devolucions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devolucions in body.
     */
    @GetMapping("/devolucions")
    public ResponseEntity<List<DevolucionDTO>> getAllDevolucions(Pageable pageable) {
        log.debug("REST request to get a page of Devolucions");
        Page<DevolucionDTO> page = devolucionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /devolucions/:id} : get the "id" devolucion.
     *
     * @param id the id of the devolucionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the devolucionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devolucions/{id}")
    public ResponseEntity<DevolucionDTO> getDevolucion(@PathVariable Long id) {
        log.debug("REST request to get Devolucion : {}", id);
        Optional<DevolucionDTO> devolucionDTO = devolucionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(devolucionDTO);
    }

    /**
     * {@code DELETE  /devolucions/:id} : delete the "id" devolucion.
     *
     * @param id the id of the devolucionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devolucions/{id}")
    public ResponseEntity<Void> deleteDevolucion(@PathVariable Long id) {
        log.debug("REST request to delete Devolucion : {}", id);
        devolucionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
