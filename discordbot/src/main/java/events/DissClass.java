package events;

import java.util.Random;

public class DissClass {

    private static final String[] JOKES = {
        "If brains were gasoline, {name} couldn't power an ant's motorcycle around the inside of a Cheerio.",
        "{name} is so boring, their diary reads like the terms and conditions.",
        "Some drink from the fountain of knowledge, but it seems {name} just gargled.",
        "It's not that I'm smarter than {name}; it's just that I do less stupid things.",
        "I'd like to see things from {name}'s point of view, but I can't seem to get my head that far up my own.",
        "If ignorance is bliss, {name} must be the happiest person alive.",
        "{name} brings so much joy, whenever they leave the room.",
        "Mirrors can't talk. Lucky for {name}, they can't laugh either.",
        "I'm not saying {name} is dumb, just that they have bad luck thinking.",
        "{name} is the reason why aliens won't talk to us.",
        "If {name} was a vegetable, they'd be a 'cabbage' case.",
        "I'm not insulting {name}, I'm describing them.",
        "{name} is like a cloud. When they disappear, it's a beautiful day.",
        "I'm not sure what's brighter, the sun or {name}'s future.",
        "{name} is so unoriginal, they copy and paste their personality.",
        "If {name} was a movie, they'd be rated 'G' for 'Gullible'."
    };

    public static String getRandomJoke(String name) {
        Random random = new Random();
        int index = random.nextInt(JOKES.length);
        return JOKES[index].replace("{name}", name);
    }

    public static void main(String[] args) {
        String joke = getRandomJoke(args[0]);
        System.out.println(joke);
    }
}
