package de.kevtv.kevin.minecraft_spigot_trade_system.helper;

import java.util.Map;

public class HashMapHelper {

    /**
     * Funktion welche den Key mit dem Value aus der HashMap hollt
     * @param map
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
