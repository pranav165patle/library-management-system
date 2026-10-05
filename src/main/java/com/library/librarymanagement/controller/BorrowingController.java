package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.BorrowingRequest;
import com.library.librarymanagement.entity.Borrowing;
import com.library.librarymanagement.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @GetMapping
    public List<Borrowing> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    @GetMapping("/{id}")
    public Borrowing getBorrowingById(@PathVariable Long id) {
        return borrowingService.getBorrowingById(id);
    }

    @PostMapping
    public Borrowing addBorrowing(
            @Valid @RequestBody BorrowingRequest request) {

        return borrowingService.addBorrowing(request);
    }

    @PostMapping("/issue")
    public Borrowing issueBook(
            @Valid @RequestBody BorrowingRequest request) {

        return borrowingService.issueBook(request);
    }

    @PutMapping("/return/{id}")
    public Borrowing returnBook(@PathVariable Long id) {
        return borrowingService.returnBook(id);
    }
}