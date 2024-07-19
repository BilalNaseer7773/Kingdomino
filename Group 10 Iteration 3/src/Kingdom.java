import java.io.Serializable;
import java.awt.*;
import java.util.*;

public class Kingdom implements Serializable {
    private Terrain[][] terrainSquares; // an array of the terrain in the kingdom
    private final Player kingdomOwner; // the player that owns this kingdom

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    Kingdom(Player kingdomOwner){
        this.kingdomOwner = kingdomOwner;
        initializeTerrain();
    }

    public Terrain[][] getTerrainSquares() {
        return terrainSquares;
    }

    // this method initializes the kingdom as a 9x9 array of empty terrain
    public void initializeTerrain(){
        terrainSquares = new Terrain[9][9];
        for(int x=0; x<9; x++)
            for(int y=0; y<9; y++){
                if(x==4 && y==4) terrainSquares[x][y] = new Terrain(TerrainType.START, 0);
                else terrainSquares[x][y] = new Terrain(TerrainType.EMPTY, 0);  // NOTE should we have empty squares here or leave NULL?
            }
        maxX = 4; minX = 4;
        maxY = 4; minY = 4;
    }


    // returns an array of legal points to place a given terrain1 tile of domino
    public ArrayList<Point> getLegalTerrain1Placements(Terrain terrainToBePlaced){
        ArrayList<Point> points = new ArrayList<>();
        for(int x=0; x<9; x++)
            for(int y=0; y<9; y++){
                Point point = new Point(x,y);
                Terrain up = y==0 ? terrainSquares[x][y]: terrainSquares[x][y-1];      // I think this will usually work but maybe not always? Will need bugtesting
                Terrain left = x==0 ? terrainSquares[x][y]: terrainSquares[x-1][y];
                Terrain right = x==8 ? terrainSquares[x][y]: terrainSquares[x+1][y];
                Terrain down = y==8 ? terrainSquares[x][y]: terrainSquares[x][y+1];
                if(getLegalTerrain2Placements(point).size() > 0){ // check if there is a valid place to put second terrain
                    if (isCompatible(terrainToBePlaced, up) || isCompatible(terrainToBePlaced, left) // check if the terrain is compatible with a terrain next to it and that it isn't OOB
                    || isCompatible(terrainToBePlaced, right) || isCompatible(terrainToBePlaced, down) && !(isOOB(point)))
                    points.add(new Point(x,y));
                }
                
            }
        return points;
    }

    public ArrayList<Point> getLegalTerrain2Placements(Point terrain1Coord){
        ArrayList<Point> points = new ArrayList<>();
        int x = (int)terrain1Coord.getX();
        int y = (int)terrain1Coord.getY();
        
        // check right
        if(x+1<=8){
            Point rPoint = new Point(x+1,y);
            if(isLegalTerrain2(terrainSquares[x+1][y]) && !(isOOB(rPoint))) points.add(rPoint);
        }
        // check left
        if(x-1>=0){
            Point lPoint = new Point(x-1,y);
            if(isLegalTerrain2(terrainSquares[x-1][y]) && !(isOOB(lPoint))) points.add(lPoint);
        }
        // check down
        if(y+1<=8){
            Point dPoint = new Point(x,y+1);
            if(isLegalTerrain2(terrainSquares[x][y+1]) && !(isOOB(dPoint))) points.add(dPoint);
        }
        // check up
        if(y-1>=0){
            Point uPoint = new Point(x,y-1);
            if(isLegalTerrain2(terrainSquares[x][y-1]) && !(isOOB(uPoint))) points.add(uPoint);
        }

        return points;
    }

    public boolean isLegalTerrain2(Terrain terrain){
        if(terrain.getType().equals(TerrainType.EMPTY)) return true;
        else return false;
    }


    // returns true if the given point is out of bounds in the current kingdom
    public boolean isOOB(Point coord){
        int x = (int)coord.getX();
        int y = (int)coord.getY();
        if(x > maxX && (x-minX >= 5)) return true;
        if(x < minX && (maxX - x >= 5)) return true;
        if(y > maxY && (y-minY >= 5)) return true;
        if(y < minY && (maxY - y >= 5)) return true;
        return false;
    }


    // checks to see if two pieces of terrain can be placed by each other
    public boolean isCompatible(Terrain terrain1, Terrain terrain2){
        if (terrain1.type.equals(terrain2.type) || terrain1.type.equals(TerrainType.START) || terrain2.type.equals(TerrainType.START)) return true;
        else return false;
    }

    // this method adds a domino to the kingdom
    public void placeDomino(Domino domino, Point coord1, Point coord2, Boolean isTerrain1PlacedFirst){
        if(isTerrain1PlacedFirst){
            setTerrain(domino.getTerrain1(), coord1);
            setTerrain(domino.getTerrain2(), coord2);
        }
        else{
            setTerrain(domino.getTerrain2(), coord1);
            setTerrain(domino.getTerrain1(), coord2);
        }
        
    }

    // this method changes the terrain at x,y to the given terrain
    public void setTerrain(Terrain terrain, Point coord){
        int x = (int) coord.getX();
        int y = (int) coord.getY();
        terrainSquares[x][y] = terrain;
        if (x>maxX) maxX = x;
        if (x<minX) minX = x;
        if (y>maxY) maxY = y;
        if (y<minY) minY = y;
    }

    @Override
    public String toString() {
        String output = new String();
        for(int y = 0; y<9; y++){
            for(int x = 0; x<9; x++){
                output += ("|| " + (terrainSquares[x][y] + " ||"));
            }
            output += "\n";
        }
        return output;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }
}
