package com.library.management.library_management.service;

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

    public BookService(BookRepository bookRepository,
                       S3Service s3Service
    ){
        this.bookRepository = bookRepository;
        this.s3Service = s3Service;
    }

    // create a book
    public Book createBook(Book book,
                           MultipartFile cover
    ) throws IOException {

        String key = s3Service.uploadFile(cover);

        book.setCoverImageKey(key);

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
                    if (bookDetails.getAuthor_id() != null){
                        book.setAuthor_id(bookDetails.getAuthor_id());
                    }
                    if (bookDetails.getPublisher() != null){
                        book.setPublisher(bookDetails.getPublisher());
                    }
                    if (bookDetails.getPublisher_id() != null){
                        book.setPublisher_id(bookDetails.getPublisher_id());
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
