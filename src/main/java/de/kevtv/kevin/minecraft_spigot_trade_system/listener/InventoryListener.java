package de.kevtv.kevin.minecraft_spigot_trade_system.listener;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.HashMapHelper;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.Border;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Objects;
import java.util.UUID;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(event.getView().getTitle().equals(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name"))) && TradeAcceptListener.tradeRequests.containsValue(player.getUniqueId().toString()) || TradeAcceptListener.tradeRequests.containsKey(player.getUniqueId().toString())) {

            String uuid = player.getUniqueId().toString();
            Player gettedPlayer = null;

            if(TradeAcceptListener.tradeRequests.containsKey(uuid)) {

                String value = TradeAcceptListener.tradeRequests.get(uuid);
                UUID gettedUUID = UUID.fromString(value);
                gettedPlayer = Bukkit.getPlayer(gettedUUID);
                assert gettedPlayer != null;
                TradeAcceptListener.removeTradeRequest(uuid, TradeAcceptListener.tradeRequests.get(uuid));
                gettedPlayer.closeInventory();

            } else if(TradeAcceptListener.tradeRequests.containsValue(uuid)) {

                String gettedUUIDString = HashMapHelper.getKey(TradeAcceptListener.tradeRequests, uuid);
                assert gettedUUIDString != null;
                UUID gettedUUID = UUID.fromString(gettedUUIDString);
                gettedPlayer = Bukkit.getPlayer(gettedUUID);
                assert gettedPlayer != null;
                TradeAcceptListener.removeTradeRequest(gettedUUIDString, uuid);
                gettedPlayer.closeInventory();

            }
            assert gettedPlayer != null;
            gettedPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-closed-inventory")), player.getName()));
            player.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-you-closed-inventory")), gettedPlayer.getName()));
        }
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        if(event.getCurrentItem() != null) {
            if (Objects.equals(Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName(), Border.getBorderMeta().getDisplayName())) {
                event.setCancelled(true);
            }
        }

    }

    public static void registerTexts() {
        if(TextConfig.getTextConfig().getString("inventoryListener-closed-inventory") == null) {
            TextConfig.getTextConfig().set("inventoryListener-closed-inventory", "§cDein Tradepartner %s hat das Trade Inventar geschlossen und damit den Tausch beendet!");
        }
        if(TextConfig.getTextConfig().getString("inventoryListener-you-closed-inventory") == null) {
            TextConfig.getTextConfig().set("inventoryListener-you-closed-inventory", "§cDu hast dein Trade Inventar geschlossen und damit den Tausch mit %s beendet!");
        }
    }
}