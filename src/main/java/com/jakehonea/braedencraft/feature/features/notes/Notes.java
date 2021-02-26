package com.jakehonea.braedencraft.feature.features.notes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

@Feature.Info(
        value = "Notes",
        version = "1.0"
)
public class Notes extends Feature implements CommandExecutor {

    public static final String KEY = "notes";

    public Notes() {

        PluginCommand command = instance.getCommand("notes");

        command.setExecutor(this);

    }

    public List<String> getNotes(Player player) {

        JsonElement found = instance.getStorageHandler().getStorage(player).get(KEY);

        List<String> notes = Lists.newArrayList();

        if (found == null)
            return notes;

        found.getAsJsonArray().forEach(element -> notes.add(element.getAsString()));

        return notes;

    }

    public void addNote(Player player, String note) {

        List<String> notes = getNotes(player);

        notes.add(note);

        JsonArray array = new JsonArray();
        notes.forEach(array::add);

        instance.getStorageHandler().getStorage(player).put(KEY, array);

    }

    public void removeNote(Player player, String note) {

        List<String> notes = getNotes(player);

        JsonArray array = new JsonArray();
        notes.stream().filter(n -> !n.equals(note))
                .forEach(array::add);

        instance.getStorageHandler().getStorage(player).put(KEY, array);

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Location loc = e.getEntity().getLocation();

        addNote(e.getEntity(), String.format("You died at %d, %d, %d.", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        e.getEntity().sendMessage(BraedenCraft.PRIMARY + "Your death location has been written in your notebook. (/notes)");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;

        new NoteUI((Player) sender, getNotes((Player) sender));

        return true;

    }

}
