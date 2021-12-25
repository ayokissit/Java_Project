package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    public Connection connection;
    public Statement statement;
    public ResultSet resSet;

    // Подключение к БД
    public void conn() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:.\\SQLite\\countries.s3db");
        statement = connection.createStatement();

        System.out.println("База Подключена!");
    }

    // Создание БД
    public void createDB(List<String> headers) throws ClassNotFoundException, SQLException {
        var builder = new StringBuilder("CREATE TABLE if not exists 'countries' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, ");

        for (var i = 0; i < headers.size(); i++) {
            if (i < 2)
                builder.append("'%s' varchar(50), ".formatted(headers.get(i)));
            else if (i == 2)
                builder.append("'%s' int, ".formatted(headers.get(i)));
            else if (i < headers.size() - 1)
                builder.append("'%s' Decimal(5, 15), ".formatted(headers.get(i)));
            else
                builder.append("'%s' Decimal(5, 15)); ".formatted(headers.get(i)));
        }
        statement.execute(builder.toString());
        System.out.println("Таблица создана или уже существует.");
    }

    // Ввести значения с помощью запроса
    public void writeDB(String query) throws SQLException {
        statement.execute(query);
    }

    public ArrayList<Object> getColumnValues(String name, String query) {
        try {
            resSet = statement.executeQuery(query);
            var list = new ArrayList<>();
            while (resSet.next()) {
                list.add(resSet.getString(name));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    // Закрыть БД и все что с ней связано
    public void closeDB() throws ClassNotFoundException, SQLException {
        connection.close();
        statement.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }
}

