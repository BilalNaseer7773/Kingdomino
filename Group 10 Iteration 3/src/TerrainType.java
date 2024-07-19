import java.awt.*;
import java.io.Serializable;

public enum TerrainType implements ImageSet, Serializable {
    
    FIELD("Field", "field.png"),
    FOREST("Forest", "forest.png"),
    OCEAN("Ocean", "wave.png"),
    PLAINS("Plains,", "plains.png"),
    MINES("Mines", "mine.png"),
    DESERT("Desert", "desert.png"),
    START("Castle", "castle.png"),
    EMPTY("Empty", "mist.png");


    private final String name;
    private final Image image;

    TerrainType(String name, String image){
        this.name = name;
        this.image = getImageFromString(image);
    }

    public Image getImage(){
        return image;
    }
}
