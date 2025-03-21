package src;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class ChartPanel extends JPanel {
    private CategoryChart chart;
    private XChartPanel<CategoryChart> chartPanel;
    private JTable table;

    public ChartPanel(JTable table) {
        this.table = table;

        // Create a smaller chart
        chart = new CategoryChartBuilder()
                .width(450)  // Set smaller width for the chart
                .height(700) // Set smaller height for the chart
                .title("Transaction Amounts")
                .xAxisTitle("Category")
                .yAxisTitle("Average Amount")
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        // Style the chart
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLabelRotation(90); // Rotate x-axis labels by 45 degrees
        chart.getStyler().setYAxisDecimalPattern("#.##");
        chart.getStyler().setYAxisMin((double) 400);

        // Initialize chartPanel with the smaller chart
        chartPanel = new XChartPanel<>(chart);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        // Initial chart update
        updateChart();
    }

    // Method to update the chart based on filtered data from the table
    public void updateChart() {
        TableModel model = table.getModel();
        Map<String, Double> categorySums = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();

        int categoryColumnIndex = 4; // Adjust based on dataset
        int amountColumnIndex = 5; // Adjust based on dataset

        int rowCount = table.getRowCount();

        // Iterate through all rows
        for (int i = 0; i < rowCount; i++) {
            int modelRow = table.convertRowIndexToModel(i);

            Object categoryObj = model.getValueAt(modelRow, categoryColumnIndex);
            Object amountObj = model.getValueAt(modelRow, amountColumnIndex);

            if (categoryObj != null && amountObj != null) {
                try {
                    String category = categoryObj.toString();
                    double amount = Double.parseDouble(amountObj.toString());

                    // Aggregate sums per category
                    categorySums.put(category, categorySums.getOrDefault(category, 0.0) + amount);
                    categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
                } catch (NumberFormatException ex) {
                    System.err.println("Skipping invalid data: " + amountObj);
                }
            }
        }

        // Calculate the average for each category
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Double> averages = new ArrayList<>();

        for (String category : categorySums.keySet()) {
            double totalAmount = categorySums.get(category);
            int count = categoryCounts.get(category);
            double average = totalAmount / count; // Calculate average amount

            categories.add(category);
            averages.add(average); // Store the average instead of total
        }

        // Update or add series
        if (chart.getSeriesMap().containsKey("Transactions")) {
            chart.updateCategorySeries("Transactions", categories, averages, null);
        } else {
            chart.addSeries("Transactions", categories, averages);
        }

        // Refresh the chart panel
        chartPanel.revalidate();
        chartPanel.repaint();
    }
}
