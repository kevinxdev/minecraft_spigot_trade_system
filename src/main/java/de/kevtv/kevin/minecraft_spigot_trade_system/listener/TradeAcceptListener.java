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



}
