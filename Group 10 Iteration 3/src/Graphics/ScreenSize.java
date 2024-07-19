import java.awt.*;
import java.io.Serializable;

public class ScreenSize extends Dimension implements Serializable {

    private static ScreenSize[] standardSizes;

    public static ScreenSize[] getStandardSizes(){
        if(standardSizes==null){
            standardSizes = new ScreenSize[]{new ScreenSize(1200, 700, false), new ScreenSize(600, 420, false), new ScreenSize(0,0,true)};
        }
        return standardSizes;
    }


    private boolean fullscreen;

    ScreenSize(int width, int height, boolean fullscreen){
        super(width, height);
        this.fullscreen = fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    @Override
    public String toString() {
        if(isFullscreen()){
            return "Fullscreen";
        }
        return width + ", " + height;
    }
}
