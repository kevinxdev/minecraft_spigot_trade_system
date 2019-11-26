package de.kevtv.kevin.minecraft_spigot_trade_system;

import de.kevtv.kevin.minecraft_spigot_trade_system.commands.TradeCommand;
import de.kevtv.kevin.minecraft_spigot_trade_system.config.MySQLConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.data.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main plugin;

    /**
     * Der erste Aufruf wenn das Plugin geladen wird
     */
    @Override
    public void onEnable() {
        // Plugin startup logic

        setPlugin(this);

        MySQLConfig.setMySQLConfig();
        MySQL.connectToMySQL();

        MySQL.setupMySQLTables();

        setTextConfig();
        registerCommands();

    }

    /**
     * Der letzte Aufruf wenn z.B. der Server beendet wird
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MySQL.disconnectMySQL();
    }

    /**
     * Setze Instanz der Main Klasse
     * @param plugin
     */
    private static void setPlugin(Main plugin) {
        Main.plugin = plugin;
    }

    /**
     * Getter für die Main Instanz
     * @return
     */
    public static Main getPlugin() {
         return plugin;
    }

    /**
     * Setze die Standard Antworten z.B. Fehlermeldungen bei Commands
     */
    private void setTextConfig() {
        TextConfig.createTextConfig();
        if(TextConfig.getTextConfig().getString("trade-no-player") == null) {
            TextConfig.getTextConfig().set("trade-no-player", "§cDu bist kein Spieler!");
        }
        if(TextConfig.getTextConfig().getString("trade-no-permissions") == null) {
            TextConfig.getTextConfig().set("trade-no-permissions", "§cDu hast keine Berechtigung diesen Command zu nutzen!");
        }
        if(TextConfig.getTextConfig().getString("trade-wrong-arguments") == null) {
            TextConfig.getTextConfig().set("trade-wrong-arguments", "§cDer Command heißt /trade <Spielername>!");
        }
        if(TextConfig.getTextConfig().getString("trade-player-is-not-online") == null) {
            TextConfig.getTextConfig().set("trade-player-is-not-online", "§cDer Spieler %s ist nicht online!");
        }
        if(TextConfig.getTextConfig().getString("trade-player-does-not-exist") == null) {
            TextConfig.getTextConfig().set("trade-player-does-not-exist", "$cDer Spieler %s existiert nicht!");
        }

        TextConfig.save();

        System.out.println("Set config files");
    }

    /**
     * Registriere Commands für User
     */
    private void registerCommands() {
        Objects.requireNonNull(getCommand("trade")).setExecutor(new TradeCommand());
    }

}
