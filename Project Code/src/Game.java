import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.*;

public class Game implements Serializable {

    private static final int NUMBER_OF_DOMINOES = 48;
    private transient Kingdomino program;
    private final int numberOfPlayers;
    private King currentKing; // The king whose turn it is
    private final Player[] players;
    private King[] kings;
    private int kingPointer;
    private final PriorityQueue<King> orderedKings;
    private int kingsPerPlayer;
    private Domino[] dominoes; // the deck of dominoes
    private transient Facade facade;
    private int dominoPointer;
    private int round;
    private ArrayList<Domino> availableDominoes;
    private int deckSize;

    private Terrain selected;
    private int progression;

    private Point saveCoord; // This exists solely for the loading of save games

    private boolean mightyDuel;
    private boolean harmony;
    private boolean middleKingdom;


    Game(Kingdomino program, int numberOfPlayers){
        this.program = program;
        this.numberOfPlayers = numberOfPlayers;
        players = new Player[numberOfPlayers];
        orderedKings = new PriorityQueue<>();
        this.round = 1; // initialize round to be first round upon construction

        initializeDominos();
    }

    public void playGame(){
        if(round==1){
            if(progression==0&&kingPointer==0){
                initializeKings();
                doFirstRound();
            } else {
                facade.displayTurn(currentKing.getOwner());
                facade.displayNextDominoes(getOrderedDominoSlice(0, 3));
                facade.redesignateDominoes(getUnavailableDominoes());
                switch (progression) {
                    case 0 -> doNextFirstTurn();
                    case 4 -> selectDomino(currentKing.getSelectedDomino());
                }
            }
        } else{
            facade.displayTurn(currentKing.getOwner());
            if(dominoPointer+3<deckSize){
                facade.displayDominoes(getOrderedDominoSlice(dominoPointer, dominoPointer+3));
                if(dominoPointer+7<deckSize) {
                    facade.displayNextDominoes(getOrderedDominoSlice(dominoPointer + 4, dominoPointer + 7));
                    facade.redesignateDominoes(getUnavailableDominoes());
                }
            }
            switch (progression) {
                case 0 -> doNextTurn1();
                case 1 -> doNextTurn2(selected);
                case 2 -> doNextTurn3(saveCoord);
                case 3 -> placeDomino(saveCoord);
                case 4 -> selectDomino(currentKing.getSelectedDomino());
            }
        }
    }



    public void doFirstRound(){
        Domino[] doms = getOrderedDominoSlice(dominoPointer, dominoPointer + 3);
        facade.displayNextDominoes(doms);
        availableDominoes = new ArrayList<>(List.of(doms));
        shuffleKings();
        kingPointer = 0;
        doNextFirstTurn();
    }


    public void selectDomino(Domino domino){
        progression = 4;
        availableDominoes.remove(domino);
        currentKing.setSelectedDomino(domino);
        addOrderedKing(currentKing); // add the current king to pqueue based on their selected domino
        endTurn();
    }

    private void endTurn(){
        if(kingPointer == numberOfPlayers*kingsPerPlayer-1) { // if this is the last player, move to the next round
            if(round==1){
                dominoPointer-=4;
            }
            doNextRound();
        }
        else {
            kingPointer ++; // increment because the turn is over
            if (round == 1) doNextFirstTurn(); // move to next turn
            else doNextTurn1();
        }
    }

    public void doNextRound(){
        kingPointer = 0;
        round ++;
        dominoPointer += 4;
        if(dominoPointer+3<deckSize){
            determineTurnOrder(); // add all players from PQ to players to determine turn order
            facade.displayDominoes(getOrderedDominoSlice(dominoPointer, dominoPointer + 3));
            if(dominoPointer+7<deckSize){
                Domino[] doms = getOrderedDominoSlice(dominoPointer + 4, dominoPointer + 7);
                facade.displayNextDominoes(doms);
                availableDominoes = new ArrayList<>(List.of(doms));
            } else{
                facade.cleanNextDominoes();
            }
            doNextTurn1();
        } else{
            endGame();
        }
    }

    // Turns in the first round
    public void doNextFirstTurn(){
        currentKing = kings[kingPointer];
        progression = 0;
        facade.displayTurn(currentKing.getOwner());
        currentKing.getOwner().takeTurnPartD(facade, getOrderedDominoSlice(dominoPointer, dominoPointer+3), harmony, middleKingdom);
    }


    // turns in all other rounds

    // part 1 of doNextTurn
    public void doNextTurn1(){
        progression = 0;
        currentKing = kings[kingPointer];
        currentKing.getOwner().selectDomino(currentKing.getSelectedDomino());
        facade.displayTurn(currentKing.getOwner());
        currentKing.getOwner().takeTurnPart1(facade, currentKing);
    }

