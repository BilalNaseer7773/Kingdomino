public class Kingdom {
    private Terrain[][] terrainSquares; // an array of the terrain in the kingdom
    private Player kingdomOwner; // the player that owns this kingdom

    Kingdom(Player kingdomOwner){
        this.kingdomOwner = kingdomOwner;
        initializeTerrain();
    }

    // this method initializes the kingdom as a 9x9 array of empty terrain
    public void initializeTerrain(){
        terrainSquares = new Terrain[9][9];
        for(int x=0; x<9; x++)
            for(int y=0; y<9; y++){
                if(x==4 && y==4) terrainSquares[x][y] = new Terrain(TerrainType.STARTING, 0);
                else terrainSquares[x][y] = new Terrain(TerrainType.EMPTY, 0);  // NOTE should we have empty squares here or leave NULL?
            }
    }


    // this method checks the kingdom to confirm if it's valid (dominoes are within 5x5 grid)
    public boolean isLegal(){
        return true;
    }


    // this method changes the terrain at x,y to the given terrain
    public void setTerrain(Terrain terrain, int xCoord, int yCoord){
        terrainSquares[xCoord][yCoord] = terrain;
    }

}
