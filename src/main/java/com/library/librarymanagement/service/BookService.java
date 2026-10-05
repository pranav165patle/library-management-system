package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.BookRequest;
import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.exception.ResourceNotFoundException;
import com.library.librarymanagement.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        )
                );
    }

    public Book addBook(BookRequest request) {

        Book book = new Book();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategoryId(request.getCategoryId());
        book.setQuantity(request.getQuantity());

        // New book ki available quantity = total quantity
        book.setAvailableQuantity(request.getQuantity());

        book.setCreatedAt(LocalDateTime.now());

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookRequest request) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        )
                );

        existingBook.setTitle(request.getTitle());
        existingBook.setIsbn(request.getIsbn());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setCategoryId(request.getCategoryId());
        existingBook.setQuantity(request.getQuantity());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        )
                );

        bookRepository.delete(book);
    }
}

