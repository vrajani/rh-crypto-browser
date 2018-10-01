package pl.vrajani.model;

public class CryptoCurrencyStatus {

    String symbol;
    Double lastBuyPrice;
    Double lastSalePrice;
    int limitBuyCount = 0;
    int limitSellCount = 0;
    int durationSinceLastBuy;
    int durationSinceLastSell;

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

    public int getDurationSinceLastBuy() {
        return durationSinceLastBuy;
    }

    public void setDurationSinceLastBuy(int durationSinceLastBuy) {
        this.durationSinceLastBuy = durationSinceLastBuy;
    }

    public int getDurationSinceLastSell() {
        return durationSinceLastSell;
    }

    public void setDurationSinceLastSell(int durationSinceLastSell) {
        this.durationSinceLastSell = durationSinceLastSell;
    }

    public void decrementBuyDuration (){
        if(durationSinceLastBuy > 0){
            durationSinceLastBuy--;
        }
    }

    public void incrementBuyDuration (){
        if(durationSinceLastBuy < 1000){
            durationSinceLastBuy++;
        }
    }

    public void decrementSellDuration (){
        if(durationSinceLastSell > 0){
            durationSinceLastSell--;
        }
    }

    public void incrementSellDuration (){
        if(durationSinceLastSell < 1000){
            durationSinceLastSell++;
        }
    }
}
