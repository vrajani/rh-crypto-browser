package pl.vrajani.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import pl.vrajani.service.ChromeDriverService;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
