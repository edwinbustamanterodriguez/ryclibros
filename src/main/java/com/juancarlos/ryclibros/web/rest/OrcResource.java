package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.OrcRepository;
import com.juancarlos.ryclibros.service.OrcService;
import com.juancarlos.ryclibros.service.dto.OrcDTO;
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
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Orc}.
 */
@RestController
@RequestMapping("/api")
public class OrcResource {

    private final Logger log = LoggerFactory.getLogger(OrcResource.class);

    private static final String ENTITY_NAME = "orc";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrcService orcService;

    private final OrcRepository orcRepository;

    public OrcResource(OrcService orcService, OrcRepository orcRepository) {
        this.orcService = orcService;
        this.orcRepository = orcRepository;
    }

    /**
     * {@code POST  /orcs} : Create a new orc.
     *
     * @param orcDTO the orcDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orcDTO, or with status {@code 400 (Bad Request)} if the orc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orcs")
    public ResponseEntity<OrcDTO> createOrc(@Valid @RequestBody OrcDTO orcDTO) throws URISyntaxException {
        log.debug("REST request to save Orc : {}", orcDTO);
        if (orcDTO.getId() != null) {
            throw new BadRequestAlertException("A new orc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrcDTO result = orcService.save(orcDTO);
        return ResponseEntity
            .created(new URI("/api/orcs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orcs/:id} : Updates an existing orc.
     *
     * @param id the id of the orcDTO to save.
     * @param orcDTO the orcDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orcDTO,
     * or with status {@code 400 (Bad Request)} if the orcDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orcDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orcs/{id}")
    public ResponseEntity<OrcDTO> updateOrc(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody OrcDTO orcDTO)
        throws URISyntaxException {
        log.debug("REST request to update Orc : {}, {}", id, orcDTO);
        if (orcDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orcDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orcRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrcDTO result = orcService.save(orcDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orcDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orcs/:id} : Partial updates given fields of an existing orc, field will ignore if it is null
     *
     * @param id the id of the orcDTO to save.
     * @param orcDTO the orcDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orcDTO,
     * or with status {@code 400 (Bad Request)} if the orcDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orcDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orcDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/orcs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrcDTO> partialUpdateOrc(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrcDTO orcDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Orc partially : {}, {}", id, orcDTO);
        if (orcDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orcDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orcRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrcDTO> result = orcService.partialUpdate(orcDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orcDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /orcs} : get all the orcs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orcs in body.
     */
    @GetMapping("/orcs")
    public ResponseEntity<List<OrcDTO>> getAllOrcs(Pageable pageable) {
        log.debug("REST request to get a page of Orcs");
        Page<OrcDTO> page = orcService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orcs/:id} : get the "id" orc.
     *
     * @param id the id of the orcDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orcDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orcs/{id}")
    public ResponseEntity<OrcDTO> getOrc(@PathVariable Long id) {
        log.debug("REST request to get Orc : {}", id);
        Optional<OrcDTO> orcDTO = orcService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orcDTO);
    }

    /**
     * {@code DELETE  /orcs/:id} : delete the "id" orc.
     *
     * @param id the id of the orcDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orcs/{id}")
    public ResponseEntity<Void> deleteOrc(@PathVariable Long id) {
        log.debug("REST request to delete Orc : {}", id);
        orcService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
