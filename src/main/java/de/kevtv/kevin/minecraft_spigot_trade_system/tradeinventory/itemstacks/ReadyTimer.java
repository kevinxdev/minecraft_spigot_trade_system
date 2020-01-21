package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks;

import de.kevtv.kevin.minecraft_spigot_trade_system.Main;
import de.kevtv.kevin.minecraft_spigot_trade_system.commands.TradeAcceptCommand;
import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.data.MySQL;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.HashMapHelper;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.InventoryListener;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.TradeInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.UUID;

public class ReadyTimer {

    private Ready ready1;
    private Ready ready2;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private int slot;
    private int state;
    private Player tradeInvPlayer;
    private BukkitTask id;
    private BukkitScheduler scheduler;

    public ReadyTimer(Ready ready1, Ready ready2, int slot) {
        this.ready1 = ready1;
        this.ready2 = ready2;
        if (TradeAcceptCommand.tradeInventoryHashMap.get(ready1.getPlayer()) != null) {
            tradeInvPlayer = ready1.getPlayer();
        } else {
            tradeInvPlayer = Bukkit.getPlayer(UUID.fromString(Objects.requireNonNull(HashMapHelper.getKey(TradeAcceptListener.tradeRequests, ready1.getPlayer().getUniqueId().toString()))));
        }
        setSlot(slot);
        setNotReadyTimerState();
    }

    public Player getPlayer1() {
        return ready1.getPlayer();
    }

    public Player getPlayer2() {
        return ready2.getPlayer();
    }

    public void checkState() {
        if (ready1.getState() == 1) {
            if (ready2.getState() == 1) {
                setReadyTimerState();
            } else {
                setOneIsReadyTimerState(ready2.getPlayer());
            }
        }
        if (ready2.getState() == 1) {
            if (ready1.getState() == 1) {
                setReadyTimerState();
            } else {
                setOneIsReadyTimerState(ready1.getPlayer());
            }
        }
        if (ready1.getState() == 2) {
            if (ready2.getState() == 0) {
                cancelTask();
                ready1.setReadyState();
                setOneIsReadyTimerState(ready2.getPlayer());
            }
        }
        if (ready2.getState() == 2) {
            if (ready1.getState() == 0) {
                cancelTask();
                ready2.setReadyState();
                setOneIsReadyTimerState(ready1.getPlayer());
            }
        }
        if (ready1.getState() == 0 && ready2.getState() == 0) {
            setNotReadyTimerState();
        }
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(Material material) {
        this.itemStack = new ItemStack(material);
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

    public void setNotReadyTimerState() {
        setState(0);
        setItemStack(Material.RED_STAINED_GLASS_PANE);
        setItemMeta();
        itemMeta.setDisplayName(TextConfig.getTextConfig().getString("tradeInventory-notReadyTimer-state-name"));
        itemStack.setItemMeta(itemMeta);
        TradeAcceptCommand.tradeInventoryHashMap.get(ready1.getPlayer()).getInventory().setItem(getSlot(), getItemStack());
    }

    public void setOneIsReadyTimerState(Player player) {
        setItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        setItemMeta();
        itemMeta.setDisplayName(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-oneIsReadyTimer-state-name")), player.getName()));
        itemStack.setItemMeta(itemMeta);
        TradeAcceptCommand.tradeInventoryHashMap.get(ready1.getPlayer()).getInventory().setItem(getSlot(), getItemStack());
    }

    public void setReadyTimerState() {
        setState(1);
        setItemStack(Material.GREEN_STAINED_GLASS_PANE);
        setItemMeta();
        itemMeta.setDisplayName(TextConfig.getTextConfig().getString("tradeInventory-ready-state-name"));
        itemStack.setItemMeta(itemMeta);
        TradeAcceptCommand.tradeInventoryHashMap.get(ready1.getPlayer()).getInventory().setItem(getSlot(), getItemStack());
        readyTimerCountdown();
    }

    public void readyTimerCountdown() {
        setState(2);
        setItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ready1.setDoneState();
        ready2.setDoneState();
        scheduler = Bukkit.getServer().getScheduler();
        id = scheduler.runTaskTimer(Main.getPlugin(), new Runnable() {
            int countDown = 3;

            @Override
            public void run() {
                if (countDown < 0) {
                    cancelTask();
                    closeInventorys();
                    return;
                }
                System.out.println(countDown);
                itemMeta.setDisplayName(String.format(Objects.requireNonNull(TextConfig.getTextConfig().getString("tradeInventory-timer-countdown")), this.countDown));
                itemStack.setItemMeta(itemMeta);
                TradeAcceptCommand.tradeInventoryHashMap.get(tradeInvPlayer).getInventory().setItem(getSlot(), getItemStack());
                this.countDown--;
            }
        }, 20, 20);

    }

    private void cancelTask() {
        scheduler.cancelTask(id.getTaskId());
    }

    private void closeInventorys() {
        for (ItemStack item : TradeInventory.items.get(ready1.getPlayer())) {
            ready2.getPlayer().getInventory().addItem(item);
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setLocalizedName("");
            item.setItemMeta(itemMeta);
        }
        for (ItemStack item : TradeInventory.items.get(ready2.getPlayer())) {
            ready1.getPlayer().getInventory().addItem(item);
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setLocalizedName("");
            item.setItemMeta(itemMeta);
        }
        for (int i = 0; i < TradeInventory.moneyAmounts.size(); i++) {
            if (TradeInventory.moneyAmounts.get(i).getPlayer() == ready1.getPlayer()) {
                MySQL.addMoneyOfPlayer(ready2.getPlayer(), TradeInventory.moneyAmounts.get(i).getMoney());
                MySQL.remMoneyOfPlayer(ready1.getPlayer(), TradeInventory.moneyAmounts.get(i).getMoney());
            }
        }
        for (int i = 0; i < TradeInventory.moneyAmounts.size(); i++) {
            if (TradeInventory.moneyAmounts.get(i).getPlayer() == ready2.getPlayer()) {
                MySQL.addMoneyOfPlayer(ready1.getPlayer(), TradeInventory.moneyAmounts.get(i).getMoney());
                MySQL.remMoneyOfPlayer(ready2.getPlayer(), TradeInventory.moneyAmounts.get(i).getMoney());
            }
        }
        InventoryListener.finished = true;
        ready1.getPlayer().closeInventory();
        ready2.getPlayer().closeInventory();
        InventoryListener.finished = false;
        TradeInventory.clearArrays(ready1.getPlayer());
        TradeInventory.clearArrays(ready2.getPlayer());
        TradeAcceptCommand.tradeInventoryHashMap.remove(tradeInvPlayer);
    }

}
