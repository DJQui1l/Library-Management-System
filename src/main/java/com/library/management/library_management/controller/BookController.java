package com.library.management.library_management.controller;
import com.library.management.library_management.model.Book;
import com.library.management.library_management.service.BookService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/books")

public class BookController {

    private final BookService bookService;

    //create a bookService object
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    //create a book
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    //get all books
    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    //Get book by ID;
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id){
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Integer id, @RequestBody Book book){
        return ResponseEntity.ok(
                bookService.updateBookById(id, book)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Integer id){
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();

    }


}
