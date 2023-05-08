package com.ccsw.tutorialcategory.category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.tutorialcategory.category.model.Category;

/**
 * @author ccsw
 *
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}