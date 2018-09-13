package pl.vrajani.model;

public class CryptoCurrency extends CryptoCurrencyStatus {

    private String symbol;
    private Double price;
    private Double day1diff;
    private Double hour1diff;
    private Double cost;

    public CryptoCurrency(CryptoCurrencyBuilder builder){
        this.symbol = builder.symbol;
        this.price = builder.price;
        this.day1diff = builder.day1diff;
        this.hour1diff = builder.hour1diff;
        this.lastBuyPrice = builder.lastBuyPrice;
        this.lastSalePrice = builder.lastSalePrice;
        this.avgCost = builder.avgCost;
        this.equity = builder.equity;
        this.count = builder.count;
        this.cost = builder.cost;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDay1diff() {
        return day1diff;
    }

    public Double getHour1diff() {
        return hour1diff;
    }

    public Double getCost() {
        return cost;
    }

    @Override
    public String toString(){
        return new StringBuilder().append("{[symbol=").append(symbol)
                .append("],[price=").append(price)
                .append("],[day1diff=").append(day1diff)
                .append("],[hour1diff=").append(hour1diff)
                .append("],[lastBuyPrice=").append(lastBuyPrice)
                .append("],[lastSalePrice=").append(lastSalePrice)
                .append("],[avgCost=").append(avgCost)
                .append("],[equity=").append(equity)
                .append("],[count=").append(count)
                .append("]}").toString();
    }
}
