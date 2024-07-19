import java.io.Serializable;
import java.awt.*;
import java.util.ArrayList;

public abstract class Player implements Serializable, Comparable<Player> {
    private final String name;
    private final Kingdom kingdom;
    private Domino selectedDomino;
    private final Sprite sprite;
    private final PlayerColor color;
    private King[] kings;

    Player(String name, Sprite sprite, PlayerColor color, int maxKingdomSize){
        this.name = name;
        this.color = color;
        kingdom = new Kingdom(this, maxKingdomSize);
        this.sprite = sprite;
    }

    public void selectDomino(Domino domino){
        selectedDomino = domino;
    }

    public void placeTile(Terrain terrain, Point coord){
        kingdom.setTerrain(terrain, coord);
    }

    public abstract void takeTurnPart1(Facade facade, King king);
    public abstract void takeTurnPart2(Facade facade, ArrayList<Point> points);
    public abstract void takeTurnPart3(Facade facade, ArrayList<Point> points);
    public abstract void takeTurnPartD(Facade facade, Domino[] choices, boolean harmony, boolean middleKingdom);

    public void generateKings(int num){
        kings = new King[num];
        for(int i = 0; i<num;i++){
            kings[i] = new King(this);
        }
    }

    public King getKing(int kingNum){
        return kings[kingNum];
    }

    public int calculateScore(boolean harmony, boolean middleKingdom){
        kingdom.initializeSearchArray();
        return kingdom.calculateScore(harmony, middleKingdom);
    }


    // GETTERS AND SETTERS

    public Sprite getSprite() {
        return sprite;
    }

    public String getName() {
        return name;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public Domino getSelectedDomino() {
        return selectedDomino;
    }

    @Override public int compareTo(Player p){
        if(p.selectedDomino.getNumber() > this.selectedDomino.getNumber()) return -1;
        else return 1;
    }

    public PlayerColor getColor() {
        return color;
    }
}
