package conversor.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.Map;

public record ExchangeRates(String baseCode, Instant lastUpdate, Instant nextUpdate,
                            Map<String, BigDecimal> rates) {

    public ExchangeRates {
        rates = Map.copyOf(rates);
    }

    public boolean supports(String code) {
        BigDecimal rate = rates.get(code);
        return rate != null && rate.signum() > 0;
    }

    public BigDecimal rate(String from, String to) {
        if (!supports(from) || !supports(to)) {
            throw new IllegalArgumentException("Divisa no soportada: " + from + " o " + to);
        }
        if (from.equals(to)) {
            return BigDecimal.ONE;
        }
        return rates.get(to).divide(rates.get(from), MathContext.DECIMAL64);
    }

    public BigDecimal convert(BigDecimal amount, String from, String to) {
        return amount.multiply(rate(from, to), MathContext.DECIMAL64);
    }

    public boolean isStale(Instant now) {
        return now.isAfter(nextUpdate);
    }
}