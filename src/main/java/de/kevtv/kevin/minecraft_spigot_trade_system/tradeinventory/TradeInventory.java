package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class TradeInventory {

    public static ArrayList<Player> players1 = new ArrayList<>();
    public static ArrayList<Player> players2 = new ArrayList<>();
    public static ArrayList<Ready> readies = new ArrayList<>();
    public static ArrayList<MoneyAmount> moneyAmounts = new ArrayList<>();
    public static ArrayList<MoneyMinus> moneyMinuses = new ArrayList<>();
    public static ArrayList<MoneyPlus> moneyPluses = new ArrayList<>();
    public static ArrayList<ReadyTimer> readyTimers = new ArrayList<>();
    public static HashMap<Player, ArrayList<ItemStack>> items = new HashMap<>();
    private Inventory inventory;
    private HashMap<Integer, ItemStack> slot = new HashMap<>();

    public static void clearArrays(Player player) {
        for (int i = 0; i < moneyAmounts.size(); i++) {
            if (moneyAmounts.get(i).getPlayer() == player) {
                moneyAmounts.remove(moneyAmounts.get(i));
            }
        }
        for (int i = 0; i < moneyMinuses.size(); i++) {
            if (moneyMinuses.get(i).getPlayer() == player) {
                moneyMinuses.remove(moneyMinuses.get(i));
            }
        }
        for (int i = 0; i < moneyPluses.size(); i++) {
            if (moneyPluses.get(i).getPlayer() == player) {
                moneyPluses.remove(moneyPluses.get(i));
            }
        }
        for (int i = 0; i < readies.size(); i++) {
            if (readies.get(i).getPlayer() == player) {
                readies.remove(readies.get(i));
            }
        }
        for (int i = 0; i < readyTimers.size(); i++) {
            if (readyTimers.get(i).getPlayer1().getPlayer() == player) {
                readyTimers.remove(readyTimers.get(i));
            } else if (readyTimers.get(i).getPlayer2().getPlayer() == player) {
                readyTimers.remove(readyTimers.get(i));
            }
        }

    }

    public static void registerTexts() {
        if (TextConfig.getTextConfig().getString("tradeInventory-inventory-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-inventory-name", "Trade Inventar");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-border-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-border-name", "Border");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-moneyAmount-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-moneyAmount-name", "%s Geos");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-moneyMinus-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-moneyMinus-name", "Minus 100 Geos");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-moneyPlus-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-moneyPlus-name", "Plus 100 Geos");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-anvilInventory-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-anvilInventory-name", "Geos angeben:");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-notReady-state-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-notReady-state-name", "Nicht Bereit");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-ready-state-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-ready-state-name", "Bereit");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-notReadyTimer-state-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-notReadyTimer-state-name", "Niemand ist bereit");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-oneIsReadyTimer-state-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-oneIsReadyTimer-state-name", "Warte auf %s!");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-ready-state-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-ready-state-name", "Bereit!");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-done-state-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-done-state-name", "Trade!");
        }
        if (TextConfig.getTextConfig().getString("tradeInventory-timer-countdown") == null) {
            TextConfig.getTextConfig().set("tradeInventory-timer-countdown", "In %s Sekunden fertig!");
        }
    }

    public void createInventory(Player player) {
        inventory = Bukkit.createInventory(null, 54, Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name")));
        placeBorder(player);
    }

    private void placeBorder(Player player) {
        Border.setBorder();
        Border.setBorderMeta();
        Border.setBorderName();
        for (int i = 4; i < inventory.getSize(); i += 9) {
            inventory.setItem(i, Border.getBorder());
        }
        for (int i = 36; i < 45; i++) {
            inventory.setItem(i, Border.getBorder());
        }

        Player player2 = Bukkit.getPlayer(UUID.fromString(TradeAcceptListener.tradeRequests.get(player.getUniqueId().toString())));

        players1.add(player);
        players2.add(player2);

        items.put(player, new ArrayList<>(16));
        items.put(player2, new ArrayList<>(16));

        MoneyAmount moneyAmount1 = new MoneyAmount(player, 46);
        MoneyAmount moneyAmount2 = new MoneyAmount(player2, 52);

        moneyAmounts.add(moneyAmount1);
        moneyAmounts.add(moneyAmount2);

        MoneyMinus moneyMinus1 = new MoneyMinus(moneyAmount1);
        MoneyMinus moneyMinus2 = new MoneyMinus(moneyAmount2);

        moneyMinus1.setSlot(45);
        moneyMinus2.setSlot(51);

        moneyMinuses.add(moneyMinus1);
        moneyMinuses.add(moneyMinus2);

        inventory.setItem(45, moneyMinus1.getMoneyMinus());
        inventory.setItem(51, moneyMinus2.getMoneyMinus());

        MoneyPlus moneyPlus1 = new MoneyPlus(moneyAmount1);
        MoneyPlus moneyPlus2 = new MoneyPlus(moneyAmount2);

        moneyPlus1.setSlot(47);
        moneyPlus2.setSlot(53);

        moneyPluses.add(moneyPlus1);
        moneyPluses.add(moneyPlus2);

        inventory.setItem(47, moneyPlus1.getMoneyPlus());
        inventory.setItem(53, moneyPlus2.getMoneyPlus());

        Ready ready1 = new Ready(player, 48);
        Ready ready2 = new Ready(player2, 50);

        readies.add(ready1);
        readies.add(ready2);

        inventory.setItem(ready1.getSlot(), ready1.getItemStack());
        inventory.setItem(ready2.getSlot(), ready2.getItemStack());

        ReadyTimer readyTimer = new ReadyTimer(ready1, ready2, 49);

        readyTimers.add(readyTimer);

        inventory.setItem(readyTimer.getSlot(), readyTimer.getItemStack());


    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory(Player player) {
        player.openInventory(getInventory());
    }

    public void setItemsToInv() {
        items.forEach((key, value) -> {
            int[] p1 = {0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30};
            int[] p2 = {5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35};
            int j = 0;
            int k = 0;
            for (ItemStack itemStack : value) {
                if (players1.contains(key)) {
                    slot.put(p1[j], itemStack);
                    inventory.setItem(p1[j], itemStack);
                    j++;
                } else if (players2.contains(key)) {
                    slot.put(p2[k], itemStack);
                    inventory.setItem(p2[k], itemStack);
                    k++;
                }
            }
        });
    }

    private Inventory moveup(int[] array, int index) {
        int newIndex = index++;
        if (index < array.length) {
            inventory.setItem(index, inventory.getItem(newIndex));
            inventory.setItem(newIndex, new ItemStack(Material.AIR));
            return moveup(array, newIndex);
        } else {
            return inventory;
        }
    }
}