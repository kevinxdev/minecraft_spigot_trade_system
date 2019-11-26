package de.kevtv.kevin.minecraft_spigot_trade_system.config;

import de.kevtv.kevin.minecraft_spigot_trade_system.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TextConfig {

    private static File textConfigFile;
    private static FileConfiguration textConfig;

    /**
     * Getter für das Bearbeiten/Abrufen der Dateiinhalte
     * @return
     */
    public static FileConfiguration getTextConfig() {
        return textConfig;
    }

    /**
     * Erstelle die Konfigurationsdatei für die TextConfig
     */
    public static void createTextConfig() {
        textConfigFile = new File(Main.getPlugin().getDataFolder(), "text.yml");
        if(!textConfigFile.exists()) {
            textConfigFile.getParentFile().mkdir();
            Main.getPlugin().saveResource("text.yml", false);
        }
        textConfig = new YamlConfiguration();
        try {
            textConfig.load(textConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Speichere die Konfigurationsdatei
     */
    public static void save() {
        try {
            textConfig.save(textConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
