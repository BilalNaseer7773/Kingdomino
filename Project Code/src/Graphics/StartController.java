import javax.swing.*;
import java.awt.*;

public class StartController implements MenuController{

    public final static int START = 0;
    public final static int PAUSE = 1;

    private final MainWindow target;

    private final int mode;

    private JLabel logo;
    private ScalingImage background;
    private final Image backgroundImage;
    private JButton topButton;
    private JButton loadGame;
    private JButton settings;
    private JButton quitGame;

    StartController(MainWindow target, Image backgroundImage, int mode) {
        this.target = target;
        this.backgroundImage = backgroundImage;
        this.mode = mode;
        if(mode==START){
            target.clearPreserved();
            target.getProgram().setGame(null);
        }
    }

    @Override
    public void setup() {
        logo = new JLabel(new ImageIcon(OtherImage.LOGO.getImage()));
        background = new ScalingImage(backgroundImage);
        settings = new JButton("Settings");
        if(mode==START){
            target.getProgram().titleSong();
            quitGame = new JButton("Quit");
            topButton = new JButton("New Game");
            loadGame = new JButton("Load Game");
            topButton.addActionListener(e -> target.toMenu(MainWindow.SETUP, false));
            quitGame.addActionListener(e -> System.exit(0));
            loadGame.addActionListener(e -> target.toMenu(MainWindow.LOAD, false));
        } else{
            quitGame = new JButton("Return to Main Menu");
            topButton = new JButton("Continue");
            loadGame = new JButton("Save Game");
            topButton.addActionListener(e -> target.toMenu(MainWindow.GAME, false));
            quitGame.addActionListener(e->{
                target.clearPreserved();
                target.toMenu(MainWindow.START, false);
            });
            loadGame.addActionListener(e -> target.toMenu(MainWindow.SAVE, false));
        }
        settings.addActionListener(e -> target.toMenu(MainWindow.SETTINGS, false));
    }

    @Override
    public void sizing(int width, int height) {
        // Kingdomino logo
        int xSize = logo.getIcon().getIconWidth();
        int ySize = logo.getIcon().getIconHeight();
        logo.setBounds(width / 2 - xSize / 2, height / 4 - ySize / 2, xSize, ySize);

        // Background
        double screenRatio = (double) width / height;
        double imageRatio = (double) background.getImageWidth() / background.getImageHeight();
        xSize = screenRatio > imageRatio ? width : (int) (height * imageRatio);
        ySize = screenRatio > imageRatio ? (int) (width / imageRatio) : height;
        background.setBounds(width / 2 - xSize / 2, 0, xSize, ySize);

        // Buttons
        xSize = width / 3;
        ySize = height / 20;
        int buttonX = width / 2 - xSize / 2;
        int buttonY = 2 * height / 4 - ySize / 2;
        int spacing = height / 10;
        topButton.setBounds(buttonX, buttonY, xSize, ySize);
        loadGame.setBounds(buttonX, buttonY + spacing, xSize, ySize);
        settings.setBounds(buttonX, buttonY + spacing * 2, xSize, ySize);
        quitGame.setBounds(buttonX, buttonY + 3 * spacing, xSize, ySize);

        background.repaint();
        logo.repaint();
        topButton.repaint();
        loadGame.repaint();
        settings.repaint();
        quitGame.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(logo, JLayeredPane.PALETTE_LAYER);
        pane.add(topButton, JLayeredPane.PALETTE_LAYER);
        pane.add(loadGame, JLayeredPane.PALETTE_LAYER);
        pane.add(settings, JLayeredPane.PALETTE_LAYER);
        pane.add(quitGame, JLayeredPane.PALETTE_LAYER);
        pane.add(background, JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void removeComponents() {

    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
    }
}
