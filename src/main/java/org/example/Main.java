package org.example;

import org.example.GUI.main.MainWindow;
import javax.swing.SwingUtilities;

public class Main {
   public static void main(String[] args) {
      SwingUtilities.invokeLater(MainWindow::new);
   }
}
