import javax.swing.*;
import java.awt.event.ActionEvent;

public class SettingsController implements MenuController{

    private static final int VISUAL = 0;
    private static final int AUDIO = 1;

    private final MainWindow target;

    private JButton visualBtn;
    private JButton audioBtn;
    private JButton apply;
    private JButton back;
    private JPanel panel;
    private MenuController menu;
    private final MenuController previous;
    private final Settings tempSettings;

    public SettingsController(MainWindow target, MenuController previous) {
        this.target = target;
        this.previous = previous;
        tempSettings = target.getProgram().getSettings().clone();
    }

    @Override
    public void setup() {
        panel = new JPanel();
        visualBtn = new JButton("Display Settings");
        visualBtn.addActionListener((ActionEvent e)-> switchMenu(VISUAL));
        audioBtn = new JButton("Audio Settings");
        audioBtn.addActionListener((ActionEvent e)-> switchMenu(AUDIO));
        apply = new JButton("Apply");
        apply.addActionListener((ActionEvent e) -> {
            target.getProgram().setSettings(tempSettings);
            target.getProgram().applySettings();
            target.getProgram().saveSettings();
        });
        back = new JButton("Back");
        back.addActionListener((ActionEvent e)-> {
            Kingdomino game = target.getProgram();
            game.applySettings();
            target.toMenu(previous, false);
        });
        menu = new VisualSettings_Controller(target, panel, tempSettings);
        menu.setup();
    }

    @Override
    public void sizing(int width, int height) {
        int xoffset = width / 20;
        int yoffset = height / 20;
        int buttonW = (width - xoffset*2) / 4;
        visualBtn.setBounds(xoffset, yoffset, buttonW, yoffset);
        visualBtn.repaint();
        audioBtn.setBounds(xoffset, yoffset * 3, buttonW, yoffset);
        audioBtn.repaint();
        back.setBounds(xoffset, height - yoffset * 2, buttonW, yoffset);
        back.repaint();
        apply.setBounds(xoffset, height - yoffset * 4, buttonW, yoffset);
        apply.repaint();

        int panelw = 3 * buttonW;
        int panelh = height - yoffset * 2;
        panel.setBounds(xoffset + buttonW, yoffset, panelw, panelh);
        panel.repaint();
        menu.sizing(panelw, panelh);
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(visualBtn, JLayeredPane.PALETTE_LAYER);
        pane.add(apply, JLayeredPane.PALETTE_LAYER);
        pane.add(audioBtn, JLayeredPane.PALETTE_LAYER);
        pane.add(back, JLayeredPane.PALETTE_LAYER);
        pane.add(panel, JLayeredPane.PALETTE_LAYER);

        menu.addComponents();
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        menu.coloring(palette, theme);
    }

    @Override
    public void removeComponents() {

    }

    private void switchMenu(int switchMenu){
        menu.removeComponents();
        if(switchMenu==VISUAL){
            menu = new VisualSettings_Controller(target, panel, tempSettings);
        } else if (switchMenu==AUDIO){
            menu = new AudioSettings_Controller(target, panel, tempSettings);
        }
        menu.setup();
        menu.addComponents();
        target.resizeEvent();
        target.refreshColors();
    }
}
