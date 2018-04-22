package com.sorsix.martin.urlshortener.domain.exceptions;

public class UrlNotValidException extends RuntimeException {

    public UrlNotValidException() {
    }

    public UrlNotValidException(String url) {
        super(String.format("'%s' is not a valid URL format", url));
    }
}
