package events;

import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

public class MainEventHandler implements EventListener {

    /**
     * This method is called when a generic event occurs.
     * It checks if the event is an instance of MessageReceivedEvent and handles the
     * event accordingly.
     * If the event is a message received event from a non-bot user, it splits the
     * message content by spaces and checks if the first element is "!bot".
     * If it is, it calls the handleCommands method to handle the bot commands.
     *
     * @param event The generic event that occurred.
     */

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent messageEvent = (MessageReceivedEvent) event;
            if (messageEvent.getAuthor().isBot()) {
                return;
            }

            String[] messageContent = messageEvent.getMessage().getContentRaw().split(" ");
            printUserMessage(messageEvent);

            if (messageContent[0].equals("!bot")) {
                handleCommands(messageEvent, messageContent);
            }
        } else if (event instanceof GuildMemberJoinEvent) {
            GuildMemberJoinEvent joinEvent = (GuildMemberJoinEvent) event;
            joinEvent.getGuild().getTextChannelsByName("welcome", true).get(0).sendMessage("Welcome to the server, " + joinEvent.getMember().getAsMention() + "!").queue();
        }
        else if(event instanceof GuildMemberRemoveEvent){
            GuildMemberRemoveEvent removeEvent = (GuildMemberRemoveEvent) event;
            removeEvent.getGuild().getTextChannelsByName("welcome", true).get(0).sendMessage("Goodbye, " + removeEvent.getMember().getAsMention() + "!").queue();
        }
    }

    public void handleCommands(MessageReceivedEvent messageEvent, String[] messageContent) {

        String[] remainderArray = Arrays.copyOfRange(messageContent, 2, messageContent.length);
        switch (messageContent[1]) {
            case "diss":
                String joke = DissClass.getRandomJoke(messageContent[2]);
                messageEvent.getChannel().sendMessage(joke).queue();
                break;
            case "pic":
                try {
                    sendChannelMessage(messageEvent, "Generating Image! This could take a minute...");
                    String prompt = String.join(" ", remainderArray);
                    String image = Image.generateImage(prompt);
                    sendChannelMessage(messageEvent, image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "help":
                sendPrivateMessage(messageEvent,
                        "Commands \n!bot help -list of commands \n!bot pic <prompt> -image generation\n !bot diss <name> -random diss");
                break;
            case "valorant":
                ValorantAPI valorantAPI = new ValorantAPI();
                String message = ValorantAPI.handleCommand(remainderArray);
                sendChannelMessage(messageEvent, message);
                break;
            case "GPT":
                break;
            case "coinflip":
                try {
                    Games.coinFlip(messageEvent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "higherlower":
                // String startGameMessage =
                // Games.startHigherLowerGame(messageEvent.getChannel().getId());
                // messageEvent.getChannel().sendMessage(startGameMessage).queue();
                Games.higherLower(messageEvent);
                break;

            // case "guess":
            // try {
            // int guess = Integer.parseInt(remainderArray[0]);
            // String guessResult = Games.guessNumber(messageEvent.getChannel().getId(),
            // guess);
            // messageEvent.getChannel().sendMessage(guessResult).queue();
            // } catch (NumberFormatException e) {
            // messageEvent.getChannel().sendMessage("Please enter a valid
            // number.").queue();
            // }
            // break;

            case "admin":
                if (messageEvent.getAuthor().getId().equals("170728847711535104")) {
                    switch (messageContent[2]) {
                        case "shutdown":
                            messageEvent.getChannel().sendMessage("Shutting down...").queue();
                            messageEvent.getJDA().shutdown();
                            break;
                        default:
                            messageEvent.getChannel()
                                    .sendMessage("Invalid command. Type !bot help for a list of commands.")
                                    .queue();
                            break;
                    }
                } else {
                    messageEvent.getChannel().sendMessage("You do not have permission to use this command.")
                            .queue();
                }
                break;

            default:
                messageEvent.getChannel().sendMessage("Invalid command. Type !bot help for a list of commands.")
                        .queue();
                break;
        }
    }

    /**
     * Sends a message to the channel where the event occurred.
     *
     * @param messageEvent The event object representing the message received event.
     * @param message      The message to be sent to the channel.
     */
    public void sendChannelMessage(MessageReceivedEvent messageEvent, String message) {
        messageEvent.getChannel().sendMessage(message).queue();
    }

    /**
     * Sends a private message to the author of a message event.
     * 
     * @param messageEvent The message event from which to retrieve the author.
     * @param message      The message to send.
     */
    public void sendPrivateMessage(MessageReceivedEvent messageEvent, String message) {
        messageEvent.getAuthor().openPrivateChannel().queue((channel) -> {
            channel.sendMessage(message).queue();
        });
    }



    /**
     * Prints the details of a user message.
     * 
     * @param messageEvent The event containing the user message.
     */
    public void printUserMessage(MessageReceivedEvent messageEvent) {
        String messageContent = messageEvent.getMessage().getContentRaw();
        String author = messageEvent.getAuthor().getName();
        String authorId = messageEvent.getAuthor().getId();
        String messageTime = messageEvent.getMessage().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.print("\n");
        System.out.println("Author: " + author);
        System.out.println("Author ID: " + authorId);
        System.out.println("Message: " + messageContent);
        System.out.println("Time: " + messageTime);
        System.out.print("\n");
    }

    /**
     * Logs a command by creating a LogEntry object and writing it to a log file.
     * 
     * @param messageEvent the MessageReceivedEvent object representing the received
     *                     message
     */
    public void logCommand(MessageReceivedEvent messageEvent) {
        String messageContent = messageEvent.getMessage().getContentRaw();
        String author = messageEvent.getAuthor().getName();
        String authorId = messageEvent.getAuthor().getId();
        String messageTime = messageEvent.getMessage().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Create a LogEntry object
        LogEntry logEntry = new LogEntry(author, authorId, messageContent, messageTime);

        // Check if the file exists
        File file = new File("/src/main/resources/logs.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write the log entry to logs.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println(logEntry.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Represents a log entry containing information about the author, author ID,
     * message content, and message time.
     */
    private static class LogEntry {
        private String author;
        private String authorId;
        private String messageContent;
        private String messageTime;

        public LogEntry(String author, String authorId, String messageContent, String messageTime) {
            this.author = author;
            this.authorId = authorId;
            this.messageContent = messageContent;
            this.messageTime = messageTime;
        }

        @Override
        public String toString() {
            return "Author: " + author + "\n" +
                    "Author ID: " + authorId + "\n" +
                    "Message: " + messageContent + "\n" +
                    "Time: " + messageTime + "\n";
        }

    }

}
