package br.unifacisa.decentralized.web.rest;

import br.unifacisa.decentralized.domain.Pedido;
import br.unifacisa.decentralized.security.AuthoritiesConstants;
import br.unifacisa.decentralized.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static br.unifacisa.decentralized.config.Constants.MICROSERVICE_PEDIDO;

/**
 * REST controller for managing Pedido.
 */
@RestController
@RequestMapping("/api")
public class PedidoResource {

    public PedidoResource() {
    }


    @PostMapping("/pedidos")
    @Timed
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        try{
            HttpEntity<String> httpEntity = new HttpEntity<>(createJSONPedido(pedido), httpHeaders);
            return restTemplate.exchange(MICROSERVICE_PEDIDO + "/api/pedidos", HttpMethod.POST, httpEntity, Pedido.class);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/pedidos")
    @Timed
    public ResponseEntity<Pedido> updatePedido(@RequestBody Pedido pedido) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        try{
            HttpEntity<String> httpEntity = new HttpEntity<>(createJSONPedido(pedido), httpHeaders);
            return restTemplate.exchange(MICROSERVICE_PEDIDO + "/api/pedidos", HttpMethod.PUT, httpEntity, Pedido.class);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pedidos")
    @Timed
    public List<Pedido> getAllPedidos() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Pedido[]> pedido = restTemplate.exchange(MICROSERVICE_PEDIDO + "/api/pedidos", HttpMethod.GET, httpEntity, Pedido[].class);
        return Arrays.asList(pedido.getBody());
    }

    @GetMapping("/pedidos/{id}")
    @Timed
    public ResponseEntity<Pedido> getPedido(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(MICROSERVICE_PEDIDO + "/api/pedidos/"+id, HttpMethod.GET, httpEntity, Pedido.class);
    }

    @DeleteMapping("/pedidos/{id}")
    @Timed
    public ResponseEntity<Void> deletePedido(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(MICROSERVICE_PEDIDO + "/api/pedidos/"+id, HttpMethod.DELETE, httpEntity, Void.class);
    }

    @GetMapping("/pedidos/descricao/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> getDescricaoPedido(@PathVariable String id){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(MICROSERVICE_PEDIDO + "/api/pedidos/descricao/"+id, HttpMethod.GET, httpEntity, String.class);
    }

    private String createJSONPedido(Pedido pedido) throws JSONException{
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", pedido.getId() == null ? JSONObject.NULL : pedido.getId());
        jsonObject.put("idUsuario", pedido.getIdUsuario());
        jsonObject.put("idProduto", pedido.getIdProduto());

        return jsonObject.toString();
    }
}
