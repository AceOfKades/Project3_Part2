package src;
//GUI class

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
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

        // Table Model (Dynamic Data)
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model); // JTable with data model
        JScrollPane scrollPane = new JScrollPane(table); //holds table
        JPanel panel = new JPanel(); //for holding filters

        // Add Sample Data
        addData(model);

        //Set up Sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        //Create integer comparator for columns 0 (ID number) and 2 (Age)
        Comparator<String> numericComparator = (str1, str2) -> {
            Integer num1 = Integer.parseInt(str1);
            Integer num2 = Integer.parseInt(str2);
            return num1.compareTo(num2);
        };

        //Create float comparator for column 5 (Purchase Amount)
        Comparator<String> floatComparator = (str1, str2) -> {
            Float flo1 = Float.parseFloat(str1);
            Float flo2 = Float.parseFloat(str2);
            return flo1.compareTo(flo2);
        };

        //create date formatting
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false); //be strict about date parse

        //Create date comparator for column 7 (Transaction date)
        Comparator<String> dateComparator = (str1, str2) ->{
            try {       //fiddly magic to keep parse happy (assumes an unhandled exception without the try statement
                Date date1 = dateFormat.parse(str1);
                Date date2 = dateFormat.parse(str2);
                return date1.compareTo(date2);
            } catch (ParseException e){
                return 0; //keep original order if parse fails
            }
        };

        //set sorters to comparators for columns
        sorter.setComparator(0, numericComparator);
        sorter.setComparator(2, numericComparator);
        sorter.setComparator(5, floatComparator);
        sorter.setComparator(7, dateComparator);


        //Set default to sort by ID (Ascending)
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        /*TO DO
        Create toggleable filters for:
        * [] Product Category
        * [] Payment Method
        * [] Name
        * [] Country
        * [] Year of Transaction*/

        //add frame and panel elements
        frame.add(scrollPane, BorderLayout.CENTER); // Add scrollPane to JFrame

        //display frame
        frame.setVisible(true);
    }

    private static void addData(DefaultTableModel model) {
        ArrayList<ArrayList<String>> dataset = DataProcess.tokenizeArray(DataProcess.createArray());

        for(ArrayList<String> dataLine : dataset){
            model.addRow(dataLine.toArray());
        }
    }
}
