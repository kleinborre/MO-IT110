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

public class grayButton extends JButton{
    
    private boolean hover = false;

    public grayButton() {
        setContentAreaFilled(false);
        setForeground(Color.BLACK); // Initial font color
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setFont(new Font("Arial", Font.BOLD, 14)); // Initial font

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                setForeground(new Color(40, 40, 40)); // Darker text on hover
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                setForeground(Color.BLACK); // Reset font color
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

        if (hover) {
            // Faded effect on hover
            g2.setColor(new Color(174, 178, 181, 150)); // #aeb2b5 with 150 alpha
        } else {
            // Normal button color
            g2.setColor(new Color(174, 178, 181)); // #aeb2b5
        }

        g2.fillRoundRect(0, 0, width, height, height, height);
        grphcs.drawImage(img, 0, 0, null);
        super.paintComponent(grphcs);
    }
    
}
