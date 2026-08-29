package com.library.management.library_management.controller;

import com.library.management.library_management.model.Author;
import com.library.management.library_management.service.AuthorService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
// or import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorController {

    private final AuthorService authorService;

    //create an authorService object that handles the business logic for the controller to use.
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    //create an author

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorService.createAuthor(author);
    }

    //get all authors
    @GetMapping
    public List<Author> getAllAuthors(){
        return authorService.getAllAuthors();

    }

    //get an author by ID
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Integer id){
        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Integer id){
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

}
