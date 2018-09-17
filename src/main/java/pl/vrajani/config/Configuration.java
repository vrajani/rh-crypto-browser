package pl.vrajani.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import pl.vrajani.model.CryptoCurrencyStatus;
import pl.vrajani.service.StateLoadService;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    private StateLoadService stateLoadService;

    @Bean
    public CryptoCurrencyStatus bchCryptoCurrencyStatus() throws Exception {
        return stateLoadService.readState("bch");
    }

    @Bean
    public CryptoCurrencyStatus ltcCryptoCurrencyStatus() throws Exception {
        return stateLoadService.readState("ltc");
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
