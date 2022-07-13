package tokyo.ramune.waifupicsbot.pics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HTTP {
    public Response getImage() {
        URL url = null;
        final ArrayList<String> sfwCategories = new ArrayList<>(
                Arrays.asList("waifu", "neko", "shinobu", "megumin", "bully",
                        "cuddle", "cry", "hug", "awoo", "kiss",
                        "lick", "pat", "smug", "bonk", "yeet",
                        "blush", "smile", "wave", "highfive", "handhold",
                        "nom", "bite", "glomp", "slap", "kill", "dance",
                        "kick", "happy", "wink", "poke", "cringe"));
        final ArrayList<String> nsfwCategories = new ArrayList<>(
                Arrays.asList("waifu", "neko", "trap", "blowjob"));
        try {
            String rawUrl = "https://api.waifu.pics/sfw/";
            rawUrl = rawUrl + sfwCategories.get(new Random().nextInt(sfwCategories.size() - 1) + 1);
            if (new Random().nextBoolean()) {
                rawUrl = "https://api.waifu.pics/nsfw/";
                rawUrl = rawUrl + nsfwCategories.get(new Random().nextInt(nsfwCategories.size() - 1) + 1);
            }
            System.out.println(rawUrl);
            url = new URL(rawUrl);
            System.out.println("Getting image from " + url.toString() + "...");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            http.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Connecting...");
        try {
            http.connect();
            System.out.println("Connected!");
        } catch (IOException e) {
            System.out.println("Couldn't connect to waifu images api :(");
            e.printStackTrace();
            System.exit(1);
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        String xml = "", line = "";
        while(true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            xml += line;
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Getting image url from waifu API...");
        try {
            JsonNode jsonNode = mapper.readTree(xml);
            return new Response(new URL(jsonNode.findValuesAsText("url").toString().replace("[", "").replace("]", "")));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}