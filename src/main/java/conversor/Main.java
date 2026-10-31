package conversor;

import com.google.gson.Gson;
import conversor.model.ExchangeRateResponse;
import conversor.service.ExchangeRateClient;
import conversor.ui.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main (String[] args){
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
