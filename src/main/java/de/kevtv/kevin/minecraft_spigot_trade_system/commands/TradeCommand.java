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
                    if(checkPlayer != null && checkPlayer.isOnline()) {
                        TradeAcceptListener.addTradeRequest(player.getUniqueId().toString(), checkPlayer.getUniqueId().toString());
                        sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-send-request")), checkPlayer.getName()));
                    } else {
                        if(checkPlayer != null) {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-player-is-not-online")), checkPlayer.getName()));
                        } else {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-player-does-not-exist")), args[0]));
                        }
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
}
