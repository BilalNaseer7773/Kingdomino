import java.awt.*;

public enum OtherImage implements ImageSet{
    LOGO("logo.png"),
    BACK("background.png"),
    CROSS("cross_icon.gif"),
    TILE_SELECT("button_border.png");

    String image;

    OtherImage(String imageName){
        image = imageName;
    }

    Image getImage(){
        return getImageFromString(image);
    }
}
