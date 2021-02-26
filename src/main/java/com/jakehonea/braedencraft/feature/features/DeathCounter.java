package com.jakehonea.braedencraft.feature.features;

import com.jakehonea.braedencraft.feature.Feature;
import com.jakehonea.braedencraft.feature.features.rainbow.RainbowName;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

@Feature.Info(
        value = "Death Counter",
        version = "1.0"
)
public class DeathCounter extends Feature {

    public DeathCounter() {

        Bukkit.getOnlinePlayers().stream().forEach(player ->
                player.setPlayerListName(ChatColor.RED.toString() + player.getStatistic(Statistic.DEATHS) + ChatColor.RESET + " " + player.getName()));

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if (!instance.getFeature(RainbowName.class).getStatus(e.getEntity()))
                    e.getEntity().setPlayerListName(ChatColor.RED.toString() + e.getEntity().getStatistic(Statistic.DEATHS) + ChatColor.RESET + " " + e.getEntity().getName());


            }

        }.runTaskLater(instance, 1);


    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if (!instance.getFeature(RainbowName.class).getStatus(e.getPlayer()))
                    e.getPlayer().setPlayerListName(ChatColor.RED.toString() + e.getPlayer().getStatistic(Statistic.DEATHS) + ChatColor.RESET + " " + e.getPlayer().getName());

            }

        }.runTaskLater(instance, 1);

    }

}
