import java.awt.*;
import java.net.URL;

public interface ImageSet {
    default Image getImageFromString(String name){
        try {
            URL url = getClass().getResource(name);
            return Toolkit.getDefaultToolkit().getImage(url);
        } catch (Throwable e){
            return Toolkit.getDefaultToolkit().getImage(name);
        }
    }
}
