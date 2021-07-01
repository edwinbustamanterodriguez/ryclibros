package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.LocalidadRepository;
import com.juancarlos.ryclibros.service.LocalidadService;
import com.juancarlos.ryclibros.service.dto.LocalidadDTO;
import com.juancarlos.ryclibros.service.utilies.HeaderUtilCustom;
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
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Localidad}.
 */
@RestController
@RequestMapping("/api")
public class LocalidadResource {

    private final Logger log = LoggerFactory.getLogger(LocalidadResource.class);

    private static final String ENTITY_NAME = "localidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalidadService localidadService;

    private final LocalidadRepository localidadRepository;

    public LocalidadResource(LocalidadService localidadService, LocalidadRepository localidadRepository) {
        this.localidadService = localidadService;
        this.localidadRepository = localidadRepository;
    }

    /**
     * {@code POST  /localidads} : Create a new localidad.
     *
     * @param localidadDTO the localidadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localidadDTO, or with status {@code 400 (Bad Request)} if the localidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/localidads")
    public ResponseEntity<LocalidadDTO> createLocalidad(@Valid @RequestBody LocalidadDTO localidadDTO) throws URISyntaxException {
        log.debug("REST request to save Localidad : {}", localidadDTO);
        if (localidadDTO.getId() != null) {
            throw new BadRequestAlertException("Una nueva localidad no puede tener una identificación", ENTITY_NAME, "idexists");
        }
        LocalidadDTO result = localidadService.save(localidadDTO);
        return ResponseEntity
            .created(new URI("/api/localidads/" + result.getId()))
            .headers(HeaderUtilCustom.createEntityCreationAlert(applicationName, false, "la", ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /localidads/:id} : Updates an existing localidad.
     *
     * @param id           the id of the localidadDTO to save.
     * @param localidadDTO the localidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localidadDTO,
     * or with status {@code 400 (Bad Request)} if the localidadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/localidads/{id}")
    public ResponseEntity<LocalidadDTO> updateLocalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocalidadDTO localidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Localidad : {}, {}", id, localidadDTO);
        if (localidadDTO.getId() == null) {
            throw new BadRequestAlertException("Identificación inválida", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localidadDTO.getId())) {
            throw new BadRequestAlertException("Identificación inválida", ENTITY_NAME, "idinvalid");
        }

        if (!localidadRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontró la entidad", ENTITY_NAME, "idnotfound");
        }

        LocalidadDTO result = localidadService.save(localidadDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "la", ENTITY_NAME, localidadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /localidads/:id} : Partial updates given fields of an existing localidad, field will ignore if it is null
     *
     * @param id           the id of the localidadDTO to save.
     * @param localidadDTO the localidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localidadDTO,
     * or with status {@code 400 (Bad Request)} if the localidadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the localidadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the localidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/localidads/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocalidadDTO> partialUpdateLocalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocalidadDTO localidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Localidad partially : {}, {}", id, localidadDTO);
        if (localidadDTO.getId() == null) {
            throw new BadRequestAlertException("Identificación inválida", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localidadDTO.getId())) {
            throw new BadRequestAlertException("Identificación inválida", ENTITY_NAME, "idinvalid");
        }

        if (!localidadRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontró la entidad", ENTITY_NAME, "idnotfound");
        }

        Optional<LocalidadDTO> result = localidadService.partialUpdate(localidadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "la", ENTITY_NAME, localidadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /localidads} : get all the localidads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localidads in body.
     */
    @GetMapping("/localidads")
    public ResponseEntity<List<LocalidadDTO>> getAllLocalidads(Pageable pageable) {
        log.debug("REST request to get a page of Localidads");
        Page<LocalidadDTO> page = localidadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /localidads/:id} : get the "id" localidad.
     *
     * @param id the id of the localidadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localidadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/localidads/{id}")
    public ResponseEntity<LocalidadDTO> getLocalidad(@PathVariable Long id) {
        log.debug("REST request to get Localidad : {}", id);
        Optional<LocalidadDTO> localidadDTO = localidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localidadDTO);
    }

    /**
     * {@code DELETE  /localidads/:id} : delete the "id" localidad.
     *
     * @param id the id of the localidadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/localidads/{id}")
    public ResponseEntity<Void> deleteLocalidad(@PathVariable Long id) {
        log.debug("REST request to delete Localidad : {}", id);
        localidadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtilCustom.createEntityDeletionAlert(applicationName, false, "la", ENTITY_NAME, id.toString()))
            .build();
    }
}
