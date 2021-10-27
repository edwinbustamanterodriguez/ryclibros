package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.PrestamoRepository;
import com.juancarlos.ryclibros.service.PrestamoService;
import com.juancarlos.ryclibros.service.dto.PrestamoDTO;
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
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Prestamo}.
 */
@RestController
@RequestMapping("/api")
public class PrestamoResource {

    private final Logger log = LoggerFactory.getLogger(PrestamoResource.class);

    private static final String ENTITY_NAME = "prestamo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrestamoService prestamoService;

    private final PrestamoRepository prestamoRepository;

    public PrestamoResource(PrestamoService prestamoService, PrestamoRepository prestamoRepository) {
        this.prestamoService = prestamoService;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * {@code POST  /prestamos} : Create a new prestamo.
     *
     * @param prestamoDTO the prestamoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prestamoDTO, or with status {@code 400 (Bad Request)} if the prestamo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prestamos")
    public ResponseEntity<PrestamoDTO> createPrestamo(@Valid @RequestBody PrestamoDTO prestamoDTO) throws URISyntaxException {
        log.debug("REST request to save Prestamo : {}", prestamoDTO);
        if (prestamoDTO.getId() != null) {
            throw new BadRequestAlertException("Un prestamo nuevo no puede tener una identificación", ENTITY_NAME, "idexists");
        }
        PrestamoDTO result = prestamoService.save(prestamoDTO);
        return ResponseEntity
            .created(new URI("/api/prestamos/" + result.getId()))
            .headers(HeaderUtilCustom.createEntityCreationAlert(applicationName, false, "el", ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prestamos/:id} : Updates an existing prestamo.
     *
     * @param id          the id of the prestamoDTO to save.
     * @param prestamoDTO the prestamoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestamoDTO,
     * or with status {@code 400 (Bad Request)} if the prestamoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prestamoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prestamos/{id}")
    public ResponseEntity<PrestamoDTO> updatePrestamo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrestamoDTO prestamoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Prestamo : {}, {}", id, prestamoDTO);
        if (prestamoDTO.getId() == null) {
            throw new BadRequestAlertException("Identificador inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestamoDTO.getId())) {
            throw new BadRequestAlertException("Identificador inválido", ENTITY_NAME, "idinvalid");
        }

        if (!prestamoRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontro la entidad", ENTITY_NAME, "idnotfound");
        }

        PrestamoDTO result = prestamoService.save(prestamoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "el", ENTITY_NAME, prestamoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prestamos/:id} : Partial updates given fields of an existing prestamo, field will ignore if it is null
     *
     * @param id          the id of the prestamoDTO to save.
     * @param prestamoDTO the prestamoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestamoDTO,
     * or with status {@code 400 (Bad Request)} if the prestamoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prestamoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prestamoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prestamos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PrestamoDTO> partialUpdatePrestamo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrestamoDTO prestamoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prestamo partially : {}, {}", id, prestamoDTO);
        if (prestamoDTO.getId() == null) {
            throw new BadRequestAlertException("Identificador inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestamoDTO.getId())) {
            throw new BadRequestAlertException("Identificador inválido", ENTITY_NAME, "idinvalid");
        }

        if (!prestamoRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontro la entidad", ENTITY_NAME, "idnotfound");
        }

        Optional<PrestamoDTO> result = prestamoService.partialUpdate(prestamoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "el", ENTITY_NAME, prestamoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prestamos} : get all the prestamos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prestamos in body.
     */
    @GetMapping("/prestamos")
    public ResponseEntity<List<PrestamoDTO>> getAllPrestamos(Pageable pageable) {
        log.debug("REST request to get a page of Prestamos");
        Page<PrestamoDTO> page = prestamoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/prestamos/current")
    public ResponseEntity<List<PrestamoDTO>> getAllPrestamosCurrent(Pageable pageable) {
        log.debug("REST request to get a page of Prestamos");
        Page<PrestamoDTO> page = prestamoService.findAllPrestamosCurrent(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/prestamos/_search")
    public ResponseEntity<List<PrestamoDTO>> getAllPrestamosByNameLibro(@RequestParam(required = true) String search, Pageable pageable) {
        log.debug("REST request to get a page of Prestamos by Name Libro");
        Page<PrestamoDTO> page = prestamoService.findAllByNameLibro(search, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prestamos/:id} : get the "id" prestamo.
     *
     * @param id the id of the prestamoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prestamoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prestamos/{id}")
    public ResponseEntity<PrestamoDTO> getPrestamo(@PathVariable Long id) {
        log.debug("REST request to get Prestamo : {}", id);
        Optional<PrestamoDTO> prestamoDTO = prestamoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prestamoDTO);
    }

    /**
     * {@code DELETE  /prestamos/:id} : delete the "id" prestamo.
     *
     * @param id the id of the prestamoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prestamos/{id}")
    public ResponseEntity<Void> deletePrestamo(@PathVariable Long id) {
        log.debug("REST request to delete Prestamo : {}", id);
        prestamoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtilCustom.createEntityDeletionAlert(applicationName, false, "el", ENTITY_NAME, id.toString()))
            .build();
    }
}
