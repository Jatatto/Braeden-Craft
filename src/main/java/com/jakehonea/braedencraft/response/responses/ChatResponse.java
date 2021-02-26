package com.jakehonea.braedencraft.response.responses;

import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.response.Response;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Predicate;

public abstract class ChatResponse extends Response<AsyncPlayerChatEvent> {

    @Override
    public void register(Predicate<Player> predicate) {

        this.listener = new Listener() {

            @EventHandler
            public void onQuit(PlayerQuitEvent e) {

                unregister();

            }

            @EventHandler
            public void onChat(AsyncPlayerChatEvent e) {

                if (predicate.test(e.getPlayer()))
                    onResponse(e);

            }

        };

        Bukkit.getPluginManager().registerEvents(listener, BraedenCraft.getInstance());

    }

}
