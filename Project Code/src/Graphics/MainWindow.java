import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainWindow {

    private final Kingdomino program;

    public static final int START = 0;
    public static final int SETUP = 1;
    public static final int GAME = 2;
    public static final int SETTINGS = 3;
    public static final int LOAD = 4;
    public static final int PAUSE = 5;
    public static final int SAVE = 6;

    private MenuController controller;
    private final JFrame frame;
    private MenuController preserved;

    private ColorPalette palette;
    private int theme;
    private Font font;

    private boolean manualResize;

    MainWindow(Kingdomino program){
        this.program = program;
        frame = new JFrame("Kingdomino");
        manualResize = true;
    }

    public void setup(){
        try
        {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        preserved = null;
        frame.setIconImage(Sprite.SNOW_KING.getSprite());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        toMenu(START, false);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                e.getComponent().setPreferredSize(e.getComponent().getSize());
                resizeEvent();
            }
        });

        frame.setVisible(true);
    }

    /**
     * Triggered whenever the JFrame is resized. Calls sizing() on the current MenuController.
     */
    public void resizeEvent(){
        int width = frame.getWidth();
        int height = frame.getHeight();
        if(manualResize){
            program.getSettings().getDimensions().setSize(width, height);
        }
        manualResize = true;
        Dimension x = getDimensions();
        width = x.width;
        height = x.height;
        font = new Font("Comic Sans MS",Font.BOLD,  width / 75);
        applyFont(frame.getLayeredPane());
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
            case LOAD -> new ArchiveController(this, this.controller);
            case SAVE -> new SaveController(this, this.controller, program.getGame());
            case PAUSE -> new StartController(this, getScreenshot(true), StartController.PAUSE);
            default -> new StartController(this, OtherImage.BACK.getImage(), StartController.START);
        };
        toMenu(temp, preserveOld);
    }

    public void toEndScreen(VictoryRecord data){
        toMenu(new VictoryController(this, getScreenshot(true), data), false);
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
                if (cmp instanceof JButton || cmp instanceof JSlider || cmp instanceof JCheckBox || cmp instanceof JComboBox || cmp instanceof JList) {
                    cmp.setBackground(palette.getButton(theme));
                } else if (cmp instanceof JPanel) {
                    cmp.setBackground(palette.getPanel(theme));
                    recursiveColoring((JPanel) cmp, palette);
                }
            }
        }
    }

    public void resizeTo(ScreenSize size){
        if(size==null){
            size = program.getSettings().getDimensions();
        }
        manualResize = false;
        GraphicsDevice grph = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (size.isFullscreen()){
            grph.setFullScreenWindow(frame);
        } else {
            if (grph.getFullScreenWindow() != null){
                grph.setFullScreenWindow(null);
            }
            frame.setPreferredSize(size);
            frame.setSize(size);
        }
    }

    public ColorPalette getPalette(){
        return palette;
    }

    public Kingdomino getProgram() {
        return program;
    }

    public void setPalette(ColorPalette palette) {
        this.palette = palette;
    }

    private boolean isWindows(){
        return System.getProperty("os.name").equals("Windows 11")||System.getProperty("os.name").equals("Windows 10");
    }

    public MenuController getController() {
        return controller;
    }

    public Image getScreenshot(boolean darken){
        Dimension d = getDimensions();
        BufferedImage img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);

        frame.getLayeredPane().paint(img.getGraphics());
        if(darken){
            int i, j;
            for(i=0;i<img.getWidth();i++){
                for(j=0;j<img.getHeight();j++){
                    img.setRGB(i,j, new Color(img.getRGB(i,j)).darker().darker().getRGB());
                }
            }
        }
        return img;
    }

    public void applyFont(Container container){
        for(Component c : container.getComponents()){
            c.setFont(font);
            if(c instanceof JPanel){
                applyFont((JPanel) c);
            }
        }
    }

    public Dimension getDimensions(){
        GraphicsDevice grph = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = frame.getWidth();
        int height = frame.getHeight();
        if (grph.getFullScreenWindow()==null&&isWindows()){
            width -= 13;
            height -= 36;
        }
        return new Dimension(width, height);
    }

    public void refreshColors(){
        changeColorPalette(palette);
    }
}
