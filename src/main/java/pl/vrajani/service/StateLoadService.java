package pl.vrajani.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.vrajani.model.CryptoCurrencyBuilder;
import pl.vrajani.model.CryptoCurrencyStatus;

import java.io.File;
import java.io.IOException;

@Service
public class StateLoadService {

    @Autowired
    private ObjectMapper objectMapper;

    protected void readState(CryptoCurrencyBuilder builder, String symbol){
        try {
            CryptoCurrencyStatus currencyStatus = objectMapper.readValue(new File("src/main/resources/status/"+ symbol.toLowerCase()+".json"), CryptoCurrencyStatus.class);

            builder.withLastBuyPrice(currencyStatus.getLastBuyPrice())
                    .withLastSalePrice(currencyStatus.getLastSalePrice());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}