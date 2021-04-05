package org.sun.bright.repository.nosql.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class SunElasticsearchTemplate {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

}
