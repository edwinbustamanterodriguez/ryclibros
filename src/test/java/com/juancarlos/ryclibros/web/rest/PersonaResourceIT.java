package com.juancarlos.ryclibros.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juancarlos.ryclibros.IntegrationTest;
import com.juancarlos.ryclibros.domain.Persona;
import com.juancarlos.ryclibros.repository.PersonaRepository;
import com.juancarlos.ryclibros.service.dto.PersonaDTO;
import com.juancarlos.ryclibros.service.mapper.PersonaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APATERNO = "AAAAAAAAAA";
    private static final String UPDATED_APATERNO = "BBBBBBBBBB";

    private static final String DEFAULT_AMATERNO = "BBBBBBBBBB";
    private static final String UPDATED_AMATERNO = "BBBBBBBBBB";

    private static final String DEFAULT_CI = "BBBBBBBBBB";
    private static final String UPDATED_CI = "BBBBBBBBBB";

    private static final String DEFAULT_EXPEDICION = "AAAAAAAAAA";
    private static final String UPDATED_EXPEDICION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUCION = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaMapper personaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonaMockMvc;

    private Persona persona;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createEntity(EntityManager em) {
        Persona persona = new Persona()
            .nombre(DEFAULT_NOMBRE)
            .apaterno(DEFAULT_APATERNO)
            .amaterno(DEFAULT_AMATERNO)
            .ci(DEFAULT_CI)
            .expedicion(DEFAULT_EXPEDICION)
            .telefono(DEFAULT_TELEFONO)
            .institucion(DEFAULT_INSTITUCION);
        return persona;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createUpdatedEntity(EntityManager em) {
        Persona persona = new Persona()
            .nombre(UPDATED_NOMBRE)
            .apaterno(UPDATED_APATERNO)
            .amaterno(UPDATED_AMATERNO)
            .ci(UPDATED_CI)
            .expedicion(UPDATED_EXPEDICION)
            .telefono(UPDATED_TELEFONO)
            .institucion(UPDATED_INSTITUCION);
        return persona;
    }

    @BeforeEach
    public void initTest() {
        persona = createEntity(em);
    }

    @Test
    @Transactional
    void createPersona() throws Exception {
        int databaseSizeBeforeCreate = personaRepository.findAll().size();
        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);
        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaDTO)))
            .andExpect(status().isCreated());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate + 1);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersona.getApaterno()).isEqualTo(DEFAULT_APATERNO);
        assertThat(testPersona.getAmaterno()).isEqualTo(DEFAULT_AMATERNO);
        assertThat(testPersona.getCi()).isEqualTo(DEFAULT_CI);
        assertThat(testPersona.getExpedicion()).isEqualTo(DEFAULT_EXPEDICION);
        assertThat(testPersona.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPersona.getInstitucion()).isEqualTo(DEFAULT_INSTITUCION);
    }

    @Test
    @Transactional
    void createPersonaWithExistingId() throws Exception {
        // Create the Persona with an existing ID
        persona.setId(1L);
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        int databaseSizeBeforeCreate = personaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setNombre(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApaternoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setApaterno(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCiIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setCi(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpedicionIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().size();
        // set the field null
        persona.setExpedicion(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonas() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get all the personaList
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apaterno").value(hasItem(DEFAULT_APATERNO)))
            .andExpect(jsonPath("$.[*].amaterno").value(hasItem(DEFAULT_AMATERNO)))
            .andExpect(jsonPath("$.[*].ci").value(hasItem(DEFAULT_CI)))
            .andExpect(jsonPath("$.[*].expedicion").value(hasItem(DEFAULT_EXPEDICION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].institucion").value(hasItem(DEFAULT_INSTITUCION)));
    }

    @Test
    @Transactional
    void getPersona() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        // Get the persona
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL_ID, persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(persona.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apaterno").value(DEFAULT_APATERNO))
            .andExpect(jsonPath("$.amaterno").value(DEFAULT_AMATERNO))
            .andExpect(jsonPath("$.ci").value(DEFAULT_CI))
            .andExpect(jsonPath("$.expedicion").value(DEFAULT_EXPEDICION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.institucion").value(DEFAULT_INSTITUCION));
    }

    @Test
    @Transactional
    void getNonExistingPersona() throws Exception {
        // Get the persona
        restPersonaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersona() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        int databaseSizeBeforeUpdate = personaRepository.findAll().size();

        // Update the persona
        Persona updatedPersona = personaRepository.findById(persona.getId()).get();
        // Disconnect from session so that the updates on updatedPersona are not directly saved in db
        em.detach(updatedPersona);
        updatedPersona
            .nombre(UPDATED_NOMBRE)
            .apaterno(UPDATED_APATERNO)
            .amaterno(UPDATED_AMATERNO)
            .ci(UPDATED_CI)
            .expedicion(UPDATED_EXPEDICION)
            .telefono(UPDATED_TELEFONO)
            .institucion(UPDATED_INSTITUCION);
        PersonaDTO personaDTO = personaMapper.toDto(updatedPersona);

        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersona.getApaterno()).isEqualTo(UPDATED_APATERNO);
        assertThat(testPersona.getAmaterno()).isEqualTo(UPDATED_AMATERNO);
        assertThat(testPersona.getCi()).isEqualTo(UPDATED_CI);
        assertThat(testPersona.getExpedicion()).isEqualTo(UPDATED_EXPEDICION);
        assertThat(testPersona.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPersona.getInstitucion()).isEqualTo(UPDATED_INSTITUCION);
    }

    @Test
    @Transactional
    void putNonExistingPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().size();
        persona.setId(count.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().size();
        persona.setId(count.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().size();
        persona.setId(count.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        int databaseSizeBeforeUpdate = personaRepository.findAll().size();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona.nombre(UPDATED_NOMBRE).institucion(UPDATED_INSTITUCION);

        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersona.getApaterno()).isEqualTo(DEFAULT_APATERNO);
        assertThat(testPersona.getAmaterno()).isEqualTo(DEFAULT_AMATERNO);
        assertThat(testPersona.getCi()).isEqualTo(DEFAULT_CI);
        assertThat(testPersona.getExpedicion()).isEqualTo(DEFAULT_EXPEDICION);
        assertThat(testPersona.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPersona.getInstitucion()).isEqualTo(UPDATED_INSTITUCION);
    }

    @Test
    @Transactional
    void fullUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        int databaseSizeBeforeUpdate = personaRepository.findAll().size();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona
            .nombre(UPDATED_NOMBRE)
            .apaterno(UPDATED_APATERNO)
            .amaterno(UPDATED_AMATERNO)
            .ci(UPDATED_CI)
            .expedicion(UPDATED_EXPEDICION)
            .telefono(UPDATED_TELEFONO)
            .institucion(UPDATED_INSTITUCION);

        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersona.getApaterno()).isEqualTo(UPDATED_APATERNO);
        assertThat(testPersona.getAmaterno()).isEqualTo(UPDATED_AMATERNO);
        assertThat(testPersona.getCi()).isEqualTo(UPDATED_CI);
        assertThat(testPersona.getExpedicion()).isEqualTo(UPDATED_EXPEDICION);
        assertThat(testPersona.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPersona.getInstitucion()).isEqualTo(UPDATED_INSTITUCION);
    }

    @Test
    @Transactional
    void patchNonExistingPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().size();
        persona.setId(count.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().size();
        persona.setId(count.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().size();
        persona.setId(count.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersona() throws Exception {
        // Initialize the database
        personaRepository.saveAndFlush(persona);

        int databaseSizeBeforeDelete = personaRepository.findAll().size();

        // Delete the persona
        restPersonaMockMvc
            .perform(delete(ENTITY_API_URL_ID, persona.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Persona> personaList = personaRepository.findAll();
        assertThat(personaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
