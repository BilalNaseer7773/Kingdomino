import javax.swing.*;
import java.awt.*;

public enum Sprite implements ImageSet{
    SNOW_KING ("snow_king_icon.gif", "snow_king.png", "Snowmonarch"),
    OWL_KING ("owl_king_icon.gif", "owl_king_sprite.png", "Knight Owl"),
    ROBO_KING("robo_king_icon.gif", "robo_king_sprite.png", "Robolord"),
    PLANT_KING("plant_king_icon.gif", "plant_king_sprite.png", "A Plant");

    private final String icon;
    private final String sprite;
    private final String name;
    private static final String path = "Images/";

    Sprite(String icon0, String sprite0, String name){
        icon = icon0;
        sprite = sprite0;
        this.name = name;
    }

    Image getIcon(){
        return getImageFromString(icon);
    }

    Image getSprite(){
        return getImageFromString(sprite);
    }


    @Override
    public String toString() {
        return name;
    }
}
