package com.library.management.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.library_management.model.Publisher;
import com.library.management.library_management.service.PublisherService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublisherService publisherService;

    private Publisher testPublisher;

    @BeforeEach
    void setUp() {
        testPublisher = new Publisher();
        testPublisher.setId(1);
        testPublisher.setName("Zondervan");
        testPublisher.setAddress("5300 Patterson Ave SE, Grand Rapids, MI");
        testPublisher.setPhone("616-698-6900");
    }

    @Test
    void createPublisher_ShouldReturnCreatedPublisher() throws Exception {
        // Arrange
        when(publisherService.createPublisher(any(Publisher.class))).thenReturn(testPublisher);

        String publisherJson = objectMapper.writeValueAsString(testPublisher);

        // Act & Assert
        mockMvc.perform(post("/api/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(publisherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Zondervan"))
                .andExpect(jsonPath("$.address").value("5300 Patterson Ave SE, Grand Rapids, MI"));

        System.out.println("Publisher created: " + testPublisher.getName());
        verify(publisherService, times(1)).createPublisher(any(Publisher.class));
    }

    @Test
    void getAllPublishers_ShouldReturnListOfPublishers() throws Exception {
        // Arrange
        Publisher publisher2 = new Publisher();
        publisher2.setId(2);
        publisher2.setName("Thomas Nelson");
        publisher2.setAddress("501 Thomas Nelson Dr, Nashville, TN");
        publisher2.setPhone("615-599-4000");

        List<Publisher> publishers = Arrays.asList(testPublisher, publisher2);
        when(publisherService.getAllPublishers()).thenReturn(publishers);

        // Act & Assert
        mockMvc.perform(get("/api/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Zondervan"))
                .andExpect(jsonPath("$[1].name").value("Thomas Nelson"));

        System.out.println("All publishers retrieved: " + publishers.size() + " publishers found");
        verify(publisherService, times(1)).getAllPublishers();
    }

    @Test
    void getPublisherById_WhenPublisherExists_ShouldReturnPublisher() throws Exception {
        // Arrange
        when(publisherService.getPublisherById(1)).thenReturn(Optional.of(testPublisher));

        // Act & Assert
        mockMvc.perform(get("/api/publishers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Zondervan"));

        System.out.println("Publisher found: ID 1 - " + testPublisher.getName());
        verify(publisherService, times(1)).getPublisherById(1);
    }

    @Test
    void getPublisherById_WhenPublisherNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(publisherService.getPublisherById(999)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/publishers/999"))
                .andExpect(status().isNotFound());

        System.out.println("Publisher not found: ID 999");
        verify(publisherService, times(1)).getPublisherById(999);
    }

    @Test
    void deletePublisherById_ShouldReturn204NoContent() throws Exception {
        // Arrange
        doNothing().when(publisherService).deletePublisherById(1);

        // Act & Assert
        mockMvc.perform(delete("/api/publishers/1"))
                .andExpect(status().isNoContent());

        System.out.println("Publisher deleted: ID 1");
        verify(publisherService, times(1)).deletePublisherById(1);
    }

    @Test
    void create10PublishersAtOnce_ShouldReturnCreatedPublishers() throws Exception {
        // Arrange
        String[] names = {
            "Zondervan",
            "Thomas Nelson",
            "HarperCollins",
            "B&H Publishing",
            "Multnomah",
            "Windblown Media",
            "Northfield Publishing",
            "Tyndale House",
            "Bethany House",
            "Revell"
        };

        String[] addresses = {
            "5300 Patterson Ave SE, Grand Rapids, MI",
            "501 Thomas Nelson Dr, Nashville, TN",
            "195 Broadway, New York, NY",
            "127 Ninth Ave N, Nashville, TN",
            "12265 Oracle Blvd, Colorado Springs, CO",
            "PO Box 7213, Wichita, KS",
            "501 W. 2nd St, Bloomington, MN",
            "1 Tyndale Park Dr, Carol Stream, IL",
            "3900 Bethany Dr, Bloomington, MN",
            "PO Box 1215, Grand Rapids, MI"
        };

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            Publisher publisher = new Publisher();
            publisher.setId(i + 1);
            publisher.setName(names[i]);
            publisher.setAddress(addresses[i]);
            publisher.setPhone("555-" + String.format("%04d", i + 1));

            when(publisherService.createPublisher(any(Publisher.class))).thenReturn(publisher);

            String publisherJson = objectMapper.writeValueAsString(publisher);

            mockMvc.perform(post("/api/publishers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(publisherJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(i + 1))
                    .andExpect(jsonPath("$.name").value(names[i]));

            System.out.println("Publisher created: " + publisher.getName() + " (ID: " + (i + 1) + ")");
        }

        System.out.println("Total publishers created: 10");
        verify(publisherService, times(10)).createPublisher(any(Publisher.class));
    }
}
