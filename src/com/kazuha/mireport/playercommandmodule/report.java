package com.kazuha.mireport.playercommandmodule;

import com.kazuha.mireport.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.Map;

import static com.kazuha.mireport.main.config;
import static com.kazuha.mireport.utils.publicutisl.sendReportMessageGlobal;

public class report extends Command {
    public Map<CommandSender,Long> cd = new HashMap<>();
    public report(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] arg) {
        if(!sender.hasPermission("miraireport.report")){
            sender.sendMessage("§c权限不足");
            return;
        }
        if(arg.length < 2){
            sender.sendMessage("§c参数错误: /report <玩家名> <理由>");
            return;
        }
        if(ProxyServer.getInstance().getPlayer(arg[0]) == null){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',main.config.getString("playernotOLmessage")));
            return;
        }
        if(cd.containsKey(sender)){
            if((System.currentTimeMillis() - cd.get(sender)) < (main.config.getInt("cooldown")*1000L)){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',main.config.getString("cooldownmsg").replace("%cd%", String.valueOf(main.config.getInt("cooldown") - (System.currentTimeMillis()-cd.get(sender))/1000L))));
                return;
            }
        }else{
            cd.put(sender, System.currentTimeMillis());
        }
        cd.put(sender, System.currentTimeMillis());
        StringBuilder ReportMsg = new StringBuilder();
        for(int i = 1;i< arg.length; i++)ReportMsg.append(arg[i]).append(" ");
        sendReportMessageGlobal(ReportMsg.toString(),arg[0],sender.getName(), true);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("reportSuccessMessage")));
    }
}
