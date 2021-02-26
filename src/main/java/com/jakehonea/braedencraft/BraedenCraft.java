package com.jakehonea.braedencraft;

import com.google.common.collect.Lists;
import com.jakehonea.braedencraft.feature.features.*;
import com.jakehonea.braedencraft.feature.Feature;
import com.jakehonea.braedencraft.feature.features.nickname.NameColor;
import com.jakehonea.braedencraft.feature.features.notes.Notes;
import com.jakehonea.braedencraft.feature.features.rainbow.RainbowName;
import com.jakehonea.braedencraft.storage.StorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public final class BraedenCraft extends JavaPlugin {

    public static final ChatColor PRIMARY = ChatColor.YELLOW;
    public static final ChatColor SECONDARY = ChatColor.WHITE;
    public static final ChatColor ERROR = ChatColor.RED;

    private static BraedenCraft instance;
    private List<Feature> features;
    private StorageHandler storageHandler;

    @Override
    public void onEnable() {

        instance      = this;
        this.features = Lists.newArrayList();

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        this.storageHandler = new StorageHandler();

        registerFeatures(new Locate(), new Notes(), new Compass(), new Teleport(), new DeathCounter(), new RainbowName(), new NameColor());

    }

    @Override
    public void onDisable() {

        features.forEach(this::unregisterFeature);

    }

    public StorageHandler getStorageHandler() {

        return storageHandler;

    }

    public <F extends Feature> F getFeature(Class<F> feature) {

        return (F) features.stream().filter(f -> feature.isInstance(f))
                .findFirst().orElse(null);

    }

    public void unregisterFeature(Feature feature) {

        feature.onClose();
        if (feature.getRunnable() != null)
            feature.getRunnable().cancel();

    }

    public void registerFeatures(Feature... features) {

        Arrays.stream(features).forEach(this::registerFeature);

    }

    public void registerFeature(Feature feature) {

        getLogger().log(Level.INFO, "BraedenCraft >> Loaded " + feature.getInformation().value() + " v" + feature.getInformation().version());
        Bukkit.getPluginManager().registerEvents(feature, this);
        features.add(feature);

    }

    public static BraedenCraft getInstance() {

        return instance;

    }

}
