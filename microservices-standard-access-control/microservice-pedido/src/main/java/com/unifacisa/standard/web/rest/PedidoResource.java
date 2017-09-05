package com.unifacisa.standard.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.unifacisa.standard.domain.Pedido;

import com.unifacisa.standard.domain.Produto;
import com.unifacisa.standard.domain.Usuario;
import com.unifacisa.standard.repository.PedidoRepository;
import com.unifacisa.standard.security.AuthoritiesConstants;
import com.unifacisa.standard.security.SecurityUtils;
import com.unifacisa.standard.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pedido.
 */
@RestController
@RequestMapping("/api")
public class PedidoResource {

    private final Logger log = LoggerFactory.getLogger(PedidoResource.class);

    private static final String ENTITY_NAME = "pedido";

    private final PedidoRepository pedidoRepository;
    public PedidoResource(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * POST  /pedidos : Create a new pedido.
     *
     * @param pedido the pedido to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pedido, or with status 400 (Bad Request) if the pedido has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pedidos")
    @Timed
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) throws URISyntaxException {
        log.debug("REST request to save Pedido : {}", pedido);
        if (pedido.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pedido cannot already have an ID")).body(null);
        }
        Pedido result = pedidoRepository.save(pedido);
        return ResponseEntity.created(new URI("/api/pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pedidos : Updates an existing pedido.
     *
     * @param pedido the pedido to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pedido,
     * or with status 400 (Bad Request) if the pedido is not valid,
     * or with status 500 (Internal Server Error) if the pedido couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pedidos")
    @Timed
    public ResponseEntity<Pedido> updatePedido(@RequestBody Pedido pedido) throws URISyntaxException {
        log.debug("REST request to update Pedido : {}", pedido);
        if (pedido.getId() == null) {
            return createPedido(pedido);
        }
        Pedido result = pedidoRepository.save(pedido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pedido.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pedidos : get all the pedidos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pedidos in body
     */
    @GetMapping("/pedidos")
    @Timed
    public List<Pedido> getAllPedidos() {
        log.debug("REST request to get all Pedidos");
        return pedidoRepository.findAll();
        }

    /**
     * GET  /pedidos/:id : get the "id" pedido.
     *
     * @param id the id of the pedido to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pedido, or with status 404 (Not Found)
     */
    @GetMapping("/pedidos/{id}")
    @Timed
    public ResponseEntity<Pedido> getPedido(@PathVariable String id) {
        log.debug("REST request to get Pedido : {}", id);
        Pedido pedido = pedidoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pedido));
    }

    /**
     * DELETE  /pedidos/:id : delete the "id" pedido.
     *
     * @param id the id of the pedido to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pedidos/{id}")
    @Timed
    public ResponseEntity<Void> deletePedido(@PathVariable String id) {
        log.debug("REST request to delete Pedido : {}", id);
        pedidoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    @GetMapping("/pedidos/descricao/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> getDescricaoPedido(@PathVariable String id){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ SecurityUtils.getCurrentUserJWT());
        HttpEntity<String> request = new HttpEntity<>(headers);

        try{
            Pedido pedido = pedidoRepository.findOne(id);
            Usuario usuario = restTemplate.exchange("http://localhost:8080/api/users/"+pedido.getIdUsuario(), HttpMethod.GET, request, Usuario.class).getBody();
            Produto produto = restTemplate.exchange("http://localhost:8081/api/produtos/"+pedido.getIdProduto(), HttpMethod.GET, request, Produto.class).getBody();

            return ResponseEntity.ok("Código do Pedido: "+pedido.getId()+
                "\n Nome do Usuário: "+usuario.getFirstName()+
                "\n Nome do Produto: "+produto.getNome());

        }catch(Exception ex){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
