package com.growthmul.app.lawnmover_fs.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryImage {

    @Value("${cloudinary_url}")
    private String cloudinary_url;


    @Bean
    public Cloudinary cloudinaryConfig() {
        return new Cloudinary(cloudinary_url);
    }
}
