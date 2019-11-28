package de.kevtv.kevin.minecraft_spigot_trade_system.commands;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TradeDenyCommand implements CommandExecutor {

    /**
     * Funktion für den "tradeDeny" Command
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            if(sender.hasPermission("trade.deny")) {

            } else {
                sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeDeny-no-permissions")));
            }
        } else {
            sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeDeny-no-player")));
        }

        return true;
    }

    /**
     * Registriere Antworten vom TradeDeny Command
     */
    public static void registerTexts() {
        if(TextConfig.getTextConfig().getString("tradeDeny-no-player") == null) {
            TextConfig.getTextConfig().set("tradeDeny-no-player", "§cDu bist kein Spieler!");
        }
        if(TextConfig.getTextConfig().getString("tradeDeny-no-permissions") == null) {
            TextConfig.getTextConfig().set("tradeDeny-no-permissions", "§cDu hast keine Berechtigung diesen Command zu nutzen!");
        }
    }

}
