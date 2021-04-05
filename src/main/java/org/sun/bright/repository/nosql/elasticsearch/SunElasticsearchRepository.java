package org.sun.bright.repository.nosql.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SunElasticsearchRepository extends ElasticsearchRepository<Object, Long> {


}
