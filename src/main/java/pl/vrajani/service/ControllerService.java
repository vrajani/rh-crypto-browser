package pl.vrajani.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ControllerService {
    private static Logger LOG = LoggerFactory.getLogger(ControllerService.class);

    private static final List<String> CRYPTO = Arrays.asList(new String[]{"LTC","ETH","BCH","ETC"});

    private WebDriver driver;
    private static final long INTERVAL_RATE = 120000;

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
    private StateLoadService stateLoadService;

    @Autowired
    private Map<String, CryptoCurrencyStatus> cryptoCurrencyStatusMap;

    @Scheduled(fixedRate = INTERVAL_RATE)
    public void performCheck(){
        LOG.info("Initiating the check::::");
        synchronized (this){
            if (driver == null){
                String path = "src/main/resources/chromedriver";
                if (System.getenv("OS").equalsIgnoreCase("windows")){
                    path = path + ".exe";
                }
                System.setProperty("webdriver.chrome.driver", path);

                if(System.getenv("headless").equalsIgnoreCase("false")) {
                    driver = new ChromeDriver();
                } else {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("headless");
                    options.addArguments("window-size=1400x600");
                    driver = new ChromeDriver(options);
                }
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

                ThreadWait.waitFor(4000);
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

    private void checkCryptoWithSymbol(String str) throws IOException {

        CryptoCurrencyBuilder cryptoCurrencyBuilder = new CryptoCurrencyBuilder(str);
        priceReaderService.readCurrentPrices(cryptoCurrencyBuilder, driver);

        CryptoCurrencyStatus currencyStatus = cryptoCurrencyStatusMap.get(str);
        cryptoCurrencyBuilder.withLastBuyPrice(currencyStatus.getLastBuyPrice())
                    .withLastSalePrice(currencyStatus.getLastSalePrice())
                    .withCurrencyStatus(currencyStatus);

        CryptoCurrency cryptoCurrency = cryptoCurrencyBuilder.build();
        LOG.info("Crypto Details: "+ cryptoCurrency.toString());

        if (currencyStatus.getDurationSinceLastBuy() >=16 && currencyStatus.getLimitBuyCount() > 0
                && analyseBuy.analyse(cryptoCurrency)) {
            LOG.info(str + ": Buying at price - " + cryptoCurrency.getPrice());
            actionService.buy(cryptoCurrency, driver);
            currencyStatus.setLastBuyPrice(cryptoCurrency.getPrice());
            currencyStatus.setLastSalePrice(getPercentAmount(101.0, cryptoCurrency.getPrice()));
            currencyStatus.setLimitBuyCount(currencyStatus.getLimitBuyCount() - 1);
            if(currencyStatus.getLimitBuyCount() - currencyStatus.getLimitSellCount() <= 2){
                currencyStatus.setLimitSellCount(currencyStatus.getLimitSellCount() + 1);
            }
            currencyStatus.setDurationSinceLastBuy(0);
        } else if ( currencyStatus.getDurationSinceLastSell() >= 16 && currencyStatus.getLimitSellCount() > 0
                && analyseSell.analyse(cryptoCurrency)) {
            LOG.info(str + ": Selling at price - " + cryptoCurrency.getPrice());
            actionService.sell(cryptoCurrency, driver);
            currencyStatus.setLastSalePrice(cryptoCurrency.getPrice());
            currencyStatus.setLastBuyPrice(getPercentAmount(99.0, cryptoCurrency.getPrice()));
            currencyStatus.setLimitSellCount(currencyStatus.getLimitSellCount() - 1);
            if(currencyStatus.getLimitSellCount() - currencyStatus.getLimitBuyCount() <= 2){
                currencyStatus.setLimitBuyCount(currencyStatus.getLimitBuyCount() + 1);
            }
            currencyStatus.setDurationSinceLastSell(0);
        } else {
            LOG.info(cryptoCurrency.getSymbol() + ": Waiting at price - "+cryptoCurrency.getPrice());
        }
        currencyStatus.incrementBuyDuration();
        currencyStatus.incrementSellDuration();

        if(currencyStatus.getDurationSinceLastBuy() > 500){
            currencyStatus.setLastBuyPrice(getPercentAmount(98.5, cryptoCurrency.getPrice()));
            currencyStatus.setDurationSinceLastBuy(16);
        }
        if(currencyStatus.getDurationSinceLastSell() > 500){
            currencyStatus.setLastSalePrice(getPercentAmount(101.5, cryptoCurrency.getPrice()));
            currencyStatus.setDurationSinceLastSell(16);
        }
        cryptoCurrencyStatusMap.put(str, currencyStatus);
        //Finally save the new state, for just in case.
        stateLoadService.save(currencyStatus);
    }

    private Double getPercentAmount(double percent, Double currentPrice) {
        return (currentPrice * percent)/100;
    }

}
