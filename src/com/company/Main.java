package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        var newCSV = new CSVReader("./Показатель счастья по странам 2017 - Показатель счастья по странам 2017.csv"); // Читаем данные CSV и добавляем в БД

        var newChart = new Chart(); // Класс для создания графиков
        JFrame frameName = new JFrame();
        frameName.setVisible(true);

        frameName.add(newChart.createLineChartPanel()); // Создать линейный график
        frameName.add(newChart.createHistogramPanel()); // Создать гистограмму

        calculateIndicators(); // Средний показатель здоровья
    }

    private static void calculateIndicators() {
        var db = new DataBase();
        try {
            db.conn();
            var query = "SELECT * FROM countries " +
                    "WHERE region = 'Western Europe' OR region = 'Sub-Saharan Africa' " +
                    "ORDER BY Health__Life_Expectancy_ DESC; ";

            var health = db.getColumnValues("Health__Life_Expectancy_", query);
            var averHealth = 0.0;
            for (var e : health)
                averHealth += Double.parseDouble(String.valueOf(e));

            System.out.println("\nСредний арифметический показатель здоровья среди Western Europe и Sub-Saharan Africa: " + averHealth / health.size());

            var median = 0.0;
            if (health.size() % 2 == 0) {
                median = (Double.parseDouble(String.valueOf(health.get(health.size() / 2)))
                        + Double.parseDouble(String.valueOf(health.get(health.size() / 2 - 1)))) / 2;
            }
            else
                median = Double.parseDouble(String.valueOf(health.get(health.size() / 2)));

            System.out.println("\nМедианный показатель здоровья среди Western Europe и Sub-Saharan Africa: " + median + "\n");

            db.closeDB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}