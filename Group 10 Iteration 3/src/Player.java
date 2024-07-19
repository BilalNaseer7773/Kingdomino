import java.io.Serializable;
import java.awt.*;

public abstract class Player implements Serializable, Comparable<Player> {
    private String name;
    private Kingdom kingdom;
    private Domino selectedDomino;
    private Sprite sprite;
    private PlayerColor color;
    private King king;

    Player(String name, Sprite sprite, PlayerColor color){
        this.name = name;
        this.color = color;
        kingdom = new Kingdom(this);
        this.sprite = sprite;
        this.king = new King();
    }

    public void selectDomino(Domino domino){
        selectedDomino = domino;
    }

    public void placeDomino(Point coord1, Point coord2, Boolean isTerrain1PlacedFirst){
        kingdom.placeDomino(selectedDomino, coord1, coord2, isTerrain1PlacedFirst);
    }

    public void placeTile(Point coord, Terrain terrain){
        kingdom.setTerrain(terrain, coord);
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
