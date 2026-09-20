package com.library.management.library_management.controller;

import com.library.management.library_management.service.DatabaseService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")

public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @DeleteMapping("/delete-all-records")
    public void deleteAllRecords() {
        databaseService.deleteAllRecords();
    }

}
