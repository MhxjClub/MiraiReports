package com.kazuha.mireport.playercommandmodule.ReportGui;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.inventory.Inventory;
import dev.simplix.protocolize.api.item.ItemStack;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class drawGui {
    public drawGui(ProxiedPlayer playerx, ProxiedPlayer target){
        Inventory inventory = new Inventory(InventoryType.LEGACY_CHEST_9X3);
        inventory.title("§c§l为什么举报 §n"+target.getDisplayName()+"§c§l?");
        ProtocolizePlayer player = Protocolize.playerProvider().player(playerx.getUniqueId());
        inventory.item(11,cheat());
        inventory.item(13,chat());
        inventory.item(15,abuse());
        inventory.item(26,close());
        player.openInventory(inventory);
        inventory.onClick(click->{
            switch (click.slot()){
                case 11:{
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(playerx,"report "+target.getName()+" 第三方软件作弊[G]");
                    player.closeInventory();
                    break;
                }
                case 13:{
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(playerx,"report "+target.getName()+" 言论违规/宣传[G]");
                    player.closeInventory();
                    break;
                }
                case 15:{
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(playerx,"report "+target.getName()+" 利用BUG/RMB交易[G]");
                    player.closeInventory();
                    break;
                }
                case 26:{
                    player.closeInventory();
                    break;
                }
            }
        });


    }
    public ItemStack close(){
        ItemStack cheat = new ItemStack(ItemType.BARRIER);
        cheat.displayName("§c关闭");
        cheat.addToLore("§7===========================");
        return cheat;
    }
    public ItemStack abuse(){
        ItemStack cheat = new ItemStack(ItemType.BOW);
        cheat.displayName("§c滥用");
        cheat.addToLore("§7===========================");
        cheat.addToLore("§f恶意使用BUG 获取优势/神器");
        cheat.addToLore("§f或RMB交易等扰乱游戏秩序的行为");
        return cheat;
    }
    public ItemStack chat(){
        ItemStack cheat = new ItemStack(ItemType.FLINT_AND_STEEL);
        cheat.displayName("§c言论");
        cheat.addToLore("§7===========================");
        cheat.addToLore("§f聊天或喊话含有");
        cheat.addToLore("§f辱骂/扣字/宣传/违法等内容");
        return cheat;
    }
    public ItemStack cheat(){
        ItemStack cheat = new ItemStack(ItemType.REDSTONE_TORCH);
        cheat.displayName("§c作弊");
        cheat.addToLore("§7===========================");
        cheat.addToLore("§f做到了非正常玩家可以做到的事。");
        return cheat;
    }
}
