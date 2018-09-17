package pl.vrajani.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import pl.vrajani.model.CryptoCurrency;
import pl.vrajani.model.CryptoCurrencyStatus;
import pl.vrajani.service.StateLoadService;

import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    private StateLoadService stateLoadService;

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public Map<String, CryptoCurrencyStatus> cryptoCurrencyStatusMap() throws Exception{
        Map<String, CryptoCurrencyStatus> cryptoCurrencyMap = new HashMap<>();
        cryptoCurrencyMap.put("LTC", stateLoadService.readState("ltc"));
        cryptoCurrencyMap.put("BCH", stateLoadService.readState("bch"));
        return cryptoCurrencyMap;
    }
}
