package pl.vrajani.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import pl.vrajani.model.CryptoCurrencyBuilder;
import pl.vrajani.utility.ThreadWait;

@Service
public class PriceReaderService {

    protected void readCurrentPrices(CryptoCurrencyBuilder builder, WebDriver driver) {
        Double equity = Double.parseDouble(driver.findElement(By.xpath("//div[@class='grid-2']/div[1]//h2")).getText().replace("$",""));
        Double avgCost = Double.parseDouble(driver.findElement(By.xpath("//div[@class='grid-2']/div[2]//h2")).getText().replace("$",""));
        Double cost = Double.parseDouble(driver.findElement(By.xpath("//div[@class='grid-2']/div[1]//tr[1]/td[3]")).getText().replace("$",""));
        Double count = Double.parseDouble(driver.findElement(By.xpath("//div[@class='grid-2']/div[2]//tr[1]/td[3]")).getText().replace("$",""));

        Double diff1day = Double.parseDouble(parseDiff(driver.findElement(By.xpath("//span[@class='bold'][1]/span[1]/span[2]")).getText()));

        driver.findElement(By.partialLinkText("LIVE")).click();

        ThreadWait.waitFor(2000);
        Double diff1hour = Double.parseDouble(parseDiff(driver.findElement(By.xpath("//span[@class='bold'][1]/span[1]/span[2]")).getText()));

        builder.withEquity(equity)
                .withAvgCost(avgCost)
                .withCost(cost)
                .withCount(count)
                .withDay1diff(diff1day)
                .withHour1diff(diff1hour);
    }

    private String parseDiff(String text) {
        return text.replace("(","")
                .replace(")","")
                .replace("%","");
    }
}
