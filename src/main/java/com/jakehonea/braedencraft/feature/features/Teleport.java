package com.jakehonea.braedencraft.feature.features;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.utils.Util;
import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Feature.Info(
        value = "Teleport",
        version = "1.0"
)
public class Teleport extends Feature implements CommandExecutor, TabCompleter {

    public static final long COOLDOWN = TimeUnit.DAYS.toMillis(1);
    public static final String KEY = "tp-cooldown";

    public Teleport() {

        PluginCommand command = instance.getCommand("teleport");

        command.setExecutor(this);
        command.setTabCompleter(this);

    }

    public boolean hasCooldown(Player player) {

        return getCooldown(player) > System.currentTimeMillis();

    }

    public long getCooldown(Player player) {

        JsonElement found = instance.getStorageHandler().getStorage(player).get(KEY);

        return found != null ? found.getAsLong() : 0;

    }

    public void putCooldown(Player player, long cooldown) {

        instance.getStorageHandler().getStorage(player).put(KEY, new JsonPrimitive(cooldown));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;

        if (hasCooldown(player)) {

            player.sendMessage(BraedenCraft.ERROR + "Please wait " + Util.getTime(getCooldown(player)) + " until using teleport again!");
            return true;

        }

        if (args.length < 1) {

            player.sendMessage(BraedenCraft.ERROR + "Invalid args: /tp <player>");
            return true;

        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null)
            player.sendMessage(BraedenCraft.ERROR + "This player is not online.");
        else {

            if (target.equals(player)) {

                player.sendMessage(BraedenCraft.ERROR + "You can't teleport to yourself silly.");
                return true;

            }

            putCooldown(player, System.currentTimeMillis() + COOLDOWN);
            player.teleport(target.getLocation());
            player.sendMessage(BraedenCraft.PRIMARY + "You teleported to " + target.getName() + ".");
            target.sendMessage(BraedenCraft.PRIMARY + player.getName() + " has teleported to you.");

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.getName().equalsIgnoreCase(sender.getName()))
                .map(HumanEntity::getName)
                .collect(Collectors.toList());

    }

}
