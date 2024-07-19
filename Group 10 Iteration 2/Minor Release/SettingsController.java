import javax.swing.*;
import java.awt.event.ActionEvent;

public class SettingsController implements MenuController{

    private static final int VISUAL = 0;
    private static final int AUDIO = 1;

    private final MainWindow target;

    private JButton visualBtn;
    private JButton audioBtn;
    private JButton back;
    private JPanel panel;
    private MenuController menu;
    private final MenuController previous;

    public SettingsController(MainWindow target, MenuController previous) {
        this.target = target;
        this.previous = previous;
    }

    @Override
    public void setup() {
        panel = new JPanel();
        visualBtn = new JButton("Display Settings");
        visualBtn.addActionListener((ActionEvent e)-> menu = new VisualSettings_Controller(target, panel));
        audioBtn = new JButton("Audio Settings");
        audioBtn.addActionListener((ActionEvent e)-> menu = new AudioSettings_Controller(target, panel));
        back = new JButton("Back");
        back.addActionListener(e-> target.toMenu(previous, false));

        menu = new VisualSettings_Controller(target, panel);
        menu.setup();
    }

    @Override
    public void sizing(int width, int height) {
        int xoffset = width / 20;
        int yoffset = height / 20;
        int buttonW = (width - xoffset*2) / 4;
        visualBtn.setBounds(xoffset, yoffset, buttonW, yoffset);
        audioBtn.setBounds(xoffset, yoffset * 3, buttonW, yoffset);
        back.setBounds(xoffset, height - yoffset * 2, buttonW, yoffset);

        int panelw = 3 * buttonW;
        int panelh = height - yoffset * 2;
        panel.setBounds(xoffset + buttonW, yoffset, panelw, panelh);

        menu.sizing(panelw, panelh);
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(visualBtn, JLayeredPane.PALETTE_LAYER);
        //pane.add(audioBtn, JLayeredPane.PALETTE_LAYER);
        pane.add(back, JLayeredPane.PALETTE_LAYER);
        pane.add(panel, JLayeredPane.PALETTE_LAYER);

        menu.addComponents();
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        menu.coloring(palette, theme);
    }
}
