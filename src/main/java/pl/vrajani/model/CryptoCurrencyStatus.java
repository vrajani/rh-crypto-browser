package pl.vrajani.model;

public class CryptoCurrencyStatus {

    String symbol;
    Double lastBuyPrice;
    Double lastSalePrice;
    int limitBuyCount = 0;
    int limitSellCount = 0;
    private long durationWait;

    public Double getLastBuyPrice() {
        return lastBuyPrice;
    }

    public void setLastBuyPrice(Double lastBuyPrice) {
        this.lastBuyPrice = lastBuyPrice;
    }

    public Double getLastSalePrice() {
        return lastSalePrice;
    }

    public void setLastSalePrice(Double lastSalePrice) {
        this.lastSalePrice = lastSalePrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getLimitBuyCount() {
        return limitBuyCount;
    }

    public void setLimitBuyCount(int limitBuyCount) {
        this.limitBuyCount = limitBuyCount;
    }

    public int getLimitSellCount() {
        return limitSellCount;
    }

    public void setLimitSellCount(int limitSellCount) {
        this.limitSellCount = limitSellCount;
    }

    public long getDurationWait() {
        return durationWait;
    }

    public void setDurationWait(long durationWait) {
        this.durationWait = durationWait;
    }
}
