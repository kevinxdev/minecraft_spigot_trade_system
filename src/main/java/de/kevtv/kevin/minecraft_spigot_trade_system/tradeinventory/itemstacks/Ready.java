package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks;

import de.kevtv.kevin.minecraft_spigot_trade_system.commands.TradeAcceptCommand;
import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.HashMapHelper;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.UUID;

public class Ready {

    private Player player;
    private Player tradeInvPlayer;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private int slot;
    private int state;

    public Ready(Player player, int slot) {
        this.player = player;
        if (TradeAcceptCommand.tradeInventoryHashMap.get(player) != null) {
            tradeInvPlayer = player;
        } else {
            tradeInvPlayer = Bukkit.getPlayer(UUID.fromString(Objects.requireNonNull(HashMapHelper.getKey(TradeAcceptListener.tradeRequests, player.getUniqueId().toString()))));
        }
        setSlot(slot);
        setNotReadyState();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(Material material) {
        itemStack = new ItemStack(material);
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public void setItemMeta() {
        this.itemMeta = getItemStack().getItemMeta();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setNotReadyState() {
        setState(0);
        setItemStack(Material.RED_WOOL);
        setItemMeta();
        itemMeta.setDisplayName(TextConfig.getTextConfig().getString("tradeInventory-notReady-state-name"));
        itemStack.setItemMeta(itemMeta);
        TradeAcceptCommand.tradeInventoryHashMap.get(tradeInvPlayer).getInventory().setItem(getSlot(), getItemStack());
    }

    public void setReadyState() {
        setState(1);
        setItemStack(Material.YELLOW_WOOL);
        setItemMeta();
        itemMeta.setDisplayName(TextConfig.getTextConfig().getString("tradeInventory-ready-state-name"));
        itemStack.setItemMeta(itemMeta);
        TradeAcceptCommand.tradeInventoryHashMap.get(tradeInvPlayer).getInventory().setItem(getSlot(), getItemStack());
    }

    public void setDoneState() {
        setState(2);
        setItemStack(Material.GREEN_WOOL);
        setItemMeta();
        itemMeta.setDisplayName(TextConfig.getTextConfig().getString("tradeInventory-done-state-name"));
        itemStack.setItemMeta(itemMeta);
        TradeAcceptCommand.tradeInventoryHashMap.get(tradeInvPlayer).getInventory().setItem(getSlot(), getItemStack());
    }

}
