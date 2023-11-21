package events;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import com.discord.Bot;


public class GPTClass {

    private String token;
    Properties prop = new Properties();

    public GPTClass() throws Exception {
        InputStream inputStream = Bot.class.getClassLoader().getResourceAsStream("config.properties");
        if (inputStream == null) {
            throw new FileNotFoundException("config.properties file not found in classpath");
        }
        prop.load(inputStream);
        token = prop.getProperty("OPEN_AI_TOKEN");
    }

    // public String getPicture(String message) {};
}
