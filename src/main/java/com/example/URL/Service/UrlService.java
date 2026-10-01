package com.example.URL.Service;

import com.example.URL.DTO.CreateUrlRequest;
import com.example.URL.DTO.CreateUrlResponse;
import com.example.URL.Entity.Url;
import com.example.URL.Exception.UrlNotFoundException;
import com.example.URL.Repository.UrlRepository;
import com.example.URL.Util.Base62Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public CreateUrlResponse createShortUrl(CreateUrlRequest request) {
        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setShortCode(UUID.randomUUID().toString());
        Url savedUrl = urlRepository.save(url);
        String shortCode = Base62Encoder.encode(savedUrl.getId());
        savedUrl.setShortCode(shortCode);
        urlRepository.save(savedUrl);
        CreateUrlResponse response = new CreateUrlResponse();
        response.setShortUrl("http://localhost:8080/" + savedUrl.getShortCode());
        return response;


    }
    public String getOriginalUrl(String shortCode) {

        Optional<Url> result = urlRepository.findByShortCode(shortCode);

        if (result.isPresent()) {
            Url url = result.get();
            return url.getOriginalUrl();
        } else {
            throw new UrlNotFoundException("Short URL not found: " + shortCode);
        }
    }
}