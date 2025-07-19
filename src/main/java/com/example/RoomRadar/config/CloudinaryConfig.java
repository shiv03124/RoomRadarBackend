package com.example.RoomRadar.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dxoam0p1u");
        config.put("api_key", "486581457867649");
        config.put("api_secret", "IxtJGiglKGDOeDtOKvvOCLGYkEU");

        return new Cloudinary(config);
    }
}
