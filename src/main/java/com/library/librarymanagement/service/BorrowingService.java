package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.BorrowingRequest;
import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.entity.Borrowing;
import com.library.librarymanagement.exception.ResourceNotFoundException;
import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.repository.BorrowingRepository;
import com.library.librarymanagement.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BorrowingService {

    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private final MemberRepository memberRepository;

    public BorrowingService(
            BorrowingRepository borrowingRepository,
            BookRepository bookRepository,
            MemberRepository memberRepository) {

        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public List<Borrowing> getAllBorrowings() {
        return borrowingRepository.findAll();
    }

    public Borrowing getBorrowingById(Long id) {
        return borrowingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrowing not found with id: " + id
                        )
                );
    }

    public Borrowing addBorrowing(BorrowingRequest request) {

        validateDates(request);

        Borrowing borrowing = new Borrowing();

        borrowing.setBookId(request.getBookId());
        borrowing.setMemberId(request.getMemberId());
        borrowing.setIssueDate(request.getIssueDate());
        borrowing.setDueDate(request.getDueDate());
        borrowing.setStatus(request.getStatus());

        return borrowingRepository.save(borrowing);
    }

    
    public Borrowing issueBook(BorrowingRequest request) {

        validateDates(request);
        validateStatus(request);

        if (borrowingRepository.existsByBookIdAndMemberIdAndStatus(
                request.getBookId(),
                request.getMemberId(),
                "ISSUED")) {

            throw new IllegalStateException(
                    "Member already has this book issued"
            );
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: "
                                        + request.getBookId()
                        )
                );

        if (book.getAvailableQuantity() <= 0) {
            throw new IllegalStateException(
                    "Book is not available for borrowing"
            );
        }

        memberRepository.findById(request.getMemberId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id: "
                                        + request.getMemberId()
                        )
                );

        Borrowing borrowing = new Borrowing();

        borrowing.setBookId(request.getBookId());
        borrowing.setMemberId(request.getMemberId());
        borrowing.setIssueDate(request.getIssueDate());
        borrowing.setDueDate(request.getDueDate());
        borrowing.setStatus(request.getStatus());

        book.setAvailableQuantity(
                book.getAvailableQuantity() - 1
        );

        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
    }


    private void validateDates(BorrowingRequest request) {

        if (request.getDueDate().isBefore(request.getIssueDate())) {
            throw new IllegalStateException(
                    "Due date cannot be before issue date"
            );
        }
    }

    private void validateStatus(BorrowingRequest request) {

        if (!"ISSUED".equals(request.getStatus())) {
            throw new IllegalStateException(
                    "Status must be ISSUED when issuing a book"
            );
        }
    }


    public Borrowing returnBook(Long id) {

        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrowing not found with id: " + id
                        )
                );

        if ("RETURNED".equals(borrowing.getStatus())) {
            return borrowing;
        }

        Book book = bookRepository.findById(borrowing.getBookId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: "
                                        + borrowing.getBookId()
                        )
                );

        book.setAvailableQuantity(
                book.getAvailableQuantity() + 1
        );

        bookRepository.save(book);

        LocalDate returnDate = LocalDate.now();

        borrowing.setReturnDate(returnDate);
        borrowing.setStatus("RETURNED");

        long lateDays = 0;

        if (returnDate.isAfter(borrowing.getDueDate())) {
            lateDays = ChronoUnit.DAYS.between(
                    borrowing.getDueDate(),
                    returnDate
            );
        }

        borrowing.setFine(
                BigDecimal.valueOf(lateDays * 10)
        );

        return borrowingRepository.save(borrowing);
    }
}