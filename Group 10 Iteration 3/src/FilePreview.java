import java.io.Serializable;

public class FilePreview implements Serializable {

    /**
     * This class is a preview of a game save file. Should only be created by Archivist.
     */
    private final String name;
    private final String filename;
    private final int round;
    private final Sprite[] sprites;



    FilePreview(String name, String filename, int round, Sprite[] sprites){
        this.name = name;
        this.filename = filename;
        this.round = round;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public int getRound() {
        return round;
    }

    public Sprite[] getSprites() {
        return sprites;
    }
}
