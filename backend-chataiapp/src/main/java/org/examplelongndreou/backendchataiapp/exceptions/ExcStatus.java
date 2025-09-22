package org.examplelongndreou.backendchataiapp.exceptions;


import org.springframework.http.HttpStatus;

public class ExcStatus extends Exception {
    private HttpStatus httpStatus;

    public ExcStatus(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }



}