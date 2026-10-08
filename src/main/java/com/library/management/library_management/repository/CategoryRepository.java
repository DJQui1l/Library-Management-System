package com.library.management.library_management.repository;

import com.library.management.library_management.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

//responsible for database access
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
