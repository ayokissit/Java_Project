package com.company;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class CSVReader {
    private List<String> headers;
    public DataBase db = new DataBase();
    List<String> dbHeaders;

    public CSVReader(String path) {
        try {
            Reader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            CSVParser parser = CSVParser.parse(br, CSVFormat.EXCEL.withFirstRecordAsHeader());
            headers = parser.getHeaderNames(); // Получаем заголовки для взаимодействия с CSV
            dbHeaders = headers.stream().map(str -> str.replace(".", "_")).toList(); // Получаем заголовки для взаимодействия с БД

            db.conn(); // Подключение к БД
            db.createDB(dbHeaders); // Создание БД

            parseData(path);

            db.closeDB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void parseData(String path) {
        try {
            Reader reader = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
            for (var record : records) {
                generateQueryForDB(record); // Формирование запроса для заполнения таблицы БД
            }
            System.out.println("Таблица заполнена!");
        } catch (Exception e) {
            System.out.println("Не удалось спарсить данные: " + e.getMessage());
        }
    }

    void generateQueryForDB(CSVRecord record) {
        try {
            StringBuilder builder = new StringBuilder("INSERT INTO countries (" + String.join(", ", dbHeaders) + ") " +
                    "VALUES (");

            var headersSize = headers.size();
            for (var i = 0; i < headersSize; i++) {
                if (i == headersSize - 1)
                    builder.append("'%s')".formatted(record.get(headers.get(i))));
                else {
                    builder.append("'%s', ".formatted(record.get(headers.get(i))));
                }
            }
            db.writeDB(builder.toString());
        } catch (Exception e) {
            System.out.println("Не удалось сформировать запрос: " + e.getMessage());
        }
    }
}