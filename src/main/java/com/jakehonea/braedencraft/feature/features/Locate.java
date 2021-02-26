package com.jakehonea.braedencraft.feature.features;

import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Feature.Info(
        value = "Where",
        version = "1.0"
)
public class Locate extends Feature implements CommandExecutor, TabCompleter {

    private DecimalFormat format = new DecimalFormat("#,###.##");

    public Locate() {

        PluginCommand command = instance.getCommand("where");

        command.setExecutor(this);
        command.setTabCompleter(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {

            sender.sendMessage(BraedenCraft.ERROR + "Invalid args: /where <player>");
            return true;

        }

        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {

            player.sendMessage(BraedenCraft.ERROR + "Could not find player.");
            return true;

        }

        if (!player.equals(target)) {

            Location loc = target.getLocation();

            if(!loc.getWorld().equals(player.getWorld())){

                player.sendMessage(BraedenCraft.ERROR + "This feature only works when you are both in the same world.");
                return true;

            }

            player.sendMessage(
                    BraedenCraft.PRIMARY + target.getName() + " is at " + loc.getBlockX() + ", " + loc.getBlockY() + ", "
                            + loc.getBlockZ() + " (" + format.format(player.getLocation().distance(loc)) + " blocks away)."
            );

        } else
            player.sendMessage(BraedenCraft.PRIMARY + "Are you dumb?");

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                .filter(name -> !name.equals(sender.getName())).collect(Collectors.toList());

    }
}
