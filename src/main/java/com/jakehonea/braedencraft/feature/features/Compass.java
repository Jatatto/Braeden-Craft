package com.jakehonea.braedencraft.feature.features;

import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.Util;
import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

@Feature.Info(
        value = "Compass",
        version = "1.0"
)
public class Compass extends Feature {

    public static final String FORMAT = ChatColor.GOLD + "XYZ: " + ChatColor.WHITE + " %d, %d, %d " + ChatColor.GOLD + "%s";

    public Compass() {

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {

                Bukkit.getOnlinePlayers().forEach(player -> update(player));

            }
        };

        this.runnable.runTaskTimer(BraedenCraft.getInstance(), 0, 10);

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        update(e.getPlayer());

    }

    public void update(Player player) {

        Util.sendActionBar(
                player,
                String.format(
                        FORMAT,
                        player.getLocation().getBlockX(),
                        player.getLocation().getBlockY(),
                        player.getLocation().getBlockZ(),
                        getFacing(player)
                )
        );

    }

    private String getFacing(Player player) {

        float yaw = Math.abs(player.getEyeLocation().getYaw());

        if (yaw <= 22.5f || yaw >= 360 - 22.5f)
            return "S";
        else if (yaw <= 67.5f && yaw >= 22.5f)
            return "SE";
        else if (yaw <= 112.5f && yaw >= 67.5f)
            return "E";
        else if (yaw <= 157.5f && yaw >= 112.5f)
            return "NE";
        else if (yaw <= 202.5f && yaw >= 157.5f)
            return "N";
        else if (yaw <= 247.5 && yaw >= 202.5f)
            return "NW";
        else if (yaw <= 292.5 && yaw >= 247.5)
            return "W";
        return "SW";

    }

}
