import javax.swing.*;
import java.awt.*;
import java.net.URL;

public enum OtherImage implements ImageSet{
    LOGO("logo.png"),
    BACK("background.png"),
    CROSS("cross_icon.gif");

    String image;

    OtherImage(String imageName){
        image = imageName;
    }

    Image getImage(){
        return getImageFromString(image);
    }
}
