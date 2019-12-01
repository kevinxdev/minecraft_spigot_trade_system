package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.data.MySQL;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.TradeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MoneyAmount {

    private ItemStack moneyAmount;
    private ItemMeta moneyAmountMeta;
    private Player player;
    private int money;
    private int slot;
    private Inventory inventory;
    private int playerMoneyAmount;

    public MoneyAmount(Player player, int slot) {
        setPlayer(player);
        setPlayerMoneyAmount();
        setMoneyAmount();
        setMoneyAmountMeta();
        setSlot(TradeInventory.getInventory(), slot);
        setMoney(0);
    }

    public int getPlayerMoneyAmount() {
        return playerMoneyAmount;
    }

    public void setPlayerMoneyAmount() {
        this.playerMoneyAmount = MySQL.getMoneyOfPlayer(player);
        System.out.println(playerMoneyAmount);
    }

    public void setMoney(int money) {
        this.money = money;
        setMoneyAmountDisplayName();
    }

    public void addMoney(int moneyToAdd) {
        this.money += moneyToAdd;
        setMoneyAmountDisplayName();
    }

    public void remMoney(int moneyToRem) {
        this.money -= moneyToRem;
        setMoneyAmountDisplayName();
    }

    public int getMoney() {
        return money;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setMoneyAmount() {
        moneyAmount = new ItemStack(Material.GOLD_BLOCK);
    }

    public ItemStack getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmountMeta() {
        moneyAmountMeta = getMoneyAmount().getItemMeta();
    }

    public ItemMeta getMoneyAmountMeta() {
        return moneyAmountMeta;
    }

    public void setMoneyAmountDisplayName() {
        getMoneyAmountMeta().setDisplayName(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-moneyAmount-name")), getMoney()));
        moneyAmount.setItemMeta(moneyAmountMeta);
        getInventory().setItem(slot, getMoneyAmount());
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(Inventory inventory, int slot) {
        this.slot = slot;
        setInventory(inventory);
        getInventory().setItem(slot, getMoneyAmount());
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }


}
