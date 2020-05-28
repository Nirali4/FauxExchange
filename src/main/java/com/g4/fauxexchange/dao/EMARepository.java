package com.g4.fauxexchange.dao;

import java.util.List;

import com.g4.fauxexchange.model.EMAResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EMARepository extends MongoRepository<EMAResult, String> {

    @Query(value = "{ 'code': ?0 }", fields="{ 'name': 1, 'code': 1, 'result': 1 }")
	EMAResult findByCode(String code);

    @Query(value = "{}", fields="{ 'name': 1, 'code': 1, 'result': { $slice: -1 } }")
    List<EMAResult> findAllWithRecentPrices();

}