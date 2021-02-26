package com.jakehonea.braedencraft.feature.features.notes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jakehonea.braedencraft.BraedenCraft;
import com.jakehonea.braedencraft.response.responses.ChatResponse;
import com.jakehonea.braedencraft.ui.UI;
import com.jakehonea.braedencraft.utils.QuickItem;
import com.jakehonea.braedencraft.utils.Scroller;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

public class NoteUI extends UI {

    private static final List<Integer> PANE_SLOTS = Lists.newArrayList();
    private static final ItemStack PANE = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    static {

        for (int i = 0; i < 10; i++)
             PANE_SLOTS.add(i);

        PANE_SLOTS.add(17);
        PANE_SLOTS.add(18);

        for (int i = 26; i < 36; i++)
             PANE_SLOTS.add(i);

        ItemMeta meta = PANE.getItemMeta();
        meta.setDisplayName(ChatColor.RESET.toString());
        PANE.setItemMeta(meta);

    }

    private List<String> notes;
    private Scroller<String> scroller;

    private Map<Integer, String> notesOnPage;

    public NoteUI(Player player, List<String> notes) {

        this.notes     = notes;
        this.inventory = Bukkit.createInventory(null, 9 * 4, "Notes");

        this.scroller = new Scroller<>(notes, 14);

        register(p -> p.equals(player));

        setupItems(player);

        player.openInventory(inventory);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        if (e.getSlot() == 29) {

            scroller.back();
            setupItems(player);

        } else if (e.getSlot() == 33) {

            scroller.next();
            setupItems(player);

        } else if (e.getSlot() == 31) {

            e.getWhoClicked().closeInventory();

            e.getWhoClicked().sendMessage(BraedenCraft.PRIMARY + "Please type what you'd like to add to your notebook:");

            new ChatResponse() {
                @Override
                public void onResponse(AsyncPlayerChatEvent e) {

                    e.setCancelled(true);
                    BraedenCraft.getInstance().getFeature(Notes.class).addNote(player, e.getMessage());
                    unregister();

                    e.getPlayer().sendMessage(BraedenCraft.PRIMARY + "You have added that to your notebook!");

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            new NoteUI(player, BraedenCraft.getInstance().getFeature(Notes.class).getNotes(player));

                        }

                    }.runTaskLater(BraedenCraft.getInstance(), 0);

                }

            }.register(p -> p.equals(player));

        } else if (e.isShiftClick() && e.isRightClick() && notesOnPage.containsKey(e.getSlot())) {

            String note = notesOnPage.get(e.getSlot());

            BraedenCraft.getInstance().getFeature(Notes.class).removeNote(player, note);

            this.notes    = BraedenCraft.getInstance().getFeature(Notes.class).getNotes(player);
            this.scroller = new Scroller<>(notes, 14);

            setupItems(player);

        }

    }

    @Override
    public void setupItems(Player player) {

        inventory.clear();
        this.notesOnPage = Maps.newHashMap();

        PANE_SLOTS.stream().forEach(slot -> inventory.setItem(slot, PANE));

        inventory.setItem(29, new QuickItem(Material.ARROW).setName("&cBack Page").build());
        inventory.setItem(33, new QuickItem(Material.ARROW).setName("&aNext Page").build());

        inventory.setItem(31, new QuickItem(Material.ANVIL).setName("&aClick to add a note").build());

        List<String> notes = scroller.getItemsOnPage();

        System.out.println(notes.size());

        for (int i = 0; i < notes.size(); i++) {

            int slot = 10 + i + (i > 6 ? 2 : 0);
            String note = notes.get(i);
            List<String> info = compress(note);

            info.add(" ");
            info.add("&cShift + Right Click to");
            info.add("&cdelete this note");

            inventory.setItem(
                    slot,
                    new QuickItem(Material.PAPER)
                            .setName(info.get(0))
                            .setLore(info.subList(1, info.size()))
                            .build()
            );

            notesOnPage.put(slot, note);

        }

    }

    private List<String> compress(String note) {

        List<String> compressed = Lists.newArrayList();

        if (note.length() < 25) {
            compressed.add(note);
            return compressed;
        }

        String[] words = note.split(" ");

        int currentLength = 0;
        StringBuilder builder = new StringBuilder(ChatColor.WHITE.toString());

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            builder.append(words[i] + " ");

            currentLength += word.length();

            if (currentLength > 25) {

                compressed.add(builder.toString());
                builder       = new StringBuilder(ChatColor.WHITE.toString());
                currentLength = 0;

            } else if (i == words.length - 1)
                compressed.add(builder.toString());

        }

        return compressed;

    }

}
