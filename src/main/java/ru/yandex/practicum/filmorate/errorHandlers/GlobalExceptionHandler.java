package ru.yandex.practicum.filmorate.errorHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;


@ControllerAdvice
public class GlobalExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validationHandler(ValidationException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> annotationHandler(MethodArgumentNotValidException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
