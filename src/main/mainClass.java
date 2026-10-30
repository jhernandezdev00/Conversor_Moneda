package main;

import Components.listadoDivisas;
import com.google.gson.Gson;
import config.AppConfig;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class mainClass {
    public static void main (String[] args) throws IOException, InterruptedException {
        AppConfig config = AppConfig.load();

        RatesUpadate actTasas = new RatesUpadate();
        Gson gson = new Gson();
        String response = String.valueOf(actTasas.RatesUpd("USD"));
        ConversorDivisa conversor = gson.fromJson(response, ConversorDivisa.class);

        mainFrame mFrame = new mainFrame();
        if(!(conversor.getResult().equals("success"))){
            JOptionPane.showMessageDialog(mFrame,config.text("mensajedeAdvertencia"),"ADVERTENCIA",JOptionPane.ERROR_MESSAGE);
        }

    }

}
