package com.sorsix.martin.urlshortener.api;

import com.sorsix.martin.urlshortener.domain.UrlMapping;
import com.sorsix.martin.urlshortener.service.UrlMappingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
@RestController
public class UrlController {

    private final UrlMappingService urlMappingService;


    public UrlController(UrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    @GetMapping(value = "new/**")
    public UrlMapping newUrlMapping(HttpServletRequest request) {
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        String url = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
        return this.urlMappingService.shorten(url);
    }

    @GetMapping(value = "{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = urlMappingService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, originalUrl).build();
    }


}
