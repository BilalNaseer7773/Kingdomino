import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GameController implements MenuController{

    private final MainWindow target;
    private final Game currentGame;
    private Facade facade;

    private final Player[] players;
    private int turnIndex; // The index for the players array that represents whose turn it is. -1 for when it isn't anyone's turn

    private BoardPanel[] boards;
    private JPanel sidePanel;
    private BoardPanel[] dominoes;
    private BoardPanel[] nextDominoes;

    private int width;
    private int height;

    GameController(MainWindow target, Game currentGame){
        this.target = target;
        this.currentGame = currentGame;
        this.facade = null;
        players = currentGame.getPlayers();
    }

    @Override
    public void setup() {
        // Creates boards for each player
        boards = Arrays.stream(currentGame.getPlayers()).map(e->new BoardPanel(e.getKingdom(), e.getColor())).toArray(BoardPanel[]::new);
        sidePanel = new JPanel();
    }

    @Override
    public void sizing(int width, int height) {
        this.width = width;
        this.height = height;
        int sidebarWidth = width/3;
        int sidebarX = width - sidebarWidth;
        int offset =20;
        int boardAreaWidth = width - sidebarWidth - 2 * offset;
        int boardAreaHeight = height - 2 * offset;

        sidePanel.setBounds(sidebarX, 0, sidebarWidth, height);
        sidePanel.repaint();

        if (true) {
            int boardSize = ((Math.min(boardAreaWidth, boardAreaHeight)) - offset)/2;
            switch (players.length) {
                case 4 -> {
                    boards[0].setBounds(offset, offset, boardSize, boardSize);
                    boards[1].setBounds(offset * 2 + boardSize, offset, boardSize, boardSize);
                    boards[2].setBounds(offset, offset * 2 + boardSize, boardSize, boardSize);
                    boards[3].setBounds(offset * 2 + boardSize, offset * 2 + boardSize, boardSize, boardSize);
                }
                case 3 -> {
                    boards[0].setBounds(offset, offset, boardSize, boardSize);
                    boards[1].setBounds(offset * 2 + boardSize, offset, boardSize, boardSize);
                    boards[2].setBounds(offset * 3 / 2 + boardSize / 2, offset * 2 + boardSize, boardSize, boardSize);
                }
                default -> {
                    boards[0].setBounds(offset, offset + boardAreaHeight / 4, boardSize, boardSize);
                    boards[1].setBounds(offset * 2 + boardSize, offset + boardAreaHeight / 4, boardSize, boardSize);
                }
            }
        } else {
            boolean wide = boardAreaWidth > 2* boardAreaHeight;
            int i;
            for (i=0;i<players.length;i++) {

                if (i == turnIndex) {
                    //boards[i].setBounds(offset, offset, , );
                } else {
                    //boards[i].setBounds();
                }
            }
        }
        for (BoardPanel b : boards){
            b.repaint();
        }
        if(nextDominoes!=null&&nextDominoes.length!=0) {
            int i;
            int doms = nextDominoes.length;
            int topSpace = height / 5;
            int boxOffset = sidebarWidth / 15;
            int dominoHeight = (height - topSpace - boxOffset * (2*doms+1)) / (doms*2);
            int dominoWidth = dominoHeight * 2;
            int dominoAreaY = topSpace + boxOffset * (doms + 1) + dominoHeight * (doms);
            for (i = 0; i < doms; i++) {
                if(dominoes!=null){
                    dominoes[i].setBounds(sidebarX + boxOffset, topSpace + boxOffset * (i + 1) + dominoHeight * i, dominoWidth, dominoHeight);
                    dominoes[i].repaint();
                }
                nextDominoes[i].setBounds(sidebarX + boxOffset, dominoAreaY + boxOffset * i + dominoHeight * i, dominoWidth, dominoHeight);
                nextDominoes[i].repaint();
            }
        }
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        for(BoardPanel b : boards){
            pane.add(b, JLayeredPane.PALETTE_LAYER);
        }
        pane.add(sidePanel, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        sidePanel.setBackground(players[turnIndex].getColor().getColor());
    }

    public void setTurn(int playerIndex){
        turnIndex=playerIndex;
        sizing(width, height);
    }

    public void placeTile(Point[] validPoints){
        ActionListener listen = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("selection")){
                    getCurrentBoard().removeActionListener(this);
                    facade.placeTile(getCurrentBoard().getSelection());
                }
            }
        };
        if(turnIndex>=0){
            getCurrentBoard().addActionListener(listen);
        }
        getCurrentBoard().displayOptions(validPoints);
        sizing(width, height);
    }

    public void setFacade(Facade facade) {
        this.facade = facade;
    }

    public void chooseTile(Domino domino){
        for(BoardPanel d : dominoes){
            if(d.getDomino()==domino){
                Point[] p = {new Point(0,0), new Point(1,0)};
                d.displayOptions(p);
                d.addActionListener(e->{
                    if(e.getActionCommand().equals("selection")){
                        facade.selectTerrain1(d.getSelectedTerrain());
                    }
                });
            }
        }
        sizing(width,height);
    }

    public void displayCurrentDominoes(Domino[] dominoes){
        JLayeredPane pane = target.getFrame().getLayeredPane();
        if(this.dominoes!=null){
            for(BoardPanel b : this.dominoes){
                pane.remove(b);
            }
        }
        this.dominoes = Arrays.stream(dominoes).map(BoardPanel::new).toArray(BoardPanel[]::new);
        for(BoardPanel b : this.dominoes){
            pane.add(b, JLayeredPane.MODAL_LAYER);
        }
    }

    public void displayNextDominoes(Domino[] dominoes){
        JLayeredPane pane = target.getFrame().getLayeredPane();
        if(nextDominoes!=null){
            for(BoardPanel b : nextDominoes){
                pane.remove(b);
            }
        }
        nextDominoes = Arrays.stream(dominoes).map(BoardPanel::new).toArray(BoardPanel[]::new);
        for(BoardPanel b : nextDominoes){
            pane.add(b, JLayeredPane.MODAL_LAYER);
        }
        sizing(width,height);
    }

    public void selectDomino(){
        ActionListener listen = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("totality")){
                    BoardPanel d = (BoardPanel) e.getSource();
                    d.setEnabled(false);
                    d.setSprite(players[turnIndex].getSprite().getSprite());
                    d.repaint();
                    System.out.println("mouse");
                    for(BoardPanel p : nextDominoes){
                        p.removeActionListener(this);
                    }
                    facade.selectDomino(d.getDomino());
                }
            }
        };
        for(BoardPanel d : nextDominoes){
            if(d.getSprite()==null){
                d.setEnabled(true);
                d.repaint();
                d.addActionListener(listen);
            }
        }
        sizing(width, height);
    }

    private BoardPanel getCurrentBoard(){
        for(BoardPanel b : boards){
            if(players[turnIndex].getKingdom()==b.getKingdom()){
                return b;
            }
        }
        return null;
    }
}