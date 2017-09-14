package com.unifacisa.decentralized.repository;

import com.unifacisa.decentralized.domain.Produto;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Produto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProdutoRepository extends MongoRepository<Produto, String> {

}
