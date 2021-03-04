package com.jakehonea.braedencraft.claims;

import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.utils.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClaimWandListener implements Listener {

    private final ClaimManager manager;

    public ClaimWandListener(ClaimManager manager) {

        this.manager = manager;

    }

    @EventHandler
    public void onWandInteract(PlayerInteractEvent e) {

        if (e.getItem() != null && e.getItem().getType() == Material.STICK && e.getItem().hasItemMeta() &&
                e.getItem().getItemMeta().hasDisplayName() &&
                e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Claim Wand")) {

            Pair<Location, Location> bounds = manager.getClaimWands().getOrDefault(e.getPlayer().getUniqueId(), new Pair(null, null));

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                bounds.setLeft(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(BraedenCraft.PRIMARY + "Set location one to the clicked block.");
            } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                bounds.setRight(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(BraedenCraft.PRIMARY + "Set location two to the clicked block.");
            }

        }

    }

}
