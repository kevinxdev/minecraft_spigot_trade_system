package de.kevtv.kevin.minecraft_spigot_trade_system.listener;

import de.kevtv.kevin.minecraft_spigot_trade_system.commands.TradeAcceptCommand;
import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.HashMapHelper;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.AnvilInput;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.TradeInventory;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class InventoryListener implements Listener {
    public static HashMap<Player, AnvilInput> anvilInput = new HashMap<>();
    public static ArrayList<AnvilInput> anvilInputs = new ArrayList<>();
    public static boolean finished = false;
    private static Player tradeInvPlayer;

    public static void closeInventorys(Player player) {
        String uuid = player.getUniqueId().toString();
        Player gettedPlayer = null;

        if (TradeAcceptListener.tradeRequests.containsKey(uuid)) {
            //Wenn der Spieler als Key enthalten ist

            String value = TradeAcceptListener.tradeRequests.get(uuid);
            UUID gettedUUID = UUID.fromString(value);
            gettedPlayer = Bukkit.getPlayer(gettedUUID);
            assert gettedPlayer != null;
            TradeAcceptListener.removeTradeRequest(uuid, TradeAcceptListener.tradeRequests.get(uuid));
            for (ItemStack item : TradeInventory.items.get(gettedPlayer)) {
                gettedPlayer.getInventory().addItem(item);
                ItemMeta itemMeta = item.getItemMeta();
                assert itemMeta != null;
                itemMeta.setLocalizedName("");
                item.setItemMeta(itemMeta);
            }
            for (ItemStack item : TradeInventory.items.get(player)) {
                player.getInventory().addItem(item);
                ItemMeta itemMeta = item.getItemMeta();
                assert itemMeta != null;
                itemMeta.setLocalizedName("");
                item.setItemMeta(itemMeta);
            }
            gettedPlayer.closeInventory();

        } else if (TradeAcceptListener.tradeRequests.containsValue(uuid)) {
            //Wenn der Spieler als Value enthalten ist

            String gettedUUIDString = HashMapHelper.getKey(TradeAcceptListener.tradeRequests, uuid);
            assert gettedUUIDString != null;
            UUID gettedUUID = UUID.fromString(gettedUUIDString);
            gettedPlayer = Bukkit.getPlayer(gettedUUID);
            assert gettedPlayer != null;
            TradeAcceptListener.removeTradeRequest(gettedUUIDString, uuid);
            for (ItemStack item : TradeInventory.items.get(gettedPlayer)) {
                gettedPlayer.getInventory().addItem(item);
                ItemMeta itemMeta = item.getItemMeta();
                assert itemMeta != null;
                itemMeta.setLocalizedName("");
                item.setItemMeta(itemMeta);
            }
            for (ItemStack item : TradeInventory.items.get(player)) {
                player.getInventory().addItem(item);
                ItemMeta itemMeta = item.getItemMeta();
                assert itemMeta != null;
                itemMeta.setLocalizedName("");
                item.setItemMeta(itemMeta);
            }
            gettedPlayer.closeInventory();

        }
        TradeInventory.clearArrays(player);
        TradeInventory.clearArrays(gettedPlayer);
        TradeAcceptCommand.tradeInventoryHashMap.remove(tradeInvPlayer);
        if (gettedPlayer != null) {
            gettedPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-closed-inventory")), player.getName()));
            player.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-you-closed-inventory")), gettedPlayer.getName()));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!finished) {
            if (!anvilInput.containsKey(player)) {
                if (event.getView().getTitle().equals(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name")))) {
                    closeInventorys(player);
                }
            } else {
                for (int i = 0; i < anvilInputs.size(); i++) {
                    if (!anvilInputs.get(i).inMode && anvilInputs.get(i).getPlayer() == player) {
                        if (event.getView().getTitle().equals(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name")))) {
                            anvilInputs.remove(anvilInputs.get(i));
                            i--;
                            closeInventorys(player);
                        }
                    }
                }
            }
        }

    }

    public static void registerTexts() {
        if (TextConfig.getTextConfig().getString("inventoryListener-closed-inventory") == null) {
            TextConfig.getTextConfig().set("inventoryListener-closed-inventory", "§cDein Tradepartner %s hat das Trade Inventar geschlossen und damit den Tausch beendet!");
        }
        if (TextConfig.getTextConfig().getString("inventoryListener-you-closed-inventory") == null) {
            TextConfig.getTextConfig().set("inventoryListener-you-closed-inventory", "§cDu hast dein Trade Inventar geschlossen und damit den Tausch mit %s beendet!");
        }
        if (TextConfig.getTextConfig().getString("inventoryListener-to-much") == null) {
            TextConfig.getTextConfig().set("inventoryListener-to-much", "§cZu viel!");
        }
        if (TextConfig.getTextConfig().getString("inventoryListener-that-is-not-your-item") == null) {
            TextConfig.getTextConfig().set("inventoryListener-that-is-not-your-item", "§cDas ist nicht dein Item!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null) {
                if (TradeAcceptCommand.tradeInventoryHashMap.get(player) != null) {
                    tradeInvPlayer = player;
                } else {
                    tradeInvPlayer = Bukkit.getPlayer(UUID.fromString(Objects.requireNonNull(HashMapHelper.getKey(TradeAcceptListener.tradeRequests, player.getUniqueId().toString()))));
                }
                if (TradeAcceptCommand.tradeInventoryHashMap.get(tradeInvPlayer).getInventory().getViewers().contains(player)) {
                    if (Objects.equals(Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName(), Border.getBorderMeta().getDisplayName())) {
                        event.setCancelled(true);
                    } else {
                        for (int i = 0; i < TradeInventory.moneyAmounts.size(); i++) {
                            if (TradeInventory.moneyAmounts.get(i).getMoneyAmountMeta().getDisplayName().equals(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) && TradeInventory.moneyAmounts.get(i).getPlayer() == player && event.getSlot() == TradeInventory.moneyAmounts.get(i).getSlot()) {
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
                        for (Ready ready : TradeInventory.readies) {
                            if (ready.getItemMeta().getDisplayName().equals(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) && ready.getPlayer() == player && event.getSlot() == ready.getSlot()) {
                                if (ready.getState() == 0) {
                                    ready.setReadyState();
                                } else if (ready.getState() == 1) {
                                    ready.setNotReadyState();
                                } else if (ready.getState() == 2) {
                                    ready.setNotReadyState();
                                }
                                for (ReadyTimer readyTimer : TradeInventory.readyTimers) {
                                    if (readyTimer.getPlayer1() == player || readyTimer.getPlayer2() == player) {
                                        readyTimer.checkState();
                                    }
                                }
                            }
                        }
                        if (Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.PLAYER) {
                            if (TradeInventory.items.get(player).size() < 16) {
                                ItemStack item = event.getCurrentItem();
                                ItemMeta itemMeta = item.getItemMeta();
                                assert itemMeta != null;
                                itemMeta.setLocalizedName(String.valueOf(event.getSlot()));
                                item.setItemMeta(itemMeta);
                                TradeInventory.items.get(player).add(item);
                                TradeAcceptCommand.tradeInventoryHashMap.get(tradeInvPlayer).setItemsToInv();
                                player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                            } else {
                                player.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-to-much")));
                            }
                        }
                        if (Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.CHEST) {
                            if (TradeInventory.p1al.contains(event.getSlot()) || TradeInventory.p2al.contains(event.getSlot())) {
                                if (TradeInventory.items.get(player).contains(event.getCurrentItem())) {
                                    TradeInventory.items.get(player).remove(event.getCurrentItem());
                                    ItemStack item = event.getCurrentItem();
                                    player.getInventory().addItem(item);
                                    ItemMeta itemMeta = item.getItemMeta();
                                    assert itemMeta != null;
                                    itemMeta.setLocalizedName("");
                                    item.setItemMeta(itemMeta);
                                    TradeAcceptCommand.tradeInventoryHashMap.get(tradeInvPlayer).setItemsToInv();
                                } else {
                                    player.sendMessage(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-that-is-not-your-item")));
                                }
                            }
                        }
                        event.setCancelled(true);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.print("");
        }
    }
}