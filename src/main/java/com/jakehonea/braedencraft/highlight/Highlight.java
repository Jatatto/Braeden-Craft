package com.jakehonea.braedencraft.highlight;

import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class Highlight {

    private UUID id;
    private List<Location> locations;

    private Predicate<Player> predicate;
    private boolean highlighting = true;

    public Highlight(UUID id, World world, BoundingBox box) {

        this.id  = id;

        Location min = new Location(world, box.getMinX(), box.getMinY(), box.getMinZ());

        this.locations = Lists.newArrayList();

        for (double y = 0; y <= box.getHeight(); y += 0.5) {

            locations.add(min.clone().add(0, y, 0));
            locations.add(min.clone().add(0, y, box.getWidthZ()));
            locations.add(min.clone().add(box.getWidthX(), y, box.getWidthZ()));
            locations.add(min.clone().add(box.getWidthX(), y, 0));

        }

        for (double x = 0; x <= box.getWidthX(); x += 0.5) {

            locations.add(min.clone().add(x, 0, 0));
            locations.add(min.clone().add(x, box.getHeight(), 0));
            locations.add(min.clone().add(x, 0, box.getWidthZ()));
            locations.add(min.clone().add(x, box.getHeight(), box.getWidthZ()));

        }

        for (double z = 0; z <= box.getWidthZ(); z += 0.5) {

            locations.add(min.clone().add(0, 0, z));
            locations.add(min.clone().add(box.getWidthX(), 0, z));
            locations.add(min.clone().add(0, box.getHeight(), z));
            locations.add(min.clone().add(box.getWidthX(), box.getHeight(), z));

        }

    }

    public boolean display() {

        Player player = Bukkit.getPlayer(id);

        if (player == null || !highlighting)
            return false;

        locations.forEach(loc -> player.spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 2)));

        return highlighting;

    }

    public void stop() {

        this.highlighting = false;

    }

    public void addConditional(Predicate<Player> predicate){

        this.predicate = predicate;

    }

    public Predicate<Player> getPredicate() {

        return predicate;

    }

    public UUID getId() {

        return id;

    }

}
