import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TablePanel {

    public static void displayTable() {
        JFrame frame = new JFrame("E-Commerce Transactions Dataset");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1000);

        // Column Names
        String[] columnNames = {"ID", "Name", "Age", "Country", "Product Category", "Payment Method", "Transaction Date"};

        // Table Model (Dynamic Data)
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model); // JTable with data model

        // 🔹 Wrap JTable inside JScrollPane (FIX)
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER); // Add scrollPane to JFrame

        // Add Sample Data
        addData(model);

        frame.setVisible(true);
    }

    private static void addData(DefaultTableModel model) {
        ArrayList<ArrayList<String>> dataset = DataProcess.tokenizeArray(DataProcess.createArray());

        for(ArrayList<String> x : dataset){
            model.addRow(x.toArray());
        }
    }
}
