package com.library.management.library_management.controller;

import com.library.management.library_management.model.Author;
import com.library.management.library_management.service.AuthorService;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthorById(@PathVariable Integer id, @RequestBody Author authorDetails){
        Author updatedAuthor = authorService.updateAuthorById(id, authorDetails);
        if (updatedAuthor != null) {
            return ResponseEntity.ok(updatedAuthor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Integer id){
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }

}
