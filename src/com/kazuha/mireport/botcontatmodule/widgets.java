package com.kazuha.mireport.botcontatmodule;

import com.kazuha.mireport.main;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bungee.event.MiraiGroupMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Random;

import static com.kazuha.mireport.main.config;

public class widgets implements Listener {
    @EventHandler
    public void onChat(MiraiGroupMessageEvent e){
        if(!main.config.getBoolean("enable-widgets")){
            return;
        }
        if(e.getGroupID() != main.config.getLong("player-group-num")){
            return;
        }
        if(e.getMessage() == main.config.getString("muteme")){
            Random random = new Random();
            int bantime = random.nextInt(10);
            MiraiBot.getBot(config.getLong("botaccount")).getGroup(main.config.getLong("player-group-num")).getMember(e.getSenderID()).setMute(bantime);
        }
    }
}
