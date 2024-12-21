package Components;

import javax.swing.*;
import java.awt.*;

public class imagePanel {
    public void agregarImagenAlPanel(ImageIcon path, JPanel panel){
        panel.repaint();
        panel.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(path.getImage(), 0, 0, getWidth(), getHeight(), this);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 0, 0, 0),
                        0, getHeight(), new Color(0, 0, 0, 200));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        });
        panel.revalidate();
        panel.repaint();
    }
}
