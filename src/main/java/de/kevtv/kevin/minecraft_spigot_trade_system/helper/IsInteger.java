package de.kevtv.kevin.minecraft_spigot_trade_system.helper;

public class IsInteger {

    public static boolean isInteger(String eingabe) {
        boolean isValidInteger = false;
        try {
            Integer.parseInt(eingabe);
            isValidInteger = true;
        } catch (NumberFormatException e) {

        }
        return isValidInteger;
    }

}
