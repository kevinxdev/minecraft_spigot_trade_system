package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MoneyPlus {

    private ItemStack moneyPlus;
    private ItemMeta moneyPlusMeta;
    private MoneyAmount moneyAmount;

    public MoneyPlus(MoneyAmount moneyAmount) {
        setMoneyAmount(moneyAmount);
        setMoney(0);
        setMoneyPlus();
        setMoneyPlusMeta();
        setMoneyPlusDisplayName();
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

    public void setMoneyPlus() {
        moneyPlus = new ItemStack(Material.EMERALD_BLOCK);
    }

    public ItemStack getMoneyPlus() {
        return moneyPlus;
    }

    public void setMoneyPlusMeta() {
        moneyPlusMeta = getMoneyPlus().getItemMeta();
    }

    public ItemMeta getMoneyPlusMeta() {
        return moneyPlusMeta;
    }

    public void setMoneyPlusDisplayName() {
        getMoneyPlusMeta().setDisplayName(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-moneyPlus-name")));
        moneyPlus.setItemMeta(moneyPlusMeta);
    }

}
