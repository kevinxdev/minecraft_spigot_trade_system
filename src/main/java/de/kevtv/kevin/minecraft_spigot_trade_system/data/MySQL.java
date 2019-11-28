package de.kevtv.kevin.minecraft_spigot_trade_system.data;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.MySQLConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

    private static Connection connection;

    /**
     * Baue eine Verbindung zur Datenbank auf
     */
    public static void connectToMySQL() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + MySQLConfig.getMySQLData("host") + ":" + MySQLConfig.getMySQLData("port") + "/" + MySQLConfig.getMySQLData("database") + "?autoReconnect=true", MySQLConfig.getMySQLData("user"), MySQLConfig.getMySQLData("password"));
            System.out.println("Die Verbindung zur MySQL wurde hergestellt!");
        } catch (SQLException e) {
            System.out.println("Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
        }
    }

    /**
     * Setze alle Datenbank Tabellen auf
     */
    public static void setupMySQLTables() {
        setupMySQLmoneyTable();
    }

    /**
     * Setze den moneyTable auf
     */
    private static void setupMySQLmoneyTable() {
        updateMySQL("CREATE TABLE IF NOT EXISTS " + MySQLConfig.getMySQLData("tables.moneyTable") + "(Name varchar(32), Money int, Bank int)");
    }

    /**
     * Funktion zur Ausführung von MySQL Commands
     * @param query
     */
    private static void updateMySQL(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            disconnectMySQL();
            connectToMySQL();
        }
    }

    /**
     * Trenne die Verbindung zur Datenbank
     */
    public static void disconnectMySQL() {
        if(connection != null) {
            try {
                connection.close();
                System.out.println("Die Verbindung zur MySQL wurde erfolgreich beendet!");
            } catch (SQLException e) {
                System.out.println("Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
            }
        }
    }

}
