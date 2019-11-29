package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.TradeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MoneyAmount {

    private ItemStack moneyAmount;
    private ItemMeta moneyAmountMeta;
    private Player player;
    private int money;
    private int slot;

    public MoneyAmount(Player player) {
        setPlayer(player);
        setMoney(0);
        setMoneyAmount();
        setMoneyAmountMeta();
        setMoneyAmountDisplayName();
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int moneyToAdd) {
        this.money += moneyToAdd;
    }

    public void remMoney(int moneyToRem) {
        this.money -= moneyToRem;
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
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
