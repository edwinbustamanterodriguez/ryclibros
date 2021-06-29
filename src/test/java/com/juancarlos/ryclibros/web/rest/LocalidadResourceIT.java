package com.juancarlos.ryclibros.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juancarlos.ryclibros.IntegrationTest;
import com.juancarlos.ryclibros.domain.Localidad;
import com.juancarlos.ryclibros.repository.LocalidadRepository;
import com.juancarlos.ryclibros.service.dto.LocalidadDTO;
import com.juancarlos.ryclibros.service.mapper.LocalidadMapper;
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
 * Integration tests for the {@link LocalidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocalidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/localidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private LocalidadMapper localidadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalidadMockMvc;

    private Localidad localidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localidad createEntity(EntityManager em) {
        Localidad localidad = new Localidad().nombre(DEFAULT_NOMBRE);
        return localidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localidad createUpdatedEntity(EntityManager em) {
        Localidad localidad = new Localidad().nombre(UPDATED_NOMBRE);
        return localidad;
    }

    @BeforeEach
    public void initTest() {
        localidad = createEntity(em);
    }

    @Test
    @Transactional
    void createLocalidad() throws Exception {
        int databaseSizeBeforeCreate = localidadRepository.findAll().size();
        // Create the Localidad
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);
        restLocalidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localidadDTO)))
            .andExpect(status().isCreated());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeCreate + 1);
        Localidad testLocalidad = localidadList.get(localidadList.size() - 1);
        assertThat(testLocalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createLocalidadWithExistingId() throws Exception {
        // Create the Localidad with an existing ID
        localidad.setId(1L);
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        int databaseSizeBeforeCreate = localidadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localidadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadRepository.findAll().size();
        // set the field null
        localidad.setNombre(null);

        // Create the Localidad, which fails.
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        restLocalidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localidadDTO)))
            .andExpect(status().isBadRequest());

        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocalidads() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList
        restLocalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getLocalidad() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get the localidad
        restLocalidadMockMvc
            .perform(get(ENTITY_API_URL_ID, localidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingLocalidad() throws Exception {
        // Get the localidad
        restLocalidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocalidad() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();

        // Update the localidad
        Localidad updatedLocalidad = localidadRepository.findById(localidad.getId()).get();
        // Disconnect from session so that the updates on updatedLocalidad are not directly saved in db
        em.detach(updatedLocalidad);
        updatedLocalidad.nombre(UPDATED_NOMBRE);
        LocalidadDTO localidadDTO = localidadMapper.toDto(updatedLocalidad);

        restLocalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localidadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
        Localidad testLocalidad = localidadList.get(localidadList.size() - 1);
        assertThat(testLocalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingLocalidad() throws Exception {
        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();
        localidad.setId(count.incrementAndGet());

        // Create the Localidad
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalidad() throws Exception {
        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();
        localidad.setId(count.incrementAndGet());

        // Create the Localidad
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalidad() throws Exception {
        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();
        localidad.setId(count.incrementAndGet());

        // Create the Localidad
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalidadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localidadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalidadWithPatch() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();

        // Update the localidad using partial update
        Localidad partialUpdatedLocalidad = new Localidad();
        partialUpdatedLocalidad.setId(localidad.getId());

        restLocalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalidad))
            )
            .andExpect(status().isOk());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
        Localidad testLocalidad = localidadList.get(localidadList.size() - 1);
        assertThat(testLocalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateLocalidadWithPatch() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();

        // Update the localidad using partial update
        Localidad partialUpdatedLocalidad = new Localidad();
        partialUpdatedLocalidad.setId(localidad.getId());

        partialUpdatedLocalidad.nombre(UPDATED_NOMBRE);

        restLocalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalidad))
            )
            .andExpect(status().isOk());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
        Localidad testLocalidad = localidadList.get(localidadList.size() - 1);
        assertThat(testLocalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingLocalidad() throws Exception {
        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();
        localidad.setId(count.incrementAndGet());

        // Create the Localidad
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localidadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalidad() throws Exception {
        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();
        localidad.setId(count.incrementAndGet());

        // Create the Localidad
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalidad() throws Exception {
        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();
        localidad.setId(count.incrementAndGet());

        // Create the Localidad
        LocalidadDTO localidadDTO = localidadMapper.toDto(localidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalidadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(localidadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalidad() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        int databaseSizeBeforeDelete = localidadRepository.findAll().size();

        // Delete the localidad
        restLocalidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, localidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
