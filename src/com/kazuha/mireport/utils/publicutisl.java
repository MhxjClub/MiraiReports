package com.kazuha.mireport.utils;

import me.dreamvoid.miraimc.api.MiraiBot;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.Title;
//  - "&f[&7STAFF&f]&a[QQ %reporter%] &f举报了 &c%reported% &f原因: %reason% &7(&f来自: &a%server% &7)"
import java.sql.Struct;
import java.util.List;

import static com.kazuha.mireport.main.*;

public class publicutisl {
    public static String FuckYouColorCodes(String whatthe){
        whatthe = whatthe.replace("&1","").replace("&2","").replace("&3","").replace("&4","").replace("&5","").replace("&6","").replace("&7", "").replace("&8", "").replace("&9", "").replace("&0", "");
        whatthe = whatthe.replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "").replace("&e", "").replace("&f", "");
        return whatthe;
    }
    public static String toQQMessage(String... Message){
        StringBuilder finaled = new StringBuilder();
        for(String s : Message){
            finaled.append(s).append("\n");
        }
        return finaled.toString();
    }
    public static void sendReportMessageGlobal(String ReportMessage, String ReportedPlayer, String reporter, Boolean ingame){
        if(ProxyServer.getInstance().getPlayer(ReportedPlayer) == null){
            return;
        }
        String arg;
        if(ingame) {
             arg = config.getString("reportFormatinGame");
        }else {
            arg = config.getString("reportFormatinQQ");
        }
                MiraiBot.getBot(config.getLong("botaccount")).getGroup(config.getLong("admin-group-num")).sendMessage(FuckYouColorCodes(arg.replace("%reporter%", reporter).replace("%reported%", ReportedPlayer).replace("%reason%", ReportMessage).replace("%server%", ProxyServer.getInstance().getPlayer(ReportedPlayer).getServer().getInfo().getName())));
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                if (p.hasPermission("miraireport.notify")) {
                    Title titles = ProxyServer.getInstance().createTitle().subTitle((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', config.getString("subtitle").replace("%reporter%", reporter).replace("%reported%", ReportedPlayer)))).create());
                    Title title = ProxyServer.getInstance().createTitle().title((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', config.getString("title").replace("%reporter%", reporter).replace("%reported%", ReportedPlayer)))).create());
                    p.sendTitle(title);
                    p.sendTitle(titles);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', arg.replace("%reporter%", reporter).replace("%reported%", ReportedPlayer).replace("%reason%", ReportMessage).replace("%server%", ProxyServer.getInstance().getPlayer(ReportedPlayer).getServer().getInfo().getName())));
                }
            }
        }
}
