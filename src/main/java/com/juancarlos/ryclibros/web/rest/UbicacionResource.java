package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.UbicacionRepository;
import com.juancarlos.ryclibros.service.UbicacionService;
import com.juancarlos.ryclibros.service.dto.UbicacionDTO;
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
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Ubicacion}.
 */
@RestController
@RequestMapping("/api")
public class UbicacionResource {

    private final Logger log = LoggerFactory.getLogger(UbicacionResource.class);

    private static final String ENTITY_NAME = "ubicacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UbicacionService ubicacionService;

    private final UbicacionRepository ubicacionRepository;

    public UbicacionResource(UbicacionService ubicacionService, UbicacionRepository ubicacionRepository) {
        this.ubicacionService = ubicacionService;
        this.ubicacionRepository = ubicacionRepository;
    }

    /**
     * {@code POST  /ubicacions} : Create a new ubicacion.
     *
     * @param ubicacionDTO the ubicacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ubicacionDTO, or with status {@code 400 (Bad Request)} if the ubicacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ubicacions")
    public ResponseEntity<UbicacionDTO> createUbicacion(@Valid @RequestBody UbicacionDTO ubicacionDTO) throws URISyntaxException {
        log.debug("REST request to save Ubicacion : {}", ubicacionDTO);
        if (ubicacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new ubicacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UbicacionDTO result = ubicacionService.save(ubicacionDTO);
        return ResponseEntity
            .created(new URI("/api/ubicacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ubicacions/:id} : Updates an existing ubicacion.
     *
     * @param id the id of the ubicacionDTO to save.
     * @param ubicacionDTO the ubicacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ubicacionDTO,
     * or with status {@code 400 (Bad Request)} if the ubicacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ubicacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ubicacions/{id}")
    public ResponseEntity<UbicacionDTO> updateUbicacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UbicacionDTO ubicacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ubicacion : {}, {}", id, ubicacionDTO);
        if (ubicacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ubicacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ubicacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UbicacionDTO result = ubicacionService.save(ubicacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ubicacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ubicacions/:id} : Partial updates given fields of an existing ubicacion, field will ignore if it is null
     *
     * @param id the id of the ubicacionDTO to save.
     * @param ubicacionDTO the ubicacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ubicacionDTO,
     * or with status {@code 400 (Bad Request)} if the ubicacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ubicacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ubicacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ubicacions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UbicacionDTO> partialUpdateUbicacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UbicacionDTO ubicacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ubicacion partially : {}, {}", id, ubicacionDTO);
        if (ubicacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ubicacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ubicacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UbicacionDTO> result = ubicacionService.partialUpdate(ubicacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ubicacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ubicacions} : get all the ubicacions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ubicacions in body.
     */
    @GetMapping("/ubicacions")
    public ResponseEntity<List<UbicacionDTO>> getAllUbicacions(Pageable pageable) {
        log.debug("REST request to get a page of Ubicacions");
        Page<UbicacionDTO> page = ubicacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ubicacions/:id} : get the "id" ubicacion.
     *
     * @param id the id of the ubicacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ubicacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ubicacions/{id}")
    public ResponseEntity<UbicacionDTO> getUbicacion(@PathVariable Long id) {
        log.debug("REST request to get Ubicacion : {}", id);
        Optional<UbicacionDTO> ubicacionDTO = ubicacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ubicacionDTO);
    }

    /**
     * {@code DELETE  /ubicacions/:id} : delete the "id" ubicacion.
     *
     * @param id the id of the ubicacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ubicacions/{id}")
    public ResponseEntity<Void> deleteUbicacion(@PathVariable Long id) {
        log.debug("REST request to delete Ubicacion : {}", id);
        ubicacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
