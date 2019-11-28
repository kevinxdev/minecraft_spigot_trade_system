package de.kevtv.kevin.minecraft_spigot_trade_system.listener;

import java.util.HashMap;

public class TradeAcceptListener {

    private static HashMap<String, String> tradeRequests = new HashMap<>();

    /**
     * Füge neue Tradeanfrage der tradeRequest HashMap hinzu
     * @param sender_uuid
     * @param receiver_uuid
     */
    public static void addTradeRequest(String sender_uuid, String receiver_uuid) {
        tradeRequests.put(sender_uuid, receiver_uuid);
    }

    /**
     * Überprüft ob ein passendes paar in der HashMap existiert
     * @param sender_uuid
     * @param receiver_uuid
     * @return
     */
    public static boolean isTradeRequest(String sender_uuid, String receiver_uuid) {
        if(tradeRequests.containsKey(sender_uuid) && tradeRequests.containsValue(receiver_uuid)) {
            return true;
        }
        return false;
    }

    /**
     * Entferne die tradeRequest aus der HashMap tradeRequests
     * @param sender_uuid
     * @param receiver_uuid
     */
    public static void removeTradeRequest(String sender_uuid, String receiver_uuid) {
        if(tradeRequests.containsKey(sender_uuid) && tradeRequests.containsValue(receiver_uuid)) {
            tradeRequests.remove(sender_uuid, receiver_uuid);
        }
    }

}
