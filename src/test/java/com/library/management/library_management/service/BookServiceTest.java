package com.library.management.library_management.service;

import com.library.management.library_management.model.Author;
import com.library.management.library_management.model.Book;
import com.library.management.library_management.model.Publisher;
import com.library.management.library_management.repository.AuthorRepository;
import com.library.management.library_management.repository.BookRepository;
import com.library.management.library_management.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private BookService bookService;

    private Author joyceMeyer;
    private Publisher faithWords;
    private Book battleOfTheMind;
    private MultipartFile coverImage;

    @BeforeEach
    void setUp() {
        // Create author: Joyce Meyer
        joyceMeyer = new Author();
        joyceMeyer.setId(1);
        joyceMeyer.setName("Joyce Meyer");
        joyceMeyer.setPhone("555-1000");
        joyceMeyer.setEmail("joyce@joycemeyer.org");
        joyceMeyer.setAddress("Fenton, MO");
        joyceMeyer.setBio("Christian author and speaker");

        // Create publisher: FaithWords (Joyce Meyer's publisher)
        faithWords = new Publisher();
        faithWords.setId(1);
        faithWords.setName("FaithWords");
        faithWords.setAddress("Hachette Book Group, 1290 Avenue of the Americas, New York, NY");
        faithWords.setPhone("212-364-1100");

        // Create book: The Battle of the Mind
        battleOfTheMind = new Book();
        battleOfTheMind.setId(1);
        battleOfTheMind.setTitle("The Battle of the Mind");
        battleOfTheMind.setCategory("Christian Living");
        battleOfTheMind.setAuthor_id(1); // References Joyce Meyer
        battleOfTheMind.setPublisher_id(1); // References FaithWords

        // Create mock cover image
        coverImage = new MockMultipartFile(
                "cover",
                "battle_of_the_mind_cover.jpg",
                "image/jpeg",
                "test cover image content".getBytes()
        );
    }

    @Test
    void createBook_WhenAuthorAndPublisherExist_ShouldSetNamesAndReturnBook() throws IOException {
        // Arrange
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn("books/battle_of_the_mind_cover.jpg");
        when(authorRepository.findById(1)).thenReturn(Optional.of(joyceMeyer));
        when(publisherRepository.findById(1)).thenReturn(Optional.of(faithWords));
        when(bookRepository.save(any(Book.class))).thenReturn(battleOfTheMind);

        // Act
        Book result = bookService.createBook(battleOfTheMind, coverImage);

        // Assert
        assertNotNull(result);
        assertEquals("Joyce Meyer", result.getAuthor());
        assertEquals("FaithWords", result.getPublisher());
        assertEquals("books/battle_of_the_mind_cover.jpg", result.getCoverImageKey());

        System.out.println("Book created: " + result.getTitle() + " by " + result.getAuthor() + " (Publisher: " + result.getPublisher() + ")");
        verify(s3Service, times(1)).uploadFile(coverImage);
        verify(authorRepository, times(1)).findById(1);
        verify(publisherRepository, times(1)).findById(1);
        verify(bookRepository, times(1)).save(battleOfTheMind);
    }

    @Test
    void createBook_WhenAuthorNotFound_ShouldThrowIllegalArgumentException() throws IOException {
        // Arrange
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn("books/cover.jpg");
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookService.createBook(battleOfTheMind, coverImage)
        );

        assertEquals("Author not found with id: 1", exception.getMessage());

        System.out.println("Test passed: IllegalArgumentException thrown when author not found (ID: 1)");
        verify(s3Service, times(1)).uploadFile(coverImage);
        verify(authorRepository, times(1)).findById(1);
        verify(publisherRepository, never()).findById(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_WhenPublisherNotFound_ShouldThrowIllegalArgumentException() throws IOException {
        // Arrange
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn("books/cover.jpg");
        when(authorRepository.findById(1)).thenReturn(Optional.of(joyceMeyer));
        when(publisherRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookService.createBook(battleOfTheMind, coverImage)
        );

        assertEquals("Publisher not found with id: 1", exception.getMessage());

        System.out.println("Test passed: IllegalArgumentException thrown when publisher not found (ID: 1)");
        verify(s3Service, times(1)).uploadFile(coverImage);
        verify(authorRepository, times(1)).findById(1);
        verify(publisherRepository, times(1)).findById(1);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_WhenBothAuthorAndPublisherNotFound_ShouldThrowIllegalArgumentExceptionForAuthor() throws IOException {
        // Arrange
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn("books/cover.jpg");
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookService.createBook(battleOfTheMind, coverImage)
        );

        assertEquals("Author not found with id: 1", exception.getMessage());

        System.out.println("Test passed: IllegalArgumentException thrown for author first when both author and publisher not found");
        verify(s3Service, times(1)).uploadFile(coverImage);
        verify(authorRepository, times(1)).findById(1);
        verify(publisherRepository, never()).findById(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_WithDifferentAuthorAndPublisher_ShouldSetCorrectNames() throws IOException {
        // Arrange - Create different author and publisher
        Author rickWarren = new Author();
        rickWarren.setId(2);
        rickWarren.setName("Rick Warren");

        Publisher zondervan = new Publisher();
        zondervan.setId(2);
        zondervan.setName("Zondervan");

        Book purposeDrivenLife = new Book();
        purposeDrivenLife.setId(2);
        purposeDrivenLife.setTitle("The Purpose Driven Life");
        purposeDrivenLife.setAuthor_id(2);
        purposeDrivenLife.setPublisher_id(2);

        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn("books/purpose_driven_life_cover.jpg");
        when(authorRepository.findById(2)).thenReturn(Optional.of(rickWarren));
        when(publisherRepository.findById(2)).thenReturn(Optional.of(zondervan));
        when(bookRepository.save(any(Book.class))).thenReturn(purposeDrivenLife);

        // Act
        Book result = bookService.createBook(purposeDrivenLife, coverImage);

        // Assert
        assertNotNull(result);
        assertEquals("Rick Warren", result.getAuthor());
        assertEquals("Zondervan", result.getPublisher());

        System.out.println("Book created: " + result.getTitle() + " by " + result.getAuthor() + " (Publisher: " + result.getPublisher() + ")");
        verify(authorRepository, times(1)).findById(2);
        verify(publisherRepository, times(1)).findById(2);
    }
}
