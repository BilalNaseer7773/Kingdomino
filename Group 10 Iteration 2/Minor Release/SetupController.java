import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.ArrayList;

public class SetupController implements MenuController{

    private final MainWindow game;
    private final Kingdomino program;

    private JButton backButton;
    private JButton helpButton;
    private JButton startButton;
    private JButton resetButton;
    private JPanel playerSlider;
    private JPanel gamemodeBox;
    private JSlider playerNum;
    private ArrayList<JCheckBox> gamemodes;
    private ArrayList<PlayerBoxController> players;


    SetupController(MainWindow game, Kingdomino program) {
        this.game = game;
        this.program = program;
    }

    // Create the game and move menus to the game menu.
    public void CreateGame(){
        int numberOfPlayers = playerNum.getValue();
        Game newGame = new Game(program, numberOfPlayers);
        program.StartGame(newGame);
        game.toMenu(MainWindow.GAME, false);
    }

    @Override
    public void setup() {
        // Buttons
        backButton = new JButton("Back");
        backButton.addActionListener(e -> game.toMenu(MainWindow.START, true));
        helpButton = new JButton("Help");
        //helpButton.addActionListener(e -> System.out.println(numberOfPlayers)); // this is for debugging
        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> CreateGame());
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            game.clearPreserved();
            game.toMenu(MainWindow.SETUP, false);
        });

        // # of Players slider box
        playerSlider = new JPanel();
        playerSlider.setLayout(new GridLayout(2, 0));
        JLabel playerLabel = new JLabel("Number of Players", SwingConstants.CENTER);
        playerSlider.add(playerLabel);
        playerNum = new JSlider(2, 4);
        playerNum.addChangeListener((ChangeEvent e) -> playerCountChange(((JSlider) e.getSource()).getValue()));
        playerNum.setMajorTickSpacing(1);
        playerNum.setPaintTicks(true);
        playerNum.setPaintLabels(true);
        playerSlider.add(playerNum);
        playerSlider.setBorder(BorderFactory.createBevelBorder(0));

        // Gamemode selection box
        gamemodeBox = new JPanel();
        gamemodeBox.setLayout(new BorderLayout());
        JLabel label = new JLabel("Additional Gamemodes", SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        gamemodeBox.add(label, BorderLayout.NORTH);
        JPanel box1 = new JPanel();
        box1.setLayout(new GridLayout(2,2));
        gamemodes = new ArrayList<>(4);
        gamemodes.add(new JCheckBox("The Mighty Duel"));
        gamemodes.add(new JCheckBox("Harmony"));
        gamemodes.add(new JCheckBox("Middle Kingdom"));
        gamemodes.add(new JCheckBox("Dynasty"));
        for (JCheckBox gmmd : gamemodes){
            box1.add(gmmd);
        }
        gamemodeBox.add(box1, BorderLayout.CENTER);
        gamemodeBox.setBorder(BorderFactory.createBevelBorder(0));

        // Player Boxes
        players = new ArrayList<>(4);
        int i;
        for (i=1;i<5;i++){
            PlayerBoxController play = new PlayerBoxController("Player " + i, game, i, PlayerColor.values()[i-1]);
            play.setup();
            players.add(play);
        }

        playerCountChange(4);
        playerNum.setValue(4);
    }

    private void playerCountChange(int plyrs){
        for (PlayerBoxController play : players){
            if(players.indexOf(play) >= plyrs){
                play.disable();
            } else{
                play.enable();
            }
        }
        JCheckBox duel = gamemodes.get(0);
        if(plyrs==2){
            duel.setEnabled(true);
        }else{
            duel.setEnabled(false);
            duel.setSelected(false);
        }
    }

    @Override
    public void sizing(int width, int height) {
        int offset = 20;
        int xSize = width / 10;
        int ySize = 30;
        backButton.setBounds(offset, offset, xSize, ySize);
        helpButton.setBounds(width - offset - xSize, offset, xSize, ySize);
        startButton.setBounds(width - offset - xSize, height - ySize - offset, xSize, ySize);
        resetButton.setBounds(offset, height - ySize - offset, xSize, ySize);
        xSize = width / 4;
        ySize = height / 5;
        playerSlider.setBounds(width / 4 - xSize / 2, height / 3 - ySize / 2, xSize, ySize);
        gamemodeBox.setBounds(3 * width / 4 - xSize / 2, height / 3 - ySize / 2, xSize, ySize);

        backButton.repaint();
        helpButton.repaint();
        startButton.repaint();
        resetButton.repaint();
        playerSlider.repaint();
        gamemodeBox.repaint();

        for (PlayerBoxController box : players){
            box.sizing(width, height);
        }
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = game.getFrame().getLayeredPane();
        pane.add(backButton, JLayeredPane.PALETTE_LAYER);
        pane.add(helpButton, JLayeredPane.PALETTE_LAYER);
        pane.add(startButton, JLayeredPane.PALETTE_LAYER);
        pane.add(resetButton, JLayeredPane.PALETTE_LAYER);
        pane.add(playerSlider, JLayeredPane.PALETTE_LAYER);
        pane.add(gamemodeBox, JLayeredPane.PALETTE_LAYER);
        for (PlayerBoxController box : players){
            box.addComponents();
        }
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        for (PlayerBoxController pl : players){
            pl.coloring(palette, theme);
        }
    }
}
