package com.library.management.library_management.controller;
import com.library.management.library_management.model.Category;
import com.library.management.library_management.service.CategoryService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    // init the CategoriesService class to use it's functions
    private final CategoryService categoryService;


    //create a constructor
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    // create a category
    @PostMapping
    public Category createCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }




}
