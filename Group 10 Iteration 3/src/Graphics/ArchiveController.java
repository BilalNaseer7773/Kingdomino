import javax.swing.*;
import java.util.function.Consumer;

public class ArchiveController implements MenuController{

    public static final int LOAD = 0;
    public static final int SAVE = 1;

    private MainWindow target;
    private MenuController previous;

    private JButton backButton;
    private JList<FilePreview> list;
    private JButton archiveButton;
    private JButton delete;
    private final Consumer<ArchiveController> mode;

    private final Consumer<ArchiveController> loadGame = (ArchiveController a)->{

    };

    private final Consumer<ArchiveController> saveGame = (ArchiveController a)->{

    };

    public ArchiveController(MainWindow target, MenuController previous, int mode) {
        this.target = target;
        this.previous = previous;
        this.mode = switch(mode){
            case LOAD -> loadGame;
            case SAVE -> saveGame;
            default -> null;
        };
    }

    @Override
    public void setup() {
        backButton = new JButton("Back");
        list = new JList<>(Archivist.getArchivist().loadPreviews());
        list.setCellRenderer(new PreviewRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        backButton.addActionListener(e->target.toMenu(previous, false));
        list.addListSelectionListener(e->refresh());
    }

    @Override
    public void sizing(int width, int height) {
        int xSize = width / 10;
        int ySize = height / 20;
        int xoffset = width / 20;
        int yoffset = height / 20;
        backButton.setBounds(xoffset, yoffset, xSize, ySize);

        xSize = width / 2;
        ySize = height;
        list.setBounds(width / 2 - xSize / 2, 0, xSize, ySize);

        backButton.repaint();
        list.repaint();
    }

    @Override
    public void addComponents() {
        JLayeredPane pane = target.getFrame().getLayeredPane();
        pane.add(backButton, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public void coloring(ColorPalette palette, int theme) {

    }

    private void refresh(){
        if(list.getSelectedIndex()==-1){

        } else{

        }
    }
}
