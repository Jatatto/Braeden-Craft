package com.jakehonea.braedencraft;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Util {

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
