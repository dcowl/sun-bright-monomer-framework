package org.sun.bright.repository.nosql.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class SunMongoTemplate {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Object save) {
        mongoTemplate.save(save);
    }
    
}
