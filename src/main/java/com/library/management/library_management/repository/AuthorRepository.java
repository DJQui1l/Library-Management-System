package com.library.management.library_management.repository;

import com.library.management.library_management.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

//responsible for database access
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    // this repository is responsible for the Author entity, and the Author's ID is an Integer.
    // It extends JpaRepository, which provides CRUD operations and more.


}
