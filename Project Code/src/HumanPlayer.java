import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class HumanPlayer extends Player implements Serializable {
       
    HumanPlayer(String name, Sprite sprite, PlayerColor color, int maxBoardSize){
        super(name, sprite, color, maxBoardSize);
    }

    // Find legal selections for the selected domino and get terrain1Selection from facade
    @Override
    public void takeTurnPart1(Facade facade, King king) {
        boolean tileL = getKingdom().getLegalTerrain1Placements(king.getSelectedDomino().getTerrain1()).size() > 0;
        boolean tileR = getKingdom().getLegalTerrain1Placements(king.getSelectedDomino().getTerrain2()).size() > 0;
        facade.askForTerrain1Selection(king.getSelectedDomino(), tileL, tileR);
    }

    @Override
    public void takeTurnPart2(Facade facade, ArrayList<Point> points) {
        facade.askForTerrain1Placement(points);
    }

    @Override
    public void takeTurnPart3(Facade facade, ArrayList<Point> points) {
        facade.askForTerrain2Placement(points);
    }

    @Override
    public void takeTurnPartD(Facade facade, Domino[] selections, boolean harmony, boolean middleKingdom) {
        facade.askForDomino();
    }
}