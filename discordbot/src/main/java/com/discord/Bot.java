package com.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import commands.CommandManager;
import events.JoinEvent;
import events.MainEventHandler;
import events.LeaveEvent;

public class Bot {

    /**
     * Starts the Discord bot.
     * 
     * @throws Exception if there is an error starting the bot.
     */
    public static void startBot() throws Exception {

        // Load properties from the 'config.properties' file
        Properties prop = new Properties();
        InputStream inputStream = Bot.class.getClassLoader().getResourceAsStream("config.properties");
        if (inputStream == null) {
            // If the config file is not found, an exception is thrown
            throw new FileNotFoundException("config.properties file not found in classpath");
        }
        prop.load(inputStream); // Load the properties from the input stream

        // Retrieve the bot token from the properties
        String token = prop.getProperty("TOKEN");

        // JDABuilder is used to configure and construct the JDA (Java Discord API)
        // instance
        JDABuilder builder = JDABuilder.createDefault(token);

        // Set the activity (status) of the bot, which is displayed in Discord

        builder.setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS, "!bot help"));

        // Enable intents (almost like permissions) - these are necessary to receive
        // certain types of events
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_REACTIONS);

        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);

        // Add event listeners - here, you add your custom event handler
        builder.addEventListeners(new MainEventHandler(), new JoinEvent(), new LeaveEvent(), new CommandManager());

        // Build and start the bot
        builder.build();
    }

}
