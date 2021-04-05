package org.sun.bright.repository.nosql.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SunNeo4jRepository extends Neo4jRepository<Object, Long> {



}
