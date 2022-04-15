package tokyo.ramune.waifupicsbot;

import tokyo.ramune.waifupicsbot.pics.HTTP;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        URL imageURL = null;

        try {
            imageURL = new URL(new HTTP().getImage(new Random().nextBoolean()));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

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

        StatusUpdate statusUpdate;

        if (new Random().nextInt(100) == 1) {
            statusUpdate = new StatusUpdate("#waifu #Waifus #anime #animegirl");
        } else {
            statusUpdate = new StatusUpdate("");
        }

        statusUpdate.setMedia(new File("image." + imageFormat));

        System.out.println("Uploading waifu image to Twitter...");

        try {
            twitter.updateStatus(statusUpdate);
            System.out.println("Success!");
        } catch (Exception e) {
            System.exit(1);
            e.printStackTrace();
        }
        new File("image." + imageFormat).deleteOnExit();
    }
}
