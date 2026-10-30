package conversor;

import com.google.gson.Gson;
import conversor.model.ExchangeRateResponse;
import conversor.service.ExchangeRateClient;
import conversor.ui.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main (String[] args) throws IOException, InterruptedException {
        conversor.config.AppConfig config = conversor.config.AppConfig.load();

        ExchangeRateClient actTasas = new ExchangeRateClient();
        Gson gson = new Gson();
        String response = String.valueOf(actTasas.RatesUpd("USD"));
        ExchangeRateResponse conversor = gson.fromJson(response, ExchangeRateResponse.class);

        MainFrame mFrame = new MainFrame();
        if(!(conversor.getResult().equals("success"))){
            JOptionPane.showMessageDialog(mFrame,config.text("mensajedeAdvertencia"),"ADVERTENCIA",JOptionPane.ERROR_MESSAGE);
        }

    }

}
