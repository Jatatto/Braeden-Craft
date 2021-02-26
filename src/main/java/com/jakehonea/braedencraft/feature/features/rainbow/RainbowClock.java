package com.jakehonea.braedencraft.feature.features.rainbow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RainbowClock {

    public static int INDEX = 0;
    public static boolean GOING_UP;

    public static List<ChatColor> COLORS = Arrays.asList(ChatColor.DARK_RED, ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW,
            ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.BLUE, ChatColor.DARK_BLUE, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE);

    private UUID id;
    private String string;

    public RainbowClock(UUID id, String name) {

        this.id     = id;
        this.string = name;

    }

    public void run() {

        String name = "";

        int index = INDEX;

        boolean up = GOING_UP;

        for (char character : ChatColor.stripColor(string).toCharArray()) {

            if (character == ' ')
                name += " ";
            else {

                if (up && index == COLORS.size() - 1)
                    up = false;

                if (!up && index == 0)
                    up = true;

                name += COLORS.get(index) + "" + character;

                index += (up ? 1 : -1);

            }

        }

        name += ChatColor.RESET;

        if (Bukkit.getPlayer(id) != null) {

            Player player = Bukkit.getPlayer(id);

            player.setPlayerListName(ChatColor.RED.toString() + player.getStatistic(Statistic.DEATHS) + ChatColor.RESET + " " + name);

        }

    }

    public UUID getId() {
        return id;
    }
}


