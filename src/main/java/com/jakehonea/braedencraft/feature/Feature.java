package com.jakehonea.braedencraft.feature;

import com.jakehonea.braedencraft.BraedenCraft;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Feature implements Listener {

    private Info information;
    protected BraedenCraft instance = BraedenCraft.getInstance();
    protected BukkitRunnable runnable;

    public Feature() {

        information = getClass().getAnnotation(Info.class);

/*        if (information.needsStorage()) {

            this.file = new File(BraedenCraft.getInstance().getDataFolder(), information.value() + ".yml");

            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            this.config = YamlConfiguration.loadConfiguration(file);

        }*/

    }

    public void onClose(){}

    public Info getInformation() {

        return information;

    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {

        String value();

        String version();

    }

}
