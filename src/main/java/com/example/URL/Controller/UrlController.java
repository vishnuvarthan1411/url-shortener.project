package com.example.URL.Controller;

import com.example.URL.DTO.CreateUrlRequest;
import com.example.URL.DTO.CreateUrlResponse;
import com.example.URL.Service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping
    public CreateUrlResponse createUrl(@Valid @RequestBody CreateUrlRequest request) {
        return urlService.createShortUrl(request);
    }
}