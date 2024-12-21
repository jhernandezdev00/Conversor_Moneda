package main;

import Components.RoundedButton;
import Components.conversor;
import Components.imagePanel;
import Components.listadoDivisas;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;

public class mainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel panelIzq;
    private JPanel panelDer;
    private ImageIcon imagen_Prin;
    private RoundedButton btnconversion = new RoundedButton();
    private JComboBox<String>  tiposDivisaDestino = new JComboBox<String>();
    private JComboBox<String> tiposDivisaOrigen = new JComboBox<String> ();
    private JTextField registroCantidad = new JTextField();
    private JPanel contenedorPrincipal = new JPanel();
    private JPanel panelContenedor = new JPanel();
    private Gson gson = new Gson();
    private listadoDivisas listDivisas = new listadoDivisas();
    private conversor conv = new conversor();
    private Properties prop = new Properties();
    private JLabel texto = new JLabel();
    private JPanel resultados = new JPanel();

    private JLabel resultado_Titulo = new JLabel();
    private RoundedButton btnRegresar = new RoundedButton();

    public mainFrame(){
        this.setContentPane(mainPanel);
        configurarFrame();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(760,450);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setMinimumSize(new Dimension(760,450));
    }

    private void configurarFrame() {
        imagePanel imgPan = new imagePanel();

        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setPreferredSize(new Dimension(200, 550));
        panelDer.setPreferredSize(new Dimension(500,550));
        panelDer.setLayout(new BorderLayout());
        panelDer.setBackground(new Color(237,251,205, 255));

        try{

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


            contenedorPrincipal.setLayout(new BorderLayout());
            contenedorPrincipal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            contenedorPrincipal.setSize(new Dimension(20, 20));
            panelDer.add("Center",contenedorPrincipal);
            contenedorPrincipal.setBackground(new Color(237,251,205, 192));

            JLabel titulo_Enu = new JLabel(("<html>" + (prop.getProperty("etiquetaTitulo")).replace("\n", "<br>") + "</html>"));
            titulo_Enu.setFont(fontRaleway.deriveFont(20f));
            titulo_Enu.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorPrincipal.add("North",titulo_Enu);

            JLabel etiqueta_Pie = new JLabel(("<html>" + (prop.getProperty("etiquetaPie")).replace("\n", "<br>") + "</html>"));
            etiqueta_Pie.setFont(fontRaleway.deriveFont(16f));
            etiqueta_Pie.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorPrincipal.add("South",etiqueta_Pie);

            panelContenedor.setLayout(new GridBagLayout());
            panelContenedor.setBackground(new Color(237,251,205, 192));
            contenedorPrincipal.add("Center", panelContenedor);
            GridBagConstraints gbc = new GridBagConstraints();

            JLabel etiqueta_Instruccion = new JLabel(("<html>" + (prop.getProperty("etiquetaInstruccion")).replace("\n", "<br>") + "</html>"));
            etiqueta_Instruccion.setFont(fontRaleway.deriveFont(18f));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.CENTER;
            panelContenedor.add(etiqueta_Instruccion,gbc);


            registroCantidad.setPreferredSize(new Dimension(160, 25));
            registroCantidad.setBorder(BorderFactory.createEmptyBorder());
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            panelContenedor.add(registroCantidad,gbc);


            FileReader jsonDivisas = new FileReader("src/Components/Divisas.json");
            listDivisas = gson.fromJson(jsonDivisas, listadoDivisas.class);
            Collection<String> nameCountrys = listDivisas.getCountryList();
            for (String valor : nameCountrys) {
                tiposDivisaOrigen.addItem(valor);
                tiposDivisaDestino.addItem(valor);
            }

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            panelContenedor.add(tiposDivisaOrigen,gbc);

            texto.setText("a");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.CENTER;
            panelContenedor.add(texto,gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.WEST;
            panelContenedor.add(tiposDivisaDestino,gbc);

            btnconversion.setText(prop.getProperty("botonConvertir"));
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.CENTER;
            panelContenedor.add(btnconversion,gbc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contenedorPrincipal.setVisible(true);
                resultados.removeAll();
            }
        });

        btnconversion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cantidad = registroCantidad.getText();

                if(!cantidad.isEmpty()){
                    Double cantidadDouble = Double.parseDouble(cantidad);
                    String CountryRate1 = String.valueOf(tiposDivisaOrigen.getSelectedItem());
                    String CountryRate2 = String.valueOf(tiposDivisaDestino.getSelectedItem());

                    String claveCountryOrg = listDivisas.getCodCountry(CountryRate1);
                    String claveCountryDes = listDivisas.getCodCountry(CountryRate2);

                    System.out.println(claveCountryOrg);
                    System.out.println(claveCountryDes);
                    try {
                        final Double montoConvertido = conv.getConversion(claveCountryOrg,claveCountryDes,cantidadDouble);

                        if(!Double.isNaN(montoConvertido)){

                            panelDer.add(resultados);
                            contenedorPrincipal.setVisible(false);
                            resultados.setVisible(true);

                            resultados.setLayout(new GridBagLayout());
                            GridBagConstraints gbc1 = new GridBagConstraints();
                            resultados.setBackground(new Color(237,251,205, 192));

                            gbc1.gridx = 0;
                            gbc1.gridy = 0;
                            gbc1.anchor = GridBagConstraints.CENTER;
                            JLabel resultadoConversion = new JLabel(prop.getProperty("mensajeConversion"));
                            gbc1.insets = new Insets(10, 10, 10, 10);
                            resultadoConversion.setFont(new Font("Arial", Font.BOLD, 30));
                            resultados.add(resultadoConversion,gbc1);

                            gbc1.gridx = 0;
                            gbc1.gridy = 1;
                            gbc1.anchor = GridBagConstraints.CENTER;
                            JLabel divisasConversion = new JLabel("$ "+cantidadDouble+" "+CountryRate1+" = ");
                            divisasConversion.setFont(new Font("Arial", Font.BOLD, 28));
                            resultados.add(divisasConversion,gbc1);

                            gbc1.gridx = 0;
                            gbc1.gridy = 2;
                            gbc1.anchor = GridBagConstraints.CENTER;
                            JLabel divisasConvertida = new JLabel("$ "+montoConvertido+" "+CountryRate2);
                            divisasConvertida.setFont(new Font("Arial", Font.BOLD, 22));
                            resultados.add(divisasConvertida,gbc1);

                            btnRegresar.setText(prop.getProperty("regresarMenu"));
                            gbc1.gridx = 0;
                            gbc1.gridy = 3;
                            gbc1.anchor = GridBagConstraints.CENTER;
                            resultados.add(btnRegresar,gbc1);

                        }else {
                            JOptionPane.showMessageDialog(panelContenedor,"ADVERTENCIA","ADVERTENCIA",JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        registroCantidad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (((caracter < '0') || (caracter > '9')) && (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != '.' || registroCantidad.getText().contains(".")) ) {
                    System.out.println("Solo se permiten números");
                    e.consume();
                }
            }
        });

    }
}




