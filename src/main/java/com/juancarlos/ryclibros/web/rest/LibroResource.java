package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.LibroRepository;
import com.juancarlos.ryclibros.service.LibroQueryService;
import com.juancarlos.ryclibros.service.LibroService;
import com.juancarlos.ryclibros.service.criteria.LibroCriteria;
import com.juancarlos.ryclibros.service.dto.LibroDTO;
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
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Libro}.
 */
@RestController
@RequestMapping("/api")
public class LibroResource {

    private final Logger log = LoggerFactory.getLogger(LibroResource.class);

    private static final String ENTITY_NAME = "libro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LibroService libroService;

    private final LibroRepository libroRepository;

    private final LibroQueryService libroQueryService;

    public LibroResource(LibroService libroService, LibroRepository libroRepository, LibroQueryService libroQueryService) {
        this.libroService = libroService;
        this.libroRepository = libroRepository;
        this.libroQueryService = libroQueryService;
    }

    /**
     * {@code POST  /libros} : Create a new libro.
     *
     * @param libroDTO the libroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new libroDTO, or with status {@code 400 (Bad Request)} if the libro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/libros")
    public ResponseEntity<LibroDTO> createLibro(@Valid @RequestBody LibroDTO libroDTO) throws URISyntaxException {
        log.debug("REST request to save Libro : {}", libroDTO);
        if (libroDTO.getId() != null) {
            throw new BadRequestAlertException("A new libro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LibroDTO result = libroService.save(libroDTO);
        return ResponseEntity
            .created(new URI("/api/libros/" + result.getId()))
            .headers(HeaderUtilCustom.createEntityCreationAlert(applicationName, false, "un", ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /libros/:id} : Updates an existing libro.
     *
     * @param id       the id of the libroDTO to save.
     * @param libroDTO the libroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libroDTO,
     * or with status {@code 400 (Bad Request)} if the libroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the libroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/libros/{id}")
    public ResponseEntity<LibroDTO> updateLibro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LibroDTO libroDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Libro : {}, {}", id, libroDTO);
        if (libroDTO.getId() == null) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libroDTO.getId())) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idinvalid");
        }

        if (!libroRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontró la entidad", ENTITY_NAME, "idnotfound");
        }

        LibroDTO result = libroService.save(libroDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "el", ENTITY_NAME, libroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /libros/:id} : Partial updates given fields of an existing libro, field will ignore if it is null
     *
     * @param id       the id of the libroDTO to save.
     * @param libroDTO the libroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libroDTO,
     * or with status {@code 400 (Bad Request)} if the libroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the libroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the libroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/libros/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LibroDTO> partialUpdateLibro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LibroDTO libroDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Libro partially : {}, {}", id, libroDTO);
        if (libroDTO.getId() == null) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libroDTO.getId())) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idinvalid");
        }

        if (!libroRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontró la entidad", ENTITY_NAME, "idnotfound");
        }

        Optional<LibroDTO> result = libroService.partialUpdate(libroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "el", ENTITY_NAME, libroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /libros} : get all the libros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of libros in body.
     */
    @GetMapping("/libros")
    public ResponseEntity<List<LibroDTO>> getAllLibros(LibroCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Libros by criteria: {}", criteria);
        Page<LibroDTO> page = libroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /libros/count} : count all the libros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/libros/count")
    public ResponseEntity<Long> countLibros(LibroCriteria criteria) {
        log.debug("REST request to count Libros by criteria: {}", criteria);
        return ResponseEntity.ok().body(libroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /libros/:id} : get the "id" libro.
     *
     * @param id the id of the libroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the libroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/libros/{id}")
    public ResponseEntity<LibroDTO> getLibro(@PathVariable Long id) {
        log.debug("REST request to get Libro : {}", id);
        Optional<LibroDTO> libroDTO = libroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(libroDTO);
    }

    /**
     * {@code DELETE  /libros/:id} : delete the "id" libro.
     *
     * @param id the id of the libroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/libros/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        log.debug("REST request to delete Libro : {}", id);
        libroService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtilCustom.createEntityDeletionAlert(applicationName, false, "el", ENTITY_NAME, id.toString()))
            .build();
    }
}
