package tokyo.ramune.waifupicsbot.pics;

public enum ImageNSFWCategory {
    WAIFU,
    NEKO,
    TRAP,
    BLOWJOB;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
