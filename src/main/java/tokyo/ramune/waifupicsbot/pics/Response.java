package tokyo.ramune.waifupicsbot.pics;

import java.net.URL;

public record Response(URL imageURL, URL imageSourceURL, String name, String description) {
}
