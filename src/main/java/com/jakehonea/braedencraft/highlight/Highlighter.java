package com.jakehonea.braedencraft.highlight;

import com.google.common.collect.Lists;
import com.jakehonea.braedencraft.BraedenCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Highlighter {

    private List<Highlight> highlights;
    private List<Highlight> toAdd;

    public Highlighter(BraedenCraft instance) {

        this.highlights = Lists.newArrayList();
        this.toAdd      = Lists.newArrayList();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {

            highlights.addAll(toAdd);
            toAdd.clear();

            highlights.removeIf(highlight -> !highlight.display() || (highlight.getPredicate() != null && highlight.getPredicate().test(Bukkit.getPlayer(highlight.getId()))));

        }, 0, 12);


    }

    public List<Highlight> getHighlights() {

        List<Highlight> list = Lists.newArrayList();

        list.addAll(highlights);
        list.addAll(toAdd);

        return highlights;

    }

    public void putHighlight(Highlight highlight) {

        this.toAdd.add(highlight);

    }

    public boolean hasHighlight(Player player) {

        return getHighlights().stream()
                .anyMatch(highlight -> highlight.getId().equals(player.getUniqueId()));

    }

    public Highlight getHighlight(Player player) {

        return getHighlights().stream()
                .filter(highlight -> highlight.getId().equals(player.getUniqueId()))
                .findFirst().orElse(null);

    }

}
