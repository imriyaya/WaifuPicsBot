package tokyo.ramune.waifupicsbot;

import tokyo.ramune.waifupicsbot.pics.HTTP;
import tokyo.ramune.waifupicsbot.pics.Response;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {

    private static TwitterFactory factory;

    public static void main(String[] args) {
        factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        Response response = new HTTP().getImage();
        URL imageURL, sourceURL;
        String name, description;

        imageURL = response.imageURL();
        sourceURL = response.imageSourceURL();
        name = response.name();
        description = response.description();

        String imageFormat = "jpg";
        if (imageURL.toString().endsWith("gif")) {
            imageFormat = "gif";
        }

        System.out.println("Converting image to image." + imageFormat);

        try (InputStream in = imageURL.openStream()) {
            Files.copy(in, new File("image." + imageFormat).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (name.length() >= 30) {
            name = name.substring(30) + "...";
        }

        if (description.length() >= 50) {
            description = description.substring(50) + "...";
        }

        StatusUpdate statusUpdate = new StatusUpdate(
                "Image type: " + name + "\n" +
                        "Image description: " + description + "\n" +
                        "Source: " + sourceURL + "\n" +
                        "#waifu #Waifus #anime #animegirl");

        statusUpdate.setMedia(new File("image." + imageFormat));

        System.out.println("Uploading waifu image to Twitter...");

        try {
            twitter.updateStatus(statusUpdate);
            System.out.println("Success!");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        new File("image." + imageFormat).deleteOnExit();
    }

    public static TwitterFactory getFactory() {
        return factory;
    }
}
