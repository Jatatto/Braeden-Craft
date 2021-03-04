package com.jakehonea.braedencraft.storage;

import com.google.common.collect.Lists;
import com.jakehonea.braedencraft.BraedenCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class StorageHandler implements Listener {

    private List<PlayerStorage> cached;

    public StorageHandler() {

        this.cached = Lists.newArrayList();
        Arrays.stream(BraedenCraft.getInstance().getDataFolder().listFiles())
                .filter(file -> file.getName().endsWith(".data"))
                .forEach(file -> cached.add(new PlayerStorage(file)));

        Bukkit.getOnlinePlayers().forEach(this::loadPlayer);

        Bukkit.getPluginManager().registerEvents(this, BraedenCraft.getInstance());

    }

    public PlayerStorage getStorage(Player player) {

        return cached.stream().filter(storage -> storage.getPlayer().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);

    }

    public PlayerStorage createStorage(Player player) {

        PlayerStorage playerStorage = new PlayerStorage(new File(BraedenCraft.getInstance().getDataFolder(), player.getUniqueId().toString() + ".data"));
        playerStorage.setPlayer(player.getUniqueId());

        cached.add(playerStorage);
        return playerStorage;

    }

    public void loadPlayer(Player player) {

        if (getStorage(player) == null)
            createStorage(player);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        loadPlayer(e.getPlayer());

    }

}
