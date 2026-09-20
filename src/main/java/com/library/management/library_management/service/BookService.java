package com.library.management.library_management.service;

import com.library.management.library_management.model.Author;
import com.library.management.library_management.repository.AuthorRepository;
import com.library.management.library_management.model.Publisher;
import com.library.management.library_management.repository.PublisherRepository;
import com.library.management.library_management.model.Book;
import com.library.management.library_management.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final S3Service s3Service;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository,
                       S3Service s3Service,
                       AuthorRepository authorRepository,
                       PublisherRepository publisherRepository
    ){
        this.bookRepository = bookRepository;
        this.s3Service = s3Service;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    // create a book
    public Book createBook(Book book,
                           MultipartFile cover
    ) throws IOException {

        String key = s3Service.uploadFile(cover);

        book.setCoverImageKey(key);

        //find author by id
        Author author = authorRepository.findById(book.getAuthorEntity().getId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found with id: " + book.getAuthorEntity().getId()));
        book.setAuthor(author.getName());

        //find publisher by id
        Publisher publisher = publisherRepository.findById(book.getPublisherEntity().getId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found with id: " + book.getPublisherEntity().getId()));
        book.setPublisher(publisher.getName());

        return bookRepository.save(book);
    }

    //get all books
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    //get book by id
    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    //update book by id

    public Book updateBookById(Integer id, Book bookDetails, MultipartFile cover) {
        return bookRepository.findById(id)
                .map(book -> {

                    //check if the updated book details are not null before updating
                    if (bookDetails.getTitle() != null){
                        book.setTitle(bookDetails.getTitle());
                    }
                    if (bookDetails.getCategory() != null){
                        book.setCategory(bookDetails.getCategory());
                    }
                    if (bookDetails.getAuthor() != null){
                        book.setAuthor(bookDetails.getAuthor());
                    }
                    // Collect validation errors
                    StringBuilder errors = new StringBuilder();

                    if (bookDetails.getAuthorEntity().getId() != null){
                        //find author by id
                        Author author = authorRepository.findById(bookDetails.getAuthorEntity().getId())
                                .orElse(null);
                        if (author == null) {
                            errors.append("Author not found with id: ").append(bookDetails.getAuthorEntity().getId()).append(". ");
                        } else {
                            // set author entity id and name for book's foreign key relationship to it.
                            book.getAuthorEntity().setId(author.getId());
                            book.setAuthor(author.getName());
                        }
                    }

                    if (bookDetails.getPublisher() != null){
                        book.setPublisher(bookDetails.getPublisher());
                    }

                    if (bookDetails.getPublisherEntity().getId() != null){
                        //find publisher by id
                        Publisher publisher = publisherRepository.findById(bookDetails.getPublisherEntity().getId())
                                .orElse(null);
                        if (publisher == null) {
                            errors.append("Publisher not found with id: ").append(bookDetails.getPublisherEntity().getId()).append(". ");
                        } else {
                            // set publisher entity id and name for book's foreign key relationship to it.
                            book.getPublisherEntity().setId(publisher.getId());
                            book.setPublisher(publisher.getName());
                        }
                    }

                    // Throw combined error if any validation failed
                    if (errors.length() > 0) {
                        throw new IllegalArgumentException(errors.toString().trim());
                    }

                    if (cover != null){
                        try {
                            String key = s3Service.uploadFile(cover);
                            book.setCoverImageKey(key);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload cover image", e);
                        }
                    }


                    return bookRepository.save(book);
                })
                .orElse(null);

    }
    //delete book by id
    public void deleteBookById(Integer id){
        bookRepository.deleteById(id);
    }



}
