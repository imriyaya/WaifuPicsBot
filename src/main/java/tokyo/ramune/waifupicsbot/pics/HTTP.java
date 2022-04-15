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
import java.util.Random;

public class HTTP {
    public String getImage(boolean isNSFW) {
        URL url = null;
        try {
            if (isNSFW) {
                url = new URL("https://waifu.pics/api/nsfw/" + ImageNSFWCategory.values()[(int) (Math.random() * ( ImageNSFWCategory.values().length))].toString());
            } else {
                url = new URL("https://waifu.pics/api/sfw/" + ImageSFWCategory.values()[(int) (Math.random() * ( ImageSFWCategory.values().length))].toString());
            }
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
            return jsonNode.findValue("url").toString().replace('"', ' ').replace(" ", "");
        } catch (JsonProcessingException e) {
            System.exit(1);
            System.exit(1);
            return null;
        }
    }
}