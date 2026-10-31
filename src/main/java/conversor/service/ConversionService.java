package conversor.service;

import conversor.model.Conversion;
import conversor.model.ExchangeRates;
import conversor.service.ExchangeRateException.Reason;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.List;

public class ConversionService {
    private static final String BASE_CODE = "USD";

    private final ExchangeRateClient client;
    private volatile ExchangeRates cached;

    public ConversionService(ExchangeRateClient client) {
        this.client = client;
    }

    public synchronized ExchangeRates rates() throws ExchangeRateException {
        if (cached == null || cached.isStale(Instant.now())) {
            cached = client.fetchLatest(BASE_CODE);
        }
        return cached;
    }

    public Conversion convert(BigDecimal amount, String from, String to) throws ExchangeRateException {
        ExchangeRates rates = rates();
        for (String code : List.of(from, to)) {
            if (!rates.supports(code)) {
                throw new ExchangeRateException(Reason.UNSUPPORTED_CODE, code);
            }
        }
        BigDecimal rate = rates.rate(from, to);
        BigDecimal inverse = BigDecimal.ONE.divide(rate, MathContext.DECIMAL64);
        BigDecimal result = amount.multiply(rate, MathContext.DECIMAL64);
        return new Conversion(from, to, amount, result, rate, inverse, rates.lastUpdate());
    }
}