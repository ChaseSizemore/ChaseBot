package events;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        System.out.println("someone has joined the server");
        System.out.println("member: " + event.getMember().getEffectiveName());
        TextChannel welcomeChannel = event.getGuild().getTextChannelsByName("welcome", true).stream().findFirst()
                .orElse(null);

        if (welcomeChannel != null) {
            welcomeChannel.sendMessage(event.getUser().getAsMention() + " has joined the server!").queue();
        } else {
            // If "welcome" channel is not found, send the message to any available text
            // channel
            TextChannel anyChannel = event.getGuild().getTextChannels().stream().findFirst().orElse(null);
            if (anyChannel != null) {
                anyChannel.sendMessage(event.getUser().getAsMention() + " has joined the server.").queue();
            }
        }
    }

}
