package pl.vrajani.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import pl.vrajani.model.CryptoCurrency;

@Service
public class ActionService {

    public final static String AMOUNT = "2";

    protected void buy(CryptoCurrency cryptoCurrency, WebDriver driver){
        driver.findElement(By.partialLinkText("Buy")).click();
        driver.findElement(By.xpath("")).sendKeys(AMOUNT);
        driver.findElement(By.partialLinkText("Review")).click();
        driver.findElement(By.partialLinkText("Submit")).click();
    }

    protected void sell(CryptoCurrency cryptoCurrency, WebDriver driver){
        driver.findElement(By.partialLinkText("Sell")).click();
        driver.findElement(By.xpath("")).sendKeys(AMOUNT);
        driver.findElement(By.partialLinkText("Review")).click();
        driver.findElement(By.partialLinkText("Submit")).click();
    }
}
