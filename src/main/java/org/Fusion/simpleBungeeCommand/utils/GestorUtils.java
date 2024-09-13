package org.Fusion.simpleBungeeCommand.utils;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorUtils {


    public static List<String> getCommands() throws IOException {
        File e = new File("plugins/SBC/commands");

        File[] files = e.listFiles();
        List<String> commands = new ArrayList<>();

        for (File file : files) {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            commands.add(config.getString("Command"));
            List<String> alias = config.getStringList("Alias");
            commands.addAll(alias);
        }
        return commands;
    }

    public static Map<String, List<String>> getLores() throws IOException {
        File e = new File("plugins/SBC/commands");
        File[] files = e.listFiles();
        Map<String, List<String>> lores = new HashMap<>();

        for (File file : files) {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            List<String> lore = config.getStringList("Execute");
            lores.put(config.getString("Command"), lore);
            List<String> alisas = config.getStringList("Alias");
            for (String alias : alisas) {
                lores.put(alias, lore);
            }
        }
        return lores;
    }

    public static Map<String, String> getPerms() throws IOException {
        File e = new File("plugins/SBC/commands");
        File[] files = e.listFiles();
        Map<String, String> perms = new HashMap<>();

        for (File file : files) {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            perms.put(config.getString("Command"), config.getString("Permission"));
            List<String> alisas = config.getStringList("Alias");
            for (String alias : alisas) {
                perms.put(alias, config.getString("Permission"));
            }
        }
        return perms;
    }


}
