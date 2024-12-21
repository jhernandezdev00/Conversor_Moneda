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

        //RatesUpadate actTasas = new RatesUpadate();
        //Gson gson = new Gson();
        //String response = String.valueOf(actTasas.RatesUpd());
        //ConversorDivisa conversor = gson.fromJson(response, ConversorDivisa.class);

        mainFrame mFrame = new mainFrame();

        //if(conversor.getResult().equals("error")){
            //JOptionPane.showMessageDialog(mFrame,prop.getProperty("mensaje_sinConexion"));
        //}

        //Map <String, Double> rates = conversor.getConversion_rates();
        //System.out.println(rates.get("EUR"));
        //System.out.println(conversor.getResult());
    }

}
