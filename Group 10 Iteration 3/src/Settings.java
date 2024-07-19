import java.io.Serializable;

public class Settings implements Cloneable, Serializable {
    private int theme;
    private ColorPalette palette;
    private ScreenSize dimensions;

    public int getTheme() {
        return theme;
    }

    public ColorPalette getPalette() {
        return palette;
    }

    public ScreenSize getDimensions(){
        return dimensions;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public void setPalette(ColorPalette palette) {
        this.palette = palette;
    }

    public void setDimensions(ScreenSize dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public Settings clone() {
        try {
            Settings clone = (Settings) super.clone();
                clone.theme = theme;
                clone.palette = palette;
                clone.dimensions = new ScreenSize(dimensions.width, dimensions.height, dimensions.isFullscreen());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
