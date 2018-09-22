package pl.vrajani.service.analyse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.vrajani.model.CryptoCurrency;

@Component
public class AnalyseBuy implements Analyser {
    public static Logger log = LoggerFactory.getLogger(AnalyseBuy.class);

    @Override
    public boolean analyse(CryptoCurrency cryptoCurrency) {
        if(!cryptoCurrency.getSymbol().equalsIgnoreCase("etc")) {
            if (cryptoCurrency.getEquity() >= 147) {
                log.info(cryptoCurrency.getSymbol() + ": No Buy, Reason: equity is high - " + cryptoCurrency.getEquity());
                return false;
            }
        }
//
//        if(cryptoCurrency.getLimitBuyCount() - cryptoCurrency.getLastSalePrice() <= 0) {
            if (getPercent(cryptoCurrency.getPrice(), cryptoCurrency.getLastBuyPrice()) > 99.00) {
                log.info(cryptoCurrency.getSymbol() + ": No Buy, Reason: close or higher than last buy - " + cryptoCurrency.getLastBuyPrice());
                return false;
            }
//        }

        if(cryptoCurrency.getAvgCost() <= cryptoCurrency.getPrice() &&
                getPercent(cryptoCurrency.getPrice(),cryptoCurrency.getAvgCost()) < 98.0){
            log.info(cryptoCurrency.getSymbol()+": No Buy, Reason: close or higher than average cost - "+cryptoCurrency.getAvgCost());
            return false;
        }

        if (cryptoCurrency.getDay1diff() <= -4 && cryptoCurrency.getHour1diff() <= -1.05 ){
            log.info(cryptoCurrency.getSymbol()+": Buy, Reason: in price range - "+cryptoCurrency.getPrice());
            return true;
        }

        if (cryptoCurrency.getHour1diff() <= -1.30 ) {
            log.info(cryptoCurrency.getSymbol()+": Buy, Reason: Low price since last one hour - "+cryptoCurrency.getPrice());
            return true;
        }
        log.info(cryptoCurrency.getSymbol()+": No Buy, Reason: not lower enough yet - "+cryptoCurrency.getPrice());

        return false;
    }

    //rule1: it should be below last buy price by 3% or last sell price (iffy)
    // rule2: 1daydiff >= 4 and 1hourdiff > 2
    // rule3: equity < 150
    //rule4: currentprice - avgcost >= 3
}
