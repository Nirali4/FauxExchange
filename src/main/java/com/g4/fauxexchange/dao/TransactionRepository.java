package com.g4.fauxexchange.dao;

import java.util.List;

import com.g4.fauxexchange.model.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    @Query(value = "{ 'code': ?0 }")
	public List<Transaction> findByCode(String code);

    @Query(value = "{ 'userId': ?0 }")
    public List<Transaction> findByUserId(String userId);
    
}