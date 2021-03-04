package com.jakehonea.braedencraft.feature.features.claims;

import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.claims.ClaimManager;
import com.jakehonea.braedencraft.feature.Feature;
import com.jakehonea.braedencraft.highlight.Highlight;
import com.jakehonea.braedencraft.utils.Pair;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.concurrent.TimeUnit;

@Feature.Info(
        value = "Claims",
        version = "1.0"
)
public class Claims extends Feature implements CommandExecutor {

    private ClaimManager claimManager;

    public Claims() {

        PluginCommand command = instance.getCommand("claims");

        command.setExecutor(this);

        this.claimManager = new ClaimManager(instance);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        Pair<Location, Location> selection = claimManager.getSelection(player.getUniqueId());

        if (args[0].equalsIgnoreCase("view")) {

            if (selection == null || selection.getLeft() == null || selection.getRight() == null || !selection.getLeft().getWorld().equals(selection.getRight().getWorld())) {

                player.sendMessage(BraedenCraft.ERROR + "Something wrong with claim. Try selecting the area again.");
                return true;

            }

            Highlight highlight = new Highlight(player.getUniqueId(), player.getWorld(), BoundingBox.of(selection.getLeft(), selection.getRight()));

            long time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15);

            highlight.addConditional(p -> System.currentTimeMillis() > time);

            return true;

        }

        return true;

    }

}
