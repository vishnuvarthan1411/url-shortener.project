package com.example.URL.Service;

import com.example.URL.DTO.CreateUrlRequest;
import com.example.URL.DTO.CreateUrlResponse;
import com.example.URL.Entity.Url;
import com.example.URL.Exception.AliasAlreadyExistsException;
import com.example.URL.Exception.UrlExpiredException;
import com.example.URL.Exception.UrlNotFoundException;
import com.example.URL.Repository.UrlRepository;
import com.example.URL.Util.Base62Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public CreateUrlResponse createShortUrl(CreateUrlRequest request) {

        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setExpiresAt(LocalDateTime.now().plusDays(30));

        // Custom alias check pannunga
        if (request.getCustomAlias() != null && !request.getCustomAlias().isBlank()) {

            // Alias already use aagi irukkaa nu check pannunga
            Optional<Url> existing = urlRepository.findByShortCode(request.getCustomAlias());
            if (existing.isPresent()) {
                throw new AliasAlreadyExistsException(
                        "This alias is already taken: " + request.getCustomAlias()
                );
            }

            // Custom alias-ah, shortCode-ah direct-ah use pannunga
            url.setShortCode(request.getCustomAlias());
            urlRepository.save(url);

        } else {

            // Namba existing logic — Base62 auto-generate
            url.setShortCode(UUID.randomUUID().toString()); // temporary placeholder
            Url savedUrl = urlRepository.save(url);

            String shortCode = Base62Encoder.encode(savedUrl.getId());
            savedUrl.setShortCode(shortCode);
            urlRepository.save(savedUrl);
        }

        CreateUrlResponse response = new CreateUrlResponse();
        response.setShortUrl("http://localhost:8080/" + url.getShortCode());
        return response;
    }

    public String getOriginalUrl(String shortCode) {

        Optional<Url> result = urlRepository.findByShortCode(shortCode);

        if (result.isPresent()) {
            Url url = result.get();

            if (url.getExpiresAt() != null && LocalDateTime.now().isAfter(url.getExpiresAt())) {
                throw new UrlExpiredException("This short URL has expired: " + shortCode);
            }

            return url.getOriginalUrl();
        } else {
            throw new UrlNotFoundException("Short URL not found: " + shortCode);
        }
    }
}