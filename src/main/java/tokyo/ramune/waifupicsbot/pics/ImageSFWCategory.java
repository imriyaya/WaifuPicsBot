package tokyo.ramune.waifupicsbot.pics;

public enum ImageSFWCategory {
    WAIFU,
    NEKO,
    SHINOBU,
    MEGUMIN,
    BULLY,
    CUDDLE,
    CRY,
    HUG,
    AWOO,
    KISS,
    LICK,
    PAT,
    SMUG,
    BONK,
    YEET,
    BLUSH,
    SMILE,
    WAVE,
    HIGHFIVE,
    HANDHOLD,
    NOM,
    BITE,
    GLOMP,
    SLAP,
    K;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
