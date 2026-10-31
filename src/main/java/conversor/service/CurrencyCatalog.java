package conversor.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import conversor.config.Resources;
import conversor.model.Currency;
import conversor.model.ExchangeRates;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class CurrencyCatalog {
    private final List<Currency> currencies;

    private CurrencyCatalog(List<Currency> currencies) {
        this.currencies = List.copyOf(currencies);
    }

    public static CurrencyCatalog load() {
        try (Reader reader = Resources.lector(Resources.DIVISAS_JSON)) {
            Type type = new TypeToken<Map<String, String>>() { }.getType();
            Map<String, String> names = new Gson().fromJson(reader, type);

            Collator collator = Collator.getInstance(Locale.forLanguageTag("es-MX"));
            List<Currency> sorted = names.entrySet().stream()
                    .map(e -> new Currency(e.getKey(), e.getValue()))
                    .sorted(Comparator.comparing(Currency::name, collator))
                    .toList();
            return new CurrencyCatalog(sorted);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + Resources.DIVISAS_JSON, e);
        }
    }

    public List<Currency> all() {
        return currencies;
    }

    public List<Currency> supportedBy(ExchangeRates rates) {
        return currencies.stream().filter(c -> rates.supports(c.code())).toList();
    }
}