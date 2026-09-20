package com.library.management.library_management.service;
import com.library.management.library_management.repository.AuthorRepository;
import com.library.management.library_management.repository.PublisherRepository;
import com.library.management.library_management.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    BookRepository bookRepository;
    AuthorRepository authorRepository;
    PublisherRepository publisherRepository;

    public DatabaseService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository){
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public void deleteAllRecords() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        publisherRepository.deleteAll();
    }

}
