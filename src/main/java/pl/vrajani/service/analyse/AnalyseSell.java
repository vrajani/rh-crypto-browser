package pl.vrajani.service.analyse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.vrajani.model.CryptoCurrency;

@Component
public class AnalyseSell implements Analyser {
    public static Logger log = LoggerFactory.getLogger(AnalyseSell.class);

    @Override
    public boolean analyse(CryptoCurrency cryptoCurrency) {
        if (cryptoCurrency.getEquity() <= 50){
            log.info(cryptoCurrency.getSymbol()+": No Sell, Reason: equity is high - "+cryptoCurrency.getEquity());
            return false;
        }

        if(cryptoCurrency.getLastSalePrice() >= cryptoCurrency.getPrice() &&
                getPercent(cryptoCurrency.getPrice(),cryptoCurrency.getLastSalePrice()) < 98.0){
            log.info(cryptoCurrency.getSymbol()+": No Sell, Reason: close or higher than last sell - "+cryptoCurrency.getLastSalePrice());
            return false;
        }

        if(cryptoCurrency.getLastBuyPrice() > cryptoCurrency.getPrice() &&
                getPercent(cryptoCurrency.getLastBuyPrice(), cryptoCurrency.getPrice()) < 98.0){
            log.info(cryptoCurrency.getSymbol()+": No Sell, Reason: close or higher than last buy - "+cryptoCurrency.getLastBuyPrice());
            return false;
        }

        if(cryptoCurrency.getLimitBuyCount() == 4 && cryptoCurrency.getLimitSellCount() <= 3 ) {
            log.info(cryptoCurrency.getSymbol()+": No Sell, Reason: None Bought and sold once already - "+cryptoCurrency.getLastSalePrice());
            return false;
        }

        if (cryptoCurrency.getDay1diff() <=4 && cryptoCurrency.getHour1diff() <= 0.75 ){
            log.info(cryptoCurrency.getSymbol()+": No Sell, Reason: not higher enough yet - "+cryptoCurrency.getPrice());
            return false;
        }

        return true;
    }

    //rule1: it should be above last buy price by 3%
    // rule2: 1daydiff >= 4 and 1hourdiff > 2
    // rule3: equity > 100
}
