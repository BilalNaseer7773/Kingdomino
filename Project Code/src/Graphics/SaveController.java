import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SaveController implements MenuController{

    private final MainWindow target;
    private final MenuController previous;
    private final Game game;

    private JButton back;
    private JButton saveButton;
    private JTextField name;
    private JLabel label;
    private Component preview;

    private int width;
    private int height;

    public SaveController(MainWindow target, MenuController previous, Game game) {
        this.target = target;
        this.previous = previous;
        this.game = game;
    }

    @Override
    public void setup() {
        name = new JTextField();
        name.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                change();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                change();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void change(){
                saveButton.setEnabled(!name.getText().equals(""));
                JLayeredPane pane = target.getFrame().getLayeredPane();
                pane.remove(preview);
                preview = new PreviewRenderer().getListCellRendererComponent(null, Archivist.getArchivist().generatePreview(name.getText(), game), 0, false, false);
                pane.add(preview);
                sizing(width,height);
                target.applyFont(pane);
                target.refreshColors();
            }
        });
        preview = new PreviewRenderer().getListCellRendererComponent(null, Archivist.getArchivist().generatePreview(name.getText(), game), 0, false, false);
        back = new JButton("Back");
        back.addActionListener(e-> target.toMenu(previous, false));
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e-> Archivist.getArchivist().saveGame(name.getText(), game));
        label = new JLabel("Enter a name for the save:");
    }

    @Override
    public void sizing(int width, int height) {
        this.width = width;
        this.height = height;
        int offset = width / 20;
        int buttonWidth = width / 15;
        int buttonHeight = height / 20;
        int textWidth = buttonWidth * 4;
        back.setBounds(offset, height - offset - buttonHeight, buttonWidth, buttonHeight);
        back.repaint();
        saveButton.setBounds(width - offset - buttonWidth, height - offset - buttonHeight, buttonWidth, buttonHeight);
        saveButton.repaint();
        name.setBounds(width / 2 - textWidth / 2, height - offset - buttonHeight, textWidth, buttonHeight);
        name.repaint();
        label.setBounds(width / 2 - textWidth / 2, height - offset - buttonHeight*2, textWidth, buttonHeight);
        label.repaint();
        preview.setBounds(width / 8, 0, 6*width/8, height);
        preview.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(back, JLayeredPane.PALETTE_LAYER);
        pane.add(saveButton, JLayeredPane.PALETTE_LAYER);
        pane.add(name, JLayeredPane.PALETTE_LAYER);
        pane.add(label, JLayeredPane.PALETTE_LAYER);
        pane.add(preview, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {
        label.setForeground(palette.getAccent(ColorPalette.DARK));
    }

    @Override
    public void removeComponents() {

    }
}
