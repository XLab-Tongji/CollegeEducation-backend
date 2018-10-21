package org.lab409.service;

import org.lab409.entity.ResourceEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ResourceElasticSearchRepo extends ElasticsearchRepository<ResourceEntity, Integer> {

}
