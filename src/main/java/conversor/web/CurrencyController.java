package conversor.web;

import conversor.model.ExchangeRates;
import conversor.service.ConversionService;
import conversor.service.CurrencyCatalog;
import conversor.service.ExchangeRateException;
import conversor.web.dto.ConversionResponse;
import conversor.web.dto.CurrenciesResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class CurrencyController {
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000000000");

    private final ConversionService conversionService;
    private final CurrencyCatalog catalog;

    public CurrencyController(ConversionService conversionService, CurrencyCatalog catalog) {
        this.conversionService = conversionService;
        this.catalog = catalog;
    }

    @GetMapping("/currencies")
    public CurrenciesResponse currencies() throws ExchangeRateException {
        ExchangeRates rates = conversionService.rates();
        return new CurrenciesResponse(rates.lastUpdate(), catalog.supportedBy(rates));
    }

    @GetMapping("/convert")
    public ConversionResponse convert(@RequestParam String from,
                                      @RequestParam String to,
                                      @RequestParam BigDecimal amount) throws ExchangeRateException {
        if (amount.signum() < 0 || amount.compareTo(MAX_AMOUNT) > 0) {
            throw new BadRequestException("INVALID_AMOUNT", "error.amount");
        }
        String fromCode = from.trim().toUpperCase(Locale.ROOT);
        String toCode = to.trim().toUpperCase(Locale.ROOT);
        return ConversionResponse.of(conversionService.convert(amount, fromCode, toCode));
    }
}