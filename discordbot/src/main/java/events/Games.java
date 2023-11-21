package events;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Games {

    public static void higherLower(MessageReceivedEvent messageEvent) {
        if (messageEvent.getAuthor().isBot()) {
            return; // Ignore messages sent by bots
        }

        Random random = new Random();
        int targetNumber = random.nextInt(100) + 1; // Generate a random number between 1 and 100
        int attempts = 0;
        boolean gameOver = false;

        messageEvent.getChannel().sendMessage(
                "Welcome to the Higher Lower game! Guess a number between 1 and 100! Or \"Cancel\" to cancel the game.")
                .queue();

        while (!gameOver) {
            messageEvent.getChannel().sendMessage("in game loop");
            if (messageEvent.getAuthor().isBot()) {
                continue;
            }
            String userGuess = messageEvent.getMessage().getContentRaw();

            if (userGuess.equalsIgnoreCase("cancel")) {
                messageEvent.getChannel().sendMessage("Game canceled.").queue();
                break;
            }

            int guess;

            try {
                guess = Integer.parseInt(userGuess);
            } catch (NumberFormatException e) {
                messageEvent.getChannel().sendMessage("Invalid input. Please enter a valid number.").queue();
                continue;
            }

            attempts++;

            if (guess < targetNumber) {
                messageEvent.getChannel().sendMessage("Higher!").queue();
            } else if (guess > targetNumber) {
                messageEvent.getChannel().sendMessage("Lower!").queue();
            } else {
                messageEvent.getChannel()
                        .sendMessage("Congratulations! You guessed the correct number in " + attempts + " attempts!")
                        .queue();
                gameOver = true;
            }
        }
    }

    /**
     * Simulates a coin flip and sends the result to the Discord channel.
     * 
     * @param messageEvent The MessageReceivedEvent object representing the message
     *                     event.
     * @throws InterruptedException If the thread is interrupted while sleeping.
     */
    public static void coinFlip(MessageReceivedEvent messageEvent) throws InterruptedException {

        String response;
        if (Math.random() < 0.5) {
            response = "**Heads**!";
        } else {
            response = "**Tails**!";
        }

        messageEvent.getChannel().sendMessage("Flipping a :coin:").queue();
        TimeUnit.SECONDS.sleep(1);
        messageEvent.getChannel().sendMessage(".").queue();
        TimeUnit.SECONDS.sleep(1);
        messageEvent.getChannel().sendMessage(".").queue();
        TimeUnit.SECONDS.sleep(1);
        messageEvent.getChannel().sendMessage(".").queue();
        TimeUnit.SECONDS.sleep(1);
        messageEvent.getChannel().sendMessage(response).queue();
    }

}
