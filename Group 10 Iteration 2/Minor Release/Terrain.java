public class Terrain {
    TerrainType type;
    int crowns;

    Terrain(TerrainType type, int crowns){
        this.type = type;
        this.crowns = crowns;
    }

    public TerrainType getType(){
        return type;
    }

    public int getCrowns(){
        return crowns;
    }

}
