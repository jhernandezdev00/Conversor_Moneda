package Components;

import com.google.gson.Gson;
import main.ConversorDivisa;
import main.RatesUpadate;

import java.io.IOException;
import java.util.Map;

public class conversor {
    private Double montoConvertido;
    private Double conversion_rates;

    private Gson gson = new Gson();
    private String response = "";
    private ConversorDivisa conversor = new ConversorDivisa();
    RatesUpadate actTasas = new RatesUpadate();


    public Double getConversion(String claveCountryOrg, String claveCountryDes, Double monto) throws IOException, InterruptedException {
        try{

            String status = "";
            response = String.valueOf(actTasas.RatesUpd(claveCountryOrg));
            System.out.println(response);
            conversor = gson.fromJson(response, ConversorDivisa.class);
            status = conversor.getResult();

            if(status.equals("error")){
                System.out.println(status);
                montoConvertido = Double.NaN;
            }else{
                System.out.println(status);
                conversion_rates = conversor.getConversion_ratEsp(claveCountryDes);
                System.out.println(conversion_rates);
                montoConvertido = conversion_rates*monto;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return montoConvertido;
    }
}
