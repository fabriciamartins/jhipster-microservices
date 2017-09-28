package br.unifacisa.decentralized.web.rest;

import br.unifacisa.decentralized.domain.Produto;
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

import static br.unifacisa.decentralized.config.Constants.MICROSERVICE_PRODUTO;

/**
 * REST controller for managing Produto.
 */
@RestController
@RequestMapping("/api")
public class ProdutoResource {

    public ProdutoResource(){ }


    @PostMapping("/produtos")
    @Timed
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        try{
            HttpEntity<String> httpEntity = new HttpEntity<>(createJSONProduto(produto), httpHeaders);
            return restTemplate.exchange(MICROSERVICE_PRODUTO + "/api/produtos", HttpMethod.POST, httpEntity, Produto.class);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/produtos")
    @Timed
    public ResponseEntity<Produto> updateProduto(@RequestBody Produto produto) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        try{
            HttpEntity<String> httpEntity = new HttpEntity<>(createJSONProduto(produto), httpHeaders);
            return restTemplate.exchange(MICROSERVICE_PRODUTO + "/api/produtos", HttpMethod.PUT, httpEntity, Produto.class);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/produtos")
    @Timed
    public List<Produto> getAllProdutos() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<Produto[]> produto = restTemplate.exchange(MICROSERVICE_PRODUTO + "/api/produtos", HttpMethod.GET, httpEntity, Produto[].class);

        return Arrays.asList(produto.getBody());
    }


    @GetMapping("/produtos/{id}")
    @Timed
    public ResponseEntity<Produto> getProduto(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(MICROSERVICE_PRODUTO + "/api/produtos/"+id, HttpMethod.GET, httpEntity, Produto.class);
    }


    @DeleteMapping("/produtos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduto(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(MICROSERVICE_PRODUTO + "/api/produtos/"+id, HttpMethod.DELETE, httpEntity, Void.class);
    }

    private String createJSONProduto(Produto produto) throws JSONException{
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", produto.getId() == null ? JSONObject.NULL : produto.getId());
        jsonObject.put("nome", produto.getNome());
        jsonObject.put("preco", produto.getPreco());

        return jsonObject.toString();
    }
}
