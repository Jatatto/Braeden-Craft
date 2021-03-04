package com.jakehonea.braedencraft.claims;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonPrimitive;
import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClaimManager {

    private final File directory;
    private final List<Claim> claims;
    private final Map<UUID, Pair<Location, Location>> claimWands;

    public ClaimManager(BraedenCraft instance) {

        this.directory  = new File(instance.getDataFolder(), "claims");
        this.claims     = Lists.newArrayList();
        this.claimWands = Maps.newHashMap();

        if (!directory.exists())
            directory.mkdir();

        Arrays.stream(directory.listFiles()).forEach(file -> claims.add(new Claim(file)));

        Bukkit.getPluginManager().registerEvents(new ClaimWandListener(this), instance);

    }

    public Map<UUID, Pair<Location, Location>> getClaimWands() {

        return claimWands;

    }

    public Pair<Location, Location> getSelection(UUID id) {

        return claimWands.get(id);

    }

    public void createClaim(Player creator) {

        File claimFile = new File(directory, UUID.randomUUID() + ".txt");

        Claim claim = new Claim(claimFile);
        claim.set(Claim.OWNER, new JsonPrimitive(creator.getUniqueId().toString()));

        claims.add(claim);

    }

    public void deleteClaim(Claim claim) {

        claim.getFile().delete();
        claims.remove(claim);

    }

    public boolean isInClaim(Location location) {

        return getClaim(location) != null;

    }

    public Claim getClaim(Location location) {

        return claims.stream().filter(claim -> claim.get(Claim.WORLD).getAsString().equals(location.getWorld().getName()))
                .filter(claim -> claim.getBounds().contains(location.getX(), location.getY(), location.getZ()))
                .findFirst().orElse(null);

    }

}
