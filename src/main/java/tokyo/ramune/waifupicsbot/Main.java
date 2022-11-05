package tokyo.ramune.waifupicsbot;

import tokyo.ramune.waifupicsbot.pics.HTTP;
import tokyo.ramune.waifupicsbot.pics.Response;
import tokyo.ramune.waifupicsbot.utility.DiscordWebhook;
import twitter4j.Status;
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
            StatusUpdate statusUpdate = new StatusUpdate("source: " + response.getSource()
                    + "\n#waifu #Waifus #anime #animegirl");

            statusUpdate.setMedia("media", in);

            System.out.println("Uploading waifu image to Twitter...");


            try {
                Status status = twitter.updateStatus(statusUpdate);
                try {
                    System.out.println("Sending discord webhook...");
                    DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/1012626900193132645/uFPdF1MX7GEdPGEWfdBvVVdL06T3zIvrhKNQ3JiRBDf3pWvlohcIZ0b5y5jLdxD-w7eJ");
                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setImage(status.getMediaEntities()[0].getMediaURL()));
                    discordWebhook.execute();
                } catch (Exception ignored) {
                }
                System.out.println("Success!");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
