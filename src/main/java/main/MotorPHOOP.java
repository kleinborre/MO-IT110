package main;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.UIManager;
import jframes.*;

/**
 *
 * @author STUDY MODE
 */
public class MotorPHOOP {
    public static void main(String[] args) {
        // Remove forced DPI settings
        // System.setProperty("sun.java2d.dpiAware", "true");
        // System.setProperty("sun.java2d.uiScale", "1.0");

        // Set Look and Feel to System Default (Optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch the Login Page
        EventQueue.invokeLater(() -> {
            LoginPage login = new LoginPage();
            login.setSize(960, 540); // Fixed size
            login.setMinimumSize(new Dimension(960, 540)); // Prevent downsizing
            login.setPreferredSize(new Dimension(960, 540));
            login.setResizable(false); // Prevent user resizing
            login.setLocationRelativeTo(null); // Center window
            login.setVisible(true);
        });
    }
}