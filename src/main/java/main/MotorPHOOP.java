/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package main;
import jframes.*;
/**
 *
 * @author STUDY MODE
 */
public class MotorPHOOP {
    public static void main(String[] args) {
        // Set High DPI Awareness at runtime
        System.setProperty("sun.java2d.dpiAware", "true");

        // Ensure fixed size for JFrame
        LoginPage login = new LoginPage();
        login.setSize(960, 540); // Fixed size
        login.setResizable(false); // Prevent resizing
        login.setLocationRelativeTo(null); // Center window
        login.setVisible(true);
    }
}
