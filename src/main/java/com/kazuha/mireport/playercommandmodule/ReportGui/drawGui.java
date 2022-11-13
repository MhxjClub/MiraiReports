package com.kazuha.mireport.playercommandmodule.ReportGui;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.inventory.Inventory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class drawGui {
    public drawGui(ProxiedPlayer playerx, ProxiedPlayer target){
        Inventory inventory = new Inventory(InventoryType.LEGACY_CHEST_9X3);
        inventory.title("§c举报玩家 - "+target.getDisplayName());
        ProtocolizePlayer player= Protocolize.playerProvider().player(playerx.getUniqueId());
        player.openInventory(inventory);
    }
}
