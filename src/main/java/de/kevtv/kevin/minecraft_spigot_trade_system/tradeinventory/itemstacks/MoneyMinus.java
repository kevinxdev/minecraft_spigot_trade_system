package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MoneyMinus {

    private ItemStack moneyMinus;
    private ItemMeta moneyMinusMeta;
    private MoneyAmount moneyAmount;

    public MoneyMinus(MoneyAmount moneyAmount) {
        setMoneyAmount(moneyAmount);
        setMoney(0);
        setMoneyMinus();
        setMoneyMinusMeta();
        setMoneyMinusDisplayName();
    }

    public void setMoneyAmount(MoneyAmount moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public MoneyAmount getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoney(int money) {
        moneyAmount.setMoney(money);
    }

    public void addMoney(int moneyToAdd) {
        moneyAmount.addMoney(moneyToAdd);
    }

    public void remMoney(int moneyToRem) {
        moneyAmount.remMoney(moneyToRem);
    }

    public int getMoney() {
        return moneyAmount.getMoney();
    }

    public void setPlayer(Player player) {
        moneyAmount.setPlayer(player);
    }

    public Player getPlayer() {
        return moneyAmount.getPlayer();
    }

    public void setMoneyMinus() {
        moneyMinus = new ItemStack(Material.REDSTONE_BLOCK);
    }

    public ItemStack getMoneyMinus() {
        return moneyMinus;
    }

    public void setMoneyMinusMeta() {
        moneyMinusMeta = getMoneyMinus().getItemMeta();
    }

    public ItemMeta getMoneyMinusMeta() {
        return moneyMinusMeta;
    }

    public void setMoneyMinusDisplayName() {
        getMoneyMinusMeta().setDisplayName(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-moneyMinus-name")));
        moneyMinus.setItemMeta(moneyMinusMeta);
    }

}
