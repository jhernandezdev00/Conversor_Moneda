package conversor.service;

import com.google.gson.Gson;
import conversor.model.ExchangeRateResponse;

import java.io.IOException;

public class ConversionService {
    private Double montoConvertido;
    private Double conversion_rates;

    private Gson gson = new Gson();
    private String response = "";
    private ExchangeRateResponse conversor = new ExchangeRateResponse();
    ExchangeRateClient actTasas = new ExchangeRateClient();


    public Double getConversion(String claveCountryOrg, String claveCountryDes, Double monto) throws IOException, InterruptedException {
        try{

            //String status = "";
            //response = String.valueOf(actTasas.RatesUpd(claveCountryOrg));
            //System.out.println(response);
            //conversor = gson.fromJson(response, ExchangeRateResponse.class);
            //status = conversor.getResult();

            response = String.valueOf(actTasas.RatesUpd(claveCountryOrg));
            System.out.println(response);
            conversor = gson.fromJson(response, ExchangeRateResponse.class);


            if (!conversor.isSuccess()) {
                System.out.println(conversor.getErrorType());
                montoConvertido = Double.NaN;
            } else {
                conversion_rates = conversor.getRate(claveCountryDes);
                System.out.println(conversion_rates);
                montoConvertido = conversion_rates * monto;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return montoConvertido;
    }
}
