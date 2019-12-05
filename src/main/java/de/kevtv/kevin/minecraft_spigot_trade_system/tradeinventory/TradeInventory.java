package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.Border;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyAmount;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyMinus;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class TradeInventory {

    private static Inventory inventory;
    public static ArrayList<MoneyAmount> moneyAmounts = new ArrayList<>();
    public static ArrayList<MoneyMinus> moneyMinuses = new ArrayList<>();
    public static ArrayList<MoneyPlus> moneyPluses = new ArrayList<>();

    public static void createInventory(Player player) {
        inventory = Bukkit.createInventory(null, 54, Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-inventory-name")));
        placeBorder(player);
    }

    private static void placeBorder(Player player) {
        Border.setBorder();
        Border.setBorderMeta();
        Border.setBorderName();
        for(int i = 4; i < inventory.getSize(); i+=9) {
            inventory.setItem(i, Border.getBorder());
        }
        for(int i = 36; i < 45; i++) {
            inventory.setItem(i, Border.getBorder());
        }

        Player player2 = Bukkit.getPlayer(UUID.fromString(TradeAcceptListener.tradeRequests.get(player.getUniqueId().toString())));

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


    }

    public static Inventory getInventory() {
        return inventory;
    }

    public static void openInventory(Player player) {
        player.openInventory(TradeInventory.getInventory());
    }

    public static void clearArrays(Player player) {
        for(int i = 0; i < moneyAmounts.size(); i++) {
            if(moneyAmounts.get(i).getPlayer() == player) {
                moneyAmounts.remove(moneyAmounts.get(i));
            }
        }
        for(int i = 0; i < moneyMinuses.size(); i++) {
            if(moneyMinuses.get(i).getPlayer() == player) {
                moneyMinuses.remove(moneyMinuses.get(i));
            }
        }
        for(int i = 0; i < moneyPluses.size(); i++) {
            if(moneyPluses.get(i).getPlayer() == player) {
                moneyPluses.remove(moneyPluses.get(i));
            }
        }
    }

    public static void registerTexts() {
        if(TextConfig.getTextConfig().getString("tradeInventory-inventory-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-inventory-name", "Trade Inventar");
        }
        if(TextConfig.getTextConfig().getString("tradeInventory-border-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-border-name", "Border");
        }
        if(TextConfig.getTextConfig().getString("tradeInventory-moneyAmount-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-moneyAmount-name", "%s Geos");
        }
        if(TextConfig.getTextConfig().getString("tradeInventory-moneyMinus-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-moneyMinus-name", "Minus 100 Geos");
        }
        if(TextConfig.getTextConfig().getString("tradeInventory-moneyPlus-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-moneyPlus-name", "Plus 100 Geos");
        }
        if(TextConfig.getTextConfig().getString("tradeInventory-anvilInventory-name") == null) {
            TextConfig.getTextConfig().set("tradeInventory-anvilInventory-name", "Geos angeben:");
        }
    }
}