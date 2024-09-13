package org.Fusion.simpleBungeeCommand.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.Fusion.simpleBungeeCommand.utils.GestorUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommandGestor extends Command {

    private final String usedCommand;

    public CommandGestor(String name) {
        super(name);
        this.usedCommand = name;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        File configFile = new File("plugins/SBC/config.yml");
        Configuration config;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> allCommands;
        Map<String, String> perms;
        try {
            allCommands = GestorUtils.getCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!allCommands.contains(usedCommand)) {
            return;
        }

        try {
            perms = GestorUtils.getPerms();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String perm = perms.get(usedCommand);

        if(!commandSender.hasPermission(perm) || !commandSender.hasPermission("SBC.*") ) {
            commandSender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("NotPerms"))));
            return;
        }

        Map<String, List<String>> lores;

        try {
            lores = GestorUtils.getLores();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> lore = lores.get(usedCommand);

        for(String execute : lore) {
            commandSender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', execute)));
        }



    }


}
