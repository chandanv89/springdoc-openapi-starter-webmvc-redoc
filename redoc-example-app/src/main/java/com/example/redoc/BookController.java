package com.example.redoc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Example REST controller for demonstrating Redoc API documentation.
 */
@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Operations for managing books")
public class BookController {

    private final List<Book> books = new CopyOnWriteArrayList<>(List.of(
            new Book(1L, "Clean Code", "Robert C. Martin", "9780132350884"),
            new Book(2L, "Effective Java", "Joshua Bloch", "9780134685991"),
            new Book(3L, "Spring in Action", "Craig Walls", "9781617297571")
    ));

    @GetMapping
    @Operation(summary = "List all books", description = "Returns a list of all available books")
    public List<Book> listBooks() {
        return books;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Returns a single book by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Book found")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public Book getBook(@Parameter(description = "The book ID") @PathVariable Long id) {
        return books.stream()
                .filter(b -> b.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new book", description = "Creates a new book entry")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    public Book addBook(@RequestBody Book book) {
        books.add(book);
        return book;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book", description = "Removes a book by its ID")
    @ApiResponse(responseCode = "204", description = "Book deleted")
    public void deleteBook(@Parameter(description = "The book ID") @PathVariable Long id) {
        books.removeIf(b -> b.id().equals(id));
    }

    /**
     * A simple book record.
     */
    public record Book(Long id, String title, String author, String isbn) {}
}
