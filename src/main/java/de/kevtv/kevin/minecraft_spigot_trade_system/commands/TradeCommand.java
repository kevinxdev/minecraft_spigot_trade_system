package de.kevtv.kevin.minecraft_spigot_trade_system.commands;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TradeCommand implements CommandExecutor {

    /**
     * Funktion für den "trade" Command
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
            if(sender.hasPermission("trade.trade")) {
                if(args.length == 1) {
                    Player checkPlayer = Bukkit.getPlayer(args[0]);
                    if(!args[0].equals(player.getName())) {
                        if (checkPlayer != null && checkPlayer.isOnline()) {
                            if (!TradeAcceptListener.isTradeRequest(player.getUniqueId().toString(), checkPlayer.getUniqueId().toString()) && !TradeAcceptListener.isTradeRequest(checkPlayer.getUniqueId().toString(), player.getUniqueId().toString())) {
                                TradeAcceptListener.addTradeRequest(player.getUniqueId().toString(), checkPlayer.getUniqueId().toString());
                                sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-send-request")), checkPlayer.getName()));
                                checkPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-new-trade-request")), player.getName()));
                            } else if(TradeAcceptListener.isTradeRequest(checkPlayer.getUniqueId().toString(), player.getUniqueId().toString())) {
                                sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-you-have-already-a-request")), checkPlayer.getName()));
                            } else {
                                sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-request-sended-earlier")), checkPlayer.getName()));
                            }
                        } else {
                            if (checkPlayer != null) {
                                sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-player-is-not-online")), checkPlayer.getName()));
                            } else {
                                sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-player-does-not-exist")), args[0]));
                            }
                        }
                    } else {
                        sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-no-own-request")));
                    }
                } else {
                    sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-wrong-arguments")));
                }
            } else {
                sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-no-permissions")));
            }
        } else {
            sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-no-player")));
        }
        return true;
    }

    /**
     * registriere Antworten vom Trade Command
     */
    public static void registerTexts() {
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
            TextConfig.getTextConfig().set("trade-player-does-not-exist", "§cDer Spieler %s existiert nicht!");
        }
        if(TextConfig.getTextConfig().getString("trade-send-request") == null) {
            TextConfig.getTextConfig().set("trade-send-request", "Die Tradeanfrage wurde an %s gesendet!");
        }
        if(TextConfig.getTextConfig().getString("trade-new-trade-request") == null) {
            TextConfig.getTextConfig().set("trade-new-trade-request", "Du hast eine Tradeanfrage von %s!");
        }
        if(TextConfig.getTextConfig().getString("trade-request-sended-earlier") == null) {
            TextConfig.getTextConfig().set("trade-request-sended-earlier", "Du hast bereits eine Tradeanfrage an %s gesendet!");
        }
        if(TextConfig.getTextConfig().getString("trade-no-own-request") == null) {
            TextConfig.getTextConfig().set("trade-no-own-request", "§cDu kannst dir selber keine Tradeanfrage senden!");
        }
        if(TextConfig.getTextConfig().getString("trade-you-have-already-a-request") == null) {
            TextConfig.getTextConfig().set("trade-you-have-already-a-request", "§c%s hat dir bereits eine Tradeanfrage gesendet!");
        }
    }

}
