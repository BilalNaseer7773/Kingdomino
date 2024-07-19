import javax.swing.*;
import java.awt.*;

public class GameController implements MenuController{

    private final MainWindow game;
    private Game currentGame;
    private JButton helpButton;

    private JPanel player1Board;
    private ScalingImage [][] p1GridSquares;
    private JPanel player2Board;
    private ScalingImage [][] p2GridSquares;
    private JPanel player3Board;
    private ScalingImage [][] p3GridSquares;
    private JPanel player4Board;
    private ScalingImage [][] p4GridSquares;
    
    private int numberOfPlayers;


    GameController(MainWindow game, Game currentGame){
        this.game = game;
        this.currentGame = currentGame;
        numberOfPlayers = currentGame.getNumberOfPlayers();
    }

    @Override
    public void setup() {
        // board creation

        player1Board = new JPanel();
        player2Board = new JPanel();
        p1GridSquares = new ScalingImage [10][10];
        p2GridSquares = new ScalingImage [10][10];


        player1Board.setLayout(new GridLayout(10, 10));
        player2Board.setLayout(new GridLayout(10, 10));

        drawBoard(player1Board, p1GridSquares);
        drawBoard(player2Board, p2GridSquares);

        if(numberOfPlayers >= 3){
            player3Board = new JPanel();
            p3GridSquares = new ScalingImage [10][10];
            player3Board.setLayout(new GridLayout(10, 10));
            drawBoard(player3Board, p3GridSquares);
        }

        if(numberOfPlayers == 4){
            player4Board = new JPanel();
            p4GridSquares = new ScalingImage [10][10];
            player4Board.setLayout(new GridLayout(10, 10));
            drawBoard(player4Board, p4GridSquares);
        }



        // help button

        helpButton = new JButton("Help");

    }

    public void drawBoard(JPanel playerBoard, ScalingImage[][] gridSquares){
        ImageIcon temp = new ImageIcon("Images/snow_king.png");
        for (int row = 0; row<10; row++){
            for (int col = 0; col<10; col++){
                gridSquares[col][row] = new ScalingImage(temp.getImage());
                gridSquares[col][row].setBorder(BorderFactory.createBevelBorder(0));
                playerBoard.add(gridSquares[col][row]);
            }
        }
        playerBoard.setBorder(BorderFactory.createBevelBorder(0));
    }

    @Override
    public void sizing(int width, int height) {
        int offset = 20;
        int boardWidth = width / 5;
        int boardHeight = boardWidth;
        player1Board.setBounds(offset, offset, boardWidth, boardHeight);
        player2Board.setBounds(width-offset-boardWidth, offset, boardWidth, boardHeight);
        if(numberOfPlayers>=3) player3Board.setBounds(offset + boardWidth + offset, offset, boardWidth, boardHeight);
        if(numberOfPlayers==4) player4Board.setBounds(width-offset-boardWidth-offset-boardWidth, offset, boardWidth, boardHeight);

        int buttonHeight = 30;
        int buttonWidth = width / 10;
        helpButton.setBounds(offset, height-offset-buttonHeight, buttonWidth, buttonHeight);
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = game.getFrame().getLayeredPane();
        pane.add(player1Board, JLayeredPane.PALETTE_LAYER);
        pane.add(player2Board, JLayeredPane.PALETTE_LAYER);
        if(numberOfPlayers>=3) pane.add(player3Board, JLayeredPane.PALETTE_LAYER);
        if(numberOfPlayers==4) pane.add(player4Board, JLayeredPane.PALETTE_LAYER);
        pane.add(helpButton, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {

    }
}
