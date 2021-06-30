package com.juancarlos.ryclibros.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juancarlos.ryclibros.IntegrationTest;
import com.juancarlos.ryclibros.domain.Ubicacion;
import com.juancarlos.ryclibros.repository.UbicacionRepository;
import com.juancarlos.ryclibros.service.dto.UbicacionDTO;
import com.juancarlos.ryclibros.service.mapper.UbicacionMapper;
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
 * Integration tests for the {@link UbicacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UbicacionResourceIT {

    private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final String DEFAULT_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SERIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ubicacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private UbicacionMapper ubicacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUbicacionMockMvc;

    private Ubicacion ubicacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubicacion createEntity(EntityManager em) {
        Ubicacion ubicacion = new Ubicacion().sector(DEFAULT_SECTOR).numero(DEFAULT_NUMERO).serie(DEFAULT_SERIE);
        return ubicacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubicacion createUpdatedEntity(EntityManager em) {
        Ubicacion ubicacion = new Ubicacion().sector(UPDATED_SECTOR).numero(UPDATED_NUMERO).serie(UPDATED_SERIE);
        return ubicacion;
    }

    @BeforeEach
    public void initTest() {
        ubicacion = createEntity(em);
    }

    @Test
    @Transactional
    void createUbicacion() throws Exception {
        int databaseSizeBeforeCreate = ubicacionRepository.findAll().size();
        // Create the Ubicacion
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);
        restUbicacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ubicacionDTO)))
            .andExpect(status().isCreated());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeCreate + 1);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getSector()).isEqualTo(DEFAULT_SECTOR);
        assertThat(testUbicacion.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testUbicacion.getSerie()).isEqualTo(DEFAULT_SERIE);
    }

    @Test
    @Transactional
    void createUbicacionWithExistingId() throws Exception {
        // Create the Ubicacion with an existing ID
        ubicacion.setId(1L);
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        int databaseSizeBeforeCreate = ubicacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUbicacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ubicacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = ubicacionRepository.findAll().size();
        // set the field null
        ubicacion.setSector(null);

        // Create the Ubicacion, which fails.
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        restUbicacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ubicacionDTO)))
            .andExpect(status().isBadRequest());

        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = ubicacionRepository.findAll().size();
        // set the field null
        ubicacion.setNumero(null);

        // Create the Ubicacion, which fails.
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        restUbicacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ubicacionDTO)))
            .andExpect(status().isBadRequest());

        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSerieIsRequired() throws Exception {
        int databaseSizeBeforeTest = ubicacionRepository.findAll().size();
        // set the field null
        ubicacion.setSerie(null);

        // Create the Ubicacion, which fails.
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        restUbicacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ubicacionDTO)))
            .andExpect(status().isBadRequest());

        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUbicacions() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        // Get all the ubicacionList
        restUbicacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ubicacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)));
    }

    @Test
    @Transactional
    void getUbicacion() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        // Get the ubicacion
        restUbicacionMockMvc
            .perform(get(ENTITY_API_URL_ID, ubicacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ubicacion.getId().intValue()))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE));
    }

    @Test
    @Transactional
    void getNonExistingUbicacion() throws Exception {
        // Get the ubicacion
        restUbicacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUbicacion() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();

        // Update the ubicacion
        Ubicacion updatedUbicacion = ubicacionRepository.findById(ubicacion.getId()).get();
        // Disconnect from session so that the updates on updatedUbicacion are not directly saved in db
        em.detach(updatedUbicacion);
        updatedUbicacion.sector(UPDATED_SECTOR).numero(UPDATED_NUMERO).serie(UPDATED_SERIE);
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(updatedUbicacion);

        restUbicacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ubicacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ubicacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testUbicacion.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testUbicacion.getSerie()).isEqualTo(UPDATED_SERIE);
    }

    @Test
    @Transactional
    void putNonExistingUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();
        ubicacion.setId(count.incrementAndGet());

        // Create the Ubicacion
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUbicacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ubicacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ubicacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();
        ubicacion.setId(count.incrementAndGet());

        // Create the Ubicacion
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbicacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ubicacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();
        ubicacion.setId(count.incrementAndGet());

        // Create the Ubicacion
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbicacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ubicacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUbicacionWithPatch() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();

        // Update the ubicacion using partial update
        Ubicacion partialUpdatedUbicacion = new Ubicacion();
        partialUpdatedUbicacion.setId(ubicacion.getId());

        partialUpdatedUbicacion.sector(UPDATED_SECTOR);

        restUbicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUbicacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUbicacion))
            )
            .andExpect(status().isOk());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testUbicacion.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testUbicacion.getSerie()).isEqualTo(DEFAULT_SERIE);
    }

    @Test
    @Transactional
    void fullUpdateUbicacionWithPatch() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();

        // Update the ubicacion using partial update
        Ubicacion partialUpdatedUbicacion = new Ubicacion();
        partialUpdatedUbicacion.setId(ubicacion.getId());

        partialUpdatedUbicacion.sector(UPDATED_SECTOR).numero(UPDATED_NUMERO).serie(UPDATED_SERIE);

        restUbicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUbicacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUbicacion))
            )
            .andExpect(status().isOk());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testUbicacion.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testUbicacion.getSerie()).isEqualTo(UPDATED_SERIE);
    }

    @Test
    @Transactional
    void patchNonExistingUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();
        ubicacion.setId(count.incrementAndGet());

        // Create the Ubicacion
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUbicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ubicacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ubicacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();
        ubicacion.setId(count.incrementAndGet());

        // Create the Ubicacion
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ubicacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();
        ubicacion.setId(count.incrementAndGet());

        // Create the Ubicacion
        UbicacionDTO ubicacionDTO = ubicacionMapper.toDto(ubicacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbicacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ubicacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUbicacion() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        int databaseSizeBeforeDelete = ubicacionRepository.findAll().size();

        // Delete the ubicacion
        restUbicacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, ubicacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
