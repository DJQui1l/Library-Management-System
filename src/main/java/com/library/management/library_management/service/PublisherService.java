package com.library.management.library_management.service;

import com.library.management.library_management.model.Author;
import com.library.management.library_management.model.Publisher;
import com.library.management.library_management.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PublisherService {
    //the service creates the logic for the Publisher entity to be saved, retrieved, updated, and deleted in the repository.

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    //create a Publisher
    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    //get all publishers
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    // get a publisher by ID
    public Optional<Publisher> getPublisherById(Integer id) {
        return publisherRepository.findById(id);
    }

    //update publisher by ID
    public Author updatePublisherById(Integer id, Publisher publisherDetails){
        return publisherRepository.findById(id)
                .map(author -> {
                    if (publisherDetails.getName() != null){
                        author.setName(publisherDetails.getName());
                    }
                    if (publisherDetails.getPhone()  != null){
                        author.setPhone(publisherDetails.getPhone());
                    }
                    if (authorDetails.getEmail()  != null){
                        author.setPhone(authorDetails.getEmail());
                    }
                    if (authorDetails.getAddress()  != null){
                        author.setPhone(authorDetails.getAddress());
                    }
                    if (authorDetails.getBio()  != null){
                        author.setPhone(authorDetails.getBio());
                    }

                    return authorRepository.save(author);

                }).orElse(null);
    }
    // Delete a Publisher
    public void deletePublisherById(Integer id) {
        publisherRepository.deleteById(id);
    }



}
