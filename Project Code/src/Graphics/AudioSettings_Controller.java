import javax.swing.*;

public class AudioSettings_Controller implements MenuController{

    private final MainWindow target;
    private final JPanel panel;
    private final Settings settings;

    private JLabel volumeLabel;
    private JSlider volume;

    public AudioSettings_Controller(MainWindow target, JPanel panel, Settings settings) {
        this.target = target;
        this.panel = panel;
        this.settings = settings;
    }

    @Override
    public void setup() {
        volumeLabel = new JLabel("Volume:");
        volume = new JSlider(0, 100);
        volume.setMajorTickSpacing(20);
        volume.setPaintTicks(true);
        volume.setPaintLabels(true);
        volume.addChangeListener(e->{
            float temp = (float) (20 * Math.log10(volume.getValue()/100f));
            settings.setVolume(temp);
        });
    }

    @Override
    public void sizing(int width, int height) {
        int xoffset = width / 20;
        int yoffset = height / 20;

        int w = width / 3;
        int h = height / 10;
        int labelx = panel.getX() + xoffset;
        int selectx = panel.getX() + width - w;
        int y = panel.getY() + yoffset;

        volume.setBounds(selectx-w, y, w*2, h);
        volume.repaint();
        volumeLabel.setBounds(labelx, y, w, h);
        volumeLabel.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(volume, JLayeredPane.MODAL_LAYER);
        pane.add(volumeLabel, JLayeredPane.MODAL_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {

    }

    @Override
    public void removeComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.remove(volume);
        pane.remove(volumeLabel);
    }
}
