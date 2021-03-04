package com.jakehonea.braedencraft.feature.features;

import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

@Feature.Info(
        value = "Near",
        version = "1.0"
)
public class Near extends Feature implements CommandExecutor {

    public Near() {

        PluginCommand command = instance.getCommand("near");

        command.setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;

        int distance = 500;

        if (args.length > 0)
            try {
                distance = Integer.parseInt(args[0]);
            } catch (Exception ignored) {
            }

        Player player = (Player) sender;

        int finalDistance = distance;

        String nearby = player.getWorld().getEntitiesByClasses(Player.class)
                .stream()
                .filter(p -> !p.equals(player))
                .filter(p -> p.getLocation().distance(player.getLocation()) < finalDistance)
                .map(p -> String.format("%s (%dm), ", p.getName(), (int) p.getLocation().distance(player.getLocation())))
                .reduce("", (current, element) -> current + element);

        if (nearby.length() > 0)
            player.sendMessage(BraedenCraft.PRIMARY + "Nearby players: " + nearby.substring(0, nearby.length() - 2));
        else
            player.sendMessage(BraedenCraft.ERROR + "There are no players within " + finalDistance + " blocks;");

        return true;

    }


}
