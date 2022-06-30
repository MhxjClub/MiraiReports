package com.kazuha.mireport.botcontatmodule;


import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bungee.event.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bungee.event.MiraiGroupMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static com.kazuha.mireport.main.*;
import static com.kazuha.mireport.utils.publicutisl.*;

public class RecievePlayerReport implements Listener {
    @EventHandler
    public void RecieveGroup(MiraiGroupMessageEvent e){
        if(e.getGroupID()!=config.getLong("admin-group-num"))return;
        if(e.getMessage().equalsIgnoreCase(".help")){
            e.sendMessage("§f§lQQ内可用命令列表");
            e.sendMessage("§2.举报 <目标> <理由> §f 举报一个玩家");
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) != null){
                e.sendMessage("§2.ban <目标> §f 封禁一个玩家");
                e.sendMessage("§2.unban <目标> §f 解封一个玩家");
                e.sendMessage("§2.mute <目标> §f 禁言一个玩家");
                e.sendMessage("§2.kick <目标> §f 踢出一个玩家");
                e.sendMessage("§2.cmd <命令> §f (BC)执行一个命令");
            }
        }
        if(e.getMessage().startsWith(".cmd ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String runcmd = e.getMessage().replace(".cmd ", "");
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), runcmd);
            e.sendMessage("成功执行该命令");
        }
        if(e.getMessage().startsWith(".ban ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".ban ", "").replace(" ", "");
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("bancmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功封禁" + bannedPlayer + "");
        }
        if(e.getMessage().startsWith(".unban ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".unban ", "").replace(" ", "");
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("unbancmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功解封" + bannedPlayer + "");
        }
        if(e.getMessage().startsWith(".mute ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".mute ", "").replace(" ", "");
            if(ProxyServer.getInstance().getPlayer(bannedPlayer) == null){
                e.sendMessage("玩家不在线。");
                return;
            }
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("mutecmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功禁言" + bannedPlayer + "");
        }
        if(e.getMessage().startsWith(".kick ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".kick ", "").replace(" ", "");
            if(ProxyServer.getInstance().getPlayer(bannedPlayer) == null){
                e.sendMessage("玩家不在线。");
                return;
            }
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("mutecmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功踢出" + bannedPlayer + "");
        }
    }
    @EventHandler
    public void RecievePlayerReportFromQQ(MiraiFriendMessageEvent e){
        if(e.getMessage().equalsIgnoreCase(".help")){
            e.sendMessage("§f§lQQ内可用命令列表");
            e.sendMessage("§2.举报 <目标> <理由> §f 举报一个玩家");
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) != null){
                e.sendMessage("§2.ban <目标> §f 封禁一个玩家");
                e.sendMessage("§2.unban <目标> §f 解封一个玩家");
                e.sendMessage("§2.mute <目标> §f 禁言一个玩家");
                e.sendMessage("§2.kick <目标> §f 踢出一个玩家");
                e.sendMessage("§2.cmd <命令> §f (BC)执行一个命令");
            }
        }
        if(e.getMessage().startsWith(".unban ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".unban ", "").replace(" ", "");
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("unbancmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功解封" + bannedPlayer + "");
        }
        if(e.getMessage().startsWith(".ban ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".ban ", "").replace(" ", "");
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("bancmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功封禁" + bannedPlayer + "");
        }
        if(e.getMessage().startsWith(".mute ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".mute ", "").replace(" ", "");
            if(ProxyServer.getInstance().getPlayer(bannedPlayer) == null){
                e.sendMessage("玩家不在线。");
                return;
            }
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("mutecmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功禁言" + bannedPlayer + "");
        }
        if(e.getMessage().startsWith(".kick ")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            String bannedPlayer = e.getMessage().replace(".kick ", "").replace(" ", "");
            if(ProxyServer.getInstance().getPlayer(bannedPlayer) == null){
                e.sendMessage("玩家不在线。");
                return;
            }
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), config.getString("mutecmd").replace("%target%", bannedPlayer));
            e.sendMessage("已成功踢出" + bannedPlayer + "");
        }
        if(e.getMessage().startsWith(".举报 ") || e.getMessage().startsWith(".report ")){
            String[] reported = e.getMessage().replace(".举报 ", "").replace(".report ", "").split(" ");
            if(reported.length < 2){
                e.getFriend().sendMessage(FuckYouColorCodes(config.getString("qq-command-failed")));
                return;
            }
            if(instance.getProxy().getPlayer(reported[0]) == null){
                e.getFriend().sendMessage(FuckYouColorCodes(config.getString("playernotOLmessage")));
                return;
            }
            StringBuilder ReportMsg = new StringBuilder();
            for(int i = 1;i< reported.length; i++)ReportMsg.append(reported[i]).append(" ");
            sendReportMessageGlobal(ReportMsg.toString(),reported[0],String.valueOf(e.getSenderID()), false);
            e.getFriend().sendMessage(FuckYouColorCodes(config.getString("reportSuccessMessage")));
        }
    }
}
