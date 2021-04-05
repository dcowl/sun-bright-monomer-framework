package org.sun.bright.repository.nosql.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SunMongoRepository extends MongoRepository<String, Object> {



}
