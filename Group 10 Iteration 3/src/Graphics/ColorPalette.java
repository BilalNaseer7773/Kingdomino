import java.awt.Color;
import java.io.Serializable;

public enum ColorPalette implements Serializable {

    ROYAL("Royal", new Color(128, 20, 20), new Color(178, 162, 213), Color.BLACK, new Color(133, 35, 255), new Color(180, 180, 180), new Color(255, 214, 17)),
    CANDY("Candy", new Color(150, 90, 54), new Color(245, 156, 245), new Color(68, 31, 13), new Color(65, 255, 255), new Color(204, 144, 94), new Color(143, 210, 129)),
    GREYSCALE("Greyscale", Color.GRAY, new Color(217, 217, 217), Color.BLACK, new Color(134, 134, 134), new Color(180, 180, 180), Color.WHITE);

    private final String name;
    private final Color background;
    private final Color button;
    private final Color text;
    private final Color interact;
    private final Color panel;
    private final Color accent;

    public static final int Light = 0;
    public static final int DARK = 1;

    ColorPalette(String name, Color background, Color button, Color text, Color interact, Color panel, Color accent) {
        this.name = name;
        this.background = background;
        this.button = button;
        this.text = text;
        this.interact = interact;
        this.panel = panel;
        this.accent = accent;
    }

    public Color getBackground(int theme) {
        if (theme==0){
            return background;
        } else {
            return background.darker();
        }
    }

    public Color getButton(int theme) {
        return button;
    }

    public Color getText(int theme) {
        return text;
    }

    public Color getInteract(int theme) {
        return interact;
    }

    public Color getPanel(int theme) {
        if (theme==0){
            return panel;
        } else {
            return panel.darker();
        }
    }

    public Color getAccent(int theme) {
        return accent;
    }

    @Override
    public String toString() {
        return name;
    }
}
