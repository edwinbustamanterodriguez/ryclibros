package com.juancarlos.ryclibros.web.rest;

import com.juancarlos.ryclibros.repository.PersonaRepository;
import com.juancarlos.ryclibros.service.PersonaService;
import com.juancarlos.ryclibros.service.dto.PersonaDTO;
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
 * REST controller for managing {@link com.juancarlos.ryclibros.domain.Persona}.
 */
@RestController
@RequestMapping("/api")
public class PersonaResource {

    private final Logger log = LoggerFactory.getLogger(PersonaResource.class);

    private static final String ENTITY_NAME = "persona";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonaService personaService;

    private final PersonaRepository personaRepository;

    public PersonaResource(PersonaService personaService, PersonaRepository personaRepository) {
        this.personaService = personaService;
        this.personaRepository = personaRepository;
    }

    /**
     * {@code POST  /personas} : Create a new persona.
     *
     * @param personaDTO the personaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personaDTO, or with status {@code 400 (Bad Request)} if the persona has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personas")
    public ResponseEntity<PersonaDTO> createPersona(@Valid @RequestBody PersonaDTO personaDTO) throws URISyntaxException {
        log.debug("REST request to save Persona : {}", personaDTO);
        if (personaDTO.getId() != null) {
            throw new BadRequestAlertException("Una nueva persona no puede tener una identificación", ENTITY_NAME, "idexists");
        }
        PersonaDTO result = personaService.save(personaDTO);
        return ResponseEntity
            .created(new URI("/api/personas/" + result.getId()))
            .headers(HeaderUtilCustom.createEntityCreationAlert(applicationName, false, "una", ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personas/:id} : Updates an existing persona.
     *
     * @param id the id of the personaDTO to save.
     * @param personaDTO the personaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personaDTO,
     * or with status {@code 400 (Bad Request)} if the personaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personas/{id}")
    public ResponseEntity<PersonaDTO> updatePersona(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonaDTO personaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Persona : {}, {}", id, personaDTO);
        if (personaDTO.getId() == null) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personaDTO.getId())) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idinvalid");
        }

        if (!personaRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontró la entidad", ENTITY_NAME, "idnotfound");
        }

        PersonaDTO result = personaService.save(personaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "la", ENTITY_NAME, personaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personas/:id} : Partial updates given fields of an existing persona, field will ignore if it is null
     *
     * @param id the id of the personaDTO to save.
     * @param personaDTO the personaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personaDTO,
     * or with status {@code 400 (Bad Request)} if the personaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonaDTO> partialUpdatePersona(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonaDTO personaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Persona partially : {}, {}", id, personaDTO);
        if (personaDTO.getId() == null) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personaDTO.getId())) {
            throw new BadRequestAlertException("Id inválido", ENTITY_NAME, "idinvalid");
        }

        if (!personaRepository.existsById(id)) {
            throw new BadRequestAlertException("No se encontró la entidad", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonaDTO> result = personaService.partialUpdate(personaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtilCustom.createEntityUpdateAlert(applicationName, false, "la", ENTITY_NAME, personaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /personas} : get all the personas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personas in body.
     */
    @GetMapping("/personas")
    public ResponseEntity<List<PersonaDTO>> getAllPersonas(Pageable pageable) {
        log.debug("REST request to get a page of Personas");
        Page<PersonaDTO> page = personaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/personas/_search")
    public ResponseEntity<List<PersonaDTO>> getAllPersonasByQuery(
        @RequestParam(required = true) String query,
        @RequestParam(required = true) Integer filter,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of Personas");
        Page<PersonaDTO> page = personaService.findAllByQuery(query, filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/personas/or")
    public ResponseEntity<List<PersonaDTO>> getAllPersonasOR(Pageable pageable) {
        log.debug("REST request to get a page of Personas");
        Page<PersonaDTO> page = personaService.findAllOR(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/personas/or/{id}")
    public ResponseEntity<List<PersonaDTO>> getAllPersonasORByOrc(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get a page of Personas");
        Page<PersonaDTO> page = personaService.findAllORByOrc(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personas/:id} : get the "id" persona.
     *
     * @param id the id of the personaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personas/{id}")
    public ResponseEntity<PersonaDTO> getPersona(@PathVariable Long id) {
        log.debug("REST request to get Persona : {}", id);
        Optional<PersonaDTO> personaDTO = personaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personaDTO);
    }

    /**
     * {@code DELETE  /personas/:id} : delete the "id" persona.
     *
     * @param id the id of the personaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personas/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        log.debug("REST request to delete Persona : {}", id);
        personaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtilCustom.createEntityDeletionAlert(applicationName, false, "la", ENTITY_NAME, id.toString()))
            .build();
    }
}
