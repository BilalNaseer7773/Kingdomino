public enum Domino {

    ONE(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.FIELD, 0)),
    TWO(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.FIELD, 0)),
    THREE(new Terrain(TerrainType.FOREST, 0), new Terrain(TerrainType.FOREST, 0)),
    FOUR(new Terrain(TerrainType.FOREST, 0), new Terrain(TerrainType.FOREST, 0)),
    FIVE(new Terrain(TerrainType.FOREST, 0), new Terrain(TerrainType.FOREST, 0)),
    SIX(new Terrain(TerrainType.FOREST, 0), new Terrain(TerrainType.FOREST, 0)),
    SEVEN(new Terrain(TerrainType.OCEAN, 0), new Terrain(TerrainType.OCEAN, 0)),
    EIGHT(new Terrain(TerrainType.OCEAN, 0), new Terrain(TerrainType.OCEAN, 0)),
    NINE(new Terrain(TerrainType.OCEAN, 0), new Terrain(TerrainType.OCEAN, 0)),
    TEN(new Terrain(TerrainType.PLAINS, 0), new Terrain(TerrainType.PLAINS, 0)),
    ELEVEN(new Terrain(TerrainType.PLAINS, 0), new Terrain(TerrainType.PLAINS, 0)),
    TWELVE(new Terrain(TerrainType.DESERT, 0), new Terrain(TerrainType.DESERT, 0)),
    THIRTEEN(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.FOREST, 0)),
    FOURTEEN(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.OCEAN, 0)),
    FIFTEEN(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.PLAINS, 0)),
    SIXTEEN(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.DESERT, 0)),
    SEVENTEEN(new Terrain(TerrainType.FOREST, 0), new Terrain(TerrainType.OCEAN, 0)),
    EIGHTEEN(new Terrain(TerrainType.FOREST, 0), new Terrain(TerrainType.PLAINS, 0)), 
    NINETEEN(new Terrain(TerrainType.FIELD, 1), new Terrain(TerrainType.FOREST, 0)),
    TWENTY(new Terrain(TerrainType.FIELD, 1), new Terrain(TerrainType.OCEAN, 0)),
    TWENTYONE(new Terrain(TerrainType.FIELD, 1), new Terrain(TerrainType.PLAINS, 0)),
    TWENTYTWO(new Terrain(TerrainType.FIELD, 1), new Terrain(TerrainType.DESERT, 0)),
    TWENTYTHREE(new Terrain(TerrainType.FIELD, 1), new Terrain(TerrainType.MINES, 0)),
    TWENTYFOUR(new Terrain(TerrainType.FOREST, 1), new Terrain(TerrainType.FIELD, 0)),
    TWENTYFIVE(new Terrain(TerrainType.FOREST, 1), new Terrain(TerrainType.FIELD, 0)),
    TWENTYSIX(new Terrain(TerrainType.FOREST, 1), new Terrain(TerrainType.FIELD, 0)),
    TWENTYSEVEN(new Terrain(TerrainType.FOREST, 1), new Terrain(TerrainType.FIELD, 0)),
    TWENTYEIGHT(new Terrain(TerrainType.FOREST, 1), new Terrain(TerrainType.OCEAN, 0)),
    TWENTYNINE(new Terrain(TerrainType.FOREST, 1), new Terrain(TerrainType.PLAINS, 0)),
    THIRTY(new Terrain(TerrainType.OCEAN, 1), new Terrain(TerrainType.FIELD, 0)),
    THIRTYONE(new Terrain(TerrainType.OCEAN, 1), new Terrain(TerrainType.FIELD, 0)),
    THIRTYTWO(new Terrain(TerrainType.OCEAN, 1), new Terrain(TerrainType.FOREST, 0)),
    THIRTYTHREE(new Terrain(TerrainType.OCEAN, 1), new Terrain(TerrainType.FOREST, 0)),
    THIRTYFOUR(new Terrain(TerrainType.OCEAN, 1), new Terrain(TerrainType.FOREST, 0)),
    THIRTYFIVE(new Terrain(TerrainType.OCEAN, 1), new Terrain(TerrainType.FOREST, 0)),
    THIRTYSIX(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.PLAINS, 1)),
    THIRTYSEVEN(new Terrain(TerrainType.OCEAN, 0), new Terrain(TerrainType.PLAINS, 1)),
    THIRTYEIGHT(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.DESERT, 1)),
    THIRTYNINE(new Terrain(TerrainType.PLAINS, 0), new Terrain(TerrainType.DESERT, 1)),
    FORTY(new Terrain(TerrainType.MINES, 1), new Terrain(TerrainType.FIELD, 0)),
    FORTYONE(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.PLAINS, 2)),
    FORTYTWO(new Terrain(TerrainType.OCEAN, 0), new Terrain(TerrainType.PLAINS, 2)),
    FORTYTHREE(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.DESERT, 2)),
    FORTYFOUR(new Terrain(TerrainType.PLAINS, 0), new Terrain(TerrainType.DESERT, 2)),
    FORTYFIVE(new Terrain(TerrainType.MINES, 2), new Terrain(TerrainType.FIELD, 0)),
    FORTYSIX(new Terrain(TerrainType.DESERT, 0), new Terrain(TerrainType.MINES, 2)),
    FORTYSEVEN(new Terrain(TerrainType.DESERT, 0), new Terrain(TerrainType.MINES, 2)),
    FORTYEIGHT(new Terrain(TerrainType.FIELD, 0), new Terrain(TerrainType.MINES, 3));

    private final Terrain terrain1;
    private final Terrain terrain2;


    Domino(Terrain terrain1, Terrain terrain2){
        this.terrain1 = terrain1;
        this.terrain2 = terrain2;
    }

    public int getNumber(){
        return ordinal() + 1;
    }

}
