package de.kevtv.kevin.minecraft_spigot_trade_system.commands;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.TradeInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class TradeAcceptCommand implements CommandExecutor {

    public static HashMap<Player, TradeInventory> tradeInventoryHashMap = new HashMap<>();

    /**
     * Funktion für "tradeaccept" Command
     *
     * @param sender  - sender of command
     * @param command - command
     * @param label   - label
     * @param args    - input of command
     * @return - boolean return
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
                            tradeInventoryHashMap.put(checkPlayer, new TradeInventory());
                            tradeInventoryHashMap.get(checkPlayer).createInventory(checkPlayer);
                            tradeInventoryHashMap.get(checkPlayer).openInventory(player);
                            tradeInventoryHashMap.get(checkPlayer).openInventory(checkPlayer);
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

    /**
     * Registiere Antworten vom TradeAccept Command
     */
    public static void registerTexts() {
        if(TextConfig.getTextConfig().getString("tradeAccept-no-player") == null) {
            TextConfig.getTextConfig().set("tradeAccept-no-player", "§cDu bist kein Spieler!");
        }
        if(TextConfig.getTextConfig().getString("tradeAccept-no-permissions") == null) {
            TextConfig.getTextConfig().set("tradeAccept-no-permissions", "§cDu hast keine Berechtigung diesen Command zu nutzen!");
        }
        if(TextConfig.getTextConfig().getString("tradeAccept-wrong-arguments") == null) {
            TextConfig.getTextConfig().set("tradeAccept-wrong-arguments", "§cDer Command heißt /tradeaccept <Spielername>!");
        }
        if(TextConfig.getTextConfig().getString("tradeAccept-player-is-not-online") == null) {
            TextConfig.getTextConfig().set("tradeAccept-player-is-not-online", "§cDer Spieler %s ist nicht online!");
        }
        if(TextConfig.getTextConfig().getString("tradeAccept-player-does-not-exist") == null) {
            TextConfig.getTextConfig().set("tradeAccept-player-does-not-exist", "§cDer Spieler %s existiert nicht!");
        }
        if(TextConfig.getTextConfig().getString("tradeAccept-trade-successfull") == null) {
            TextConfig.getTextConfig().set("tradeAccept-trade-successfull", "Der Trade mit %s kann losgehen!");
        }
        if(TextConfig.getTextConfig().getString("tradeAccept-trade-not-successfull") == null) {
            TextConfig.getTextConfig().set("tradeAccept-trade-not-successfull", "§cDu hast keine Tradeanfrage von %s!");
        }
        if(TextConfig.getTextConfig().getString("trade-trade-accepted") == null) {
            TextConfig.getTextConfig().set("trade-trade-accepted", "Der Trade mit %s kann losgehen!");
        }
    }

}
