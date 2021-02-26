package com.jakehonea.braedencraft.feature.features.nickname;

import com.google.common.collect.Maps;
import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.ui.UI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ColorUI extends UI {

    public static final Object[][] COLOR_PAIRINGS = {
            {ChatColor.DARK_RED, Material.RED_CONCRETE, 4}, {ChatColor.RED, Material.RED_CONCRETE_POWDER, 5}, {ChatColor.YELLOW, Material.YELLOW_CONCRETE, 6},
            {ChatColor.GOLD, Material.GOLD_BLOCK, 13}, {ChatColor.GREEN, Material.LIME_CONCRETE, 14}, {ChatColor.DARK_GREEN, Material.GREEN_CONCRETE, 15},
            {ChatColor.AQUA, Material.LIGHT_BLUE_CONCRETE, 22}, {ChatColor.DARK_AQUA, Material.CYAN_CONCRETE, 23}, {ChatColor.BLUE, Material.BLUE_CONCRETE_POWDER, 24},
            {ChatColor.DARK_BLUE, Material.BLUE_CONCRETE, 31}, {ChatColor.LIGHT_PURPLE, Material.MAGENTA_CONCRETE, 32}, {ChatColor.DARK_PURPLE, Material.PURPLE_CONCRETE, 33},
            {ChatColor.GRAY, Material.LIGHT_GRAY_CONCRETE, 40}, {ChatColor.DARK_GRAY, Material.GRAY_CONCRETE, 41}, {ChatColor.BLACK, Material.BLACK_CONCRETE, 42},
            {ChatColor.WHITE, Material.WHITE_CONCRETE, 50}
    };

    private Map<ItemStack, ChatColor> itemMap;

    public ColorUI(Player player) {

        this.inventory = Bukkit.createInventory(null, 54, "Pick a color");

        register(player::equals);

        setupItems(player);

        player.openInventory(getInventory());

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        if (e.getCurrentItem() != null && itemMap.containsKey(e.getCurrentItem())) {

            Player player = (Player) e.getWhoClicked();

            ChatColor color = itemMap.get(e.getCurrentItem());

            String name = color + player.getName() + ChatColor.RESET;

            player.setDisplayName(name);
            player.setCustomName(name);

            player.sendMessage(BraedenCraft.SECONDARY + "You have changed your name to " + name + BraedenCraft.SECONDARY + ".");

            instance.getFeature(NameColor.class).setColor(player, color.toString());

        }

    }

    @Override
    public void setupItems(Player player) {

        this.inventory.clear();
        this.itemMap = Maps.newHashMap();

        for (Object[] pair : COLOR_PAIRINGS) {

            ItemStack item = new ItemStack((Material) pair[1]);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(pair[0] + player.getName());

            item.setItemMeta(meta);

            inventory.setItem((Integer) pair[2] - 1, item);
            itemMap.put(item, (ChatColor) pair[0]);

        }

    }

}
