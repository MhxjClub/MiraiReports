package com.kazuha.mireport.botcontatmodule;


import com.google.common.collect.Lists;
import com.kazuha.mireport.main;
import com.kazuha.mireport.utils.SystemUtil;
import com.kazuha.mireport.utils.publicutisl;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bungee.event.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bungee.event.MiraiGroupMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import static com.kazuha.mireport.main.*;
import static com.kazuha.mireport.utils.publicutisl.*;

public class RecievePlayerReport implements Listener {
    @EventHandler
    public void OnRequestOnPlayers(MiraiGroupMessageEvent e){
        ProxyServer.getInstance().getScheduler().runAsync(instance,()->{
            if(config.getStringList("cmd-wake").contains(e.getMessage())){
                float nvzhuang = instance.getProxy().getOnlineCount();
                double yeshi = instance.getProxy().getOnlineCount()/2.0;
                int Online = ProxyServer.getInstance().getOnlineCount();
                e.sendMessage(publicutisl.toQQMessage(
                        "梦幻星际 Mhxj.Club",
                        "=======================",
                        "在线:"+ Online+ "人",
                        "服务器CPU使用率: "+ SystemUtil.getCpuInfo()+" %",
                        "服务器内存使用率: "+ SystemUtil.getMemory()+" %",
                        "服务器负载: "+SystemUtil.getLoad()+" %",
                        "whohh女装进度: "+nvzhuang+"%",
                        "=======================",
                        "Powered By KazuhaAyato"));
            }
        });
    }
    @EventHandler
    public void RecieveGroup(MiraiGroupMessageEvent e){
        if(e.getGroupID()!=config.getLong("admin-group-num"))return;
        if(e.getMessage().equalsIgnoreCase(".help")){
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) != null){
                e.sendMessage("QQ内可用命令列表\n.举报 <目标> <理由>举报一个玩家\n.players 获取在线玩家列表 \n.ban <目标> 封禁一个玩家\n.unban <目标>  解封一个玩家\n.mute <目标>  禁言一个玩家\n.kick <目标>  踢出一个玩家\n.cmd <命令>  (BC)执行一个命令");

            }else{
                e.sendMessage("QQ内可用命令列表\n.举报 <目标> <理由>举报一个玩家");
            }
        }
        if(e.getMessage().startsWith(".players")){
            if(e.getGroupID()!=config.getLong("admin-group-num"))return;
            if(MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).getMember(e.getSenderID()) == null){
                e.sendMessage("你没有权限");
                return;
            }
            int player_count = ProxyServer.getInstance().getOnlineCount();
            StringBuffer output = new StringBuffer();
            output = output.append( "服务器在线玩家("+ player_count+ "):");
            Iterator iterator = ProxyServer.getInstance().getServers().keySet().iterator();
            while (iterator.hasNext()){
                Object cam = iterator.next();
                StringBuffer nor = new StringBuffer();
                Collection<ProxiedPlayer> player = ProxyServer.getInstance().getServerInfo(cam.toString()).getPlayers();
                List<ProxiedPlayer> players = Lists.newArrayList(player);
                for(ProxiedPlayer p : players){
                    nor.append(p.getName()).append(", ");
                }
                output.append("\n[").append(ProxyServer.getInstance().getServerInfo(cam.toString()).getName()).append("][").append(ProxyServer.getInstance().getServerInfo(cam.toString()).getPlayers().size()).append("]").append(nor);
            }
            e.sendMessage(output.toString());
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
                e.sendMessage("QQ内可用命令列表\n.举报 <目标> <理由>举报一个玩家");
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
