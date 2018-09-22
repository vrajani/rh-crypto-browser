package pl.vrajani.model;

public class CryptoCurrencyBuilder {
    String symbol;
    Double price;
    Double day1diff;
    Double hour1diff;
    Double lastBuyPrice;
    Double lastSalePrice;
    Double diff;
    Double avgCost;
    Double equity;
    Double count;
    Double cost;
    int limitBuyCount = 0;
    int limitSellCount = 0;
    long durationWait;

    public CryptoCurrencyBuilder(String symbol) {
        this.symbol = symbol;
    }

    public CryptoCurrencyBuilder withCurrencyStatus(CryptoCurrencyStatus currencyStatus){
        this.lastBuyPrice = currencyStatus.getLastBuyPrice();
        this.lastSalePrice = currencyStatus.getLastSalePrice();
        this.limitBuyCount = currencyStatus.getLimitBuyCount();
        this.limitSellCount = currencyStatus.getLimitSellCount();
        this.durationWait = currencyStatus.getDurationWait();
        return this;
    }

    public CryptoCurrencyBuilder withPrice(Double price){
        this.price = price;
        return this;
    }

    public CryptoCurrencyBuilder withDay1diff(Double day1diff){
        this.day1diff = day1diff;
        return this;
    }

    public CryptoCurrencyBuilder withHour1diff(Double hour1diff){
        this.hour1diff = hour1diff;
        return this;
    }

    public CryptoCurrencyBuilder withLastBuyPrice(Double lastBuyPrice){
        this.lastBuyPrice = lastBuyPrice;
        return this;
    }

    public CryptoCurrencyBuilder withLastSalePrice(Double lastSalePrice){
        this.lastSalePrice = lastSalePrice;
        return this;
    }

    public CryptoCurrencyBuilder withDiff(Double diff){
        this.diff = diff;
        return this;
    }

    public CryptoCurrencyBuilder withAvgCost(Double avgCost){
        this.avgCost = avgCost;
        return this;
    }

    public CryptoCurrencyBuilder withEquity(Double equity){
        this.equity = equity;
        return this;
    }

    public CryptoCurrencyBuilder withCount(Double count){
        this.count = count;
        return this;
    }

    public CryptoCurrencyBuilder withCost(Double cost){
        this.cost = cost;
        return this;
    }

    public CryptoCurrency build(){
        return new CryptoCurrency(this);
    }
}
