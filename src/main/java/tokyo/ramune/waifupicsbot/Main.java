package tokyo.ramune.waifupicsbot;

import tokyo.ramune.waifupicsbot.pics.HTTP;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        URL imageURL = null;
        try {
            imageURL = new URL(new HTTP().getImage());
        } catch (Exception ignored) {
        }
        BufferedImage readImage = null;
        File outputfile = new File("image.jpg");
        try {
            readImage = ImageIO.read(imageURL);
            ImageIO.write(readImage, "jpg", outputfile);
        } catch (Exception ignored) {
        }
        StatusUpdate statusUpdate = new StatusUpdate("#waifu #Waifus #anime #animegirl");
        statusUpdate.setMedia(outputfile);
        try {
            twitter.updateStatus(statusUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OK");
        new File("image.jpg").deleteOnExit();
    }
}
