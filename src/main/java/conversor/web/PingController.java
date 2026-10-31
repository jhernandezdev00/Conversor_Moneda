package conversor.web;

import conversor.service.ConversionService;
import conversor.service.CurrencyCatalog;
import conversor.service.ExchangeRateException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class PingController {
    private final ConversionService conversionService;
    private final CurrencyCatalog catalog;

    public PingController(ConversionService conversionService, CurrencyCatalog catalog) {
        this.conversionService = conversionService;
        this.catalog = catalog;
    }

    @GetMapping("/api/ping")
    public Map<String, Object> ping() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ok");
        response.put("divisas", catalog.all().size());
        try {
            response.put("tasasActualizadas", conversionService.rates().lastUpdate().toString());
        } catch (ExchangeRateException e) {
            response.put("errorTasas", e.reason().name());
        }
        return response;
    }
}