package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.CategoriaRepository;
import com.juancarlos.ryclibros.service.CategoriaService;
import com.juancarlos.ryclibros.service.dto.CategoriaDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Categoria}.
 */
@RestController
@RequestMapping("/api")
public class CategoriaResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);

    private static final String ENTITY_NAME = "categoria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaService categoriaService;

    private final CategoriaRepository categoriaRepository;

    public CategoriaResource(CategoriaService categoriaService, CategoriaRepository categoriaRepository) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * {@code POST  /categorias} : Create a new categoria.
     *
     * @param categoriaDTO the categoriaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoriaDTO, or with status {@code 400 (Bad Request)} if the categoria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorias")
    public ResponseEntity<CategoriaDTO> createCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) throws URISyntaxException {
        log.debug("REST request to save Categoria : {}", categoriaDTO);
        if (categoriaDTO.getId() != null) {
            throw new BadRequestAlertException("Una nueva categoría no puede tener una identificación", ENTITY_NAME, "idexists");
        }
        CategoriaDTO result = categoriaService.save(categoriaDTO);
        return ResponseEntity
            .created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtilCustom.createEntityCreationAlert(applicationName, false, "una", ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categorias/:id} : Updates an existing categoria.
     *
     * @param id           the id of the categoriaDTO to save.
     * @param categoriaDTO the categoriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> updateCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoriaDTO categoriaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Categoria : {}, {}", id, categoriaDTO);
        if (categoriaDTO.getId() == null) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaDTO.getId())) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontró la entidad", ENTITY_NAME, "idnotfound");
        }

        CategoriaDTO result = categoriaService.save(categoriaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "la", ENTITY_NAME, categoriaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categorias/:id} : Partial updates given fields of an existing categoria, field will ignore if it is null
     *
     * @param id           the id of the categoriaDTO to save.
     * @param categoriaDTO the categoriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoriaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorias/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CategoriaDTO> partialUpdateCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoriaDTO categoriaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Categoria partially : {}, {}", id, categoriaDTO);
        if (categoriaDTO.getId() == null) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaDTO.getId())) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontro la entidad", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoriaDTO> result = categoriaService.partialUpdate(categoriaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "la", ENTITY_NAME, categoriaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categorias} : get all the categorias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorias in body.
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias(Pageable pageable) {
        Page<CategoriaDTO> page = categoriaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/categorias//active-status")
    public ResponseEntity<List<CategoriaDTO>> getAllCategoriasActiveStatus(Pageable pageable) {
        Page<CategoriaDTO> page = categoriaService.findAllActiveStatus(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categorias/:id} : get the "id" categoria.
     *
     * @param id the id of the categoriaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoriaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        Optional<CategoriaDTO> categoriaDTO = categoriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoriaDTO);
    }

    /**
     * {@code DELETE  /categorias/:id} : delete the "id" categoria.
     *
     * @param id the id of the categoriaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        log.debug("REST request to delete Categoria : {}", id);
        categoriaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtilCustom.createEntityDeletionAlert(applicationName, false, "la", ENTITY_NAME, id.toString()))
            .build();
    }
}
