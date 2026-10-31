package conversor.service;

import conversor.model.ExchangeRates;
import conversor.service.ExchangeRateException.Reason;

import java.math.BigDecimal;
import java.time.Instant;

public class ConversionService {
    private static final String BASE_CODE = "USD";

    private final ExchangeRateClient client;
    private volatile ExchangeRates cached;

    public ConversionService(ExchangeRateClient client) {
        this.client = client;
    }

    public synchronized ExchangeRates rates() throws ExchangeRateException {
        if (cached == null || cached.isStale(Instant.now())) {
            System.out.println("Descargando tasas...");
            cached = client.fetchLatest(BASE_CODE);
        }
        return cached;
    }

    public BigDecimal convert(BigDecimal amount, String from, String to) throws ExchangeRateException {
        ExchangeRates rates = client.fetchLatest(BASE_CODE);
        for (String code : new String[] {from, to}) {
            if (!rates.supports(code)) {
                throw new ExchangeRateException(Reason.UNSUPPORTED_CODE, code);
            }
        }
        return rates.convert(amount, from, to);
    }
}