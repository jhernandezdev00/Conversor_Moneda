package main;

import Components.listadoDivisas;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class mainClass {
    public static void main (String[] args) throws IOException, InterruptedException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/resource/archivosTexto.properties"));

        RatesUpadate actTasas = new RatesUpadate();
        Gson gson = new Gson();
        String response = String.valueOf(actTasas.RatesUpd("USD"));
        ConversorDivisa conversor = gson.fromJson(response, ConversorDivisa.class);

        mainFrame mFrame = new mainFrame();
        if(!(conversor.getResult().equals("success"))){
            JOptionPane.showMessageDialog(mFrame,prop.getProperty("mensajedeAdvertencia"),"ADVERTENCIA",JOptionPane.ERROR_MESSAGE);
        }

    }

}
