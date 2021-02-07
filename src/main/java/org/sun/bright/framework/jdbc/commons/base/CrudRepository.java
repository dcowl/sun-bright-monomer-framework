package org.sun.bright.framework.jdbc.commons.base;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudRepository {


    default String save() {

        return "";
    }

}
