package org.example.GUI.panels;

import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JPanel {
    private static final String FONT_NAME = "Arial";

    public AboutPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("About This Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea aboutText = new JTextArea();
        aboutText.setText("\n\nInventory Invoice Management System\n\n"
                + "Version: 1.0.0\n\n"
                + "This application manages inventory and invoices.\n\n"
                + "Features:\n"
                + "• Product Management\n"
                + "• Invoice Creation\n"
                + "• Invoice History\n"
                + "• Stock Management\n\n"
                + "Created with Java Swing.\n\n"
                + "© 2026 - All rights reserved");
        aboutText.setEditable(false);
        aboutText.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setMargin(new Insets(10, 30, 10, 30));
        aboutText.setBackground(new Color(255, 250, 240));

        add(titleLabel, BorderLayout.NORTH);
        add(aboutText, BorderLayout.CENTER);
    }
}
