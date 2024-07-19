import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class MainWindow {

    private final Kingdomino program;

    public static final int START = 0;
    public static final int SETUP = 1;
    public static final int GAME = 2;
    public static final int SETTINGS = 3;

    private MenuController controller;
    private final JFrame frame;
    private MenuController preserved;

    private ColorPalette palette;
    private int theme;

    MainWindow(Kingdomino program){
        this.program = program;
        palette = ColorPalette.ROYAL;
        theme = 0;
        try
        {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        preserved = null;
        frame = new JFrame("Kingdomino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = new Dimension(1200, 700);
        frame.setPreferredSize(dim);
        frame.setSize(dim);

        toMenu(START, false);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                e.getComponent().setPreferredSize(e.getComponent().getSize());
                resizeEvent();
            }
        });

        frame.getLayeredPane().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("C"), "clrswp");
        frame.getLayeredPane().getActionMap().put("clrswp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeColorPalette(ColorPalette.values()[(palette.ordinal() + 1) % ColorPalette.values().length]);
            }
        });
        frame.setVisible(true);
    }


    /**
     * Triggered whenever the JFrame is resized. Calls sizing() on the current MenuController.
     */
    private void resizeEvent(){
        GraphicsDevice grph = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = frame.getWidth();
        int height = frame.getHeight();
        if (grph.getFullScreenWindow()==null){
            width -= 13;
            height -= 36;
        }

        controller.sizing(width, height);
        frame.getLayeredPane().repaint();
        frame.pack();
    }

    public int getTheme() {
        return theme;
    }

    /**
     * Changes the game's menu. If there is a previously preserved menu of the same type as the destination, switches to that instead.
     * @param menu  Specifies which menu to go to. Use the constants defined by this class (ex. START)
     * @param preserveOld  Whether or not to preserve the current menu controller for later use
     */
    public void toMenu(int menu, boolean preserveOld){
        MenuController temp = switch (menu) {
            case SETUP -> new SetupController(this, this.program);
            case GAME -> new GameController(this, this.program.getGame());
            case SETTINGS -> new SettingsController(this, this.controller);
            default -> new StartController(this);
        };
        toMenu(temp, preserveOld);
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    /**
     * Changes the game's menu. If there is a previously preserved menu of the same type as the destination, switches to that instead.
     * Note: in most cases you should use toMenu(int menu, boolean preserveOld) rather than this method.
     * @param menu  Specifies which menu to go to.
     * @param preserveOld  Whether or not to preserve the current menu controller for later use
     */
    public void toMenu(MenuController menu, boolean preserveOld){
        MenuController temp2 = controller;
        frame.getLayeredPane().removeAll();
        if (preserved != null && preserved.getClass().equals(menu.getClass())){
            controller = preserved;
            controller.addComponents();
        } else {
            controller = menu;
            controller.setup();
            controller.addComponents();
        }
        if (preserveOld){
            preserved = temp2;
        }
        changeColorPalette(palette);
        resizeEvent();
    }

    /**
     * Destroys the preserved MenuController.
     *
     * @see #toMenu
     */
    public void clearPreserved(){
        preserved = null;
    }

    /**
     * Returns the JFrame that this object controls.
     * @return  JFrame object
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Sets the game's color palette.
     * @param palette A ColorPalette object that specifies the desired palette
     */
    public void changeColorPalette(ColorPalette palette){
        this.palette = palette;
        frame.setBackground(palette.getBackground(theme));
        recursiveColoring(frame.getLayeredPane(), palette);
        try {
            MetalLookAndFeel.setCurrentTheme(new OceanTheme() {
                private final ColorUIResource primary3 = new ColorUIResource(palette.getInteract(theme));
                @Override
                protected ColorUIResource getPrimary3(){
                    return primary3;
                }
            });
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Throwable e){
            e.printStackTrace();
        }
        controller.coloring(palette, theme);
    }

    /**
     * To be used only by changeColorPalette(). Recursively applies color palette to all components in container.
     * @param c The Container to target for color change
     * @param palette The color palette to be applied
     * @see #changeColorPalette
     */
    private void recursiveColoring(Container c, ColorPalette palette){
        for (Component cmp : c.getComponents()){
            if (cmp instanceof JLabel && theme==ColorPalette.DARK){
                cmp.setForeground(palette.getAccent(theme));
            } else {
                cmp.setForeground(palette.getText(theme));
                if (cmp instanceof JButton || cmp instanceof JSlider || cmp instanceof JCheckBox || cmp instanceof JComboBox) {
                    cmp.setBackground(palette.getButton(theme));
                } else if (cmp instanceof JPanel) {
                    cmp.setBackground(palette.getPanel(theme));
                    recursiveColoring((JPanel) cmp, palette);
                }
            }
        }
    }

    public void resizeTo(int x, int y){
        GraphicsDevice grph = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (x < 1 || y < 1){
            grph.setFullScreenWindow(frame);
        } else {
            if (grph.getFullScreenWindow() != null){
                grph.setFullScreenWindow(null);
            }
            frame.setPreferredSize(new Dimension(x,y));
            frame.setSize(x,y);
        }
    }

    public ColorPalette getPalette(){
        return palette;
    }
}
