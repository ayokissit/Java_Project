package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Chart {
    private ArrayList<Object> countries = new ArrayList<>();
    private ArrayList<Object> health = new ArrayList<>();

    public Chart() {
        DataBase db = new DataBase();
        try {
            db.conn();

            countries = db.getColumnValues("Country", "SELECT * FROM countries");
            health = db.getColumnValues("Health__Life_Expectancy_", "SELECT * FROM countries");

            db.closeDB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Создать линейный XY график
    public JPanel createLineChartPanel() {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Показатель здоровья по странам",
                "Продолжительность жизни","Страны",
                createDatasetLineChart(),
                PlotOrientation.VERTICAL,
                true,true,false);
        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        plot.getDomainAxis().setCategoryLabelPositions(
                CategoryLabelPositions.UP_90);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(false);
        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        return chartPanel;
    }

    // Создать гистограмму
    public JPanel createHistogramPanel() {
        JFreeChart chart = createHistogram(createDatasetHistogram());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(1200, 600));

        return panel;
    }

    private DefaultCategoryDataset createDatasetLineChart( ) {
        var dataset = new DefaultCategoryDataset( );
        for (var i = 0; i < countries.size(); i++) {
            dataset.addValue(Double.parseDouble(String.valueOf(health.get(i))), "Страны", String.valueOf(countries.get(i)));
        }
        return dataset;
    }

    private CategoryDataset createDatasetHistogram() {
        var dataset = new DefaultCategoryDataset();
        for (var i = 0; i < countries.size(); i++) {
            dataset.addValue(Double.parseDouble(String.valueOf(health.get(i))), String.valueOf(countries.get(i)), "Страны");
        }

        return dataset;
    }

    private JFreeChart createHistogram(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Показатель здоровья по странам",
                null,
                "Продолжительность жизни",
                dataset);
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }
}
