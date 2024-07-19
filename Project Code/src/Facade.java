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
    public void displayDominoes(Domino[] dominos) {
        controller.displayCurrentDominoes(dominos);
    }

    public void displayNextDominoes(Domino[] dominos) {
        controller.displayNextDominoes(dominos);
    }

    public void displayTurn(Player player) {
        controller.setTurn(player);
    }

    // METHODS GAME CALLS

    public void askForDomino() {
        // gui will eventually call game.selectDomino(domino);
        controller.selectDomino();
    }

    // which of the terrain will be the terrain1
    public void askForTerrain1Selection(Domino domino, boolean tileL, boolean tileR) {
        // gui will eventually call game.doNextTurn2(selectedTerrain);
        controller.chooseTile(domino, tileL, tileR);
    }

    // where terrain1 is placed
    public void askForTerrain1Placement(ArrayList<Point> validPoints) {
        controller.placeTile(validPoints.toArray(Point[]::new));
    }

    // where terrain2 is placed
    public void askForTerrain2Placement(ArrayList<Point> validPoints) {
        controller.placeTile(validPoints.toArray(Point[]::new));
    }

    public void cleanNextDominoes(){
        controller.removeNextDominoes();
    }


    // COMPUTER PLAYER METHODS
    // Part 1
    public void displayTerrain1Selection(){
        
    }
    
    public void displayNoLegalPlacements(Domino domino){
        controller.breakDomino(domino);
    }

    // Part 2
    public void displayTerrain1Placement(){
    }

    // Part 3
    public void displayTerrain2Placement(){

    }

    // Part D
    public void displaySelectedDomino(Domino domino){
        controller.aiSelectDomino(domino);
    }

    public void redesignateDominoes(King[] kings){
        controller.redesignateDominoes(kings);
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

    public void dominoBreak(){
        game.dominoBreak();
    }
}
