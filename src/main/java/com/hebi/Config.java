package com.hebi;

import lombok.Getter;


/**
 * Config class that checks for environment variables on startup, if none are found, provide default values. Errors may
 * need to be logged.
 */
@Getter
public final class Config {

    private final String discordBotToken;
    private final String discordChannelId;
    private final String zomboidServerServiceName;

    public Config() {
        this.discordBotToken = System.getenv("DISCORD_BOT_TOKEN") != null
                ? System.getenv("DISCORD_BOT_TOKEN")
                : "your_discord_bot_token";

        this.discordChannelId = System.getenv("DISCORD_CHANNEL_ID") != null
                ? System.getenv("DISCORD_CHANNEL_ID")
                : "your_discord_channel_id";

        this.zomboidServerServiceName = System.getenv("ZOMBOID_SERVICE_NAME") != null
                ? System.getenv("ZOMBOID_SERVICE_NAME")
                : "zomboid_service";
    }
}
