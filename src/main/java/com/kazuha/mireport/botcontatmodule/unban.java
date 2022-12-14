package com.kazuha.mireport.botcontatmodule;

import com.kazuha.mireport.main;

import litebans.api.Database;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bungee.event.MiraiFriendAddEvent;
import me.dreamvoid.miraimc.bungee.event.MiraiGroupMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.xml.crypto.Data;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static com.kazuha.mireport.main.*;
import static com.kazuha.mireport.playercommandmodule.bind.qqbind;
import static com.kazuha.mireport.playercommandmodule.bind.qqexpire;

public class unban implements Listener {
    public static HashMap<Long, Long> gash = new HashMap<>();
    @EventHandler
    public void onUnban(MiraiGroupMessageEvent e){


        if(!e.getMessage().equalsIgnoreCase(".unban")){
            return;
        }
        if(gash.containsKey(e.getSenderID())){
            if((System.currentTimeMillis() - gash.get(e.getSenderID())) < 9000000){
                SimpleDateFormat format = new SimpleDateFormat("hh小时mm分钟ss秒");
                e.reply("错误：冷却中！\n请等待"+ format.format(9000000 - (System.currentTimeMillis() - gash.get(e.getSenderID()))) + "后再试");
                return;
            }
        }
        //CREATE TABLE `mhxj_robot`.`robot` ( `qq` BIGINT NOT NULL , `uuid` MEDIUMTEXT NOT NULL , `name` TEXT NOT NULL ) ENGINE = InnoDB;
        ProxyServer.getInstance().getScheduler().runAsync(instance,()->{
            try {
                Connection connection = DriverManager.getConnection(jdbc_plugin_url,config.getString("plugin-db.username"),config.getString("plugin-db.password"));
                PreparedStatement statement = connection.prepareStatement("SELECT * from robot where qq=?");
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
                        if(!sest.next()){
                            e.reply("错误：未知错误 请联系Frk");
                            return;
                        }
                        sest.last();
                        if(!sest.getString("banned_by_name").equalsIgnoreCase("LAC")){
                            e.reply("抱歉，由于你是管理组成员封禁 无法将你解封. 请联系"+ sest.getString("banned_by_name"));
                        }else{
                            ProxyServer.getInstance().getConsole().sendMessage(String.valueOf(e.getSenderID()) + "自助解封: " + set.getString("name"));
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "unban "+ set.getString("name")+" QQ群自助解封: "+String.valueOf(e.getSenderID()));
                            e.reply("解封成功！");
                            gash.put(e.getSenderID(),System.currentTimeMillis());
                        }
                        sest.close();
                        st.close();
                    }else{
                        e.reply("你没有被封禁！解个寂寞");
                    }

                }
                set.close();
                statement.close();
                connection.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

    }

    @EventHandler
    public void onBind(MiraiGroupMessageEvent e){


        if(!e.getMessage().equals(".bind")){
            return;
        }
        //CREATE TABLE `mhxj_robot`.`robot` ( `qq` BIGINT NOT NULL , `uuid` MEDIUMTEXT NOT NULL , `name` TEXT NOT NULL ) ENGINE = InnoDB;
        ProxyServer.getInstance().getScheduler().runAsync(instance,()->{
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
                    Random random = new Random();
                    Long ran = Long.parseLong(String.valueOf(random.nextInt(1145141919)));
                    qqbind.put(ran,e.getSenderID());
                    qqexpire.put(ran,System.currentTimeMillis());
                    e.reply("你的QQ绑定验证码为:"+ ran + "\n请在游戏内输入/qq "+ ran +"以完成绑定！");
                }
                set.close();
                statement.close();
                connection.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });


    }
}