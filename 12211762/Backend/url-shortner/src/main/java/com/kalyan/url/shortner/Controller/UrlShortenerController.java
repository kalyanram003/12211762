package com.kalyan.url.shortner.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorturls")
    public ResponseEntity<?> createShort(@RequestBody Map<String, Object> req) {
        String url = (String) req.get("url");
        Integer validity = req.get("validity") == null ? null : (Integer) req.get("validity");
        String shortcode = (String) req.get("shortcode");

        UrlMapping mapping = service.createShortUrl(url, validity, shortcode);

        Map<String, Object> res = new HashMap<>();
        res.put("shortLink", "http://localhost:8080/" + mapping.getShortcode());
        res.put("expiry", mapping.getExpiryAt().format(DateTimeFormatter.ISO_DATE_TIME));
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/{shortcode}")
    public RedirectView redirect(@PathVariable String shortcode) {
        UrlMapping mapping = service.getOriginalUrl(shortcode);
        return new RedirectView(mapping.getOriginalUrl());
    }

    @GetMapping("/shorturls/{shortcode}")
    public ResponseEntity<?> getStats(@PathVariable String shortcode) {
        UrlMapping mapping = service.getStats(shortcode);

        Map<String, Object> res = new HashMap<>();
        res.put("originalUrl", mapping.getOriginalUrl());
        res.put("createdAt", mapping.getCreatedAt());
        res.put("expiryAt", mapping.getExpiryAt());
        res.put("clickCount", mapping.getClickCount());

        return ResponseEntity.ok(res);
    }
}
