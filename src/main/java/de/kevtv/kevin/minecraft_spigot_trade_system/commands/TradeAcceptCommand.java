package de.kevtv.kevin.minecraft_spigot_trade_system.commands;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TradeAcceptCommand implements CommandExecutor {

    /**
     * Funktion für "tradeaccept" Command
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
            if(sender.hasPermission("trade.accept")) {
                if(args.length == 1) {
                    Player checkPlayer = Bukkit.getPlayer(args[0]);
                    if(checkPlayer != null && checkPlayer.isOnline()) {
                        if(TradeAcceptListener.isTradeRequest(checkPlayer.getUniqueId().toString(), player.getUniqueId().toString())) {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-trade-successfull")), checkPlayer.getName()));
                            checkPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("trade-trade-accepted")), player.getName()));
                        } else {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-trade-not-successfull")), checkPlayer.getName()));
                        }
                    } else {
                        if(checkPlayer != null) {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-player-is-not-online")), checkPlayer.getName()));
                        } else {
                            sender.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-player-does-not-exist")), args[0]));
                        }
                    }
                } else {
                    sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-wrong-arguments")));
                }
            } else {
                sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-no-permissions")));
            }
        } else {
            sender.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAccept-no-player")));
        }

        return true;
    }
}
