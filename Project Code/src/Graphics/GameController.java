import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class GameController implements MenuController{

    private final MainWindow target;
    private Facade facade;

    private final Player[] players;
    private Player currentPlayer; // null if it is not presently anyone's turn
    private final Hashtable<Player, KingdomBoard> boardLookUp;

    private KingdomBoard[] boards;
    private JPanel sidePanel;
    private ScalingImage playerIcon;
    private JLabel playerName;
    private DominoBoard[] dominoes;
    private DominoBoard[] nextDominoes;
    private JLabel instructions;
    private JLabel warning;
    private JButton pause;

    private int width;
    private int height;

    GameController(MainWindow target, Game currentGame){
        this.target = target;
        this.facade = null;
        players = currentGame.getPlayers();
        boardLookUp = new Hashtable<>(players.length);
        currentPlayer = null;
    }

    @Override
    public void setup() {
        // Creates boards for each player
        boards = Arrays.stream(players).map(e->{
            KingdomBoard k = new KingdomBoard(e.getKingdom(), e.getColor());
            boardLookUp.put(e, k);
            return k;
        }).toArray(KingdomBoard[]::new);
        sidePanel = new JPanel();
        playerIcon = new ScalingImage(null);
        playerName = new JLabel();
        playerName.setHorizontalAlignment(JLabel.RIGHT);
        warning = new JLabel();
        instructions = new JLabel();
        pause = new JButton("Pause Game");
        pause.setHorizontalAlignment(JButton.CENTER);
        pause.addActionListener(e-> target.toMenu(MainWindow.PAUSE, true));
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

        if (currentPlayer==null) {
            int boardSize = ((Math.min(boardAreaWidth, boardAreaHeight)) - offset)/2;
            boardSize = boardSize - boardSize%boards[0].getTileNumberX();
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
            int focusBoardAreaWidth, focusBoardAreaHeight, otherAreaWidth, otherAreaHeight, focusBoardX, focusBoardY, focusBoardSize, otherBoardSize, otherBoardX, otherBoardY;
            otherAreaHeight = height / 4;
            focusBoardAreaHeight = height - otherAreaHeight;
            otherAreaWidth = width - sidebarWidth;
            focusBoardAreaWidth = width - sidebarWidth;
            focusBoardSize = Math.min(focusBoardAreaHeight - 2*offset, focusBoardAreaWidth - 2*offset);
            focusBoardX = focusBoardAreaWidth / 2 - focusBoardSize / 2;
            focusBoardY = focusBoardAreaHeight / 2 - focusBoardSize / 2;
            otherBoardSize = otherAreaHeight - 2*offset;
            otherBoardX = otherAreaWidth / players.length;
            otherBoardY = height - otherAreaHeight / 2 - otherBoardSize / 2;
            otherBoardSize = otherBoardSize - otherBoardSize%boards[0].getTileNumberX();
            focusBoardSize = focusBoardSize - focusBoardSize%boards[0].getTileNumberX();
            ArrayList<KingdomBoard> kdms = new ArrayList<>(List.of(boards));
            kdms.remove(getCurrentBoard());
            getCurrentBoard().setBounds(focusBoardX, focusBoardY, focusBoardSize, focusBoardSize);
            int i;
            for(i=0;i<kdms.size();i++){
                kdms.get(i).setBounds(otherBoardX*(i+1) - otherBoardSize / 2,otherBoardY ,otherBoardSize,otherBoardSize);
            }
        }
        for (BoardPanel b : boards){
            b.repaint();
        }
        int topSpace = height / 4;
        int boxOffset = sidebarWidth / 15;
        instructions.setBounds(sidebarX + boxOffset, topSpace, sidebarWidth - boxOffset*2, boxOffset);
        instructions.repaint();
        if(nextDominoes!=null || dominoes != null){
            int doms = nextDominoes!=null && nextDominoes.length!= 0 ? nextDominoes.length : dominoes.length;
            int dominoHeight = (height - topSpace - boxOffset*(doms+1))/doms;
            int dominoWidth = (sidebarWidth - boxOffset*4)/2;
            dominoHeight = Math.min(dominoHeight,dominoWidth/2);
            dominoWidth = dominoHeight*2;
            int i;
            for (i = 0; i < doms; i++) {
                int y = topSpace + boxOffset * (i + 1) + dominoHeight * i;
                if(dominoes!=null){
                    dominoes[i].setBounds(sidebarX + boxOffset, y, dominoWidth, dominoHeight);
                    dominoes[i].repaint();
                }
                if(nextDominoes!=null&&nextDominoes.length!=0) {
                    nextDominoes[i].setBounds(width - boxOffset - dominoWidth, y, dominoWidth, dominoHeight);
                    nextDominoes[i].repaint();
                }
            }
        }
        int iconSize = (topSpace - boxOffset) / 8 * 6;
        int textSize = (topSpace - boxOffset) / 8 * 2;
        int playerInfoX = width - boxOffset - iconSize;

        warning.setBounds(sidebarX+boxOffset,boxOffset + iconSize,sidebarX-2*boxOffset-2*iconSize,textSize);
        warning.repaint();
        pause.setBounds(sidebarX+boxOffset, boxOffset, sidebarWidth - iconSize - boxOffset*3, textSize*2);
        pause.repaint();
        playerName.setBounds(playerInfoX-iconSize, boxOffset + iconSize, iconSize*2, textSize);
        playerName.repaint();
        playerIcon.setBounds(playerInfoX,boxOffset,iconSize,iconSize);
        playerIcon.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        for(BoardPanel b : boards){
            pane.add(b, JLayeredPane.PALETTE_LAYER);
        }
        if(dominoes!=null){
            for(DominoBoard d : dominoes){
                pane.add(d, JLayeredPane.MODAL_LAYER);
            }
        }
        if(nextDominoes!=null){
            for(DominoBoard d : nextDominoes){
                pane.add(d, JLayeredPane.MODAL_LAYER);
            }
        }
        pane.add(sidePanel, JLayeredPane.PALETTE_LAYER);
        pane.add(playerName, JLayeredPane.MODAL_LAYER);
        pane.add(playerIcon, JLayeredPane.MODAL_LAYER);
        pane.add(instructions, JLayeredPane.MODAL_LAYER);
        pane.add(warning, JLayeredPane.MODAL_LAYER);
        pane.add(pause, JLayeredPane.MODAL_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        if(currentPlayer!=null){
            playerName.setForeground(currentPlayer.getColor().getColor());
            instructions.setForeground(currentPlayer.getColor().getColor());
        }
        if(dominoes!=null){
            for(DominoBoard d : dominoes){
                d.coloring(palette, theme);
            }
        }
        if(nextDominoes!=null){
            for(DominoBoard d : nextDominoes){
                d.coloring(palette, theme);
            }
        }
        if(boards!=null){
            for(KingdomBoard b : boards){
                b.coloring(palette, theme);
            }
        }
    }

    public void setTurn(Player currentPlayer){
        this.currentPlayer = currentPlayer;
        playerIcon.setImage(currentPlayer.getSprite().getIcon());
        playerName.setText(currentPlayer.getName()+"'s turn");
        playerName.setForeground(currentPlayer.getColor().getColor());
        instructions.setForeground(currentPlayer.getColor().getColor());
        sizing(width, height);
    }

    public void placeTile(Point[] validPoints){
        ActionListener listen = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCurrentBoard().removeActionListener(this);
                facade.placeTile(getCurrentBoard().getSelection());
            }
        };
        if(currentPlayer!=null){
            getCurrentBoard().addActionListener(listen);
        }
        instructions.setText("Select where to place the tile");
        getCurrentBoard().displayOptions(Arrays.stream(validPoints).map(e->new Rectangle(e, new Dimension(1,1))).toArray(Rectangle[]::new));
        sizing(width, height);
    }

    public void setFacade(Facade facade) {
        this.facade = facade;
    }

    public void chooseTile(Domino domino, boolean tileL, boolean tileR){
        for(DominoBoard d : dominoes){
            if(d.getDomino()==domino){
                instructions.setText("Choose a tile to place");
                instructions.setHorizontalAlignment(JLabel.LEFT);
                if(!tileL&&!tileR){
                    d.breakDomino();
                    facade.dominoBreak();
                    warning.setText("No valid domino placements.");
                } else {
                    ArrayList<Rectangle> r = new ArrayList<>(2);
                    if(tileL){
                        r.add(new Rectangle(0,0,1,1));
                    }
                    if(tileR){
                        r.add(new Rectangle(1,0,1,1));
                    }
                    d.displayOptions(r.toArray(Rectangle[]::new));
                    d.addActionListener(e-> facade.selectTerrain1(d.getSelectedTerrain()));
                }
                sizing(width,height);
                break;
            }
        }
    }

    public void displayCurrentDominoes(Domino[] dominoes){
        JLayeredPane pane = target.getFrame().getLayeredPane();
        if(this.dominoes!=null){
            for(BoardPanel b : this.dominoes){
                pane.remove(b);
            }
        }
        this.dominoes = Arrays.stream(dominoes).map(DominoBoard::new).toArray(DominoBoard[]::new);
        for(BoardPanel b : this.dominoes){
            pane.add(b, JLayeredPane.MODAL_LAYER);
        }
        sizing(width,height);
    }

    public void displayNextDominoes(Domino[] dominoes){
        JLayeredPane pane = target.getFrame().getLayeredPane();
        if(nextDominoes!=null){
            for(BoardPanel b : nextDominoes){
                pane.remove(b);
            }
        }
        nextDominoes = Arrays.stream(dominoes).map(DominoBoard::new).toArray(DominoBoard[]::new);
        for(BoardPanel b : nextDominoes){
            pane.add(b, JLayeredPane.MODAL_LAYER);
        }
        sizing(width,height);
    }

    public void removeNextDominoes(){
        for(DominoBoard d : nextDominoes) {
            target.getFrame().getLayeredPane().remove(d);
        }
        nextDominoes = new DominoBoard[0];
        sizing(width,height);
        target.getFrame().repaint();
    }

    public void selectDomino(){
        ActionListener listen = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DominoBoard d = (DominoBoard) e.getSource();
                markDomino(d, currentPlayer.getSprite());
                for(BoardPanel p : nextDominoes){
                    p.removeActionListener(this);
                    p.clearOptions();
                }
                warning.setText("");
                facade.selectDomino(d.getDomino());
            }
        };
        for(DominoBoard d : nextDominoes){
            if(d.getSprite()==null){
                Rectangle[] r = {new Rectangle(0,0,2,1)};
                d.displayOptions(r);
                d.repaint();
                d.addActionListener(listen);
            }
        }
        instructions.setText("Choose a domino to place next turn");
        instructions.setHorizontalAlignment(JLabel.RIGHT);
        sizing(width, height);
    }

    private void markDomino(DominoBoard d, Sprite sprite){
        d.setSprite(sprite.getSprite());
        d.repaint();
    }

    public void redesignateDominoes(King[] kings){
        for(King k : kings){
            for (DominoBoard db : nextDominoes){
                if(db.getDomino()==k.getSelectedDomino()){
                    markDomino(db, k.getOwner().getSprite());
                }
            }
        }
    }

    public void aiSelectDomino(Domino domino){
        for(DominoBoard d : nextDominoes){
            if(d.getDomino()==domino){
                markDomino(d, currentPlayer.getSprite());
                break;
            }
        }
        warning.setText("");
    }

    private KingdomBoard getCurrentBoard(){
        return boardLookUp.get(currentPlayer);
    }

    public void breakDomino(Domino domino){
        for(DominoBoard d : dominoes){
            if(d.getDomino()==domino){
                d.breakDomino();
            }
        }
    }

    @Override
    public void removeComponents() {

    }
}