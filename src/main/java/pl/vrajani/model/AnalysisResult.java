package pl.vrajani.model;

public enum AnalysisResult {

    UNKNOWN("n/a"),
    BUY("buy"),
    SELL("sell");

    String result;
    AnalysisResult(String s) {
        result = s;
    }
    public String getReason() {
        return result;
    }
}
