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

    @Test
    void updateBookById_WhenBookExists_ShouldUpdateFields() {
        // Arrange
        Book updatedDetails = new Book();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setCategory("Updated Category");
        updatedDetails.setAuthor("Updated Author");
        updatedDetails.setPublisher("Updated Publisher");

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(bookRepository.save(any(Book.class))).thenReturn(battleOfTheMind);

        // Act
        Book result = bookService.updateBookById(1, updatedDetails, null);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", battleOfTheMind.getTitle());
        assertEquals("Updated Category", battleOfTheMind.getCategory());
        assertEquals("Updated Author", battleOfTheMind.getAuthor());
        assertEquals("Updated Publisher", battleOfTheMind.getPublisher());
        verify(bookRepository, times(1)).findById(1);
        verify(bookRepository, times(1)).save(battleOfTheMind);

        System.out.println("Test passed: Book fields updated successfully");
    }

    @Test
    void updateBookById_WhenAuthorIdProvidedAndExists_ShouldUpdateAuthorId() {
        // Arrange
        Author rickWarren = new Author();
        rickWarren.setId(2);
        rickWarren.setName("Rick Warren");

        Book updatedDetails = new Book();
        updatedDetails.setAuthor_id(2);

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(authorRepository.findById(2)).thenReturn(Optional.of(rickWarren));
        when(bookRepository.save(any(Book.class))).thenReturn(battleOfTheMind);

        // Act
        Book result = bookService.updateBookById(1, updatedDetails, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, battleOfTheMind.getAuthor_id());
        verify(bookRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).findById(2);
        verify(bookRepository, times(1)).save(battleOfTheMind);

        System.out.println("Test passed: Author ID updated to 2");
    }

    @Test
    void updateBookById_WhenAuthorIdProvidedAndNotFound_ShouldThrowIllegalArgumentException() {
        // Arrange
        Book updatedDetails = new Book();
        updatedDetails.setAuthor_id(999);

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(authorRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookService.updateBookById(1, updatedDetails, null)
        );

        assertEquals("Author not found with id: 999.", exception.getMessage());
        verify(bookRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).findById(999);
        verify(bookRepository, never()).save(any());

        System.out.println("Test passed: IllegalArgumentException thrown when author not found (ID: 999)");
    }

    @Test
    void updateBookById_WhenPublisherIdProvidedAndExists_ShouldUpdatePublisherId() {
        // Arrange
        Publisher zondervan = new Publisher();
        zondervan.setId(2);
        zondervan.setName("Zondervan");

        Book updatedDetails = new Book();
        updatedDetails.setPublisher_id(2);

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(publisherRepository.findById(2)).thenReturn(Optional.of(zondervan));
        when(bookRepository.save(any(Book.class))).thenReturn(battleOfTheMind);

        // Act
        Book result = bookService.updateBookById(1, updatedDetails, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, battleOfTheMind.getPublisher_id());
        verify(bookRepository, times(1)).findById(1);
        verify(publisherRepository, times(1)).findById(2);
        verify(bookRepository, times(1)).save(battleOfTheMind);

        System.out.println("Test passed: Publisher ID updated to 2");
    }

    @Test
    void updateBookById_WhenPublisherIdProvidedAndNotFound_ShouldThrowIllegalArgumentException() {
        // Arrange
        Book updatedDetails = new Book();
        updatedDetails.setPublisher_id(999);

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(publisherRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookService.updateBookById(1, updatedDetails, null)
        );

        assertEquals("Publisher not found with id: 999.", exception.getMessage());
        verify(bookRepository, times(1)).findById(1);
        verify(publisherRepository, times(1)).findById(999);
        verify(bookRepository, never()).save(any());

        System.out.println("Test passed: IllegalArgumentException thrown when publisher not found (ID: 999)");
    }

    @Test
    void updateBookById_WhenBothIdsProvidedAndExist_ShouldUpdateBoth() {
        // Arrange
        Author rickWarren = new Author();
        rickWarren.setId(2);
        rickWarren.setName("Rick Warren");

        Publisher zondervan = new Publisher();
        zondervan.setId(2);
        zondervan.setName("Zondervan");

        Book updatedDetails = new Book();
        updatedDetails.setAuthor_id(2);
        updatedDetails.setPublisher_id(2);

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(authorRepository.findById(2)).thenReturn(Optional.of(rickWarren));
        when(publisherRepository.findById(2)).thenReturn(Optional.of(zondervan));
        when(bookRepository.save(any(Book.class))).thenReturn(battleOfTheMind);

        // Act
        Book result = bookService.updateBookById(1, updatedDetails, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, battleOfTheMind.getAuthor_id());
        assertEquals(2, battleOfTheMind.getPublisher_id());
        verify(bookRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).findById(2);
        verify(publisherRepository, times(1)).findById(2);
        verify(bookRepository, times(1)).save(battleOfTheMind);

        System.out.println("Test passed: Both Author ID and Publisher ID updated to 2");
    }

    @Test
    void updateBookById_WhenBothIdsNotFound_ShouldThrowCombinedIllegalArgumentException() {
        // Arrange
        Book updatedDetails = new Book();
        updatedDetails.setAuthor_id(999);
        updatedDetails.setPublisher_id(888);

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(authorRepository.findById(999)).thenReturn(Optional.empty());
        when(publisherRepository.findById(888)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookService.updateBookById(1, updatedDetails, null)
        );

        assertEquals("Author not found with id: 999. Publisher not found with id: 888.", exception.getMessage());
        verify(bookRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).findById(999);
        verify(publisherRepository, times(1)).findById(888);
        verify(bookRepository, never()).save(any());

        System.out.println("Test passed: Combined IllegalArgumentException thrown when both author and publisher not found");
    }

    @Test
    void updateBookById_WhenBookNotFound_ShouldReturnNull() {
        // Arrange
        Book updatedDetails = new Book();
        updatedDetails.setTitle("Updated Title");

        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Book result = bookService.updateBookById(1, updatedDetails, null);

        // Assert
        assertNull(result);
        verify(bookRepository, times(1)).findById(1);
        verify(bookRepository, never()).save(any());

        System.out.println("Test passed: Null returned when book not found (ID: 1)");
    }

    @Test
    void updateBookById_WhenCoverProvided_ShouldUploadAndUpdateCoverKey() throws IOException {
        // Arrange
        Book updatedDetails = new Book();
        updatedDetails.setTitle("Updated Title");

        when(bookRepository.findById(1)).thenReturn(Optional.of(battleOfTheMind));
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn("books/updated_cover.jpg");
        when(bookRepository.save(any(Book.class))).thenReturn(battleOfTheMind);

        // Act
        Book result = bookService.updateBookById(1, updatedDetails, coverImage);

        // Assert
        assertNotNull(result);
        assertEquals("books/updated_cover.jpg", battleOfTheMind.getCoverImageKey());
        verify(bookRepository, times(1)).findById(1);
        verify(s3Service, times(1)).uploadFile(coverImage);
        verify(bookRepository, times(1)).save(battleOfTheMind);

        System.out.println("Test passed: Cover image uploaded and updated successfully");
    }
}
