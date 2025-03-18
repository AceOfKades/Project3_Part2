package src;//GUI Driver

import javax.swing.*;

public class DataDisplay {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TablePanel::displayTable);
    }
}
