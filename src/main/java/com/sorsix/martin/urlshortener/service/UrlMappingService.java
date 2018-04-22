package com.sorsix.martin.urlshortener.service;

import com.google.common.hash.Hashing;
import com.sorsix.martin.urlshortener.domain.UrlMapping;
import com.sorsix.martin.urlshortener.domain.exceptions.UrlNotValidException;
import com.sorsix.martin.urlshortener.domain.exceptions.UrlMappingNotFoundException;
import com.sorsix.martin.urlshortener.persistence.UrlMappingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class UrlMappingService {

    private final UrlMappingDao urlMappingDao;
    private final String APP_BASE_PATH = "http://localhost:8080/";

    @Autowired
    public UrlMappingService(UrlMappingDao urlMappingDao, UrlMappingDao urlMappingDao1) {
        this.urlMappingDao = urlMappingDao1;
    }

    public UrlMapping shorten(String url) {
        if (!isUrlValid(url)) {
            throw new UrlNotValidException(url);
        }
        String shortUrl = Hashing.murmur3_32().hashString(sanitizeURL(url), StandardCharsets.UTF_8).toString();
        UrlMapping result = urlMappingDao.findById(shortUrl)
                .orElseGet(() -> urlMappingDao.save(new UrlMapping(url, shortUrl)));
        result.setShortUrl(APP_BASE_PATH + result.getShortUrl());
        return result;
    }

    public String getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = this.urlMappingDao.findById(shortUrl)
                .orElseThrow(() -> new UrlMappingNotFoundException(shortUrl));
        return urlMapping.getOriginalUrl();
    }

    private boolean isUrlValid(String url) {
        boolean valid = true;
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            valid = false;
        }
        return valid;
    }

    /**
     * This method should take care of the various issues with a valid url
     * e.g. www.google.com,www.google.com/, http://www.google.com, http://www.google.com/
     * All the above URL should point to same shortened URL
     */
    private String sanitizeURL(String url) {
        if (url.substring(0, 6).equals("http:/"))
            url = url.substring(6);

        if (url.substring(0, 7).equals("https:/"))
            url = url.substring(7);

        if (url.charAt(url.length() - 1) == '/')
            url = url.substring(0, url.length() - 1);
        return url;
    }
}
