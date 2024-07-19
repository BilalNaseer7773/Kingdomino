import javax.swing.*;

public class StartController implements MenuController{

    private final MainWindow target;

    private JLabel logo;
    private ScalingImage background;
    private JButton newGame;
    private JButton loadGame;
    private JButton settings;
    private JButton quitGame;

    StartController(MainWindow target) {
        this.target = target;
    }

    @Override
    public void setup() {
        logo = new JLabel(new ImageIcon(OtherImage.LOGO.getImage()));
        background = new ScalingImage(OtherImage.BACK.getImage());
        newGame = new JButton("New Game");
        loadGame = new JButton("Load Game");
        settings = new JButton("Settings");
        quitGame = new JButton("Quit");
        newGame.addActionListener(e -> target.toMenu(MainWindow.SETUP, false));
        settings.addActionListener(e -> target.toMenu(MainWindow.SETTINGS, false));
        loadGame.addActionListener(e -> target.toMenu(MainWindow.LOAD, false));
        quitGame.addActionListener(e -> System.exit(0));
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
        newGame.setBounds(buttonX, buttonY, xSize, ySize);
        loadGame.setBounds(buttonX, buttonY + spacing, xSize, ySize);
        settings.setBounds(buttonX, buttonY + spacing * 2, xSize, ySize);
        quitGame.setBounds(buttonX, buttonY + 3 * spacing, xSize, ySize);

        background.repaint();
        logo.repaint();
        newGame.repaint();
        loadGame.repaint();
        settings.repaint();
        quitGame.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(logo, JLayeredPane.PALETTE_LAYER);
        pane.add(newGame, JLayeredPane.PALETTE_LAYER);
        pane.add(loadGame, JLayeredPane.PALETTE_LAYER);
        pane.add(settings, JLayeredPane.PALETTE_LAYER);
        pane.add(quitGame, JLayeredPane.PALETTE_LAYER);
        pane.add(background, JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
    }
}
