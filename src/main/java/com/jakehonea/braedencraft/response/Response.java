package com.jakehonea.braedencraft.response;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.function.Predicate;

public abstract class Response<T> {

    protected Listener listener;

    public void unregister() {

        HandlerList.unregisterAll(listener);

    }

    public abstract void register(Predicate<Player> predicate);

    public abstract void onResponse(T t);

}
