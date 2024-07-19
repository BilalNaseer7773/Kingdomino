import java.awt.*;

public enum PlayerColor {
    RED(Color.red, "Red"),
    BLUE(Color.blue, "Blue"),
    GREEN(Color.green, "Green"),
    YELLOW(Color.yellow, "Yellow"),
    PURPLE(new Color(109, 23, 133), "Purple"),
    PINK(Color.pink, "Pink"),
    ORANGE(Color.orange, "Orange"),
    BROWN(new Color(89, 44, 31), "Brown");

    private final Color color;
    private final String name;

    PlayerColor(Color color, String name){
        this.color = color;
        this.name = name;
    }



    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }
}
