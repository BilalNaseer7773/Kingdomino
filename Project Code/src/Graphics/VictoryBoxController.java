import javax.swing.*;
import java.awt.*;

public class VictoryBoxController implements MenuController{

    private final String name;
    private final Image icon;
    private final int score;
    private Point corner;

    private JLabel scoreLabel;
    private ScalingImage iconImage;
    private final JLayeredPane target;

    public VictoryBoxController(JLayeredPane target, String name, Image icon, int score) {
        this.name = name;
        this.icon = icon;
        this.score = score;
        this.target = target;
    }

    @Override
    public void setup() {
        scoreLabel = new JLabel(name + "'s score: " + score);
        iconImage = new ScalingImage(icon);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public void sizing(int width, int height) {
        int scoreSize = height / 10;
        int iconSize = Math.min(width, height - scoreSize);
        int x = corner.x + width / 2 - iconSize / 2;
        scoreLabel.setBounds(corner.x, corner.y + iconSize, width, scoreSize);
        iconImage.setBounds(x, corner.y, iconSize, iconSize);
    }

    @Override
    public void addComponents() {
        target.add(scoreLabel, JLayeredPane.PALETTE_LAYER);
        target.add(iconImage, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        scoreLabel.setForeground(palette.getAccent(ColorPalette.DARK));
    }

    @Override
    public void removeComponents() {

    }

    public void setCorner(Point corner) {
        this.corner = corner;
    }
}
