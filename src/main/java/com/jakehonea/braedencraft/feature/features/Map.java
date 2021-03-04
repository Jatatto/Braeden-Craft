package com.jakehonea.braedencraft.feature.features;

import com.jakehonea.braedencraft.feature.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

@Feature.Info(
        value = "Map",
        version = "1.0"
)
public class Map extends Feature implements CommandExecutor {

    public Map() {

        PluginCommand command = instance.getCommand("map");

        command.setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1)
            return false;

        ItemStack item = new ItemStack(Material.FILLED_MAP);

        MapMeta meta = (MapMeta) item.getItemMeta();

        MapView view = Bukkit.createMap(((Player) sender).getWorld());

        view.getRenderers().clear();

        view.setScale(MapView.Scale.FARTHEST);

        meta.setMapView(view);
        item.setItemMeta(meta);

        ((Player) sender).getInventory().addItem(item);

        new BukkitRunnable() {

            @Override
            public void run() {

                BufferedImage image = null;
                try {
                    image = ImageIO.read(new URL(args[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image scaled = image.getScaledInstance(128, 128, Image.SCALE_SMOOTH);

                view.addRenderer(new MapRenderer() {
                    @Override
                    public void render(MapView map, MapCanvas canvas, Player player) {

                        canvas.drawImage(0, 0, scaled);

                    }

                });

            }
        }.runTaskLaterAsynchronously(instance, 0);

        return true;

    }


}
