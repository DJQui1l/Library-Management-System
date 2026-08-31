package com.library.management.library_management.controller;

import com.library.management.library_management.model.Publisher;
import com.library.management.library_management.service.PublisherService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    //create a publisherService object that handles the business logic for the controller to use.
    public PublisherController(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    //create a publisher
    @PostMapping
    public Publisher createPublisher(@RequestBody Publisher publisher){
        return publisherService.createPublisher(publisher);
    }

    //get all publishers
    @GetMapping
    public List<Publisher> getAllPublishers(){
        return publisherService.getAllPublishers();
    }

    //get publisher by ID
    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Integer id){
        return publisherService.getPublisherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisherById(@PathVariable Integer id){
        publisherService.deletePublisherById(id);
        return ResponseEntity.noContent().build();
    }


}
