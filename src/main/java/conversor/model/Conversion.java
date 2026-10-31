package conversor.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Conversion(String from, String to, BigDecimal amount, BigDecimal result,
                         BigDecimal rate, BigDecimal inverseRate, Instant lastUpdate) {
}