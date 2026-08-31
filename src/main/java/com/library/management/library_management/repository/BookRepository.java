package com.library.management.library_management.repository;

import com.library.management.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

//responsible for database access
public interface BookRepository extends JpaRepository<Book, Integer> {
    //this repository is responsible for the Book entity, and the Book's ID is an Integer.
    //It extends JpaRepository, which provides CRUD operations and more.

}
