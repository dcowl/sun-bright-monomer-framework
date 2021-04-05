package org.sun.bright.repository.nosql.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;

public class SunNeo4jTemplate {

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    public void save(Object o) {
        neo4jTemplate.save(o);
    }

    public void finds() {
        neo4jTemplate.findAll(String.class);
    }

}
