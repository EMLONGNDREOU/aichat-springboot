package org.examplelongndreou.backendchataiapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExcHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof ExcStatus) {
            ExcStatus excHandler = (ExcStatus) e;
            status = excHandler.getHttpStatus();
        }

        e.printStackTrace();
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(status.value());
        errorMessage.setErrorMessage("Something went wrong: " + e.getMessage());


        return ResponseEntity.status(status).body(errorMessage);
    }

}
