package com.sorsix.martin.urlshortener.api;

import com.sorsix.martin.urlshortener.domain.exceptions.UrlMappingNotFoundException;
import com.sorsix.martin.urlshortener.domain.exceptions.UrlNotValidException;
import org.springframework.hateoas.VndErrors.VndError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UrlControllerExceptionHandler {

    @ExceptionHandler(UrlMappingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    VndError urlMappingNotFoundExceptionHandler(UrlMappingNotFoundException ex) {
        return new VndError("error", ex.getMessage());
    }

    @ExceptionHandler(UrlNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    VndError urlNotValidExceptionHandler(UrlNotValidException ex) {
        return new VndError("error", ex.getMessage());
    }
}
