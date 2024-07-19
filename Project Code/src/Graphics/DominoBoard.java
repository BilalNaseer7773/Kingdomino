import java.awt.*;

public class DominoBoard extends BoardPanel{

    private Domino domino;
    private Image sprite;
    private boolean broken;

    private static Terrain[][] makeTiles(Domino domino){
        Terrain[][] tiles = new Terrain[2][1];
        tiles[0][0] = domino.getTerrain1();
        tiles[1][0] = domino.getTerrain2();
        return tiles;
    }

    public DominoBoard(Domino domino) {
        super(makeTiles(domino));
        this.domino = domino;
        broken = false;
    }

    public void breakDomino(){
        broken = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(broken){
            g.drawImage(OtherImage.CROSS.getImage(), 0,0,getWidth(),getHeight(),this);
        } else{
            super.paintComponent(g);
            if(sprite!=null){
                g.drawImage(sprite, getWidth() / 4, 0, getWidth() / 2, getHeight(), this);
            }
        }
    }

    public Image getSprite() {
        return sprite;
    }
    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }
    public Domino getDomino() {
        return domino;
    }
}
