package com.library.management.library_management.service;

import com.library.management.library_management.model.Category;
import com.library.management.library_management.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category fictionCategory;
    private Category nonFictionCategory;

    @BeforeEach
    void setUp() {
        fictionCategory = new Category("Fiction");
        fictionCategory.setId(1);

        nonFictionCategory = new Category("Non-Fiction");
        nonFictionCategory.setId(2);
    }

    @Test
    void createCategory_ShouldReturnSavedCategory() {
        // Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(fictionCategory);

        // Act
        Category result = categoryService.createCategory(fictionCategory);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Fiction", result.getName());
        verify(categoryRepository, times(1)).save(fictionCategory);

        System.out.println("Category created: " + result.getName() + " (ID: " + result.getId() + ")");
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(fictionCategory, nonFictionCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> result = categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Fiction", result.get(0).getName());
        assertEquals("Non-Fiction", result.get(1).getName());
        verify(categoryRepository, times(1)).findAll();

        System.out.println("All categories retrieved: " + result.size() + " categories found");
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        // Arrange
        when(categoryRepository.findById(1)).thenReturn(Optional.of(fictionCategory));

        // Act
        Optional<Category> result = categoryService.getCategoryById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Fiction", result.get().getName());
        verify(categoryRepository, times(1)).findById(1);

        System.out.println("Category found: ID 1 - " + result.get().getName());
    }

    @Test
    void getCategoryById_WhenCategoryNotFound_ShouldReturnEmpty() {
        // Arrange
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Category> result = categoryService.getCategoryById(999);

        // Assert
        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findById(999);

        System.out.println("Category not found: ID 999");
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() {
        // Arrange
        Category existingCategory = new Category("Fiction");
        existingCategory.setId(1);

        Category categoryDetails = new Category("Science Fiction");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        // Act
        Category result = categoryService.updateCategory(1, categoryDetails);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Science Fiction", result.getName());
        verify(categoryRepository, times(1)).findById(1);
        verify(categoryRepository, times(1)).save(existingCategory);

        System.out.println("Category updated: ID 1 - " + result.getName());
    }

    @Test
    void updateCategory_WhenCategoryNotFound_ShouldReturnNull() {
        // Arrange
        Category categoryDetails = new Category("Unknown Category");

        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Category result = categoryService.updateCategory(999, categoryDetails);

        // Assert
        assertNull(result);
        verify(categoryRepository, times(1)).findById(999);
        verify(categoryRepository, never()).save(any());

        System.out.println("Category not found for update: ID 999");
    }

    @Test
    void deleteCategory_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(categoryRepository).deleteById(1);

        // Act
        categoryService.deleteCategory(1);

        // Assert
        verify(categoryRepository, times(1)).deleteById(1);

        System.out.println("Category deleted: ID 1");
    }
}
