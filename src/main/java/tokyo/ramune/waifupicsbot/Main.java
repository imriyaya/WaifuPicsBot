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

        URL imageURL = response.getImageURL();

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

        StatusUpdate statusUpdate = new StatusUpdate("source: " + response.getSource()
                + "\n#waifu #Waifus #anime #animegirl");

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
}
