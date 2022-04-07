package tokyo.ramune.Pics;

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

public class HTTP {
    public String getImage() {
        URL url = null;
        try {
            url = new URL("https://api.waifu.im/random/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            http.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            http.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String xml = "", line = "";
        while(true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            xml += line;
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(xml);
            return jsonNode.get("images").findValue("url").toString().replace('"', ' ').replace(" ", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}