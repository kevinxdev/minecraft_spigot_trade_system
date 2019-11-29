package de.kevtv.kevin.minecraft_spigot_trade_system.listener;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.HashMapHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class TradeAcceptListener implements Listener {

    public static HashMap<String, String> tradeRequests = new HashMap<>();

    /**
     * Füge neue Tradeanfrage der tradeRequest HashMap hinzu
     * @param sender_uuid
     * @param receiver_uuid
     */
    public static void addTradeRequest(String sender_uuid, String receiver_uuid) {
        tradeRequests.put(sender_uuid, receiver_uuid);
    }

    /**
     * Überprüft ob ein passendes paar in der HashMap existiert
     * @param sender_uuid
     * @param receiver_uuid
     * @return
     */
    public static boolean isTradeRequest(String sender_uuid, String receiver_uuid) {
        if(tradeRequests.containsKey(sender_uuid) && tradeRequests.containsValue(receiver_uuid)) {
            return true;
        }
        return false;
    }

    /**
     * Entferne die tradeRequest aus der HashMap tradeRequests
     * @param sender_uuid
     * @param receiver_uuid
     */
    public static void removeTradeRequest(String sender_uuid, String receiver_uuid) {
        if(tradeRequests.containsKey(sender_uuid) && tradeRequests.containsValue(receiver_uuid)) {
            tradeRequests.remove(sender_uuid, receiver_uuid);
        }
    }

    /**
     * Ein Event welches ausgeführt wird wenn ein Spieler den Server verlässt
     * @param event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if(tradeRequests.containsKey(uuid)) {
            removeTradeRequest(uuid, tradeRequests.get(player.getUniqueId().toString()));
        } else if(tradeRequests.containsValue(uuid)) {
            String gettedUUIDString = HashMapHelper.getKey(tradeRequests, uuid);
            assert gettedUUIDString != null;
            UUID gettedUUID = UUID.fromString(gettedUUIDString);
            Player gettedPlayer = Bukkit.getPlayer(gettedUUID);
            assert gettedPlayer != null;
            gettedPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeAcceptListener-player-quit")), player.getName()));
            removeTradeRequest(gettedUUIDString, uuid);
        }
    }

    /**
     * Registriere Antworten für den TradeAcceptListener
     */
    public static void registerTexts() {
        if(TextConfig.getTextConfig().getString("tradeAcceptListener-player-quit") == null) {
            TextConfig.getTextConfig().set("tradeAcceptListener-player-quit", "§cDer Spieler %s hat den Server verlassen und dadurch wird die Tradeanfrage gelöscht!");
        }
    }

}
