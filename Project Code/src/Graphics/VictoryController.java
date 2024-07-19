import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

public class VictoryController implements MenuController{
    private final Image backGround;
    private final MainWindow target;
    private final VictoryRecord info;

    private ScalingImage back;
    private JLabel declaration;
    private VictoryBoxController[] losers;
    private VictoryBoxController winner;
    private JButton end;

    public VictoryController(MainWindow target, Image backGround, VictoryRecord info) {
        this.backGround = backGround;
        this.target = target;
        this.info = info;
    }

    @Override
    public void setup() {
        back = new ScalingImage(backGround);
        declaration = new JLabel(info.playerNames()[info.winnerIndex()] + " is Victorious!", JLabel.CENTER);
        end = new JButton("Return to Menu");
        end.addActionListener(e->target.toMenu(MainWindow.START, false));
        end.setHorizontalAlignment(JButton.CENTER);
        losers = IntStream.range(0, info.playerNames().length)
                .filter(e-> e!=info.winnerIndex())
                .mapToObj(e->new VictoryBoxController(target.getFrame().getLayeredPane(), info.playerNames()[e], info.playerSprites()[e].getIcon(), info.playerScores()[e]))
                .toArray(VictoryBoxController[]::new);
        for (VictoryBoxController box : losers){
            box.setup();
        }
        winner = new VictoryBoxController(target.getFrame().getLayeredPane(), info.playerNames()[info.winnerIndex()], info.playerSprites()[info.winnerIndex()].getIcon(), info.playerScores()[info.winnerIndex()]);
        winner.setup();
    }

    @Override
    public void sizing(int width, int height) {
        back.setBounds(0,0, width, height);
        back.repaint();
        int losersWidth = width / 7;
        int losersY = 2* height / 3;
        int losersX = width / (losers.length+1);
        int losersHeight = height - losersY;
        int winnerX = width / 2;
        int winnerY = height / 20;
        int winnerWidth = width / 3;
        int winnerHeight = 4* height / 7;
        int i;
        VictoryBoxController box;
        for(i=0;i<losers.length;i++){
            box = losers[i];
            box.setCorner(new Point(losersX*(i+1) - losersWidth / 2, losersY));
            box.sizing(losersWidth,losersHeight);
        }
        winner.setCorner(new Point(winnerX - winnerWidth / 2, winnerY));
        winner.sizing(winnerWidth,winnerHeight);
        declaration.setBounds(width / 4, 0, width / 2, height/ 20);
        declaration.setFont(declaration.getFont().deriveFont((float)declaration.getFont().getSize()*2));
        declaration.repaint();
        end.setBounds(width / 30, width/30,width / 5,height / 15);
        end.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(back, JLayeredPane.DEFAULT_LAYER);
        pane.add(declaration, JLayeredPane.MODAL_LAYER);
        pane.add(end, JLayeredPane.MODAL_LAYER);
        for(VictoryBoxController box : losers){
            box.addComponents();
        }
        winner.addComponents();
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        declaration.setForeground(palette.getAccent(ColorPalette.DARK));
        for(VictoryBoxController box : losers){
            box.coloring(palette, theme);
        }
        winner.coloring(palette, theme);
    }

    @Override
    public void removeComponents() {

    }
}