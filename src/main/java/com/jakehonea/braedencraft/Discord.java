package com.jakehonea.braedencraft;

import com.jakehonea.braedencraft.utils.Util;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;

public class Discord implements Listener {

    private final DiscordClient client;
    private final GatewayDiscordClient gateway;
    private final String channelName;

    public Discord(BraedenCraft craft) {

        System.out.println(craft.getConfig().getString("discord.token"));

        this.client      = DiscordClient.create(craft.getConfig().getString("discord.token"));
        this.channelName = craft.getConfig().getString("discord.channel");
        this.gateway     = client.login().block();

        if (gateway != null) {

            gateway.getGuilds().toStream().forEach(g -> {

                if (g.getChannels().toStream().noneMatch(guildChannel -> guildChannel instanceof MessageChannel && guildChannel.getName().equalsIgnoreCase(channelName)))
                    g.createTextChannel(textChannelCreateSpec -> textChannelCreateSpec.setName(channelName)).block();

            });

            gateway.on(MessageCreateEvent.class).subscribe(event -> new BukkitRunnable() {

                @Override
                public void run() {

                    try {
                        if (!event.getMember().get().isBot())
                            if (event.getGuild().block().getChannelById(event.getMessage().getChannelId()).block().getName().equals(channelName)) {

                                Bukkit.broadcastMessage(BraedenCraft.PRIMARY + "[DISCORD] " + BraedenCraft.SECONDARY
                                        + event.getMember().get().getDisplayName() + ": "
                                        + event.getMessage().getContent());
                            }
                    } catch (Exception ignored) {
                    }

                }

            }.runTaskLater(craft, 0));

            Bukkit.getPluginManager().registerEvents(new Listener() {

                @EventHandler
                public void onChat(AsyncPlayerChatEvent e) {

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            gateway.getGuilds().toStream().forEach(g -> g.getChannels().toStream()
                                    .filter(channel -> channel instanceof MessageChannel && channel.getName().equals(channelName))
                                    .forEach(channel -> ((MessageChannel) channel).createMessage(e.getPlayer().getName() + ": " + e.getMessage()).block()));

                        }

                    }.runTaskLaterAsynchronously(craft, 0);


                }

            }, craft);

        }

    }


}
