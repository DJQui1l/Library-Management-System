package com.library.management.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.library_management.model.Category;
import com.library.management.library_management.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category("Fiction");
        testCategory.setId(1);
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        // Arrange
        when(categoryService.createCategory(any(Category.class))).thenReturn(testCategory);

        String categoryJson = objectMapper.writeValueAsString(testCategory);

        // Act & Assert
        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fiction"));

        System.out.println("Category created: " + testCategory.getName());
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() throws Exception {
        // Arrange
        Category category2 = new Category("Non-Fiction");
        category2.setId(2);

        List<Category> categories = Arrays.asList(testCategory, category2);
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fiction"))
                .andExpect(jsonPath("$[1].name").value("Non-Fiction"));

        System.out.println("All categories retrieved: " + categories.size() + " categories found");
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() throws Exception {
        // Arrange
        when(categoryService.getCategoryById(1)).thenReturn(Optional.of(testCategory));

        // Act & Assert
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fiction"));

        System.out.println("Category found: ID 1 - " + testCategory.getName());
        verify(categoryService, times(1)).getCategoryById(1);
    }

    @Test
    void getCategoryById_WhenCategoryNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(categoryService.getCategoryById(999)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound());

        System.out.println("Category not found: ID 999");
        verify(categoryService, times(1)).getCategoryById(999);
    }

    @Test
    void updateCategoryById_WhenCategoryExists_ShouldReturnUpdatedCategory() throws Exception {
        // Arrange
        Category existingCategory = new Category("Fiction");
        existingCategory.setId(1);

        Category categoryDetails = new Category("Science Fiction");

        when(categoryService.updateCategory(eq(1), any(Category.class))).thenReturn(existingCategory);

        String categoryJson = objectMapper.writeValueAsString(categoryDetails);

        // Act & Assert
        mockMvc.perform(put("/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isOk());

        System.out.println("Category found and updated: ID 1");
        verify(categoryService, times(1)).updateCategory(eq(1), any(Category.class));
    }

    @Test
    void updateCategoryById_WhenCategoryNotFound_ShouldReturn404() throws Exception {
        // Arrange
        Category categoryDetails = new Category("Unknown Category");

        when(categoryService.updateCategory(eq(999), any(Category.class))).thenReturn(null);

        String categoryJson = objectMapper.writeValueAsString(categoryDetails);

        // Act & Assert
        mockMvc.perform(put("/categories/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isNotFound());

        System.out.println("Category not found for update: ID 999");
        verify(categoryService, times(1)).updateCategory(eq(999), any(Category.class));
    }

    @Test
    void deleteCategoryById_ShouldReturn204NoContent() throws Exception {
        // Arrange
        doNothing().when(categoryService).deleteCategory(1);

        // Act & Assert
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());

        System.out.println("Category deleted: ID 1");
        verify(categoryService, times(1)).deleteCategory(1);
    }

    @Test
    void create10CategoriesAtOnce_ShouldReturnCreatedCategories() throws Exception {
        // Arrange
        String[] names = {
            "Fiction",
            "Non-Fiction",
            "Science Fiction",
            "Fantasy",
            "Mystery",
            "Thriller",
            "Romance",
            "Horror",
            "Biography",
            "History"
        };

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            Category category = new Category(names[i]);
            category.setId(i + 1);

            when(categoryService.createCategory(any(Category.class))).thenReturn(category);

            String categoryJson = objectMapper.writeValueAsString(category);

            mockMvc.perform(post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(categoryJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(i + 1))
                    .andExpect(jsonPath("$.name").value(names[i]));

            System.out.println("Category created: " + category.getName() + " (ID: " + (i + 1) + ")");
        }

        System.out.println("Total categories created: 10");
        verify(categoryService, times(10)).createCategory(any(Category.class));
    }
}
