package de.kevtv.kevin.minecraft_spigot_trade_system.commands;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import org.bukkit.Bukkit;
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
            Player player = (Player) sender;
            if(sender.hasPermission("trade.deny")) {
                if(args.length == 1) {
                    Player checkPlayer = Bukkit.getPlayer(args[0]);
                    if(checkPlayer != null && checkPlayer.isOnline()) {
                        if(TradeAcceptListener.isTradeRequest(checkPlayer.getUniqueId().toString(), player.getUniqueId().toString())) {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeDeny-trade-was-denied")), checkPlayer.getName()));
                            checkPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-trade-denied")), player.getName()));
                            TradeAcceptListener.removeTradeRequest(checkPlayer.getUniqueId().toString(), player.getUniqueId().toString());
                        } else {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeDeny-trade-not-denied")), checkPlayer.getName()));
                        }
                    } else {
                        if(checkPlayer != null) {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-player-is-not-online")), checkPlayer.getName()));
                        } else {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-player-does-not-exist")), args[0]));
                        }
                    }
                } else {
                    sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeDeny-wrong-arguments")));
                }
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
        if(TextConfig.getTextConfig().getString("tradeDeny-wrong-arguments") == null) {
            TextConfig.getTextConfig().set("tradeDeny-wrong-arguments", "§cDer Command heißt /tradedeny <Spielername>!");
        }
        if(TextConfig.getTextConfig().getString("tradeDeny-player-is-not-online") == null) {
            TextConfig.getTextConfig().set("tradeDeny-player-is-not-online", "§cDer Spieler %s ist nicht online!");
        }
        if(TextConfig.getTextConfig().getString("tradeDeny-player-does-not-exist") == null) {
            TextConfig.getTextConfig().set("tradeDeny-player-does-not-exist", "§cDer Spieler %s existiert nicht!");
        }
        if(TextConfig.getTextConfig().getString("tradeDeny-trade-not-denied") == null) {
            TextConfig.getTextConfig().set("tradeDeny-trade-not-denied", "§cDu hast keine Tradeanfrage von %s!");
        }
        if(TextConfig.getTextConfig().getString("tradeDeny-trade-was-denied") == null) {
            TextConfig.getTextConfig().set("tradeDeny-trade-was-denied", "Der Trade mit %s wurde abgelehnt!");
        }
        if(TextConfig.getTextConfig().getString("trade-trade-denied") == null) {
            TextConfig.getTextConfig().set("trade-trade-denied", "%s hat deine Tradeanfrage abgelehnt!");
        }
    }

}
