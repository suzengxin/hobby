package com.tianlangcloud.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tianlangcloud.model.Know;

@Repository
public interface ESKnowDao extends ElasticsearchRepository<Know, String>{
	
}
