package de.kevtv.kevin.minecraft_spigot_trade_system.config;

import de.kevtv.kevin.minecraft_spigot_trade_system.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MySQLConfig {

    private static File fileMySQL;
    private static YamlConfiguration mySQL;

    /**
     * Erstelle eine MySQL Config und setze Standarddaten
     */
    public static void setMySQLConfig() {
        fileMySQL = new File("plugins//" + Main.getPlugin().getDescription().getName(), "MySQL.yml");
        mySQL = YamlConfiguration.loadConfiguration(fileMySQL);

        mySQL.addDefault("KevTV.MySQL.host", "localhost");
        mySQL.addDefault("KevTV.MySQL.port", "3306");
        mySQL.addDefault("KevTV.MySQL.user", "root");
        mySQL.addDefault("KevTV.MySQL.password", "");
        mySQL.addDefault("KevTV.MySQL.database", "dbName");

        mySQL.addDefault("KevTV.MySQL.tables.moneyTable", "money");

        mySQL.options().copyDefaults(true);
        try {
            mySQL.save(fileMySQL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funktion zum holen von Daten aus der Datenbank Config
     * @param data
     * @return
     */
    public static String getMySQLData(String data) {
        String correctData = "";

        if(fileMySQL.exists()) {
            if(mySQL.getString("KevTV.MySQL.host") != null) {
                correctData = mySQL.getString("KevTV.MySQL." + data);
            }
        }

        return correctData;

    }

}
