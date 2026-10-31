package conversor.web.dto;

import conversor.model.Conversion;

import java.math.BigDecimal;
import java.time.Instant;

public record ConversionResponse(String from, String to, String amount, String result,
                                 String rate, String inverseRate, Instant lastUpdate) {

    public static ConversionResponse of(Conversion c) {
        return new ConversionResponse(c.from(), c.to(), plain(c.amount()), plain(c.result()),
                plain(c.rate()), plain(c.inverseRate()), c.lastUpdate());
    }

    private static String plain(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}