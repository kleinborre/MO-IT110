package buttons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class redButton extends JButton {
     private boolean hover = false;
    private final Color normalColor = new Color(190, 30, 45);  // #BE1E2D
    private final Color hoverColor = new Color(190, 30, 45, 180); // Slightly transparent when hovering

    public redButton() {
        setContentAreaFilled(false);
        setForeground(Color.WHITE); // Adjusted font color for contrast
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setFont(new Font("Arial", Font.BOLD, 14));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                setForeground(new Color(255, 230, 230)); // Lighter font color on hover
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                setForeground(Color.WHITE); // Reset font color
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        int width = getWidth();
        int height = getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(hover ? hoverColor : normalColor);
        g2.fillRoundRect(0, 0, width, height, height, height);

        grphcs.drawImage(img, 0, 0, null);
        super.paintComponent(grphcs);
    }
}
