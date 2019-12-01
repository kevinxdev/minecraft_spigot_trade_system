package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory;

import de.kevtv.kevin.minecraft_spigot_trade_system.Main;
import de.kevtv.kevin.minecraft_spigot_trade_system.helper.IsInteger;
import de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks.MoneyAmount;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public class AnvilInput {

    private AnvilGUI.Builder inventory;
    private int moneyOfUser;
    private int money;

    public AnvilGUI.Builder getInventory() {
        return inventory;
    }

    public void createInventory(Player player) {
        for(MoneyAmount moneyAmount : TradeInventory.moneyAmounts) {
            if(moneyAmount.getPlayer() == player) {
                setMoneyOfUser(moneyAmount.getMoney());
            }
        }
        inventory = new AnvilGUI.Builder()
                .onComplete((player1, text) -> {
                    if(IsInteger.isInteger(text)) {
                        money = Integer.parseInt(text);
                        for(MoneyAmount moneyAmount : TradeInventory.moneyAmounts) {
                            if(moneyAmount.getPlayer() == player1) {
                                if(money <= moneyAmount.getPlayerMoneyAmount()) {
                                    moneyAmount.setMoney(Integer.parseInt(text));
                                    return AnvilGUI.Response.close();
                                } else {
                                    moneyAmount.setMoney(moneyAmount.getPlayerMoneyAmount());
                                    return AnvilGUI.Response.close();
                                }
                            }
                        }
                    }
                    return AnvilGUI.Response.text(String.valueOf(getMoneyOfUser()));
                })
                .preventClose()
                .text(String.valueOf(getMoneyOfUser()))
                .plugin(Main.getPlugin());
    }

    public int getMoneyOfUser() {
        return moneyOfUser;
    }

    public void setMoneyOfUser(int moneyOfUser) {
        this.moneyOfUser = moneyOfUser;
    }
}
