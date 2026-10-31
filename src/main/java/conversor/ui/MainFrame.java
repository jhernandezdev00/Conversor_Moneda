package conversor.ui;

import conversor.config.AppConfig;
import conversor.config.Resources;
import conversor.model.Currency;
import conversor.service.ConversionService;
import conversor.service.CurrencyCatalog;
import conversor.service.ExchangeRateClient;
import conversor.service.ExchangeRateException;
import conversor.ui.components.ImagePanel;
import conversor.ui.components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel panelIzq;
    private JPanel panelDer;
    private ImageIcon imagen_Prin;
    private RoundedButton btnconversion = new RoundedButton();
    private JComboBox<Currency> tiposDivisaDestino = new JComboBox<>();
    private JComboBox<Currency> tiposDivisaOrigen = new JComboBox<>();
    private JTextField registroCantidad = new JTextField();
    private JPanel contenedorPrincipal = new JPanel();
    private JPanel panelContenedor = new JPanel();

    private final AppConfig config = AppConfig.load();
    private final CurrencyCatalog catalogo = CurrencyCatalog.load();
    private final ConversionService conv = new ConversionService(new ExchangeRateClient(config.apiKey()));

    private JLabel texto = new JLabel();
    private JPanel resultados = new JPanel();
    private RoundedButton btnRegresar = new RoundedButton();

    public MainFrame(){
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0x615D60));

        panelIzq = new JPanel(new BorderLayout());
        panelIzq.setPreferredSize(new Dimension(250, 200));

        panelDer = new JPanel(new BorderLayout());

        mainPanel.add(panelIzq, BorderLayout.WEST);
        mainPanel.add(panelDer, BorderLayout.CENTER);

        this.setContentPane(mainPanel);
        configurarFrame();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(760,450);
        this.setLocationRelativeTo(null);
        this.setIconImage(Resources.imagen(Resources.ICONO_APP).getImage());
        this.setResizable(false);
        this.setMinimumSize(new Dimension(760,450));
        this.setVisible(true);
    }

    private void configurarFrame() {
        ImagePanel imgPan = new ImagePanel();

        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setPreferredSize(new Dimension(200, 550));
        panelDer.setPreferredSize(new Dimension(500,550));
        panelDer.setLayout(new BorderLayout());
        panelDer.setBackground(new Color(237,251,205, 255));

        try{
            imagen_Prin = Resources.imagen(Resources.IMAGEN_INICIO);

            this.setTitle(config.text("titulo"));

            Image imgTempEscal = imagen_Prin.getImage();
            Image imgUrlInit_Escalada = imgTempEscal.getScaledInstance(200,1000, Image.SCALE_SMOOTH);
            ImageIcon imgUrlInicio_Escalada = new ImageIcon(imgUrlInit_Escalada);
            imgPan.agregarImagenAlPanel(imgUrlInicio_Escalada,panelIzq);

            JLabel titulo = new JLabel();
            String letter = ("<html>" + (config.text("titulo")).replace("\n", "<br>") + "</html>");
            Font fontRaleway = Resources.fuente(Resources.FUENTE_RALEWAY, 30f);
            titulo.setFont(fontRaleway);
            titulo.setText(letter);
            titulo.setHorizontalAlignment(SwingConstants.CENTER);
            panelDer.add("North",titulo);

            contenedorPrincipal.setLayout(new BorderLayout());
            contenedorPrincipal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            contenedorPrincipal.setSize(new Dimension(20, 20));
            panelDer.add("Center",contenedorPrincipal);
            contenedorPrincipal.setBackground(new Color(237,251,205, 192));

            JLabel titulo_Enu = new JLabel(("<html>" + (config.text("etiquetaTitulo")).replace("\n", "<br>") + "</html>"));
            titulo_Enu.setFont(fontRaleway.deriveFont(20f));
            titulo_Enu.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorPrincipal.add("North",titulo_Enu);

            JLabel etiqueta_Pie = new JLabel(("<html>" + (config.text("etiquetaPie")).replace("\n", "<br>") + "</html>"));
            etiqueta_Pie.setFont(fontRaleway.deriveFont(16f));
            etiqueta_Pie.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorPrincipal.add("South",etiqueta_Pie);

            panelContenedor.setLayout(new GridBagLayout());
            panelContenedor.setBackground(new Color(237,251,205, 192));
            contenedorPrincipal.add("Center", panelContenedor);
            GridBagConstraints gbc = new GridBagConstraints();

            JLabel etiqueta_Instruccion = new JLabel(("<html>" + (config.text("etiquetaInstruccion")).replace("\n", "<br>") + "</html>"));
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

            for (Currency divisa : catalogo.all()) {
                tiposDivisaOrigen.addItem(divisa);
                tiposDivisaDestino.addItem(divisa);
            }
            tiposDivisaOrigen.setSelectedItem(buscar("USD"));
            tiposDivisaDestino.setSelectedItem(buscar("MXN"));

            tiposDivisaOrigen.setBackground((new Color(237,251,205, 192)));
            tiposDivisaOrigen.setForeground(Color.black);
            tiposDivisaDestino.setBackground((new Color(237,251,205, 192)));
            tiposDivisaDestino.setForeground(Color.black);

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

            btnconversion.setText(config.text("botonConvertir"));
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
                if (cantidad.isEmpty()) {
                    return;
                }

                BigDecimal monto;
                try {
                    monto = new BigDecimal(cantidad);
                } catch (NumberFormatException ex) {
                    return;
                }

                Currency origen = (Currency) tiposDivisaOrigen.getSelectedItem();
                Currency destino = (Currency) tiposDivisaDestino.getSelectedItem();
                String claveOrigen = origen.code();
                String claveDestino = destino.code();
                String nombreOrigen = origen.name();
                String nombreDestino = destino.name();

                try {
                    BigDecimal montoConvertido = conv.convert(monto, claveOrigen, claveDestino)
                            .setScale(2, RoundingMode.HALF_UP);

                    panelDer.add(resultados);
                    contenedorPrincipal.setVisible(false);
                    resultados.setVisible(true);

                    resultados.setLayout(new GridBagLayout());
                    GridBagConstraints gbc1 = new GridBagConstraints();
                    resultados.setBackground(new Color(237,251,205, 192));

                    gbc1.gridx = 0;
                    gbc1.gridy = 0;
                    gbc1.anchor = GridBagConstraints.CENTER;
                    JLabel resultadoConversion = new JLabel(config.text("mensajeConversion"));
                    gbc1.insets = new Insets(10, 10, 10, 10);
                    resultadoConversion.setFont(new Font("Arial", Font.BOLD, 30));
                    resultados.add(resultadoConversion,gbc1);

                    gbc1.gridx = 0;
                    gbc1.gridy = 1;
                    gbc1.anchor = GridBagConstraints.CENTER;
                    JLabel divisasConversion = new JLabel("$ " + monto.toPlainString() + " " + nombreOrigen + " = ");
                    divisasConversion.setFont(new Font("Arial", Font.BOLD, 28));
                    resultados.add(divisasConversion,gbc1);

                    gbc1.gridx = 0;
                    gbc1.gridy = 2;
                    gbc1.anchor = GridBagConstraints.CENTER;
                    JLabel divisasConvertida = new JLabel("$ " + montoConvertido.toPlainString() + " " + nombreDestino);
                    divisasConvertida.setFont(new Font("Arial", Font.BOLD, 22));
                    resultados.add(divisasConvertida,gbc1);

                    btnRegresar.setText(config.text("regresarMenu"));
                    gbc1.gridx = 0;
                    gbc1.gridy = 3;
                    gbc1.anchor = GridBagConstraints.CENTER;
                    resultados.add(btnRegresar,gbc1);

                } catch (ExchangeRateException ex) {
                    JOptionPane.showMessageDialog(mainPanel, mensajeDeError(ex),
                            config.text("tituloError"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registroCantidad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (((caracter < '0') || (caracter > '9')) && (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != '.' || registroCantidad.getText().contains(".")) ) {
                    e.consume();
                }
            }
        });
    }

    private Currency buscar(String codigo) {
        for (Currency c : catalogo.all()) {
            if (c.code().equals(codigo)) {
                return c;
            }
        }
        return null;
    }

    private String mensajeDeError(ExchangeRateException ex) {
        return switch (ex.reason()) {
            case MISSING_KEY -> config.text("error.missingKey");
            case NETWORK -> config.text("error.network");
            case INVALID_KEY -> config.text("error.invalidKey");
            case QUOTA_REACHED -> config.text("error.quota");
            case INACTIVE_ACCOUNT -> config.text("error.inactive");
            case UNSUPPORTED_CODE -> config.text("error.unsupported", ex.detail());
            case UNKNOWN -> config.text("error.unknown", ex.detail());
        };
    }
}