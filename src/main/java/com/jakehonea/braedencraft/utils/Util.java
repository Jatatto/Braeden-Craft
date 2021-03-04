package com.jakehonea.braedencraft.utils;

import discord4j.rest.util.Color;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Util {

    public static Color chatColorToRGB(String string) {

        ChatColor color = ChatColor.WHITE;

        if (string.length() == 2)
            color = ChatColor.getByChar(string.charAt(1));

        switch (color) {
            case BLACK:
                return Color.of(0, 0, 0);
            case DARK_BLUE:
                return Color.of(0, 0, 170);
            case DARK_GREEN:
                return Color.of(0, 170, 0);
            case DARK_AQUA:
                return Color.of(0, 170, 170);
            case DARK_RED:
                return Color.of(170, 0, 0);
            case DARK_PURPLE:
                return Color.of(170, 0, 170);
            case GOLD:
                return Color.of(255, 170, 0);
            case GRAY:
                return Color.of(170, 170, 170);
            case DARK_GRAY:
                return Color.of(85, 85, 85);
            case BLUE:
                return Color.of(85, 85, 255);
            case GREEN:
                return Color.of(85, 255, 85);
            case AQUA:
                return Color.of(85, 255, 255);
            case RED:
                return Color.of(255, 85, 85);
            case LIGHT_PURPLE:
                return Color.of(255, 85, 255);
            case YELLOW:
                return Color.of(255, 255, 85);
            case WHITE:
            default:
                return Color.of(255, 255, 255);
        }

    }

    public static Block fromString(String location) {

        String[] parts = location.split(":");

        return Bukkit.getWorld(parts[0]).getBlockAt(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));

    }

    public static String toString(Block block) {

        return block.getWorld() + ":" + block.getLocation().getBlockX() + ":" + block.getLocation().getBlockY() + ":" + block.getLocation().getBlockZ();

    }

    public static void sendActionBar(Player player, String text) {

        sendPacket(player, new PacketPlayOutChat(new ChatComponentText(text), ChatMessageType.GAME_INFO, player.getUniqueId()));

    }

    public static void sendPacket(Player player, Packet packet) {

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

    }

    public static void sendPackets(Player player, Packet... packets) {

        for (Packet packet : packets)
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

    }

    public static String getTime(long time) {

        time -= System.currentTimeMillis();

        long hours = TimeUnit.MILLISECONDS.toHours(time);
        time -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        int seconds = (int) (time / 1000);

        return (hours > 0 ? hours + "h " : "") +
                (minutes > 0 ? minutes + "m " : "") +
                (seconds > 0 ? seconds + "s" : "s");

    }

}
