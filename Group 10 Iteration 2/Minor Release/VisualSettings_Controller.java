import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class VisualSettings_Controller implements MenuController{

    private final MainWindow game;
    private final JPanel target;

    private JLabel paletteLabel;
    private JComboBox<String> paletteSelect;

    private JLabel sizeLabel;
    private JComboBox<String> sizeSelect;

    private JLabel themeLabel;
    private JComboBox<String> themeSelect;

    public VisualSettings_Controller(MainWindow game, JPanel target) {
        this.game = game;
        this.target = target;
    }

    @Override
    public void setup() {
        paletteLabel = new JLabel("Color Palette:");
        ArrayList<String> pl = new ArrayList<>(ColorPalette.values().length);
        for (ColorPalette clr : ColorPalette.values()){
            pl.add(clr.getName());
        }
        paletteSelect = new JComboBox<>(pl.toArray(String[]::new));
        paletteSelect.setSelectedItem(game.getPalette().getName());
        paletteSelect.addActionListener((ActionEvent e) -> game.changeColorPalette(ColorPalette.values()[paletteSelect.getSelectedIndex()]));

        sizeLabel = new JLabel("Window Size:");
        String[] str = {"1200, 700", "600, 420", "Fullscreen"};
        sizeSelect = new JComboBox<>(str);
        sizeSelect.addActionListener((ActionEvent e)->{
            String size = (String) sizeSelect.getSelectedItem();
            if(size != null) {
                int x, y;
                if (size.equals("Fullscreen")) {
                    x = -1;
                    y = -1;
                } else {
                    String[] sizes = size.split(", ");
                    x = Integer.parseInt(sizes[0]);
                    y = Integer.parseInt(sizes[1]);
                }
                game.resizeTo(x, y);
            }
        });

        themeLabel = new JLabel("Theme:");
        String[] thms = {"Light Theme", "Dark Theme"};
        themeSelect = new JComboBox<>(thms);
        themeSelect.setSelectedItem(thms[game.getTheme()]);
        themeSelect.addActionListener((ActionEvent e)->{
            game.setTheme(themeSelect.getSelectedIndex());
            game.changeColorPalette(game.getPalette());
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
