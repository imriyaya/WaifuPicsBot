package tokyo.ramune.waifupicsbot.pics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

public class HTTP {
    public Response getImage() {
        URL url = null;
        try {
            url = new URL("https://api.waifu.im/random/");
            if (new Random().nextBoolean()) {
                url = new URL("https://api.waifu.im/random/?is_nsfw=true");
            }
        } catch (Exception e) {
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

        System.out.println("Getting image url from waifu API...");
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(xml);
            return new Response() {
                @Override
                public URL getImageURL() {
                    try {
                        return new URL(jsonNode.findPath("images").findValuesAsText("url").toString()
                                .replace("[", "")
                                .replace("]", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                        return null;
                    }
                }

                @Override
                public String getSource() {
                    return jsonNode.findPath("images").findValuesAsText("source").toString()
                            .replace("[", "")
                            .replace("]", "");
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}