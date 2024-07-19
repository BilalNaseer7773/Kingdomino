import java.util.List;
import java.util.*;
import java.awt.*;

public class Facade {

    private final Game game;
    private final GameController controller;

    Facade(Game game, GameController controller) {
        this.game = game;
        this.controller = controller;
    }

    //DISPLAY METHODS

    // This method is temporary until we have dominos displaying in GUI  // GUI OUTPUT **
    // Note that the domino number is based on order in the shuffled
    // domino stack
    public void displayDominoes(int j, int k) {
        controller.displayCurrentDominoes(convertDominoRange(j, k));
    }

    public void displayNextDominoes(int j, int k) {
        controller.displayNextDominoes(convertDominoRange(j, k));
    }

    private Domino[] convertDominoRange(int j, int k) {
        Domino[] ds = new Domino[4];
        for (int i = j; i <= k; i++) { // Display the dominos from j to k
            //System.out.println(game.getDominos()[i]); // GUI OUTPUT**
            ds[i - j] = game.getDominos()[i];
        }
        return ds;
    }

    public void displayTurn(Player player) {
        //System.out.println("It's " + player.getName() + "'s turn!");
        if (player == null) {
            controller.setTurn(-1);
        } else {
            controller.setTurn(new ArrayList<Player>(List.of(game.getPlayers())).indexOf(player));
        }
    }

    // METHODS GAME CALLS

    public void askForDomino() {
        // gui will eventually call game.selectDomino(domino);
        controller.selectDomino();
    }

    // which of the terrain will be the terrain1
    public void askForTerrain1Selection(Domino domino) {
        // gui will eventually call game.doNextTurn2(selectedTerrain);
        controller.chooseTile(domino);
    }

    // where terrain1 is placed
    public void askForTerrain1Placement(ArrayList<Point> validPoints) {
        controller.placeTile(validPoints.toArray(Point[]::new));
    }

    // where terrain2 is placed
    public void askForTerrain2Placement(ArrayList<Point> validPoints) {
        controller.placeTile(validPoints.toArray(Point[]::new));
    }

    // METHODS GUI CALLS

    public void placeTile(Point coord) {
        if (game.getProgression() == 1) {
            game.doNextTurn3(coord);
        } else if (game.getProgression() == 2) {
            game.placeDomino(coord);
        }
    }

    public void selectDomino(Domino domino) {
        game.selectDomino(domino);
    }

    public void selectTerrain1(Terrain terrain) {
        game.doNextTurn2(terrain);
    }

}
