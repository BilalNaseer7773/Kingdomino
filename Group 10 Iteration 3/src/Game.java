import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.*;

public class Game implements Serializable {

    private static final int NUMBER_OF_DOMINOS = 48;
    private transient Kingdomino program;
    private int numberOfPlayers;
    private Player currentPlayer; // The player whose turn it is
    private Player[] players;
    private int playerPointer;
    private PriorityQueue<Player> orderedPlayers; // Priority Queue of players. Players are compared based on integer value of selected domino
    private Domino[] dominos; // the deck of dominoes
    private Facade facade;
    private int dominoPointer;
    private int round;

    private Terrain selected;
    private int progression;


    Game(Kingdomino program, int numberOfPlayers){
        this.program = program;
        this.numberOfPlayers = numberOfPlayers;
        players = new Player[numberOfPlayers];
        orderedPlayers = new PriorityQueue<Player>();
        this.round = 1; // initialize round to be first round upon construction

        initializeDominos();
    }

    // NEW METHODS
    public void playGame(){
        if(round==1){
            doFirstRound();
        } else{
            switch(progression){
                case 0:
                    doNextTurn1();
                break;
                case 1:
                    doNextTurn2(selected);
                break;
                // To Do: figure out how the rest of this will work loading from a save
            }
        }
    }



    public void doFirstRound(){
        facade.displayNextDominoes(dominoPointer, dominoPointer + 3);
        shufflePlayers();
        playerPointer = 0;
        doNextFirstTurn();
    }


    public void selectDomino(Domino domino){
        System.out.println("part D");
        currentPlayer.selectDomino(domino);
        addOrderedPlayer(currentPlayer); // add the current player to pqueue based on their selected domino
        if(playerPointer == numberOfPlayers-1) { // if this is the last player, move to the next round
            doNextRound();
        }
        else {
            playerPointer ++; // increment because the turn is over
            if (round == 1) doNextFirstTurn(); // move to next turn
            else doNextTurn1();
        }
    }

    public void doNextRound(){
        playerPointer = 0;
        round ++;
        clearPlayers(); // clear players array
        addPlayersFromPQ(); // add all players from PQ to players to determine turn order
        facade.displayDominoes(dominoPointer, dominoPointer + 3);
        facade.displayNextDominoes(dominoPointer + 4, dominoPointer + 7);
        doNextTurn1();
    }

    // Turns in the first round
    public void doNextFirstTurn(){
        System.out.println("part F");
        currentPlayer = players[playerPointer];
        facade.displayTurn(currentPlayer);
        facade.askForDomino();
    }


    // turns in all other rounds

    // part 1 of doNextTurn
    public void doNextTurn1(){
        System.out.println("part1");
        progression = 0;
        currentPlayer = players[playerPointer];
        facade.displayTurn(currentPlayer);
        facade.askForTerrain1Selection(currentPlayer.getSelectedDomino());
    }

    // part 2 of doNextTurn
    public void doNextTurn2(Terrain selectedTerrain){
        System.out.println("part2");
        progression = 1;
        selected = selectedTerrain;
        ArrayList<Point> p = currentPlayer.getKingdom().getLegalTerrain1Placements(selectedTerrain);
        facade.askForTerrain1Placement(p);
    }

    public void doNextTurn3(Point coord1){
        System.out.println("part3");
        progression = 2;
        currentPlayer.getKingdom().setTerrain(selected, coord1);
        ArrayList<Point> p = currentPlayer.getKingdom().getLegalTerrain2Placements(coord1);

        facade.askForTerrain2Placement(p);
    }

    public void placeDomino(Point coord2){
        System.out.println("part4");
        progression = 3;
        currentPlayer.getKingdom().setTerrain(currentPlayer.getSelectedDomino().getOpposite(selected), coord2);
        dominoPointer += 4;
        facade.askForDomino();
    }

    public int getProgression() {
        return progression;
    }

    public void initializeDominos(){
        dominos = Domino.values(); // initialize array with every domino in the domino enumerator
        shuffleDominos();
        dominoPointer = 0;
    }

    // shuffles the Dominoes in the dominoes array
    public void shuffleDominos(){
        Random rand = new Random();
        for (int i = 0; i<NUMBER_OF_DOMINOS; i++){
            int randInt = rand.nextInt(NUMBER_OF_DOMINOS); // get a random index
            Domino temp = dominos[i]; // swap the current domino with another random domino
            dominos[i] = dominos[randInt];
            dominos[randInt] = temp;            
        }
    }

    public void addPlayersFromPQ(){
        for(int i = 0; i<numberOfPlayers; i++){
            players[i] = orderedPlayers.poll();
        }
    }

    public void clearPlayers(){
        players = new Player[numberOfPlayers]; // clear array
    }

    public void clearOrderedPlayers(){
        for(int i = 0; i<numberOfPlayers; i++){
            orderedPlayers.remove(players[i]);
        }
    }

    // Shuffles the array of players
    public void shufflePlayers() {
        Random rand = new Random();
        for(int i = 0; i<numberOfPlayers; i++){
            int randInt = rand.nextInt(numberOfPlayers); // get a random index
            Player temp = players[i]; // swap current player with a random player
            players[i] = players[randInt];
            players[randInt] = temp;
        }
    }

    // SETTERS AND GETTERS

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player){
        currentPlayer = player;    
    }
    

    public Player[] getPlayers() {
        return players;
    }

    public PriorityQueue<Player> getOrderedPlayers(){
        return orderedPlayers;
    }

    public void addOrderedPlayer(Player player){
        orderedPlayers.add(player);
    }

    public int getRound(){
        return round;
    }

    public void setRound(int round){
        this.round = round;
    }

    public Domino[] getDominos(){
        return dominos;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    public void setProgram(Kingdomino program) {
        this.program = program;
    }

    public void setPlayer(int index, Player player){
        players[index] = player;
    }

    public void setFacade(Facade facade){
        this.facade = facade;
    }

    @Override
    public String toString() {
        return "Game{" +
                "program=" + program +
                ", numberOfPlayers=" + numberOfPlayers +
                ", players=" + Arrays.toString(players) +
                ", round=" + round +
                '}';
    }

}


