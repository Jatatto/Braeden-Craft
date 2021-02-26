package com.jakehonea.braedencraft.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class QuickItem {

    private Material material;
    private String name;
    private List<String> lore;

    public QuickItem(Material material) {

        this.material = material;

    }

    public QuickItem setName(String name) {

        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return this;

    }

    public QuickItem setLore(List<String> lore) {

        this.lore = lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
        return this;

    }

    public ItemStack build() {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        if (lore != null)
            meta.setLore(lore);

        item.setItemMeta(meta);

        return item;

    }

}
