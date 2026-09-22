package com.library.management.library_management.service;
import com.library.management.library_management.model.Category;
import com.library.management.library_management.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;



    public CategoryService(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository;
    }

    //create a category
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    //get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // get a category
    public Optional<Category> getCategoryById(Integer id){
        return categoryRepository.findById(id);
    }

    //update a category
    public Category updateCategory(Integer id, Category categoryDetails){

        return categoryRepository.findById(id)
                .map(category -> {
                    if (categoryDetails.getName() != null){
                        category.setName(categoryDetails.getName());
                    }
                    return categoryRepository.save(category);
                }).orElse(null);
    }


    //delete a category
    public void deleteCategory(Integer id){
        categoryRepository.deleteById(id);
    }

}
