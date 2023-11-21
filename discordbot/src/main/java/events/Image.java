package events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.discord.Bot;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Image {

    private static final String ENDPOINT = "https://api.openai.com/v1/images/generations";

    public static String generateImage(String prompt) throws IOException{
        Properties prop = new Properties();
        InputStream inputStream = Bot.class.getClassLoader().getResourceAsStream("config.properties");
        if (inputStream == null) {
            throw new FileNotFoundException("config.properties file not found in classpath");
        }
        prop.load(inputStream);
        String token = prop.getProperty("OPEN_AI_TOKEN"); 


        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpPost httpPost = new HttpPost(ENDPOINT);
            httpPost.setHeader("Authorization", "Bearer " + token);
            httpPost.setHeader("Content-Type", "application/json");

            String jsonBody = new ObjectMapper().writeValueAsString(Map.of("prompt", prompt));
            StringEntity entity = new StringEntity(jsonBody);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());

            JsonObject jsonObject = JsonParser.parseString(responseString).getAsJsonObject();
            JsonObject dataObject = jsonObject.getAsJsonArray("data").get(0).getAsJsonObject();
            String url = dataObject.get("url").getAsString();

            return url;
            
        }catch(Exception e){
            e.printStackTrace();
        }

        return "Failed to generate image! Something in the prompt went against the API's guidelines :()";
    }
}
