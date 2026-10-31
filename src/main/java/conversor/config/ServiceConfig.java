package conversor.config;

import conversor.service.ConversionService;
import conversor.service.CurrencyCatalog;
import conversor.service.ExchangeRateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ExchangeRateClient exchangeRateClient(@Value("${exchange.api-key}") String apiKey) {
        return new ExchangeRateClient(apiKey);
    }

    @Bean
    public ConversionService currencyConversionService(ExchangeRateClient client) {
        return new ConversionService(client);
    }

    @Bean
    public CurrencyCatalog currencyCatalog() {
        return CurrencyCatalog.load();
    }
}