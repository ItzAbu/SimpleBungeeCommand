package org.Fusion.simpleBungeeCommand.customConfig;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomConfig {

    public static void setup() throws IOException {
        File file = new File("plugins/SBC/commands/default.yml");
        File configFile = new File("plugins/SBC/config.yml");

        if(!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

        config.set("Command", "default");
        config.set("Permission", "SBC.default");
        List<String> alias = new ArrayList<>();
        alias.add("df");
        config.set("Alias", alias);
        List<String> lore = new ArrayList<>();
        lore.add("&aThis is a default command");
        config.set("Execute", lore);

        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);


        Configuration config2 = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        config2.set("NotPerms", "&cYou don't have permission to execute this command");
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config2, configFile);

    }
}
