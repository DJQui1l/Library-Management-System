package com.library.management.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.library_management.model.Author;
import com.library.management.library_management.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author();
        testAuthor.setId(1);
        testAuthor.setName("Rick Warren");
        testAuthor.setPhone("555-1234");
        testAuthor.setEmail("rick@saddleback.com");
        testAuthor.setAddress("1 Saddleback Pkwy, Lake Forest, CA");
        testAuthor.setBio("American evangelical Christian pastor and author");
    }

    @Test
    void createAuthor_ShouldReturnCreatedAuthor() throws Exception {
        // Arrange
        when(authorService.createAuthor(any(Author.class))).thenReturn(testAuthor);

        String authorJson = objectMapper.writeValueAsString(testAuthor);

        // Act & Assert
        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rick Warren"))
                .andExpect(jsonPath("$.email").value("rick@saddleback.com"));

        System.out.println("Author created: " + testAuthor.getName());
        verify(authorService, times(1)).createAuthor(any(Author.class));
    }

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors() throws Exception {
        // Arrange
        Author author2 = new Author();
        author2.setId(2);
        author2.setName("C.S. Lewis");
        author2.setPhone("555-5678");
        author2.setEmail("cslewis@oxford.ac.uk");
        author2.setAddress("Oxford, UK");
        author2.setBio("British writer and lay theologian");

        List<Author> authors = Arrays.asList(testAuthor, author2);
        when(authorService.getAllAuthors()).thenReturn(authors);

        // Act & Assert
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rick Warren"))
                .andExpect(jsonPath("$[1].name").value("C.S. Lewis"));

        System.out.println("All authors retrieved: " + authors.size() + " authors found");
        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void getAuthorById_WhenAuthorExists_ShouldReturnAuthor() throws Exception {
        // Arrange
        when(authorService.getAuthorById(1)).thenReturn(Optional.of(testAuthor));

        // Act & Assert
        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rick Warren"));

        System.out.println("Author found: ID 1 - " + testAuthor.getName());
        verify(authorService, times(1)).getAuthorById(1);
    }

    @Test
    void getAuthorById_WhenAuthorNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(authorService.getAuthorById(999)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/authors/999"))
                .andExpect(status().isNotFound());

        System.out.println("Author not found: ID 999");
        verify(authorService, times(1)).getAuthorById(999);
    }

    @Test
    void updateAuthorById_WhenAuthorExists_ShouldReturnUpdatedAuthor() throws Exception {
        // Arrange
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Rick Warren Jr.");
        updatedAuthor.setPhone("555-9999");
        updatedAuthor.setEmail("rick.jr@saddleback.com");

        when(authorService.updateAuthorById(eq(1), any(Author.class))).thenReturn(testAuthor);

        String authorJson = objectMapper.writeValueAsString(updatedAuthor);

        // Act & Assert
        mockMvc.perform(put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson))
                .andExpect(status().isOk());

        System.out.println("Author found and updated: ID 1");
        verify(authorService, times(1)).updateAuthorById(eq(1), any(Author.class));
    }

    @Test
    void updateAuthorById_WhenAuthorNotFound_ShouldReturn404() throws Exception {
        // Arrange
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Unknown Author");

        when(authorService.updateAuthorById(eq(999), any(Author.class))).thenReturn(null);

        String authorJson = objectMapper.writeValueAsString(updatedAuthor);

        // Act & Assert
        mockMvc.perform(put("/api/authors/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson))
                .andExpect(status().isNotFound());

        System.out.println("Author not found for update: ID 999");
        verify(authorService, times(1)).updateAuthorById(eq(999), any(Author.class));
    }

    @Test
    void deleteAuthorById_ShouldReturn204NoContent() throws Exception {
        // Arrange
        doNothing().when(authorService).deleteAuthorById(1);

        // Act & Assert
        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isNoContent());

        System.out.println("Author deleted: ID 1");
        verify(authorService, times(1)).deleteAuthorById(1);
    }

    @Test
    void create10AuthorsAtOnce_ShouldReturnCreatedAuthors() throws Exception {
        // Arrange
        String[] names = {
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

        String[] emails = {
            "rick@saddleback.com",
            "cslewis@oxford.ac.uk",
            "lee@caseforchrist.com",
            "john@ransomedheart.com",
            "stephen@lovedare.com",
            "todd@heavenisforreal.com",
            "william@theshack.com",
            "sarah@jesuscalling.com",
            "bruce@prayerofjabez.com",
            "gary@5lovelanguages.com"
        };

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            Author author = new Author();
            author.setId(i + 1);
            author.setName(names[i]);
            author.setPhone("555-" + String.format("%04d", i + 1));
            author.setEmail(emails[i]);
            author.setAddress("Address " + (i + 1));
            author.setBio("Bio for " + names[i]);

            when(authorService.createAuthor(any(Author.class))).thenReturn(author);

            String authorJson = objectMapper.writeValueAsString(author);

            mockMvc.perform(post("/api/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(authorJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(i + 1))
                    .andExpect(jsonPath("$.name").value(names[i]));

            System.out.println("Author created: " + author.getName() + " (ID: " + (i + 1) + ")");
        }

        System.out.println("Total authors created: 10");
        verify(authorService, times(10)).createAuthor(any(Author.class));
    }
}
