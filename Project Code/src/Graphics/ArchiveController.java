import javax.swing.*;

public class ArchiveController implements MenuController{

    private final MainWindow target;
    private final MenuController previous;

    private JButton backButton;
    private JList<FilePreview> list;
    private JButton loadButton;
    private JButton delete;

    public ArchiveController(MainWindow target, MenuController previous) {
        this.target = target;
        this.previous = previous;

    }

    @Override
    public void setup() {
        backButton = new JButton("Back");
        list = new JList<>(Archivist.getArchivist().loadPreviews());
        list.setCellRenderer(new PreviewRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        backButton.addActionListener(e->target.toMenu(previous, false));
        loadButton = new JButton("Load Game");
        loadButton.addActionListener(e->load());
        delete = new JButton("Delete");
        delete.addActionListener(e->delete());
    }

    @Override
    public void removeComponents() {

    }

    @Override
    public void sizing(int width, int height) {
        int xSize = width / 10;
        int ySize = height / 20;
        int xoffset = width / 20;
        int yoffset = height / 20;
        backButton.setBounds(xoffset, yoffset, xSize, ySize);
        loadButton.setBounds(width - xoffset - xSize, height - yoffset - ySize, xSize, ySize);
        delete.setBounds(xoffset, height - yoffset - ySize, xSize, ySize);

        xSize = width / 2;
        ySize = height;
        list.setBounds(width / 2 - xSize / 2, 0, xSize, ySize);
        list.setFixedCellHeight(height / 5);

        backButton.repaint();
        loadButton.repaint();
        delete.repaint();
        list.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(backButton, JLayeredPane.PALETTE_LAYER);
        pane.add(list, JLayeredPane.PALETTE_LAYER);
        pane.add(delete, JLayeredPane.PALETTE_LAYER);
        pane.add(loadButton, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {

    }

    private void load(){
        if(list.getSelectedValue()!=null){
            target.getProgram().StartGame(Archivist.getArchivist().loadGame(list.getSelectedValue().getFilename(), target.getProgram()));
        }
    }

    private void delete(){
        if(list.getSelectedValue()!=null){
            Archivist.getArchivist().deleteSave(list.getSelectedValue().getFilename());
            target.getFrame().getLayeredPane().remove(list);
            list = new JList<>(Archivist.getArchivist().loadPreviews());
            target.getFrame().getLayeredPane().add(list);
            target.resizeEvent();
            target.refreshColors();
        }
    }
}
