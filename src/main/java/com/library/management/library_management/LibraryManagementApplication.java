package com.library.management.library_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryManagementApplication {

	public static void main(String[] args) {
		// Start the application - Database initialization is handled by DatabaseManager's afterPropertiesSet()
		SpringApplication.run(LibraryManagementApplication.class, args);
	}




}

