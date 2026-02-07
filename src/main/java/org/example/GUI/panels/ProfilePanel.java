package org.example.GUI.panels;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private static final String FONT_NAME = "Arial";

    public ProfilePanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save Profile");
        saveButton.setPreferredSize(new Dimension(150, 30));
        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Profile saved!"));
        add(saveButton, gbc);
    }
}