    // part 2 of doNextTurn
    public void doNextTurn2(Terrain selectedTerrain){
        progression = 1;
        selected = selectedTerrain;
        ArrayList<Point> p = currentKing.getOwner().getKingdom().getLegalTerrain1Placements(selectedTerrain);
        currentKing.getOwner().takeTurnPart2(facade, p);
    }

    public void doNextTurn3(Point coord1){
        saveCoord = coord1;
        progression = 2;
        currentKing.getOwner().placeTile(selected, coord1);
        ArrayList<Point> p = currentKing.getOwner().getKingdom().getLegalTerrain2Placements(coord1);

        currentKing.getOwner().takeTurnPart3(facade, p);
    }

    public void placeDomino(Point coord2){
        saveCoord = coord2;
        progression = 3;
        currentKing.getOwner().placeTile(currentKing.getOwner().getSelectedDomino().getOpposite(selected), coord2);
        lastStepTurn();
    }

    public void dominoBreak(){
        progression = 3;
        lastStepTurn();
    }

    private void lastStepTurn(){
        if(dominoPointer+7<=deckSize){
            currentKing.getOwner().takeTurnPartD(facade, availableDominoes.toArray(Domino[]::new), harmony, middleKingdom);
        } else{
            endTurn();
        }
    }

    private void endGame(){
        int[] scores = new int[numberOfPlayers];
        String[] names = new String[numberOfPlayers];
        Sprite[] sprites = new Sprite[numberOfPlayers];
        int winner = 0;
        int i;
        int maxScore = 0;
        for (i = 0; i<numberOfPlayers; i++){
            scores[i] = players[i].calculateScore(harmony, middleKingdom);
            if(scores[i]>maxScore){
                winner = i;
                maxScore = scores[i];
            }
            names[i] = players[i].getName();
            sprites[i] = players[i].getSprite();
        }
        program.endGame(new VictoryRecord(names, sprites, scores, winner));
    }

    public int getProgression() {
        return progression;
    }

    public void initializeDominos(){
        dominoes = Domino.values(); // initialize array with every domino in the domino enumerator
        shuffleDominos();
        dominoPointer = 0;
    }

    // Once players have been added to game, give them all the proper number of kings
    public void initializeKings(){
        if(numberOfPlayers == 2) {
            kingsPerPlayer = 2;
            deckSize = 24;
        }
        else {
            kingsPerPlayer = 1;
            deckSize = 48;
        }
        if(mightyDuel) deckSize = 48;
        for (int i = 0; i < numberOfPlayers; i++){
            players[i].generateKings(kingsPerPlayer);
        }

        kings = new King[numberOfPlayers*kingsPerPlayer];
        int kingsPointer = 0;

        for (int i = 0; i < numberOfPlayers; i++){
            for (int j = 0; j < kingsPerPlayer; j++, kingsPointer ++){
                kings[kingsPointer] = players[i].getKing(j);
            }
        }
    }

    // shuffles the Dominoes in the dominoes array
    public void shuffleDominos(){
        Random rand = new Random();
        for (int i = 0; i< NUMBER_OF_DOMINOES; i++){
            int randInt = rand.nextInt(NUMBER_OF_DOMINOES); // get a random index
            Domino temp = dominoes[i]; // swap the current domino with another random domino
            dominoes[i] = dominoes[randInt];
            dominoes[randInt] = temp;
        }
    }

    public void determineTurnOrder(){
        for(int i = 0; i<numberOfPlayers*kingsPerPlayer; i++){
            //players[i] = orderedPlayers.poll(); // *** CHANGED
            kings[i] = orderedKings.poll(); // get owner of the king in pq
        }
    }

    private Domino[] getOrderedDominoSlice(int j, int k){
        Domino[] dom = new Domino[k-j+1];
        int i;
        for(i=j;i<=k;i++){
            dom[i-j] = dominoes[i];
        }
        Arrays.sort(dom, (o1, o2) -> {
            int d1 = o1.getNumber();
            int d2 = o2.getNumber();
            if(d1>d2){
                return 1;
            } else if (d1<d2){
                return -1;
            }
            return 0;
        });
        return dom;
    }

    // Shuffles the array of kings
    public void shuffleKings() {
        Random rand = new Random();
        for(int i = 0; i<kings.length; i++){
            int randInt = rand.nextInt(kings.length); // get a random index
            King temp = kings[i]; // swap current player with a random player
            kings[i] = kings[randInt];
            kings[randInt] = temp;
        }
    }

    // SETTERS AND GETTERS

    public Player[] getPlayers() {
        return players;
    }

    private King[] getUnavailableDominoes(){
        King[] array = new King[kingPointer];
        System.arraycopy(kings, 0, array, 0, kingPointer - 1 + 1);
        return array;
    }

    public void setGamemodes(boolean mightyDuel, boolean harmony, boolean middleKingdom){
        this.middleKingdom = middleKingdom;
        this.harmony = harmony;
        this.mightyDuel = mightyDuel;
    }

    public void addOrderedKing(King king){
        orderedKings.add(king);
    }

    public int getRound(){
        return round;
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


