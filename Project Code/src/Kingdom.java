import java.io.Serializable;
import java.awt.*;
import java.util.*;

public class Kingdom implements Serializable {
    private Terrain[][] terrainSquares; // an array of the terrain in the kingdom
    private final Player kingdomOwner; // the player that owns this kingdom
    private Boolean[][] searchedArray;
    private Boolean[][] searchedArray2;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    private final int maxSize;

    Kingdom(Player kingdomOwner, int maxSize){
        this.kingdomOwner = kingdomOwner;
        initializeTerrain();
        this.maxSize = maxSize;
    }

    public Terrain[][] getTerrainSquares() {
        return terrainSquares;
    }


    // Methods for Score Calculation


    public void initializeSearchArray(){
        searchedArray = new Boolean[9][9];
        searchedArray2 = new Boolean[9][9];
        for (int i = 0; i<9; i++)
            for(int j = 0; j<9; j++){
                if(terrainSquares[i][j].getType().equals(TerrainType.EMPTY) || terrainSquares[i][j].getType().equals(TerrainType.START)) {searchedArray[i][j] = true; searchedArray2[i][j] = true;}
                else {searchedArray[i][j] = false; searchedArray2[i][j] = false;}
            }
    }

    public int calculateScore(boolean harmony, boolean middleKingdom){
        int score = 0;
        for (int y = 0; y<9; y++)
            for(int x = 0; x<9; x++){
                if(!searchedArray[x][y]){
                    Point startPoint = new Point(x,y);
                    TerrainType searchTerrain = terrainSquares[x][y].getType();
                    int numberOfTiles = getNumberOfConnectedTiles(startPoint, searchTerrain);
                    int numberOfCrowns = getNumberOfCrowns(startPoint, searchTerrain);
                    score += numberOfTiles*numberOfCrowns; // The score of a connected section is its tiles multiplied by its crowns
                    //System.out.println("Tiles: "+ numberOfTiles + " Crowns: " + numberOfCrowns + " Score: " + score);
                }  
            }
        if(middleKingdom){
            if((maxX-minX)/2+minX==4&&(maxY-minY)/2+minY==4){
                score+=10;
            }
        }
        if(harmony){
            boolean full = true;
            for(int i = minX; i<=maxX; i++){
                for(int j = minY; j<=maxY;j++){
                    if(terrainSquares[i][j].getType().equals(TerrainType.EMPTY)){
                        full=false;
                        break;
                    }
                }
            }
            if(full){
                score+=5;
            }
        }
        return score;
    }

    public int getNumberOfConnectedTiles(Point startPoint, TerrainType searchTerrain){
        if(searchedArray[startPoint.x][startPoint.y]){
            return 0;
        } else{
            ArrayList<Point> pointsToSearch = getPointsToSearch(startPoint, searchTerrain);

            int x = (int)startPoint.getX();
            int y = (int)startPoint.getY();
            searchedArray[x][y] = true; // searched this point

            if(pointsToSearch.size() == 0){
                return 1;
            }

            else{
                int numberOfTiles = 1;
                for(Point searchPoint: pointsToSearch){
                    numberOfTiles += getNumberOfConnectedTiles(searchPoint, searchTerrain);
                }
                return numberOfTiles;
            }
        }
    }

    public int getNumberOfCrowns(Point startPoint, TerrainType searchTerrain){
        if(searchedArray2[startPoint.x][startPoint.y]){
            return 0;
        } else{
            ArrayList<Point> pointsToSearch = getPointsToSearchCrown(startPoint, searchTerrain);

            int x = (int)startPoint.getX();
            int y = (int)startPoint.getY();
            searchedArray2[x][y] = true; // searched this point

            if(pointsToSearch.size() == 0){

                return terrainSquares[x][y].getCrowns();
            }

            else{
                int numberOfCrowns = terrainSquares[x][y].getCrowns();
                for(Point searchPoint: pointsToSearch){
                    numberOfCrowns += getNumberOfCrowns(searchPoint, searchTerrain);
                }
                return numberOfCrowns;
            }
        }
    }

