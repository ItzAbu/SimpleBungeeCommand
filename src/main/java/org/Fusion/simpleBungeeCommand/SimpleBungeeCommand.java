package org.Fusion.simpleBungeeCommand;

import net.md_5.bungee.api.plugin.Plugin;
import org.Fusion.simpleBungeeCommand.commands.CommandGestor;
import org.Fusion.simpleBungeeCommand.customConfig.CustomConfig;
import org.Fusion.simpleBungeeCommand.utils.GestorUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public final class SimpleBungeeCommand extends Plugin {

    private final File commandFolder = new File("plugins/SBC/commands");

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println("[SBC] SimpleBungeeCommand starting...");

        try {
            CustomConfig.setup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CommandEnable();

        startFileWatcher();

        System.out.println("[SBC} SimpleBungeeCommand started successfully!");


    }


    public void CommandEnable() {
        List<String> commandStructs;
        try {
            commandStructs = GestorUtils.getCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(String commandStruct : commandStructs) {
            getProxy().getPluginManager().registerCommand(this, new CommandGestor(commandStruct));
            System.out.println("[SBC] SimpleBungeeCommand started " + commandStruct);
        }
    }

    private void startFileWatcher() {
        new Thread(() -> {
            try {

                if (!commandFolder.exists()) {
                    boolean created = commandFolder.mkdir();
                    if (!created) {
                        System.out.println("Errore: impossibile creare la cartella dei comandi.");
                        return;
                    }
                }

                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = commandFolder.toPath();
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path filename = ev.context();
                        File file = new File(commandFolder, filename.toString());

                        if (file.getName().endsWith(".yml")) {
                            String commandName = filename.toString().replace(".yml", "");
                            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {

                                getProxy().getPluginManager().registerCommand(this, new CommandGestor(commandName));
                                System.out.println("Command " + commandName + " registered successfully!");
                            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {

                                getProxy().getPluginManager().unregisterCommand(new CommandGestor(commandName));
                                System.out.println("Command " + commandName + " unregistered successfully!");
                            }
                        }
                    }
                    key.reset();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
