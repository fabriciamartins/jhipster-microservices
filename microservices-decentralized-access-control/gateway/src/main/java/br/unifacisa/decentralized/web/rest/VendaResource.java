package br.unifacisa.decentralized.web.rest;

import br.unifacisa.decentralized.domain.Venda;
import br.unifacisa.decentralized.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static br.unifacisa.decentralized.config.Constants.MICROSERVICE_VENDA;

/**
 * REST controller for managing Venda.
 */
@RestController
@RequestMapping("/api")
public class VendaResource {

    public VendaResource() {

    }

    @PostMapping("/vendas")
    @Timed
    public ResponseEntity<Venda> createVenda(@RequestBody Venda venda) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        try{
            HttpEntity<String> httpEntity = new HttpEntity<>(createJSONVenda(venda), httpHeaders);
            return restTemplate.exchange(MICROSERVICE_VENDA + "/api/vendas", HttpMethod.POST, httpEntity, Venda.class);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/vendas")
    @Timed
    public ResponseEntity<Venda> updateVenda(@RequestBody Venda venda) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        try{
            HttpEntity<String> httpEntity = new HttpEntity<>(createJSONVenda(venda), httpHeaders);
            return restTemplate.exchange(MICROSERVICE_VENDA + "/api/vendas", HttpMethod.PUT, httpEntity, Venda.class);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/vendas")
    @Timed
    public List<Venda> getAllVendas() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<Venda[]> produto = restTemplate.exchange(MICROSERVICE_VENDA + "/api/vendas", HttpMethod.GET, httpEntity, Venda[].class);

        return Arrays.asList(produto.getBody());
    }

    @GetMapping("/vendas/{id}")
    @Timed
    public ResponseEntity<Venda> getVenda(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(MICROSERVICE_VENDA + "/api/vendas/"+id, HttpMethod.GET, httpEntity, Venda.class);
    }

    @DeleteMapping("/vendas/{id}")
    @Timed
    public ResponseEntity<Void> deleteVenda(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(MICROSERVICE_VENDA + "/api/vendas/"+id, HttpMethod.DELETE, httpEntity, Void.class);
    }

    private String createJSONVenda(Venda venda) throws JSONException{
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", venda.getId() == null ? JSONObject.NULL : venda.getId());
        jsonObject.put("idPedido", venda.getIdPedido());
        jsonObject.put("valor", venda.getValor());

        return jsonObject.toString();
    }
}
