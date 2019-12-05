package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory;

import de.kevtv.kevin.minecraft_spigot_trade_system.Main;
import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.HashMapHelper;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.IsInteger;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.InventoryListener;
import de.kevtv.kevin.minecraft_spigot_trade_system.listener.TradeAcceptListener;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyAmount;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class AnvilInput {

    private AnvilGUI inventory;
    private int moneyOfUser;
    private int money;
    private boolean closing;
    private Player player;
    public boolean inMode;

    public AnvilInput() {
        inMode = true;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void createInventory(Player player) {
        inMode = true;
        setPlayer(player);
        for(MoneyAmount moneyAmount : TradeInventory.moneyAmounts) {
            if(moneyAmount.getPlayer() == player) {
                setMoneyOfUser(moneyAmount.getMoney());
            }
        }
        closing = false;
        inventory = new AnvilGUI.Builder()
                .onClose(player1 -> {
                    inMode = false;
                    InventoryListener.anvilInputs.remove(InventoryListener.anvilInput.get(player1));
                    InventoryListener.anvilInput.remove(player1);
                    if(!closing) {
                        System.out.println("CloseANVIL");
                        InventoryListener.closeInventorys(player1);
                    } else {
                        TradeInventory.openInventory(player1);
                    }
                })
                .onComplete((player1, text) -> {
                    if(IsInteger.isInteger(text)) {
                        money = Integer.parseInt(text);
                        for(MoneyAmount moneyAmount : TradeInventory.moneyAmounts) {
                            if(moneyAmount.getPlayer() == player1) {
                                closing = true;
                                if(money < 0) {
                                    moneyAmount.setMoney(0);
                                    return AnvilGUI.Response.close();
                                } else if(money <= moneyAmount.getPlayerMoneyAmount()) {
                                    moneyAmount.setMoney(Integer.parseInt(text));
                                    return AnvilGUI.Response.close();
                                } else {
                                    moneyAmount.setMoney(moneyAmount.getPlayerMoneyAmount());
                                    return AnvilGUI.Response.close();
                                }
                            }
                        }
                    }
                    return AnvilGUI.Response.close();
                })
                .text(String.valueOf(getMoneyOfUser()))
                .plugin(Main.getPlugin())
                .open(getPlayer());
    }

    public int getMoneyOfUser() {
        return moneyOfUser;
    }

    public void setMoneyOfUser(int moneyOfUser) {
        this.moneyOfUser = moneyOfUser;
    }
}
