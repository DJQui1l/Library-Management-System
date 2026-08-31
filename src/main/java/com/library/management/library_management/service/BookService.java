package com.library.management.library_management.service;

import com.library.management.library_management.model.Book;
import com.library.management.library_management.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    // create a book
    public Book createBook(Book book){
        return bookRepository.save(book);
    }

    //get all books
    public List<Book> getAllBooks(){
        return bookRepository.findAll();


    }

    //get book by id
    public Optional<Book> getBookById(Integer id){
        return bookRepository.findById(id);
    }

    //delete book by id
    public void deleteBookById(Integer id){
        bookRepository.deleteById(id);
    }



}
