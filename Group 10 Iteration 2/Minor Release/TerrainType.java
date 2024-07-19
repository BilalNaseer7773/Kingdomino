public enum TerrainType {
    
    FIELD("field"),
    FOREST("forest"),
    OCEAN("ocean"),
    PLAINS("plains"),
    MINES("mines"),
    DESERT("desert"),
    STARTING("starting"),
    EMPTY("empty");


    private final String name;

    TerrainType(String name){
        this.name = name;
    }
}
