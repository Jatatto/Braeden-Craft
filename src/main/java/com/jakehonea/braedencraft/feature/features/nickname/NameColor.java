package com.jakehonea.braedencraft.feature.features.nickname;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@Feature.Info(
        value = "Nick Color",
        version = "1.0"
)
public class NameColor extends Feature implements CommandExecutor {

    public static final String KEY = "nick-name-color";

    public NameColor() {

        PluginCommand command = instance.getCommand("nick");
        command.setExecutor(this);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        e.getPlayer().setDisplayName(getColor(e.getPlayer()) + e.getPlayer().getName() + ChatColor.RESET);

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        String name = e.getPlayer().getCustomName() != null ? e.getPlayer().getCustomName() : e.getPlayer().getName();

        e.setFormat(name + BraedenCraft.SECONDARY + ": " + ChatColor.WHITE + e.getMessage());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player)
            new ColorUI((Player) sender);

        return true;

    }

    public void setColor(Player player, String color) {

        instance.getStorageHandler().getStorage(player).put(KEY, new JsonPrimitive(color));

    }

    public String getColor(Player player) {

        JsonElement found = instance.getStorageHandler().getStorage(player).get(KEY);

        return found != null ? found.getAsString() : ChatColor.WHITE.toString();

    }

}
