package com.demo.tokenizer.ControllerTests;

import com.demo.tokenizer.Controller.advice.GlobalExceptionHandler;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler testSubject = new GlobalExceptionHandler();

    @Test
    void testExceptionEntityExistsHandler() {

        EntityExistsException exception =
                new EntityExistsException("Some provided accounts already exist.");

        ResponseEntity<String> response =
                testSubject.exceptionEntityExistsHandler(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Some provided accounts already exist.",
                response.getBody());
    }

    @Test
    void testExceptionEntityNotFoundHandler() {

        EntityNotFoundException exception =
                new EntityNotFoundException("Some account numbers were not found.");

        ResponseEntity<String> response =
                testSubject.exceptionEntityNotFoundHandler(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Some account numbers were not found.",
                response.getBody());
    }
}
