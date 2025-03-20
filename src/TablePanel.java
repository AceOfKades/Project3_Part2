package src;
// GUI class

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TablePanel {

    public static void displayTable() {
        JFrame frame = new JFrame("E-Commerce Transactions Dataset");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        // Column Names
        String[] columnNames = {"ID", "Name", "Age", "Country", "Product Category", "Purchase Amount", "Payment Method", "Transaction Date"};

        // Table Model and JTable
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add Sample Data
        addData(model);

        //Set up Sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Assign numeric comparators dynamically for column 0 (ID) and 2 (Age)
        int[] numericColumns = {0, 2};
        for (int col : numericColumns) sorter.setComparator(col, createComparator("int"));
        // float comparator for column 5 (Purchase Amount)
        sorter.setComparator(5, createComparator("float"));
        // date comparator for column 7 (Transaction Date)
        sorter.setComparator(7, createComparator("date"));

        // Default sorting by ID (Ascending)
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));

        // Create filters dynamically
        Map<String, Integer> filterColumns = new LinkedHashMap<>();
        filterColumns.put(columnNames[3], 3); //country
        filterColumns.put(columnNames[4], 4); //product category
        filterColumns.put(columnNames[6], 6); //payment method

        Map<String, JComboBox<String>> filters = new HashMap<>();
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        for (Map.Entry<String, Integer> entry : filterColumns.entrySet()) {
            JComboBox<String> comboBox = createComboBoxFilter(entry.getKey());
            filters.put(entry.getKey(), comboBox);
            filterPanel.add(new JLabel(entry.getKey() + ": "));
            filterPanel.add(comboBox);
        }

        // Filtering logic
        ActionListener filterAction = e -> {
            ArrayList<RowFilter<Object, Object>> activeFilters = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : filterColumns.entrySet()) {
                String selectedValue = (String) filters.get(entry.getKey()).getSelectedItem();
                if (!"All".equals(selectedValue)) {
                    activeFilters.add(RowFilter.regexFilter(selectedValue, entry.getValue()));
                }
            }
            sorter.setRowFilter(activeFilters.isEmpty() ? null : RowFilter.andFilter(activeFilters));
        };

        // Attach event listeners to all filters
        filters.values().forEach(comboBox -> comboBox.addActionListener(filterAction));

        //stats panel

        //chart panel

        //details panel
        JTextArea detailsTextArea = new JTextArea(9, 20);
        detailsTextArea.setEditable(false);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    StringBuilder details = new StringBuilder();
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        details.append(table.getColumnName(i)).append(": ")
                                .append(table.getValueAt(row, i)).append("\n");
                    }
                    detailsTextArea.setText(details.toString());
                }
            }
        });

        // organize frame elements
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(detailsTextArea, BorderLayout.NORTH);

        frame.setLayout(new BorderLayout());
        frame.add(filterPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    // Method to create JComboBox filter
    private static JComboBox<String> createComboBoxFilter(String filterType) {
        Map<String, String[]> filterOptions = new HashMap<>();
        filterOptions.put("Product Category", new String[]{"All", "Beauty", "Books", "Clothing", "Electronics", "Grocery", "Home & Kitchen", "Sports", "Toys"});
        filterOptions.put("Payment Method", new String[]{"All", "Cash on Delivery", "Credit Card", "Debit Card", "Net Banking", "PayPal", "UPI"});
        filterOptions.put("Country", new String[]{"All", "Australia", "Brazil", "Canada", "France", "Germany", "India", "Japan", "Mexico", "UK", "USA"});

        JComboBox<String> comboBox = new JComboBox<>(filterOptions.getOrDefault(filterType, new String[]{"All"}));
        comboBox.setPreferredSize(new Dimension(120, 25));
        return comboBox;
    }

    // Generic comparator for sorting different data types
    private static Comparator<String> createComparator(String type) {
        return switch (type) {
            case "int" -> Comparator.comparingInt(Integer::parseInt);
            case "float" -> Comparator.comparingDouble(Double::parseDouble);
            case "date" -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                yield (str1, str2) -> {
                    try {
                        return dateFormat.parse(str1).compareTo(dateFormat.parse(str2));
                    } catch (ParseException e) {
                        return 0;
                    }
                };
            }
            default -> String::compareTo;
        };
    }

    // Loads dataset into the table
    private static void addData(DefaultTableModel model) {
        ArrayList<ArrayList<String>> dataset = DataProcess.tokenizeArray(DataProcess.createArray());
        dataset.forEach(dataLine -> model.addRow(dataLine.toArray()));
    }
}