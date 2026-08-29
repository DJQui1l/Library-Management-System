package com.library.management.library_management.repository;

import com.library.management.library_management.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

//responsible for database access
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    // this repository is responsible for the Publisher entity, and the Publisher's ID is an Integer.
    // It extends JpaRepository, which provides CRUD operations and more.

}
