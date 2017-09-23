package com.unifacisa.standard.repository;

import com.unifacisa.standard.domain.Venda;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Venda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendaRepository extends MongoRepository<Venda, String> {

}
