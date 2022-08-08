package com.kazuha.mireport;

import com.kazuha.mireport.botcontatmodule.RecievePlayerReport;
import com.kazuha.mireport.botcontatmodule.unban;
import com.kazuha.mireport.botcontatmodule.widgets;
import com.kazuha.mireport.playercommandmodule.bind;
import com.kazuha.mireport.playercommandmodule.miraireport;
import com.kazuha.mireport.playercommandmodule.report;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class main extends Plugin {
    public static Configuration config;
    public static Plugin instance;
    public static String jdbc_plugin_url;
    @Override
    public void onEnable() {
        instance = this;
        saveConfigFile();
        getLogger().info("============================================");
        getLogger().info(ChatColor.DARK_AQUA + "Mirai" + ChatColor.YELLOW + "Reports" + ChatColor.GRAY + " By " + ChatColor.AQUA + "Kazuha" + ChatColor.GOLD + "Ayato");
        getLogger().info("插件版本" + ChatColor.DARK_GREEN + getDescription().getVersion());
        getLogger().info("============================================");
        getLogger().info("加载配置文件..");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbc_plugin_url = "jdbc:mysql://"+config.getString("plugin-db.url")+"/"+config.getString("plugin-db.db");
        try {
            Connection connection = DriverManager.getConnection(jdbc_plugin_url,config.getString("plugin-db.username"),config.getString("plugin-db.password"));
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `mhxj_robot`.`robot` ( `qq` BIGINT NOT NULL , `uuid` MEDIUMTEXT NOT NULL , `name` TEXT NOT NULL ) ENGINE = InnoDB;");
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getProxy().getPluginManager().registerListener(this, new unban());
        getProxy().getPluginManager().registerCommand(this, new bind("qq"));
        getProxy().getPluginManager().registerCommand(this, new report("report"));
        getProxy().getPluginManager().registerListener(this, new widgets());
        getProxy().getPluginManager().registerCommand(this, new miraireport("miraireport"));
        getProxy().getPluginManager().registerListener(this, new RecievePlayerReport());
        getLogger().info("加载完成！");
    }
    @Override
    public void onDisable(){
    }
    public void saveConfigFile() {
        File dir = getDataFolder();
        if (!dir.exists()) dir.mkdir();
        File file = new File(dir, "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
