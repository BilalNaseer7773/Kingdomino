import java.awt.*;

public class KingdomBoard extends BoardPanel{

    public static final int TOPLEFT = 0;
    public static final int BOTTOMLEFT = 1;
    public static final int TOPRIGHT = 2;
    public static final int BOTTOMRIGHT = 3;
    public static final int STATIC = 4;

    private int mode;
    private final Kingdom kingdom;
    private final PlayerColor color;

    KingdomBoard(Kingdom kingdom, PlayerColor color) {
        super(kingdom.getTerrainSquares());
        this.kingdom = kingdom;
        this.color = color;
        mode = STATIC;
    }


    @Override
    protected void paintComponent(Graphics g) {
        if(mode==STATIC){
            super.paintTiles(g, 0, 0, tiles.length-1, tiles.length-1, color.getColor());
        } else{
            int maxX, maxY, minX, minY;
            final int size = tiles.length;
            maxX = Math.min(kingdom.getMaxX() + 2, size-1);
            minX = Math.max(kingdom.getMinX() - 2, 0);
            maxY = Math.min(kingdom.getMaxY() + 2, size-1);
            minY = Math.max(kingdom.getMinY() - 2, 0);
            int width = maxX - minX;
            int height = maxY - minY;
            // Adjustments to zoom in if the board is at 4 or 5
            if(width>=8){
                maxX-=width-7;
                minX+=width-7;
                width-=2*(width-7);
            }
            if(height>=8){
                maxY-=height-7;
                minY+=height-7;
                height-=2*(height-7);
            }
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }
}
