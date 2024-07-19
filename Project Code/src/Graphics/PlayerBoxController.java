import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerBoxController implements MenuController {

    public static final String EASY = "Easy";
    public static final String HARD = "Hard";
    public static final String HUMAN = "Human";

    private JPanel panel;
    private final String name;
    private final int ordinance;
    private final PlayerColor color;
    private final MainWindow target;
    private boolean enabled;
    private JComboBox<String> ai;

    private JTextField nom;

    private JComboBox<Sprite> iconSelect;
    private JComboBox<PlayerColor> colorSelect;

    PlayerBoxController(String name, MainWindow target, int ordinance, PlayerColor color){
        this.name = name;
        this.target = target;
        this.ordinance = ordinance;
        this.color = color;
        enabled = true;
    }

    @Override
    public void setup(){
        panel = new JPanel();
        panel.setBorder(new EtchedBorder());

        iconSelect = new JComboBox<>(Sprite.values()) {
            @Override
            protected void paintComponent(Graphics g) {
                this.getComponent(0).setVisible(enabled);
                super.paintComponent(g);
                if(this.getSelectedItem()!=null) {
                    if (enabled) {
                        g.drawImage(((Sprite) this.getSelectedItem()).getIcon(), 0, 0, this.getWidth(), this.getHeight(), this);
                    } else {
                        g.drawImage(OtherImage.CROSS.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
                    }
                }
            }
        };

        iconSelect.setSelectedIndex(ordinance-1);

        nom = new JTextField(name);

        colorSelect = new JComboBox<>(PlayerColor.values()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(this.getSelectedItem()!=null) {
                    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                    image.setRGB(0, 0, ((PlayerColor) this.getSelectedItem()).getColor().getRGB());
                    g.drawImage(image, 0, 0, this.getWidth(), getHeight(), this);
                }
            }
        };
        colorSelect.setSelectedItem(color);

        String[] opt = {HUMAN, EASY, HARD};
        ai = new JComboBox<>(opt);
    }

    @Override
    public void sizing(int width, int height) {
        int xSize = width / 7;
        int ySize = xSize * 2;
        int xCorner = ordinance * width / 5 - xSize / 2;
        int yCorner = 3 * height / 4 - ySize / 2;
        panel.setBounds(xCorner, yCorner, xSize, ySize);

        int offset = 5;
        iconSelect.setBounds(xCorner + offset, yCorner + offset, xSize - 2 * offset, xSize - 2 * offset);
        nom.setBounds(xCorner + offset, yCorner + ySize / 2, xSize - 2 * offset, xSize / 6);
        colorSelect.setBounds(xCorner + offset, yCorner + ySize / 2 + xSize / 6, xSize - 2 * offset, xSize / 6);
        ai.setBounds(xCorner + offset, yCorner + ySize / 2 + 2 * xSize / 6, xSize - 2 * offset, xSize / 6);

        panel.repaint();
        iconSelect.repaint();
        nom.repaint();
        colorSelect.repaint();
        ai.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(iconSelect, JLayeredPane.MODAL_LAYER);
        pane.add(nom, JLayeredPane.MODAL_LAYER);
        pane.add(colorSelect, JLayeredPane.MODAL_LAYER);
        pane.add(ai, JLayeredPane.MODAL_LAYER);
        pane.add(panel, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
    }

    public void disable(){
        enabled = false;
        ai.setEnabled(false);
        nom.setEnabled(false);
        iconSelect.setEnabled(false);
        colorSelect.setEnabled(false);
    }

    public void enable(){
        enabled = true;
        ai.setEnabled(true);
        nom.setEnabled(true);
        iconSelect.setEnabled(true);
        colorSelect.setEnabled(true);
    }

    public Player getPlayer(boolean mightyDuel){
        Player temp;
        String dif = (String) ai.getSelectedItem();
        if(!dif.equals(HUMAN)){
            int df = switch(dif){
                case EASY -> 1;
                case HARD -> 0;
                default -> 2;
            };
            temp = new ComputerPlayer(nom.getText(), (Sprite) iconSelect.getSelectedItem(), (PlayerColor) colorSelect.getSelectedItem(), df, mightyDuel? 7 : 5); // eventually set difficulty here
        } else{
            temp = new HumanPlayer(nom.getText(), (Sprite) iconSelect.getSelectedItem(), (PlayerColor) colorSelect.getSelectedItem(), mightyDuel? 7 : 5);
        }
        return temp;
    }

    @Override
    public void removeComponents() {

    }
}