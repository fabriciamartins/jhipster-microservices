package com.unifacisa.standard.web.rest;

import com.unifacisa.standard.VendaApp;

import com.unifacisa.standard.domain.Venda;
import com.unifacisa.standard.repository.VendaRepository;
import com.unifacisa.standard.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VendaResource REST controller.
 *
 * @see VendaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VendaApp.class)
public class VendaResourceIntTest {

    private static final String DEFAULT_ID_PEDIDO = "AAAAAAAAAA";
    private static final String UPDATED_ID_PEDIDO = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restVendaMockMvc;

    private Venda venda;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VendaResource vendaResource = new VendaResource(vendaRepository);
        this.restVendaMockMvc = MockMvcBuilders.standaloneSetup(vendaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venda createEntity() {
        Venda venda = new Venda()
            .idPedido(DEFAULT_ID_PEDIDO)
            .valor(DEFAULT_VALOR);
        return venda;
    }

    @Before
    public void initTest() {
        vendaRepository.deleteAll();
        venda = createEntity();
    }

    @Test
    public void createVenda() throws Exception {
        int databaseSizeBeforeCreate = vendaRepository.findAll().size();

        // Create the Venda
        restVendaMockMvc.perform(post("/api/vendas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(venda)))
            .andExpect(status().isCreated());

        // Validate the Venda in the database
        List<Venda> vendaList = vendaRepository.findAll();
        assertThat(vendaList).hasSize(databaseSizeBeforeCreate + 1);
        Venda testVenda = vendaList.get(vendaList.size() - 1);
        assertThat(testVenda.getIdPedido()).isEqualTo(DEFAULT_ID_PEDIDO);
        assertThat(testVenda.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    public void createVendaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vendaRepository.findAll().size();

        // Create the Venda with an existing ID
        venda.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendaMockMvc.perform(post("/api/vendas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(venda)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Venda> vendaList = vendaRepository.findAll();
        assertThat(vendaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllVendas() throws Exception {
        // Initialize the database
        vendaRepository.save(venda);

        // Get all the vendaList
        restVendaMockMvc.perform(get("/api/vendas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venda.getId())))
            .andExpect(jsonPath("$.[*].idPedido").value(hasItem(DEFAULT_ID_PEDIDO.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())));
    }

    @Test
    public void getVenda() throws Exception {
        // Initialize the database
        vendaRepository.save(venda);

        // Get the venda
        restVendaMockMvc.perform(get("/api/vendas/{id}", venda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(venda.getId()))
            .andExpect(jsonPath("$.idPedido").value(DEFAULT_ID_PEDIDO.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()));
    }

    @Test
    public void getNonExistingVenda() throws Exception {
        // Get the venda
        restVendaMockMvc.perform(get("/api/vendas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateVenda() throws Exception {
        // Initialize the database
        vendaRepository.save(venda);
        int databaseSizeBeforeUpdate = vendaRepository.findAll().size();

        // Update the venda
        Venda updatedVenda = vendaRepository.findOne(venda.getId());
        updatedVenda
            .idPedido(UPDATED_ID_PEDIDO)
            .valor(UPDATED_VALOR);

        restVendaMockMvc.perform(put("/api/vendas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVenda)))
            .andExpect(status().isOk());

        // Validate the Venda in the database
        List<Venda> vendaList = vendaRepository.findAll();
        assertThat(vendaList).hasSize(databaseSizeBeforeUpdate);
        Venda testVenda = vendaList.get(vendaList.size() - 1);
        assertThat(testVenda.getIdPedido()).isEqualTo(UPDATED_ID_PEDIDO);
        assertThat(testVenda.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    public void updateNonExistingVenda() throws Exception {
        int databaseSizeBeforeUpdate = vendaRepository.findAll().size();

        // Create the Venda

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVendaMockMvc.perform(put("/api/vendas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(venda)))
            .andExpect(status().isCreated());

        // Validate the Venda in the database
        List<Venda> vendaList = vendaRepository.findAll();
        assertThat(vendaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteVenda() throws Exception {
        // Initialize the database
        vendaRepository.save(venda);
        int databaseSizeBeforeDelete = vendaRepository.findAll().size();

        // Get the venda
        restVendaMockMvc.perform(delete("/api/vendas/{id}", venda.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Venda> vendaList = vendaRepository.findAll();
        assertThat(vendaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venda.class);
        Venda venda1 = new Venda();
        venda1.setId("id1");
        Venda venda2 = new Venda();
        venda2.setId(venda1.getId());
        assertThat(venda1).isEqualTo(venda2);
        venda2.setId("id2");
        assertThat(venda1).isNotEqualTo(venda2);
        venda1.setId(null);
        assertThat(venda1).isNotEqualTo(venda2);
    }
}
