import org.w3c.dom.css.Rect;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanProperty;
import java.util.ArrayList;

public abstract class BoardPanel extends JPanel {

    protected Terrain[][] tiles;
    private int realSize;
    private ArrayList<ButtonAlter> buttons;
    private Rectangle[] buttonAreas;
    private boolean active;
    private Point selection;

    BoardPanel(Terrain[][] tiles){
        this.tiles = tiles;
        realSize=0;
        setLayout(null);
        buttons = new ArrayList<>();
        selection = null;
        active = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintTiles(g, 0, 0, tiles.length-1, tiles[0].length-1, Color.GRAY);
    }

    protected void paintTiles(Graphics g, int minX, int minY, int maxX, int maxY, Color background){
        int i2 = 0;
        int j2, i, j;
        int crownSize = realSize / 2;
        Terrain tile;
        Point[] points;
        for(i=minX;i<=maxX;i++){
            j2=0;
            for (j=minY;j<=maxY;j++){
                tile = tiles[i][j];
                if(tile.getType()==TerrainType.START){
                    g.drawImage(tile.getType().getImage(), i2*realSize,j2*realSize, realSize, realSize, background, this);
                } else{
                    g.drawImage(tile.getType().getImage(), i2*realSize,j2*realSize, realSize, realSize, this);
                }
                if(tile.getCrowns()>0){
                    points = getOrderedLocation(realSize, tile.getCrowns(), crownSize / 2, crownSize / 2);
                    for(Point p : points){
                        g.drawImage(OtherImage.CROWN.getImage(), p.x + i2*realSize, p.y + j2*realSize, crownSize, crownSize, this);
                    }
                }
                j2++;
            }
            i2++;
        }
        if(active){
            Rectangle r;
            for(i=0;i<buttonAreas.length;i++){
                r = buttonAreas[i];
                g.drawImage(OtherImage.TILE_SELECT.getImage(), r.x*realSize, r.y*realSize, r.width*realSize, r.height*realSize, this);
            }
        }
    }

    public void displayOptions(Rectangle[] buttonAreas){
        clearOptions();
        active = true;
        this.buttonAreas = buttonAreas;
        ButtonAlter option;
        for(Rectangle r : buttonAreas){
            int i, j;
            for (i=0;i<r.width;i++){
                for (j=0;j<r.height;j++){
                    option = new ButtonAlter(r.x+i, r.y+j);
                    buttons.add(option);
                    option.addActionListener(e->selectionMade((ButtonAlter) e.getSource()));
                    this.add(option);
                }
            }
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
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm, x, y, width, height);
        if(buttons!=null){
            for (ButtonAlter b: buttons){
                b.repaint();
            }
        }
    }

    public void clearOptions(){
        removeAll();
        active = false;
        repaint();
        buttons = new ArrayList<>();
    }

    public void coloring(ColorPalette palette, int theme){
        for(ButtonAlter x : buttons){
            x.setBackground(new Color(0,0,0,0));
        }
    }

    private void selectionMade(ButtonAlter button){
        clearOptions();
        selection = new Point(button.ix, button.iy);
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selection"));
    }


    public Point getSelection() {
        return selection;
    }

    public Terrain getSelectedTerrain(){
        return tiles[selection.x][selection.y];
    }

    public int getTileNumberX(){
        return tiles.length;
    }

    public int getTileNumberY(){
        return tiles[0].length;
    }

    private Point[] getOrderedLocation(int size, int total, int xOffset, int yOffset){
        Point[] p = new Point[total];
        switch (total) {
            case 3 -> {
                p[0] = new Point(size / 2, size / 3);
                p[1] = new Point(size / 4, 2 * size / 3);
                p[2] = new Point(3 * size / 4, 2 * size / 3);
            }
            case 2 -> {
                p[0] = new Point(size / 4, size / 2);
                p[1] = new Point(3 * size / 4, size / 2);
            }
            case 1 -> p[0] = new Point(size / 2, size / 2);
        }
        for (Point point : p){
            point.setLocation(point.x - xOffset, point.y - yOffset);
        }
        return p;
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