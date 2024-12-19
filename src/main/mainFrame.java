package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
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
        this.setSize(760,450);
        this.setLocationRelativeTo(null);
        //setResizable(false);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(760,450));
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

            this.setTitle(prop.getProperty("titulo"));

            Image imgTempEscal = imagen_Prin.getImage();
            Image imgUrlInit_Escalada = imgTempEscal.getScaledInstance(200,1000, Image.SCALE_SMOOTH);
            ImageIcon imgUrlInicio_Escalada = new ImageIcon(imgUrlInit_Escalada);
            imgPan.agregarImagenAlPanel(imgUrlInicio_Escalada,panelIzq);

            JLabel titulo = new JLabel();
            String letter = ("<html>" + (prop.getProperty("titulo")).replace("\n", "<br>") + "</html>");
            Font fontRaleway = Font.createFont(Font.TRUETYPE_FONT, new File(prop.getProperty("fontRal"))).deriveFont(30f);
            titulo.setFont(fontRaleway);

            titulo.setText(letter);

            titulo.setHorizontalAlignment(SwingConstants.CENTER);
            panelDer.add("North",titulo);

            JPanel contenedorPrincipal = new JPanel();
            contenedorPrincipal.setLayout(new BorderLayout());
            contenedorPrincipal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            contenedorPrincipal.setSize(new Dimension(20, 20));
            panelDer.add("Center",contenedorPrincipal);
            contenedorPrincipal.setBackground(new Color(237,251,205, 192));

            JLabel titulo_Enu = new JLabel(("<html>" + (prop.getProperty("etiquetaTitulo")).replace("\n", "<br>") + "</html>"));
            titulo_Enu.setFont(fontRaleway.deriveFont(20f));
            titulo_Enu.setHorizontalAlignment(SwingConstants.CENTER);
            //titulo_Enu.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
            contenedorPrincipal.add("North",titulo_Enu);


            JLabel etiqueta_Pie = new JLabel(("<html>" + (prop.getProperty("etiquetaPie")).replace("\n", "<br>") + "</html>"));
            etiqueta_Pie.setFont(fontRaleway.deriveFont(16f));
            etiqueta_Pie.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorPrincipal.add("South",etiqueta_Pie);

            JPanel panelContenedor = new JPanel();
            panelContenedor.setLayout(new GridBagLayout());
            panelContenedor.setBackground(Color.red);
            contenedorPrincipal.add("Center", panelContenedor);

            GridBagConstraints gbc = new GridBagConstraints();
            //gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel etiqueta_Instruccion = new JLabel(("<html>" + (prop.getProperty("etiquetaInstruccion")).replace("\n", "<br>") + "</html>"));
            etiqueta_Instruccion.setFont(fontRaleway.deriveFont(18f));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.CENTER;
            panelContenedor.add(etiqueta_Instruccion,gbc);

            JTextField registroCantidad = new JTextField();
            registroCantidad.setPreferredSize(new Dimension(160, 25));
            registroCantidad.setBorder(BorderFactory.createEmptyBorder());
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            panelContenedor.add(registroCantidad,gbc);

            JComboBox<String> tiposDivisaOrigen = new JComboBox<String> ();
            tiposDivisaOrigen.addItem("as");
            tiposDivisaOrigen.addItem("as1");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            panelContenedor.add(tiposDivisaOrigen,gbc);

            JComboBox<String>  tiposDivisaDestino = new JComboBox<String>();
            tiposDivisaDestino.addItem("as1");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST;
            panelContenedor.add(tiposDivisaDestino,gbc);

            JButton btnconversion = new JButton();
            btnconversion.setText("Convertir");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.CENTER;
            panelContenedor.add(btnconversion,gbc);
            

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
