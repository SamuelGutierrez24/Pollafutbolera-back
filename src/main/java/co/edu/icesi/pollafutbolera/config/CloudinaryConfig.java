package co.edu.icesi.pollafutbolera.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dqqmg0hi5");
        config.put("api_key", "434726976211178");
        config.put("api_secret", "t3NbRb9HxedYNen5b45e1BXDpLo");
        return new Cloudinary(config);
    }
}