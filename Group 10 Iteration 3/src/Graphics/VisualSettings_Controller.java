import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisualSettings_Controller implements MenuController{

    private final MainWindow game;
    private final JPanel target;

    private JLabel paletteLabel;
    private JComboBox<ColorPalette> paletteSelect;

    private JLabel sizeLabel;
    private JComboBox<ScreenSize> sizeSelect;

    private JLabel themeLabel;
    private JComboBox<String> themeSelect;

    private Settings tempSettings;

    public VisualSettings_Controller(MainWindow game, JPanel target, Settings settings) {
        this.game = game;
        this.target = target;
        tempSettings = settings;
    }

    @Override
    public void setup() {
        paletteLabel = new JLabel("Color Palette:");
        paletteSelect = new JComboBox<>(ColorPalette.values());
        paletteSelect.setSelectedItem(game.getPalette());
        paletteSelect.addActionListener((ActionEvent e) -> {
                    ColorPalette temp = (ColorPalette) paletteSelect.getSelectedItem();
                    if(temp !=null){
                        game.changeColorPalette(temp);
                    }
                    tempSettings.setPalette(temp);
                });
        sizeLabel = new JLabel("Window Size:");
        ScreenSize[] sizes = ScreenSize.getStandardSizes();
        sizeSelect = new JComboBox<>(sizes);
        if((new ArrayList<>(List.of(sizes))).contains(tempSettings.getDimensions())){
            sizeSelect.setSelectedItem(tempSettings.getDimensions());
        }
        sizeSelect.addActionListener((ActionEvent e)->{
            ScreenSize size = (ScreenSize) sizeSelect.getSelectedItem();
            if(size!=null){
                game.resizeTo(size);
            }
            tempSettings.setDimensions(size);
        });

        themeLabel = new JLabel("Theme:");
        String[] thms = {"Light Theme", "Dark Theme"};
        themeSelect = new JComboBox<>(thms);
        themeSelect.setSelectedItem(thms[game.getTheme()]);
        themeSelect.addActionListener((ActionEvent e)->{
            game.setTheme(themeSelect.getSelectedIndex());
            game.changeColorPalette(game.getPalette());
            tempSettings.setTheme(themeSelect.getSelectedIndex());
            });
    }

    @Override
    public void sizing(int width, int height) {
        int xoffset = width / 20;
        int yoffset = height / 20;
        int space = height / 30;

        int w = width / 3;
        int h = height / 10;
        int labelx = target.getX() + xoffset;
        int selectx = target.getX() + width - w;
        int y = target.getY() + yoffset;

        sizeLabel.setBounds(labelx, y, w, h);
        sizeSelect.setBounds(selectx, y, w, h);
        paletteLabel.setBounds(labelx, y + h + space, w, h);
        paletteSelect.setBounds(selectx, y + h + space, w, h);
        themeLabel.setBounds(labelx, y + 2*(space + h), w, h);
        themeSelect.setBounds(selectx, y + 2*(space + h), w,h);

        sizeLabel.repaint();
        sizeSelect.repaint();
        paletteSelect.repaint();
        paletteLabel.repaint();
        themeSelect.repaint();
        themeLabel.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = game.getFrame().getLayeredPane();

        pane.add(paletteLabel, JLayeredPane.MODAL_LAYER);
        pane.add(paletteSelect, JLayeredPane.MODAL_LAYER);
        pane.add(sizeLabel, JLayeredPane.MODAL_LAYER);
        pane.add(sizeSelect, JLayeredPane.MODAL_LAYER);
        pane.add(themeLabel, JLayeredPane.MODAL_LAYER);
        pane.add(themeSelect, JLayeredPane.MODAL_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {

    }
}
