package events;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ValorantAPI {

    final static String APIRoute = "https://api.henrikdev.xyz/valorant/v1";

    public static String handleCommand(String[] args){
        switch(args[0]){
            case "mmr":
                return getMMR(args[1], args[2]).join();
            default:
                return args[1] + " is not a valid command";
        }
    }
    public static CompletableFuture<HttpResponse<String>> makeRequest(String route){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(route))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .exceptionally(e -> {
                System.err.println("Request failed: " + e.getMessage());
                return null;
            });
    }

    public static CompletableFuture<HttpResponse<String>> getPUUID(String username, String tag){
        String route = APIRoute + "/account/" + username + "/" + tag;
        return makeRequest(route).thenApply(response -> {
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            return response;
        });
    }
    

    public static CompletableFuture<String> getMMR(String username, String tag) {
        String route = APIRoute + "/mmr/na/" + username + "/" + tag;
        return makeRequest(route).thenApply(response -> {
            // Ensure response is HttpResponse<String>
            if (response instanceof HttpResponse) {
                HttpResponse<String> httpResponse = (HttpResponse<String>) response;
    
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(httpResponse.body(), JsonObject.class);
    
                // Extracting additional fields
                String currentTierPatched = jsonResponse.getAsJsonObject("data").get("currenttierpatched").getAsString();
                int currentTier = jsonResponse.getAsJsonObject("data").get("currenttier").getAsInt();
                int rankInTier = jsonResponse.getAsJsonObject("data").get("ranking_in_tier").getAsInt();
                int elo = jsonResponse.getAsJsonObject("data").get("elo").getAsInt();
                int mmrChangeToLastGame = jsonResponse.getAsJsonObject("data").get("mmr_change_to_last_game").getAsInt();
                // String largeImageUrl = jsonResponse.getAsJsonObject("data").getAsJsonObject("images").get("large").getAsString();
    
                // Formatting the response
                String result = String.format("Current Tier Patched: %s\nCurrent Tier: %d\nRank in Tier: %d\nELO: %d\nMMR Change to Last Game: %d\n",
                        currentTierPatched, currentTier, rankInTier, elo, mmrChangeToLastGame);
    
                System.out.println(result);
    
                return result;
            } else {
                return "Invalid response type";
            }
        });
    }


}
