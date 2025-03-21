package src;

import javax.swing.*;
import javax.swing.table.*;

//average amount spent
//number of transactions
//average age

public class StatsPanel extends JPanel {
    private JLabel totalTransactionsLabel, avgAmountLabel, avgAgeLabel;
    private JTable table;

    public StatsPanel(JTable table) {
        this.table = table;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        totalTransactionsLabel = new JLabel("Total Transactions: 0");
        avgAmountLabel = new JLabel("Average Spent: 0.0");
        avgAgeLabel = new JLabel("Average Age: 0");

        add(totalTransactionsLabel);
        add(Box.createVerticalStrut(5));
        add(avgAmountLabel);
        add(Box.createVerticalStrut(5));
        add(avgAgeLabel);

        // Attach listener for table updates
        attachTableListener();

        // Initial calculation
        updateStats();
    }

    private void attachTableListener() {
        // Recalculate stats when table data changes
        TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) table.getRowSorter();
        if (sorter != null) {
            sorter.addRowSorterListener(e -> updateStats());
        }
    }

    private void updateStats() {
        TableModel model = table.getModel();
        TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) table.getRowSorter();

        int rowCount = table.getRowCount();
        int amountColumnIndex = 5;
        int ageColumnIndex = 2;

        double totalAmount = 0.0;
        int totalAge = 0;

        for (int i = 0; i < rowCount; i++) {
            int modelRow = table.convertRowIndexToModel(i);
            Object value = model.getValueAt(modelRow, amountColumnIndex);
            Object age = model.getValueAt(modelRow, ageColumnIndex);

            if (value instanceof Number) {
                totalAmount += ((Number) value).doubleValue();
            } else {
                try {
                    totalAmount += Double.parseDouble(value.toString());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format at row " + modelRow + ": " + value);
                }
            }

            if (age instanceof Number) {
                totalAge += ((Number) age).intValue();
            }else{
                try {
                    totalAge += Integer.parseInt(age.toString());
                }catch (NumberFormatException e){
                    System.err.println("Invalid number format at row " + modelRow + ": " + age);
                }
            }
        }

        double avgAmount = (rowCount > 0) ? totalAmount / rowCount : 0.0;
        int avgAge = (rowCount>0) ? totalAge / rowCount : 0;

        // Update labels
        totalTransactionsLabel.setText("Total Transactions: " + rowCount);
        avgAmountLabel.setText(String.format("Average Amount: %.2f", avgAmount));
        avgAgeLabel.setText("Average Age: " + avgAge);
    }

}
