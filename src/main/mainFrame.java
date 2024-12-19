package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class mainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel panelIzq;
    private JPanel panelDer;
    private ImageIcon imagen_Prin;

    public mainFrame(){
        this.setContentPane(mainPanel);
        configurarFrame();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(750,550);
        this.setLocationRelativeTo(null);
        //setResizable(false);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(700,450));
    }

    private void configurarFrame() {
        imagePanel imgPan = new imagePanel();

        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setPreferredSize(new Dimension(200, 550));
        panelDer.setPreferredSize(new Dimension(900,550));
        panelDer.setLayout(new BorderLayout());
        panelDer.setBackground(new Color(237,251,205, 255));

        try{
            Properties prop = new Properties();
            prop.load(new FileInputStream("src/resource/archivosTexto.properties"));
            imagen_Prin = new ImageIcon(prop.getProperty("urlImageInicio"));

            Image imgTempEscal = imagen_Prin.getImage();
            Image imgUrlInit_Escalada = imgTempEscal.getScaledInstance(200,1000, Image.SCALE_SMOOTH);
            ImageIcon imgUrlInicio_Escalada = new ImageIcon(imgUrlInit_Escalada);
            imgPan.agregarImagenAlPanel(imgUrlInicio_Escalada,panelIzq);

            JLabel titulo = new JLabel();
            String letter = ("<html>" + (prop.getProperty("titulo")).replace("\n", "<br>") + "</html>");
            Font fontRaleway = Font.createFont(Font.TRUETYPE_FONT, new File(prop.getProperty("fontRal"))).deriveFont(30f);
            titulo.setFont(fontRaleway);

            titulo.setText(letter);

            titulo.setHorizontalAlignment(SwingConstants.RIGHT);
            panelDer.add("North",titulo);

            JPanel contenedorPrincipal = new JPanel();
            //contenedorPrincipal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            contenedorPrincipal.setSize(new Dimension(20, 20));
            panelDer.add("Center",contenedorPrincipal);
            contenedorPrincipal.setBackground(new Color(237,251,205, 192));



        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void main (String[] args){
        mainFrame mFrame = new mainFrame();
        //System.out.println(mFrame.returnText());
    }
}

//public String returnText(){
//String a1 = titular.getText();
//return a1;
//}
