package pl.vrajani.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.vrajani.model.CryptoCurrency;
import pl.vrajani.model.CryptoCurrencyBuilder;
import pl.vrajani.model.CryptoCurrencyStatus;
import pl.vrajani.service.analyse.AnalyseBuy;
import pl.vrajani.service.analyse.AnalyseSell;
import pl.vrajani.utility.ThreadWait;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ControllerService {
    private static Logger LOG = LoggerFactory.getLogger(ControllerService.class);

    private static final List<String> CRYPTO = Arrays.asList(new String[]{"LTC","BCH"});

    private WebDriver driver;

    @Autowired
    private ChromeDriverService chromeDriverService;

    @Autowired
    private PriceReaderService priceReaderService;

    @Autowired
    private AnalyseBuy analyseBuy;

    @Autowired
    private AnalyseSell analyseSell;

    @Autowired
    private ActionService actionService;

    @Autowired
    private Map<String, CryptoCurrencyStatus> cryptoCurrencyStatusMap;

    @Scheduled(fixedRate = 150000)
    public void performCheck(){
        LOG.info("Initiating the check::::");
        synchronized (this){
            if (driver == null){
                String path = "src/main/resources/chromedriver";
                if (System.getenv("OS").equalsIgnoreCase("windows")){
                    path = path + ".exe";
                }
                System.setProperty("webdriver.chrome.driver", path);

                driver = new ChromeDriver();
                chromeDriverService.openRH(driver);
                System.setProperty("isStart","true");
                LOG.info("Opening RH first time.....");
           } else {
                LOG.info("RH is already open.");
            }
        }

        LOG.info("Logged into RH");

        checkAllCrypto();
    }

    private void checkAllCrypto() {
        CRYPTO.stream().forEach(str -> {
            try {
                LOG.info("Working with Crypto: " + str);

                ThreadWait.waitFor(7000);
                driver.findElement(By.partialLinkText(str)).click();
                LOG.info("Reached on crypto page for symbol: " + str);

                ThreadWait.waitFor(3000);
                checkCryptoWithSymbol(str);

            } catch (Exception ex){
                LOG.error("Exception occured::: ",ex);
            } finally {
                LOG.info("Going back to Home page !!");
                driver.findElement(By.partialLinkText("Home")).click();
            }
        });
    }

    private void checkCryptoWithSymbol(String str) {

        CryptoCurrencyBuilder cryptoCurrencyBuilder = new CryptoCurrencyBuilder(str);
        priceReaderService.readCurrentPrices(cryptoCurrencyBuilder, driver);

        CryptoCurrencyStatus currencyStatus = cryptoCurrencyStatusMap.get(str);
        cryptoCurrencyBuilder.withLastBuyPrice(currencyStatus.getLastBuyPrice())
                    .withLastSalePrice(currencyStatus.getLastSalePrice());

        CryptoCurrency cryptoCurrency = cryptoCurrencyBuilder.build();
        LOG.info("Crypto Details: "+ cryptoCurrency.toString());

        if (cryptoCurrency.getLimitSellCount() > 0 && analyseSell.analyse(cryptoCurrency)) {
            LOG.info(cryptoCurrency.getSymbol() + ": Selling at price - " + cryptoCurrency.getPrice());
            actionService.sell(cryptoCurrency, driver);
            currencyStatus.setLastSalePrice(cryptoCurrency.getPrice());
            cryptoCurrency.setLimitSellCount(cryptoCurrency.getLimitSellCount()-1);

        } else if (cryptoCurrency.getLimitBuyCount() > 0 && analyseBuy.analyse(cryptoCurrency)){
            LOG.info(cryptoCurrency.getSymbol() + ": Buying at price - "+cryptoCurrency.getPrice());
            actionService.buy(cryptoCurrency, driver);
            currencyStatus.setLastBuyPrice(cryptoCurrency.getPrice());
            cryptoCurrency.setLimitBuyCount(cryptoCurrency.getLimitBuyCount()-1);

        } else {
            LOG.info(cryptoCurrency.getSymbol() + ": Waiting at price - "+cryptoCurrency.getPrice());
        }
    }
}
