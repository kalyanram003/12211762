package com.kalyan.url.shortner.Model;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UrlMapping {
    @Id
    private String shortcode;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiryAt;
    private int clickCount = 0;

    
   
}
