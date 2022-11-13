package com.kazuha.mireport.playercommandmodule;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

import static com.kazuha.mireport.main.config;
import static com.kazuha.mireport.main.jdbc_plugin_url;

public class bind extends Command {
    public bind(String name) {
        super(name);
    }

    public static HashMap<Long, Long> qqbind = new HashMap<>();
    public static HashMap<Long, Long> qqexpire = new HashMap<>();

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage("§c用法错误. 正确用法: /qq <验证码>");
            return;
        }
        if (qqbind.containsKey(Long.parseLong(strings[0]))) {
            if ((System.currentTimeMillis() - qqexpire.get(Long.parseLong(strings[0]))) > 600000) {
                qqexpire.remove(Long.parseLong(strings[0]));
                qqbind.remove(Long.parseLong(strings[0]));
                commandSender.sendMessage("§c验证码已过期！请尝试重新开始验证过程");
                return;
            } else {
                try {
                    Connection connection = DriverManager.getConnection(jdbc_plugin_url, config.getString("plugin-db.username"), config.getString("plugin-db.password"));
                    PreparedStatement state = connection.prepareStatement("SELECT * FROM `robot` WHERE `name`=?");
                    state.setString(1, commandSender.getName());
                    ResultSet set = state.executeQuery();
                    if(set.next()){
                        commandSender.sendMessage("§c你已经绑定过了！");
                        set.close();
                        connection.close();
                        return;
                    }
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO `robot`(`qq`, `uuid`, `name`) VALUES (?,?,?)");
                    statement.setLong(1, qqbind.get(Long.parseLong(strings[0])));
                    statement.setString(2, String.valueOf(ProxyServer.getInstance().getPlayer(commandSender.getName()).getUniqueId()));
                    statement.setString(3, commandSender.getName());
                    statement.executeUpdate();
                    commandSender.sendMessage("§a验证成功！你可以使用.unban了!");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            commandSender.sendMessage("§c错误：请先加入QQ群："+ config.getLong("player-group-num")+"输入.bind以开始验证！");
            return;
        }
    }
}
