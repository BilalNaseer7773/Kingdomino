import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player implements Serializable {
    
    private int difficulty; // 0 is EASY, 1 is HARD

    // Stored values for the hard ai
    private Random rand;
    private Terrain selectedTerrain1;
    private Point terrain1Placement;
    private Point terrain2Placement;

    private Domino currentDomino;

    private Boolean checkingTerrain2;

    private Boolean[][] traversalArray;

    ComputerPlayer(String name, Sprite sprite, PlayerColor color, int difficulty, int maxBoardSize){
        super(name, sprite, color, maxBoardSize);
        this.difficulty = difficulty;
        rand = new Random();
        if (difficulty == 1) initializeTraversalArray();
    }

    // Initialize traversal array with all false values for hard AI
    public void initializeTraversalArray(){
        traversalArray = new Boolean[9][9];
        for (int i = 0; i<9; i++)
            for(int j = 0; j<9; j++){
                traversalArray[i][j] = false;
            }
    }

    // Find legal selections for the selected domino and randomly get a selection
    @Override
    public void takeTurnPart1(Facade facade, King king) {
        boolean tileL = getKingdom().getLegalTerrain1Placements(getSelectedDomino().getTerrain1()).size() > 0;
        boolean tileR = getKingdom().getLegalTerrain1Placements(getSelectedDomino().getTerrain2()).size() > 0;
        boolean isTerrain1PlacedFirst;

        // EASY AI IMPLEMENTATION

        if (difficulty == 1){
            // if there is a choice, choose randomly
            if (tileL && tileR){
                int randint = rand.nextInt(2);
                if(randint == 1) isTerrain1PlacedFirst = true;
                else isTerrain1PlacedFirst = false;
            }

            // otherwise choose the only option, or break the domino if no options
            else{
                if(tileL) isTerrain1PlacedFirst = true;
                else if(tileR) isTerrain1PlacedFirst = false;
                else {
                    facade.displayNoLegalPlacements(king.getSelectedDomino()); // SKIP DOMINO **
                    facade.dominoBreak(); // Tell game to skip domino
                    return;
                }; 
            }

            // tell the game to select the terrain. Display selection on GUI
            Terrain selectedTerrain;
            if (isTerrain1PlacedFirst) selectedTerrain = getSelectedDomino().getTerrain1();
            else selectedTerrain = getSelectedDomino().getTerrain2();
            facade.displayTerrain1Selection(); // GUI **
            facade.selectTerrain1(selectedTerrain);
        }

        // HARD AI IMPLEMENTATION

        else{
            facade.displayTerrain1Selection();
            facade.selectTerrain1(selectedTerrain1);
        }
        
    }

    // Place the first terrain tile
    @Override
    public void takeTurnPart2(Facade facade, ArrayList<Point> points) {
        // EASY AI
        if (difficulty == 1){
            int randInd = rand.nextInt(points.size());
            Point selectedPoint = points.get(randInd);
            facade.displayTerrain1Placement(); // GUI **
            facade.placeTile(selectedPoint);
        }

        // HARD AI
        else{
            facade.displayTerrain1Placement();
            facade.placeTile(terrain1Placement);
        }
    }

    // Place the second terrain tile
    @Override
    public void takeTurnPart3(Facade facade, ArrayList<Point> points) {
        // EASY AI
        if (difficulty == 1){
            int randInd = rand.nextInt(points.size());
            Point selectedPoint = points.get(randInd);
            facade.displayTerrain2Placement(); // GUI **
            facade.placeTile(selectedPoint);
        }

        // HARD AI
        else{
            facade.displayTerrain2Placement();
            facade.placeTile(terrain2Placement);
        }
    }

    // Select a domino
    @Override
    public void takeTurnPartD(Facade facade, Domino[] selections, boolean harmony, boolean middleKingdom) {
        // EASY AI
        if (difficulty == 1){
            int randInd = rand.nextInt(selections.length);
            Domino selectedDomino = selections[randInd];
            facade.displaySelectedDomino(selectedDomino); // GUI **
            facade.selectDomino(selectedDomino);
        }

        // HARD AI
        else{
            Domino bestDomino = selections[0];
            int maxDominoVal = 0;
            for(Domino domino : selections){
                currentDomino = domino;
                if(maxValueDomino(domino, harmony, middleKingdom) > maxDominoVal) bestDomino = domino;
            }
            facade.displaySelectedDomino(bestDomino);
            facade.selectDomino(bestDomino);

        }


    }

    // method which returns maxValue of a domino
    public int maxValueDomino(Domino domino, boolean harmony, boolean middleKingdom){
        // Terrain1 is first terrain
        checkingTerrain2 = false;
        int mvt1 = maxValueTerrain1(domino.getTerrain1(), harmony, middleKingdom);
        int mvt2 = maxValueTerrain2(domino.getTerrain2(), harmony, middleKingdom);
        int maxValue = mvt1 + mvt2;
        selectedTerrain1 = domino.getTerrain1();

        // Terrain2 is first terrain
        checkingTerrain2 = true;
        mvt1 = maxValueTerrain1(domino.getTerrain2(), harmony, middleKingdom);
        mvt2 = maxValueTerrain2(domino.getTerrain1(), harmony, middleKingdom);
        int otherValue = mvt1 + mvt2;
        if(otherValue > maxValue){
            maxValue = otherValue;
            selectedTerrain1 = domino.getTerrain2();
        }

        return maxValue;
    }

    public int maxValueTerrain1(Terrain terrain, boolean harmony, boolean middleKingdom){

        int maxValue = 0;
        Kingdom currentKingdom = getKingdom();
        Kingdom tempKingdom = new Kingdom(this, currentKingdom.getMaxSize()); // make a copy of current kingdom state
        for (int i = 0; i<9; i++)
            for(int j = 0; j<9; j++){
                tempKingdom.setTerrain(currentKingdom.getTerrainSquares()[i][j], new Point(i,j));
            }
        
        tempKingdom.setMaxX(currentKingdom.getMaxX());
        tempKingdom.setMaxY(currentKingdom.getMaxY());
        tempKingdom.setMinX(currentKingdom.getMinX());
        tempKingdom.setMinY(currentKingdom.getMinY());

        ArrayList<Point> points = tempKingdom.getLegalTerrain1Placements(terrain);

        int value;
        for(Point point:points){
            tempKingdom.setTerrain(terrain, point);
            tempKingdom.initializeSearchArray();
            value = tempKingdom.calculateScore(harmony, middleKingdom);
            if(value>=maxValue) {
                maxValue = value;
                terrain1Placement = point;
            }
            tempKingdom.setTerrain(new Terrain(TerrainType.EMPTY, 0), point);
        }

        return maxValue;

    }

    public int maxValueTerrain2(Terrain terrain, boolean harmony, boolean middleKingdom){

        int maxValue = 0;

        Kingdom currentKingdom = getKingdom();

        Kingdom tempKingdom = new Kingdom(this, currentKingdom.getMaxSize()); // make a copy of current kingdom state
        for (int i = 0; i<9; i++)
            for(int j = 0; j<9; j++){
                tempKingdom.setTerrain(currentKingdom.getTerrainSquares()[i][j], new Point(i,j));
            }


        tempKingdom.setMaxX(currentKingdom.getMaxX());
        tempKingdom.setMaxY(currentKingdom.getMaxY());
        tempKingdom.setMinX(currentKingdom.getMinX());
        tempKingdom.setMinY(currentKingdom.getMinY());

        ArrayList<Point> points = tempKingdom.getLegalTerrain2Placements(terrain1Placement);

        int value;
        for(Point point:points){
            Terrain firstTerrain;
            if(checkingTerrain2) firstTerrain = currentDomino.getTerrain2();
            else firstTerrain = currentDomino.getTerrain1();
            tempKingdom.setTerrain(firstTerrain, terrain1Placement);
            tempKingdom.setTerrain(terrain, point);
            tempKingdom.initializeSearchArray();
            value = tempKingdom.calculateScore(harmony, middleKingdom);
            if(value>=maxValue) {
                maxValue = value;
                terrain2Placement = point;
            }
            tempKingdom.setTerrain(new Terrain(TerrainType.EMPTY, 0), terrain1Placement);
            tempKingdom.setTerrain(new Terrain(TerrainType.EMPTY, 0), point);
        }


        return maxValue;

    }
}
