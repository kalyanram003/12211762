package com.kalyan.url.shortner.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalyan.url.shortner.Model.UrlMapping;
import com.kalyan.url.shortner.Repository.UrlMappingRepository;
import com.kalyan.url.shortner.Logging.LoggerService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlMappingRepository repo;

    @Autowired
    private LoggerService logger;

    public UrlMapping createShortUrl(String url, Integer validity, String shortcode) {
        if (validity == null) validity = 30;
        if (shortcode == null || shortcode.isEmpty()) {
            shortcode = UUID.randomUUID().toString().substring(0, 6);
        } else {
            if (repo.existsById(shortcode)) {
                logger.log("backend", "error", "handler", "Custom shortcode already exists: " + shortcode);
                throw new RuntimeException("Shortcode already taken");
            }
        }

        UrlMapping mapping = new UrlMapping();
        mapping.setShortcode(shortcode);
        mapping.setOriginalUrl(url);
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setExpiryAt(LocalDateTime.now().plusMinutes(validity));

        repo.save(mapping);
        logger.log("backend", "info", "controller", "Short URL created: " + shortcode);

        return mapping;
    }

    public UrlMapping getOriginalUrl(String shortcode) {
        Optional<UrlMapping> mapping = repo.findById(shortcode);
        if (mapping.isEmpty()) {
            logger.log("backend", "warn", "handler", "Shortcode not found: " + shortcode);
            throw new RuntimeException("Shortcode not found");
        }

        UrlMapping m = mapping.get();
        if (m.getExpiryAt().isBefore(LocalDateTime.now())) {
            logger.log("backend", "warn", "handler", "Shortcode expired: " + shortcode);
            throw new RuntimeException("Shortcode expired");
        }

        m.setClickCount(m.getClickCount() + 1);
        repo.save(m);
        logger.log("backend", "info", "controller", "Redirecting to: " + m.getOriginalUrl());

        return m;
    }

    public UrlMapping getStats(String shortcode) {
        return repo.findById(shortcode)
                .orElseThrow(() -> new RuntimeException("Shortcode not found"));
    }
}
