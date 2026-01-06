package com.virtual.demo.controller;

import com.virtual.demo.models.History;
import com.virtual.demo.repositories.HistoryRepo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class HistoryController {

    HistoryRepo historyRepo;

    public HistoryController(HistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    @GetMapping("/histories")
    public List<History> getHistories() {
        return historyRepo.findAll();
    }
}
