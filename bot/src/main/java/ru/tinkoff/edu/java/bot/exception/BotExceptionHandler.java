package ru.tinkoff.edu.java.bot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class BotExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRequestParametersException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequestException(InvalidRequestParametersException exception) {

        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Invalid request parameters",
                String.valueOf(HttpStatus.BAD_REQUEST.value()), //400
                BAD_REQUEST.name(),
                BAD_REQUEST.getReasonPhrase()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> exception(Exception exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Server error",
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), //500
                INTERNAL_SERVER_ERROR.name(),
                INTERNAL_SERVER_ERROR.getReasonPhrase()
            ));
    }
}
