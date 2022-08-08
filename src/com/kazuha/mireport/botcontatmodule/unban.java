package com.kazuha.mireport.botcontatmodule;

import com.kazuha.mireport.main;
import jline.internal.Log;
import litebans.api.Database;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bungee.event.MiraiGroupMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.xml.crypto.Data;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static com.kazuha.mireport.main.config;
import static com.kazuha.mireport.main.jdbc_plugin_url;
import static com.kazuha.mireport.playercommandmodule.bind.qqbind;
import static com.kazuha.mireport.playercommandmodule.bind.qqexpire;

public class unban implements Listener {
    public static HashMap<Long, Long> gash = new HashMap<>();
    @EventHandler
    public void onUnban(MiraiGroupMessageEvent e){

        if(e.getGroupID() != main.config.getLong("player-group-num")){
            return;
        }
        if(!e.getMessage().equalsIgnoreCase(".unban")){
            return;
        }
        if(gash.containsKey(e.getSenderID())){
            if((System.currentTimeMillis() - gash.get(e.getSenderID())) > 7200000){
                SimpleDateFormat format = new SimpleDateFormat("HH小时mm分钟ss秒");
                e.reply("错误：冷却中！\n请等待"+ format.format(System.currentTimeMillis() - gash.get(e.getSenderID())) + "后再试");
                return;
            }
        }
        //CREATE TABLE `mhxj_robot`.`robot` ( `qq` BIGINT NOT NULL , `uuid` MEDIUMTEXT NOT NULL , `name` TEXT NOT NULL ) ENGINE = InnoDB;
        Thread thread = new Thread(() -> {
            try {
                Connection connection = DriverManager.getConnection(jdbc_plugin_url,config.getString("plugin-db.username"),config.getString("plugin-db.password"));
                PreparedStatement statement = connection.prepareStatement("SELECT * from mreport where qq=?");
                statement.setLong(1,e.getSenderID());
                ResultSet set = statement.executeQuery();
                if(!set.next()){
                    e.reply("你没有绑定账号！请寻找管理员解封");
                }else{
                    UUID uuid = UUID.fromString(set.getString("uuid"));
                    if(Database.get().isPlayerBanned(uuid,null)){
                        PreparedStatement st = Database.get().prepareStatement("SELECT * FROM {bans} WHERE uuid=?");
                        st.setString(1,uuid.toString());
                        ResultSet sest = st.executeQuery();
                        if(sest.getString("banned_by_uuid").equals("CONSOLE")){
                            ProxyServer.getInstance().getConsole().sendMessage(String.valueOf(e.getSenderID()) + "自助解封: " + set.getString("name"));
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "unban "+ set.getString("name")+" QQ群自助解封: "+String.valueOf(e.getSenderID()));
                            e.reply("解封成功！");
                            gash.put(e.getSenderID(),System.currentTimeMillis());
                        }else{
                            e.reply("抱歉，由于你是管理组成员封禁 无法将你解封. 请联系"+ sest.getString("banned_by_name"));
                        }
                        sest.close();
                        st.close();
                    }

                }
                set.close();
                statement.close();
                connection.close();
                Thread.currentThread().stop();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        thread.start();
    }
    @EventHandler
    public void onBind(MiraiGroupMessageEvent e){

        if(e.getGroupID() != main.config.getLong("player-group-num")){
            return;
        }
        if(!e.getMessage().equals(".bind")){
            return;
        }
        //CREATE TABLE `mhxj_robot`.`robot` ( `qq` BIGINT NOT NULL , `uuid` MEDIUMTEXT NOT NULL , `name` TEXT NOT NULL ) ENGINE = InnoDB;
        Thread thread = new Thread(() -> {
            try {
                Connection connection = DriverManager.getConnection(jdbc_plugin_url,config.getString("plugin-db.username"),config.getString("plugin-db.password"));
                PreparedStatement statement = connection.prepareStatement("SELECT * from robot where qq=?");
                statement.setLong(1,e.getSenderID());
                ResultSet set = statement.executeQuery();
                if(set.next()){
                    e.reply("你已经绑定过了！");
                    set.close();
                    connection.close();
                    return;
                }else{
                    e.reply("验证已开始！请查看私聊获取验证码!");
                    Random random = new Random();
                    StringBuilder builder = new StringBuilder();
                    builder = builder.append(random.nextInt());
                    qqbind.put(Long.getLong(builder.toString()),e.getSenderID());
                    qqexpire.put(Long.getLong(builder.toString()),System.currentTimeMillis());
                    MiraiBot.getBot(config.getLong("botaccount")).getFriend(e.getSenderID()).sendMessage("你的QQ绑定验证码为:\n"+ builder.toString() + "\n请在游戏内输入/qq "+ builder.toString() +"以完成绑定！");
                }
                set.close();
                statement.close();
                connection.close();
                Thread.currentThread().stop();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        thread.start();
    }
}