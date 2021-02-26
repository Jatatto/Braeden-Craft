package com.jakehonea.braedencraft.feature.features.rainbow;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Feature.Info(
        value = "Rainbow Name",
        version = "1.0"
)
public class RainbowName extends Feature implements CommandExecutor {

    private static final String KEY = "rainbow";

    private List<RainbowClock> clocks;
    private List<RainbowClock> remove;

    public RainbowName() {

        PluginCommand command = instance.getCommand("rainbow");

        command.setExecutor(this);

        this.clocks = Lists.newArrayList();
        this.remove = Lists.newArrayList();

        Bukkit.getOnlinePlayers().forEach(p -> update(p, getStatus(p)));

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {

                if (RainbowClock.GOING_UP && RainbowClock.INDEX == RainbowClock.COLORS.size() - 1)
                    RainbowClock.GOING_UP = false;

                if (!RainbowClock.GOING_UP && RainbowClock.INDEX == 0)
                    RainbowClock.GOING_UP = true;

                clocks.removeAll(remove);
                remove.clear();
                clocks.forEach(RainbowClock::run);

                RainbowClock.INDEX += (RainbowClock.GOING_UP ? 1 : -1);

            }

        };

        this.runnable.runTaskTimer(instance, 0, 1);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        boolean current = getStatus(player);

        current = !current;

        player.sendMessage(current ?
                BraedenCraft.PRIMARY + "You have enabled your gay status. :)" :
                BraedenCraft.SECONDARY + "You have disabled your gay status. :("
        );

        update(player, current);

        return true;

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        update(e.getPlayer(), getStatus(e.getPlayer()));

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {

        clocks.stream().filter(clock -> clock.getId().equals(e.getPlayer().getUniqueId())).forEach(clock -> remove.add(clock));

    }

    public boolean getStatus(Player player) {

        JsonElement found = instance.getStorageHandler().getStorage(player).get(KEY);

        return found != null && found.getAsBoolean();

    }

    public void update(Player player, boolean status) {

        instance.getStorageHandler().getStorage(player).put(KEY, new JsonPrimitive(status));
        if (!status) {

            clocks.stream().filter(clock -> clock.getId().equals(player.getUniqueId())).forEach(clock -> remove.add(clock));
            player.setPlayerListName(ChatColor.RED.toString() + player.getStatistic(Statistic.DEATHS) + ChatColor.RESET + " " + player.getName());

        } else
            clocks.add(new RainbowClock(player.getUniqueId(), player.getName()));

    }

}
