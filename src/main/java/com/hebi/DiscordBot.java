package com.hebi;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

public class DiscordBot extends ListenerAdapter {

    private final String token;
    private final String channelId;

    private MessageChannel channel;

    public DiscordBot(String token, String channelId) {
        this.token = token;
        this.channelId = channelId;
    }

    // Initialize the bot and start listening for messages
    public void start() throws Exception {
        JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(this)
                .build()
                .awaitReady();
    }

    // Send a message to the specified channel
    public void sendMessage(String message) {
        if (channel != null) {
            channel.sendMessage(message).queue();
        } else {
            System.err.println("Channel not initialized");
        }
    }

    // Event handler for when a message is received in the channel
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Check if the message is from the monitored channel
        if (event.getChannel().getId().equals(channelId)) {
            String content = event.getMessage().getContentRaw();
            System.out.println("Message received: " + content);
        }
    }

    // Initialize the text channel once the bot is ready
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        channel = event.getJDA().getTextChannelById(channelId);
        if (channel != null) {
            System.out.println("Bot is ready and connected to the channel!");
        } else {
            System.err.println("Failed to connect to channel with ID: " + channelId);
        }
    }
}