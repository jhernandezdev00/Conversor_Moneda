package conversor.web.dto;

import conversor.model.Currency;

import java.time.Instant;
import java.util.List;

public record CurrenciesResponse(Instant lastUpdate, List<Currency> currencies) {
}