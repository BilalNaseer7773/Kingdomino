import javax.swing.*;
import java.awt.*;
import java.net.URL;

public interface ImageSet {
    default Image getImageFromString(String name){
        ImageIcon img;
        try {
            URL url = getClass().getResource(name);
            img = new ImageIcon(url);
        } catch (Throwable e){
            img = new ImageIcon(name);
        }

        return img.getImage();
    }
}
