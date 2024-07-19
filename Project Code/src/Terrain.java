import java.io.Serializable;


public class Terrain implements Serializable {
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

    @Override
    public String toString() {
        return type.name() + "*".repeat(crowns);
    }

}
