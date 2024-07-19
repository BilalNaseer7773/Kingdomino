import javax.swing.*;
import java.awt.*;

public class PreviewRenderer implements ListCellRenderer<FilePreview> {
    private class PreviewPanel extends JPanel{
        private FilePreview value;

        public PreviewPanel(FilePreview value) {
            super();
            this.value = value;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            int imgWidth = width / value.getSprites().length;
            int i = 0;
            for (Sprite sprite : value.getSprites()){
                g.drawImage(sprite.getSprite(), i, 0, imgWidth, height, this);
                i+=imgWidth;
            }
            Color temp = g.getColor();
            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(0,0 , width, height);
            int offset = width / 20;
            Font tempF = g.getFont();
            g.setColor(new Color(255,255, 255));
            g.setFont(Font.getFont(Font.SANS_SERIF));
            g.drawString(value.getName(), offset, offset);
            g.drawString("Round: "+value.getRound(), width - offset - width / 5, height - offset - g.getFont().getSize());
            g.drawString("Players: "+ value.getSprites().length, offset, height - offset - g.getFont().getSize());
            g.setFont(tempF);
            g.setColor(temp);
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends FilePreview> list, FilePreview value, int index, boolean isSelected, boolean cellHasFocus) {
        return new PreviewPanel(value);
    }
}
