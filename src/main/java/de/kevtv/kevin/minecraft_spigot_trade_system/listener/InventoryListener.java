package de.kevtv.kevin.minecraft_spigot_trade_system.listener;

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

import java.util.Objects;
import java.util.UUID;

public class InventoryListener implements Listener {

    private boolean isInTrade;
    private AnvilInput anvilInput;

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(!isInTrade && event.getView().getTitle().equals(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name"))) && (TradeAcceptListener.tradeRequests.containsValue(player.getUniqueId().toString()) || TradeAcceptListener.tradeRequests.containsKey(player.getUniqueId().toString()))) {
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
            TradeInventory.clearArrays(player);
            TradeInventory.clearArrays(gettedPlayer);
            assert gettedPlayer != null;
            gettedPlayer.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-closed-inventory")), player.getName()));
            player.sendMessage(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("inventoryListener-you-closed-inventory")), gettedPlayer.getName()));
        }
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        isInTrade = false;
        if(event.getCurrentItem() != null) {
            anvilInput = new AnvilInput();
            anvilInput.createInventory(player);
            anvilInput.getInventory().onClose(TradeInventory::openInventory);
            if(TradeInventory.getInventory().getViewers().contains(player)) {
                if (Objects.equals(Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName(), Border.getBorderMeta().getDisplayName())) {
                    event.setCancelled(true);
                } else {
                    for (int i = 0; i < TradeInventory.moneyAmounts.size(); i++) {
                        if (TradeInventory.moneyAmounts.get(i).getMoneyAmountMeta().getDisplayName().equals(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) && TradeInventory.moneyAmounts.get(i).getPlayer() == player && event.getSlot() == TradeInventory.moneyAmounts.get(i).getSlot()) {
                            isInTrade = true;
                            anvilInput.setMoneyOfUser(TradeInventory.moneyAmounts.get(i).getMoney());
                            player.closeInventory();
                            anvilInput.getInventory().open(player);
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
            } else {
                int count = 0;
                for (int i = 0; i < TradeInventory.moneyAmounts.size(); i++) {
                    if(TradeInventory.moneyAmounts.get(i).getPlayer() != player) {
                        count++;
                    }
                }
                if(count == TradeInventory.moneyAmounts.size()) {
                    System.out.println("test");
                    anvilInput.getInventory().onClose(player1 -> {
                        System.out.println("test2");
                        anvilInput.getInventory().preventClose();
                    });

                }

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