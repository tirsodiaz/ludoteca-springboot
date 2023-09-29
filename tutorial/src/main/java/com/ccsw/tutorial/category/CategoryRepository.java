package com.ccsw.tutorial.category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.tutorial.category.model.Category;

/**
 * @author ccsw
 *
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}