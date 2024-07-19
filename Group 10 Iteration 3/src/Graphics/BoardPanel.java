import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.beans.BeanProperty;
import java.util.ArrayList;

// I know that this class is a mess, I intend to split it up using inheritance later

public class BoardPanel extends JPanel {

    //Modes
    public static final int TOPLEFT = 0;
    public static final int BOTTOMLEFT = 1;
    public static final int TOPRIGHT = 2;
    public static final int BOTTOMRIGHT = 3;
    public static final int STATIC = 4;

    private int mode;

    private Terrain[][] tiles;
    private int realSize;
    private ArrayList<ButtonAlter> buttons;
    private final PlayerColor color;
    private final Kingdom kingdom;
    private Player owner;
    private boolean active;

    private Domino domino;
    private Point selection;

    private Image sprite;

    private ButtonAlter totality;

    BoardPanel(Kingdom kingdom, PlayerColor color){
        this.tiles = kingdom.getTerrainSquares();
        this.color = color;
        this.kingdom = kingdom;
        domino=null;
        owner=null;
        construct();
    }

    BoardPanel(Domino domino){
        this.domino=domino;
        tiles = new Terrain[2][1];
        tiles[0][0] = domino.getTerrain1();
        tiles[1][0] = domino.getTerrain2();
        color=PlayerColor.PURPLE;
        kingdom=null;
        owner=null;
        construct();
    }

    private void construct(){
        realSize=0;
        totality = new ButtonAlter(0,0);
        sprite = null;
        setLayout(null);
        buttons = new ArrayList<>();
        mode = STATIC;
        selection = null;
        active = false;
        totality.addActionListener(e->{
            fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "totality"));
        });
        add(totality);
        setEnabled(false);

    }

    public Player getOwner() {
        return owner;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int size = tiles.length;
        int i,j, maxX, maxY, minX, minY;
        if(mode==STATIC){
            minX = 0;
            maxX = size-1;
            minY = 0;
            maxY = tiles[0].length - 1;
        }else{
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
        int i2 = 0;
        int j2;
        for(i=minX;i<=maxX;i++){
            j2=0;
            for (j=minY;j<=maxY;j++){
                if(tiles[i][j].getType()==TerrainType.START){
                    g.drawImage(tiles[i][j].getType().getImage(), i2*realSize,j2*realSize, realSize, realSize, color.getColor(), this);
                } else{
                    g.drawImage(tiles[i][j].getType().getImage(), i2*realSize,j2*realSize, realSize, realSize, this);
                }
                j2++;
            }
            i2++;
        }
        if(active){
            for(ButtonAlter b : buttons){
                boolean h = g.drawImage(OtherImage.TILE_SELECT.getImage(), b.ix*b.getWidth(), b.iy*b.getHeight(), b.getWidth(), b.getHeight(), this);
            }
        } else if (isEnabled()){
            g.drawImage(OtherImage.TILE_SELECT.getImage(), 0,0,getWidth(),getHeight(),this);
        }
        if(sprite!=null){
            g.drawImage(sprite, getWidth() / 4, 0, getWidth() / 2, getHeight(), this);
        }
    }

    public void displayOptions(Point[] validPoints){
        active = true;
        ButtonAlter option;
        for(Point p : validPoints){
            option = new ButtonAlter(p.x, p.y);
            buttons.add(option);
            option.addActionListener(e->selectionMade((ButtonAlter) e.getSource()));
            this.add(option);
        }
        repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        realSize = width / tiles.length;
        for (ButtonAlter b: buttons){
            b.setBounds(realSize*b.ix,realSize*b.iy,realSize,realSize);
        }
        if(isEnabled()){
            totality.setBounds(0,0,getWidth(),getHeight());
        }
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm, x, y, width, height);
        if(buttons!=null){
            for (ButtonAlter b: buttons){
                b.repaint();
            }
        }
        if(isEnabled()&& totality!=null){
            totality.repaint();
        }
    }

    public Domino getDomino() {
        return domino;
    }

    private void selectionMade(ButtonAlter button){
        removeAll();
        active = false;
        repaint();
        buttons = new ArrayList<>();
        selection = new Point(button.ix, button.iy);
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selection"));
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Image getSprite() {
        return sprite;
    }

    public Point getSelection() {
        return selection;
    }

    public Terrain getSelectedTerrain(){
        return tiles[selection.x][selection.y];
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    ////////////////////////////////// The Following is Copied from javax.swing.AbstractButton  ////////////////////////////////////////
//// Why didn't I just inherit from JButton?
//// Because JButton's graphics hate me.

    /**
     * Adds an <code>ActionListener</code> to the button.
     * @param l the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes an <code>ActionListener</code> from the button.
     *
     * @param l the listener to be removed
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this AbstractButton with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    @BeanProperty(bound = false)
    public ActionListener[] getActionListeners() {
        return listenerList.getListeners(ActionListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the <code>event</code>
     * parameter.
     *
     * @param event  the <code>ActionEvent</code> object
     * @see EventListenerList
     */
    protected void fireActionPerformed(ActionEvent event) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                // Lazily create the event:
                if (e == null) {
                    String actionCommand = event.getActionCommand();
                    if(actionCommand == null) {
                        actionCommand = "";
                    }
                    e = new ActionEvent(this,
                            ActionEvent.ACTION_PERFORMED,
                            actionCommand,
                            event.getWhen(),
                            event.getModifiers());
                }
                ((ActionListener)listeners[i+1]).actionPerformed(e);
            }
        }
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    //////////////////////////////// End of AbstractButton content ////////////////////////////////



    private static class ButtonAlter extends JButton{

        public final int ix;
        public final int iy;

        ButtonAlter(int ix, int iy){
            this.ix = ix;
            this.iy = iy;
            setBackground(new Color(0, 0, 0, 0));
        }

        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
        }
    }
}