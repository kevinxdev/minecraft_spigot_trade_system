package de.kevtv.kevin.minecraft_spigot_trade_system.tradeinventory.itemstacks;

import de.kevtv.kevin.minecraft_spigot_trade_system.config.TextConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Border {

    private static ItemStack border;
    private static ItemMeta borderMeta;

    public static void setBorder() {
        border = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
    }

    public static ItemStack getBorder() {
        return border;
    }

    public static void setBorderMeta() {
        borderMeta = getBorder().getItemMeta();
    }

    public static ItemMeta getBorderMeta() {
        return borderMeta;
    }

    public static void setBorderName() {
        getBorderMeta().setDisplayName(TextConfig.getTextConfig().getString("tradeInventory-border-name"));
        border.setItemMeta(borderMeta);
    }

}
