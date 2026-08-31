package com.library.management.library_management;

import com.library.management.library_management.model.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import com.library.management.library_management.repository.BookRepository;

@SpringBootApplication
public class LibraryManagementApplication {

	public static void main(String[] args) {
		// Start the application - Database initialization is handled by DatabaseManager's afterPropertiesSet()
		SpringApplication.run(LibraryManagementApplication.class, args);

	}




}

