package com.ccsw.tutorialcategory.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorialcategory.category.model.Category;
import com.ccsw.tutorialcategory.category.model.CategoryDto;
import com.ccsw.tutorialcategory.exception.MyBadAdviceException;
import com.ccsw.tutorialcategory.exception.MyConflictAdviceException;
import com.ccsw.tutorialcategory.feignclient.GameClient;
import com.ccsw.tutorialcategory.model.GameDto;

import jakarta.transaction.Transactional;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GameClient gameClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Category get(Long id) {

        return this.categoryRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> findAll() {

        return (List<Category>) this.categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, CategoryDto dto) {

        Category category;

        if (id == null) {
            category = new Category();
        } else {
            category = this.get(id);
        }

        if (category == null)
            throw new MyBadAdviceException("Category id doesn't exist");

        category.setName(dto.getName()); // BeanUtils.copyProperties(dto, category, "id");
        this.categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        List<GameDto> games = gameClient.find(null, id, null);
        if (games.isEmpty() || games.size() == 0)
            this.categoryRepository.deleteById(id);
        else
            throw new MyConflictAdviceException("Category already used in game!");

    }

}