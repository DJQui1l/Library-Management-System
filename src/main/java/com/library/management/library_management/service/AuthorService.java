package com.library.management.library_management.service;

import com.library.management.library_management.model.Author;
import com.library.management.library_management.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class AuthorService {
    //the service creates the logic for the Author entity to be saved, retrieved, updated, and deleted in the repository.

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    //create an Author
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    //get all authors
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // get an author by ID
    public Optional<Author> getAuthorById(Integer id) {
        return authorRepository.findById(id);
    }

    // Delete an Author
    public void deleteAuthorById(Integer id) {
        authorRepository.deleteById(id);
    }



}
