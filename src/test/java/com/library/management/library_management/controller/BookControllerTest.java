package com.library.management.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.library_management.model.Book;
import com.library.management.library_management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1);
        testBook.setTitle("The Purpose Driven Life");
        testBook.setCategory("Christian Living");
        testBook.setAuthor("Rick Warren");
        testBook.setAuthor_id(1);
        testBook.setPublisher("Zondervan");
        testBook.setPublisher_id(1);
        testBook.setCoverImageKey("books/cover.jpg");
    }

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        // Arrange
        when(bookService.createBook(any(Book.class), any())).thenReturn(testBook);

        String bookJson = objectMapper.writeValueAsString(testBook);
        MockMultipartFile bookPart = new MockMultipartFile("book", "", "application/json", bookJson.getBytes());
        MockMultipartFile coverPart = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "test image".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/books")
                .file(bookPart)
                .file(coverPart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Purpose Driven Life"))
                .andExpect(jsonPath("$.category").value("Christian Living"));

        System.out.println("Book created: " + testBook.getTitle());
        verify(bookService, times(1)).createBook(any(Book.class), any());
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        // Arrange
        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Mere Christianity");
        book2.setCategory("Theology");
        book2.setAuthor("C.S. Lewis");

        List<Book> books = Arrays.asList(testBook, book2);
        when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Purpose Driven Life"))
                .andExpect(jsonPath("$[1].title").value("Mere Christianity"));

        System.out.println("All books retrieved: " + books.size() + " books found");
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        // Arrange
        when(bookService.getBookById(1)).thenReturn(Optional.of(testBook));

        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Purpose Driven Life"));

        System.out.println("Book found: ID 1 - " + testBook.getTitle());
        verify(bookService, times(1)).getBookById(1);
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(bookService.getBookById(999)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());

        System.out.println("Book not found: ID 999");
        verify(bookService, times(1)).getBookById(999);
    }

    @Test
    void updateBookById_WhenBookExists_ShouldReturnUpdatedBook() throws Exception {
        // Arrange
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setCategory("Updated Category");
        updatedBook.setAuthor("Updated Author");

        when(bookService.updateBookById(eq(1), any(Book.class), any())).thenReturn(testBook);

        String bookJson = objectMapper.writeValueAsString(updatedBook);
        MockMultipartFile bookPart = new MockMultipartFile("book", "", "application/json", bookJson.getBytes());
        MockMultipartFile coverPart = new MockMultipartFile("cover", "new-cover.jpg", "image/jpeg", "new image".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/books/1")
                .file(bookPart)
                .file(coverPart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(status().isOk());

        System.out.println("Book found and updated: ID 1");
        verify(bookService, times(1)).updateBookById(eq(1), any(Book.class), any());
    }

    @Test
    void updateBookById_WhenBookNotFound_ShouldReturn404() throws Exception {
        // Arrange
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");

        when(bookService.updateBookById(eq(999), any(Book.class), any())).thenReturn(null);

        String bookJson = objectMapper.writeValueAsString(updatedBook);
        MockMultipartFile bookPart = new MockMultipartFile("book", "", "application/json", bookJson.getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/books/999")
                .file(bookPart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(status().isNotFound());

        System.out.println("Book not found for update: ID 999");
        verify(bookService, times(1)).updateBookById(eq(999), any(Book.class), any());
    }

    @Test
    void updateBookById_WithoutCover_ShouldReturnUpdatedBook() throws Exception {
        // Arrange
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");

        when(bookService.updateBookById(eq(1), any(Book.class), any())).thenReturn(testBook);

        String bookJson = objectMapper.writeValueAsString(updatedBook);
        MockMultipartFile bookPart = new MockMultipartFile("book", "", "application/json", bookJson.getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/books/1")
                .file(bookPart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(status().isOk());

        System.out.println("Book found and updated without cover: ID 1");
        verify(bookService, times(1)).updateBookById(eq(1), any(Book.class), any());
    }

    @Test
    void deleteBookById_ShouldReturn204NoContent() throws Exception {
        // Arrange
        doNothing().when(bookService).deleteBookById(1);

        // Act & Assert
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        System.out.println("Book deleted: ID 1");
        verify(bookService, times(1)).deleteBookById(1);
    }

    @Test
    void create10BooksAtOnce_ShouldReturnCreatedBooks() throws Exception {
        // Arrange
        String[] titles = {
            "The Purpose Driven Life",
            "Mere Christianity",
            "The Case for Christ",
            "Wild at Heart",
            "The Love Dare",
            "Heaven is for Real",
            "The Shack",
            "Jesus Calling",
            "The Prayer of Jabez",
            "The Five Love Languages"
        };

        String[] authors = {
            "Rick Warren",
            "C.S. Lewis",
            "Lee Strobel",
            "John Eldredge",
            "Stephen Kendrick",
            "Todd Burpo",
            "William Paul Young",
            "Sarah Young",
            "Bruce Wilkinson",
            "Gary Chapman"
        };

        String[] publishers = {
            "Zondervan",
            "HarperCollins",
            "Zondervan",
            "Thomas Nelson",
            "B&H Publishing",
            "Thomas Nelson",
            "Windblown Media",
            "Thomas Nelson",
            "Multnomah",
            "Northfield Publishing"
        };

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            book.setId(i + 1);
            book.setTitle(titles[i]);
            book.setCategory("Christian Living");
            book.setAuthor(authors[i]);
            book.setAuthor_id(i + 1);
            book.setPublisher(publishers[i]);
            book.setPublisher_id(i + 1);
            book.setCoverImageKey("books/cover" + (i + 1) + ".jpg");

            when(bookService.createBook(any(Book.class), any())).thenReturn(book);

            String bookJson = objectMapper.writeValueAsString(book);
            MockMultipartFile bookPart = new MockMultipartFile("book", "", "application/json", bookJson.getBytes());
            MockMultipartFile coverPart = new MockMultipartFile("cover", "cover" + (i + 1) + ".jpg", "image/jpeg", "test image".getBytes());

            mockMvc.perform(multipart("/api/books")
                    .file(bookPart)
                    .file(coverPart))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(i + 1))
                    .andExpect(jsonPath("$.title").value(titles[i]));

            System.out.println("Book created: " + book.getTitle() + " (ID: " + (i + 1) + ")");
        }

        System.out.println("Total books created: 10");
        verify(bookService, times(10)).createBook(any(Book.class), any());
    }
}
