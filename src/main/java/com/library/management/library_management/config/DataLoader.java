package com.library.management.library_management.config;

import com.library.management.library_management.model.Author;
import com.library.management.library_management.model.Book;
import com.library.management.library_management.model.Publisher;
import com.library.management.library_management.repository.AuthorRepository;
import com.library.management.library_management.repository.BookRepository;
import com.library.management.library_management.repository.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public DataLoader(AuthorRepository authorRepository, PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only load data if repositories are empty
        if (authorRepository.count() == 0 && publisherRepository.count() == 0 && bookRepository.count() == 0) {
            loadAuthors();
            loadPublishers();
            loadBooks();
        }
    }

    private void loadAuthors() {
        Author author1 = new Author(
            "John C. Maxwell",
            "+1-404-555-1234",
            "john.maxwell@example.com",
            "Atlanta, Georgia, USA",
            "Leadership expert, speaker, and author of over 70 books"
        );
        authorRepository.save(author1);

        Author author2 = new Author(
            "Rick Warren",
            "+1-949-555-2345",
            "rick.warren@example.com",
            "Lake Forest, California, USA",
            "Pastor and author of The Purpose Driven Life"
        );
        authorRepository.save(author2);

        Author author3 = new Author(
            "Stephen R. Covey",
            "+1-801-555-3456",
            "stephen.covey@example.com",
            "Provo, Utah, USA",
            "Author of The 7 Habits of Highly Effective People"
        );
        authorRepository.save(author3);

        Author author4 = new Author(
            "C.S. Lewis",
            "+44-20-4567-8901",
            "cs.lewis@example.com",
            "Oxford, England",
            "Christian apologist and author of The Chronicles of Narnia"
        );
        authorRepository.save(author4);

        Author author5 = new Author(
            "Dale Carnegie",
            "+1-212-555-4567",
            "dale.carnegie@example.com",
            "New York, USA",
            "Author of How to Win Friends and Influence People"
        );
        authorRepository.save(author5);

        System.out.println("Loaded 5 authors");
    }

    private void loadPublishers() {
        Publisher publisher1 = new Publisher(
            "Bloomsbury Publishing",
            "50 Bedford Square, London, UK",
            "+44-20-7631-1626"
        );
        publisherRepository.save(publisher1);

        Publisher publisher2 = new Publisher(
            "Penguin Random House",
            "1745 Broadway, New York, USA",
            "+1-212-782-9000"
        );
        publisherRepository.save(publisher2);

        Publisher publisher3 = new Publisher(
            "HarperCollins",
            "195 Broadway, New York, USA",
            "+1-212-207-7000"
        );
        publisherRepository.save(publisher3);

        Publisher publisher4 = new Publisher(
            "Macmillan Publishers",
            "120 Broadway, New York, USA",
            "+1-212-981-8000"
        );
        publisherRepository.save(publisher4);

        Publisher publisher5 = new Publisher(
            "Simon & Schuster",
            "1230 Avenue of the Americas, New York, USA",
            "+1-212-698-7000"
        );
        publisherRepository.save(publisher5);

        System.out.println("Loaded 5 publishers");
    }

    private void loadBooks() {
        // Get the first author and publisher for linking
        Author author1 = authorRepository.findAll().get(0);
        Author author2 = authorRepository.findAll().get(1);
        Author author3 = authorRepository.findAll().get(2);
        Author author4 = authorRepository.findAll().get(3);
        Author author5 = authorRepository.findAll().get(4);

        Publisher publisher1 = publisherRepository.findAll().get(0);
        Publisher publisher2 = publisherRepository.findAll().get(1);
        Publisher publisher3 = publisherRepository.findAll().get(2);
        Publisher publisher4 = publisherRepository.findAll().get(3);
        Publisher publisher5 = publisherRepository.findAll().get(4);

        Book book1 = new Book(
            "The 21 Irrefutable Laws of Leadership",
            "Leadership",
            author1.getName(),
            author1,
            publisher1.getName(),
            publisher1
        );
        bookRepository.save(book1);

        Book book2 = new Book(
            "Developing the Leader Within You",
            "Leadership",
            author1.getName(),
            author1,
            publisher1.getName(),
            publisher1
        );
        bookRepository.save(book2);

        Book book3 = new Book(
            "The Purpose Driven Life",
            "Christian Living",
            author2.getName(),
            author2,
            publisher2.getName(),
            publisher2
        );
        bookRepository.save(book3);

        Book book4 = new Book(
            "The Purpose Driven Church",
            "Christian Ministry",
            author2.getName(),
            author2,
            publisher2.getName(),
            publisher2
        );
        bookRepository.save(book4);

        Book book5 = new Book(
            "The 7 Habits of Highly Effective People",
            "Self-Help",
            author3.getName(),
            author3,
            publisher3.getName(),
            publisher3
        );
        bookRepository.save(book5);

        Book book6 = new Book(
            "First Things First",
            "Productivity",
            author3.getName(),
            author3,
            publisher3.getName(),
            publisher3
        );
        bookRepository.save(book6);

        Book book7 = new Book(
            "Mere Christianity",
            "Christian Apologetics",
            author4.getName(),
            author4,
            publisher4.getName(),
            publisher4
        );
        bookRepository.save(book7);

        Book book8 = new Book(
            "The Chronicles of Narnia",
            "Christian Fiction",
            author4.getName(),
            author4,
            publisher4.getName(),
            publisher4
        );
        bookRepository.save(book8);

        Book book9 = new Book(
            "How to Win Friends and Influence People",
            "Self-Help",
            author5.getName(),
            author5,
            publisher5.getName(),
            publisher5
        );
        bookRepository.save(book9);

        Book book10 = new Book(
            "How to Stop Worrying and Start Living",
            "Self-Help",
            author5.getName(),
            author5,
            publisher5.getName(),
            publisher5
        );
        bookRepository.save(book10);

        System.out.println("Loaded 10 books");
    }
}
