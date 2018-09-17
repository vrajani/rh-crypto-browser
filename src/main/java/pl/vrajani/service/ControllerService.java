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

@Component
public class ControllerService {
    Logger log = LoggerFactory.getLogger(ControllerService.class);

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
    private CryptoCurrencyStatus bchCryptoCurrencyStatus;

    @Autowired
    private CryptoCurrencyStatus ltcCryptoCurrencyStatus;

    private int buyCount = 0;
    private int sellCount = 0;

    @Scheduled(fixedRate = 150000)
    public void performCheck(){
        log.info("Initiating the check::::");
        synchronized (this){
            String path = "src/main/resources/chromedriver";
            if (System.getenv("OS").equalsIgnoreCase("windows")){
                path = path + ".exe";
            }
            System.setProperty("webdriver.chrome.driver", path);

            if (driver == null){
               driver = new ChromeDriver();
               chromeDriverService.openRH(driver);
               System.setProperty("isStart","true");
               log.info("Opening RH first time.....");
           } else {
                log.info("RH is already open.");
            }
        }

        log.info("Logged into RH");

        checkAllCrypto();
    }

    private void checkAllCrypto() {
        CRYPTO.stream().forEach(str -> {
            try {
                log.info("Working with Crypto: " + str);

                ThreadWait.waitFor(7000);
                driver.findElement(By.partialLinkText(str)).click();
                log.info("Reached on crypto page for symbol: " + str);

                ThreadWait.waitFor(3000);
                checkCryptoWithSymbol(str);

            } catch (Exception ex){
                log.error("Exception occured::: ",ex);
            } finally {
                log.info("Going back to Home page !!");
                driver.findElement(By.partialLinkText("Home")).click();
            }
        });
    }

    private void checkCryptoWithSymbol(String str) {

        CryptoCurrencyBuilder cryptoCurrencyBuilder = new CryptoCurrencyBuilder(str);
        priceReaderService.readCurrentPrices(cryptoCurrencyBuilder, driver);

        if(str.equalsIgnoreCase("bch")){
            cryptoCurrencyBuilder.withLastBuyPrice(bchCryptoCurrencyStatus.getLastBuyPrice())
                    .withLastSalePrice(bchCryptoCurrencyStatus.getLastSalePrice());
        } else{
            cryptoCurrencyBuilder.withLastBuyPrice(ltcCryptoCurrencyStatus.getLastBuyPrice())
                    .withLastSalePrice(ltcCryptoCurrencyStatus.getLastSalePrice());
        }
        CryptoCurrency cryptoCurrency = cryptoCurrencyBuilder.build();
        log.info("Crypto Details: "+ cryptoCurrency.toString());

        if (sellCount < 4 && analyseSell.analyse(cryptoCurrency)) {
            log.info(cryptoCurrency.getSymbol() + ": Selling at price - " + cryptoCurrency.getPrice());
            actionService.sell(cryptoCurrency, driver);
            if (cryptoCurrency.getSymbol().equalsIgnoreCase("bch")) {
                bchCryptoCurrencyStatus.setLastSalePrice(cryptoCurrency.getPrice());
            } else {
                ltcCryptoCurrencyStatus.setLastSalePrice(cryptoCurrency.getPrice());
            }
            sellCount++;
        } else if (buyCount < 4 && analyseBuy.analyse(cryptoCurrency)){
            log.info(cryptoCurrency.getSymbol() + ": Buying at price - "+cryptoCurrency.getPrice());
            actionService.buy(cryptoCurrency, driver);
            if(cryptoCurrency.getSymbol().equalsIgnoreCase("bch")){
                bchCryptoCurrencyStatus.setLastBuyPrice(cryptoCurrency.getPrice());
            } else{
                ltcCryptoCurrencyStatus.setLastBuyPrice(cryptoCurrency.getPrice());
            }
            buyCount++;
        } else {
            log.info(cryptoCurrency.getSymbol() + ": Waiting at price - "+cryptoCurrency.getPrice());
        }
    }
}