    public ArrayList<Point> getPointsToSearch(Point point, TerrainType searchTerrain){
        ArrayList<Point> points = new ArrayList<>();
        int x = (int)point.getX();
        int y = (int)point.getY();
        Point up = new Point(x,y-1);
        Point down = new Point(x, y+1);
        Point left = new Point(x-1, y);
        Point right = new Point(x+1, y);

        if (y-1 >= 0 && terrainSquares[x][y-1].getType().equals(searchTerrain) && !searchedArray[x][y-1]) // If its the correct terrain type and hasn't been searched
            points.add(up);
        
        if (y+1 < terrainSquares.length && terrainSquares[x][y+1].getType().equals(searchTerrain) && !searchedArray[x][y+1])
            points.add(down);
            
        if (x-1 >= 0 && terrainSquares[x-1][y].getType().equals(searchTerrain) && !searchedArray[x-1][y])
            points.add(left);

        if (x+1 < terrainSquares.length && terrainSquares[x+1][y].getType().equals(searchTerrain) && !searchedArray[x+1][y])
            points.add(right);

        return points;
    } 

    public ArrayList<Point> getPointsToSearchCrown(Point point, TerrainType searchTerrain){
        ArrayList<Point> points = new ArrayList<>();
        int x = (int)point.getX();
        int y = (int)point.getY();
        Point up = new Point(x,y-1);
        Point down = new Point(x, y+1);
        Point left = new Point(x-1, y);
        Point right = new Point(x+1, y);

        if (y-1 >= 0 && terrainSquares[x][y-1].getType().equals(searchTerrain) && !searchedArray2[x][y-1]) // If its the correct terrain type and hasn't been searched
            points.add(up);
        
        if (y+1 < terrainSquares.length && terrainSquares[x][y+1].getType().equals(searchTerrain) && !searchedArray2[x][y+1])
            points.add(down);
            
        if (x-1 >= 0 && terrainSquares[x-1][y].getType().equals(searchTerrain) && !searchedArray2[x-1][y])
            points.add(left);

        if (x+1 < terrainSquares.length && terrainSquares[x+1][y].getType().equals(searchTerrain) && !searchedArray2[x+1][y])
            points.add(right);

        return points;
    } 


    // Other Methods

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
                if(getLegalTerrain2Placements(point).size() > 0 && terrainSquares[x][y].getType().equals(TerrainType.EMPTY)){ // check if there is a valid place to put second terrain and the current point is empty
                    if (isCompatible(terrainToBePlaced, up) || isCompatible(terrainToBePlaced, left) // check if the terrain is compatible with a terrain next to it and that it isn't OOB
                    || isCompatible(terrainToBePlaced, right) || isCompatible(terrainToBePlaced, down) && !(isOOB(point)) )
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
        if(x > maxX && (x-minX >= maxSize)) return true;
        if(x < minX && (maxX - x >= maxSize)) return true;
        if(y > maxY && (y-minY >= maxSize)) return true;
        if(y < minY && (maxY - y >= maxSize)) return true;
        return false;
    }

    public int getMaxSize() {
        return maxSize;
    }

    // checks to see if two pieces of terrain can be placed by each other
    public boolean isCompatible(Terrain terrain1, Terrain terrain2){
        if (terrain1.type.equals(terrain2.type) || terrain1.type.equals(TerrainType.START) || terrain2.type.equals(TerrainType.START)) return true;
        else return false;
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
        String output = "";
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

    public void setMaxX(int x) {
        maxX = x;
    }

    public void setMaxY(int y) {
        maxY = y;
    }

    public void setMinX(int x) {
        minX = x;
    }

    public void setMinY(int y) {
        minY = y;
    }

}
