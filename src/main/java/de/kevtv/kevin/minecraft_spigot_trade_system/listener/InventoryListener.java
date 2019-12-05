package de.kevtv.kevin.minecraft_spigot_trade_system.listener;

import de.kevtv.kevin.minecraft_spigot_trade_system.Main;
import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.HashMapHelper;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.AnvilInput;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.TradeInventory;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.Border;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyAmount;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyMinus;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyPlus;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class InventoryListener implements Listener {
    public static HashMap<Player, AnvilInput> anvilInput = new HashMap<>();
    public static ArrayList<AnvilInput> anvilInputs = new ArrayList<>();

    public void closePlayerInventory(Player player) {

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        //Wenn anvilInput initialisiert ist
        if (!anvilInput.containsKey(player)) {
            System.out.println("not");
            if (event.getView().getTitle().equals(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name")))) {
                System.out.println("CloseINVFALSE1");
                closeInventorys(player);
            }
        } else {
            System.out.println("yes");
            for (int i = 0; i < anvilInputs.size(); i++) {
                if (!anvilInputs.get(i).inMode && anvilInputs.get(i).getPlayer() == player) {
                    if (event.getView().getTitle().equals(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name")))) {
                        System.out.println("CloseINVFALSE2");
                        anvilInputs.remove(anvilInputs.get(i));
                        i--;
                        closeInventorys(player);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() != null) {
            //Wenn das Item ein Item ist
            if(TradeInventory.getInventory().getViewers().contains(player)) {
                //Wenn das Inventar den jetzigen Spieler enthält
                if (Objects.equals(Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName(), Border.getBorderMeta().getDisplayName())) {
                    //Wenn das Item, welches der Spieler hält eine Border ist
                    event.setCancelled(true);
                } else {
                    for (int i = 0; i < TradeInventory.moneyAmounts.size(); i++) {
                        if (TradeInventory.moneyAmounts.get(i).getMoneyAmountMeta().getDisplayName().equals(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) && TradeInventory.moneyAmounts.get(i).getPlayer() == player && event.getSlot() == TradeInventory.moneyAmounts.get(i).getSlot()) {
                            //Wenn das Item ein Goldblock ist
                            System.out.println("new");
                            anvilInput.put(player, new AnvilInput());
                            anvilInput.get(player).createInventory(player);
                            anvilInputs.add(anvilInput.get(player));
                        }
                    }
                    for (MoneyMinus moneyMinus : TradeInventory.moneyMinuses) {
                        if (moneyMinus.getMoneyMinusMeta().getDisplayName().equals(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) && moneyMinus.getPlayer() == player && event.getSlot() == moneyMinus.getSlot()) {
                            if (moneyMinus.getMoney() - 100 >= 0) {
                                moneyMinus.remMoney(100);
                            }
                        }
                    }
                    for (MoneyPlus moneyPlus : TradeInventory.moneyPluses) {
                        if (moneyPlus.getMoneyPlusMeta().getDisplayName().equals(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) && moneyPlus.getPlayer() == player && event.getSlot() == moneyPlus.getSlot()) {
                            if (moneyPlus.getMoney() + 100 <= moneyPlus.getPlayerMoneyAmount()) {
                                moneyPlus.addMoney(100);
                            } else {
                                moneyPlus.addMoney(moneyPlus.getPlayerMoneyAmount() - moneyPlus.getMoney());
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }

    }

    public static void closeInventorys(Player player) {
        String uuid = player.getUniqueId().toString();
        Player gettedPlayer = null;

        if(TradeAcceptListener.tradeRequests.containsKey(uuid)) {
            //Wenn der Spieler als Key enthalten ist

            String value = TradeAcceptListener.tradeRequests.get(uuid);
            UUID gettedUUID = UUID.fromString(value);
            gettedPlayer = Bukkit.getPlayer(gettedUUID);
            assert gettedPlayer != null;
            TradeAcceptListener.removeTradeRequest(uuid, TradeAcceptListener.tradeRequests.get(uuid));
            gettedPlayer.closeInventory();

        } else if(TradeAcceptListener.tradeRequests.containsValue(uuid)) {
            //Wenn der Spieler als Value enthalten ist

            String gettedUUIDString = HashMapHelper.getKey(TradeAcceptListener.tradeRequests, uuid);
            assert gettedUUIDString != null;
            UUID gettedUUID = UUID.fromString(gettedUUIDString);
            gettedPlayer = Bukkit.getPlayer(gettedUUID);
            assert gettedPlayer != null;
            TradeAcceptListener.removeTradeRequest(gettedUUIDString, uuid);
            gettedPlayer.closeInventory();

        }
        TradeInventory.clearArrays(player);
        TradeInventory.clearArrays(gettedPlayer);
        if(gettedPlayer != null) {
            gettedPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-closed-inventory")), player.getName()));
            player.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-you-closed-inventory")), gettedPlayer.getName()));
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