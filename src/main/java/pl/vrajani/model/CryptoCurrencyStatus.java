package pl.vrajani.model;

public class CryptoCurrencyStatus {

    String symbol;
    Double lastBuyPrice;
    Double lastSalePrice;
    Double avgCost;
    Double equity;
    Double count;

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

    public Double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Double avgCost) {
        this.avgCost = avgCost;
    }

    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
