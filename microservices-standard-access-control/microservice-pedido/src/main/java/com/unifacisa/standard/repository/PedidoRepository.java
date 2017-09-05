package com.unifacisa.standard.repository;

import com.unifacisa.standard.domain.Pedido;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Pedido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {

}
