package com.jakehonea.braedencraft.ui;

import com.jakehonea.braedencraft.BraedenCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Predicate;

public abstract class UI {

    protected Listener listener;
    protected Inventory inventory;
    protected BraedenCraft instance = BraedenCraft.getInstance();

    public Inventory getInventory() {

        return inventory;

    }

    public void register(Predicate<Player> predicate) {

        this.listener = new Listener() {

            @EventHandler
            public void onInventoryInteract(InventoryClickEvent e) {

                if (e.getWhoClicked() instanceof Player && e.getInventory().equals(inventory) && predicate.test((Player) e.getWhoClicked()))
                    onClick(e);

            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent e) {

                if (e.getPlayer() instanceof Player && e.getInventory().equals(inventory) && predicate.test((Player) e.getPlayer())) {

                    onClose(e);
                    HandlerList.unregisterAll(listener);

                }

            }

        };

        Bukkit.getPluginManager().registerEvents(listener, instance);

    }

    public void onClick(InventoryClickEvent e) {
    }

    public void onClose(InventoryCloseEvent e) {
    }

    public abstract void setupItems(Player player);

}
