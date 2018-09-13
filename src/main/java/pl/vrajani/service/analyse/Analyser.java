package pl.vrajani.service.analyse;

import pl.vrajani.model.CryptoCurrency;

@FunctionalInterface
public interface Analyser {
    boolean analyse(CryptoCurrency cryptoCurrency);

    default Double getPercent(Double source, Double base){
        if (source == null || base == null || base == 0){
            return 0.0;
        }
        return (source * 100)/base;
    }
}
