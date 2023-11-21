package events;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LeaveEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        // Check if the user is banned
        event.getGuild().retrieveBan(event.getUser()).queue(
            (ban) -> sendMessageToChannel(event, event.getUser().getAsMention() + " has been banned from the server."),
            (failure) -> {
                // If not banned, assume the user was kicked or left voluntarily
                String message = "It seems " + event.getUser().getAsMention() + " is no longer part of the server.";
                sendMessageToChannel(event, message);
            }
        );
    }

    private void sendMessageToChannel(GuildMemberRemoveEvent event, String message) {
        TextChannel generalChannel = event.getGuild().getTextChannelsByName("general", true).stream().findFirst()
                .orElse(null);

        if (generalChannel != null) {
            generalChannel.sendMessage(message).queue();
        } else {
            // If "general" channel is not found, send the message to any available text channel
            TextChannel anyChannel = event.getGuild().getTextChannels().stream().findFirst().orElse(null);
            if (anyChannel != null) {
                anyChannel.sendMessage(message).queue();
            }
        }
    }
}
