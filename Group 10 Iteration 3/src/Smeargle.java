import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Smeargle {
    public static void main(String[] args){
        Nerd nerd = new Nerd();
        JFrame frame = new JFrame("Test");
        frame.setPreferredSize(new Dimension(1200, 700));
        Kingdom king = new Kingdom(new HumanPlayer("yo", Sprite.OWL_KING, PlayerColor.BLUE));
        Point[] btns = {new Point(1,4), new Point(3,6)};
        int i,j;
        Random rand = new Random();
        for(i=0;i<9;i++){
            for(j=0;j<9;j++){
                king.getTerrainSquares()[i][j] = new Terrain(TerrainType.values()[rand.nextInt(TerrainType.values().length-1)], 0);
            }
        }
        BoardPanel brd = new BoardPanel(king, PlayerColor.BLUE);
        brd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("selection")){
                    Point p = brd.getSelection();
                    System.out.println("x: " + p.x + " y: " + p.y);
                }
                nerd.leaveZone();
            }
        });
        frame.add(brd);
        brd.repaint();
        brd.displayOptions(btns);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.repaint();
        frame.pack();
        frame.setVisible(true);
        brd.setBounds(0,0, frame.getWidth(), frame.getHeight());


        nerd.waitZone();
    }

    public static class Nerd {
        public Nerd() {
        }

        public synchronized void waitZone(){
            try {
                wait();
                System.out.println("success");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public synchronized void leaveZone(){
            notify();
        }
    }
}
