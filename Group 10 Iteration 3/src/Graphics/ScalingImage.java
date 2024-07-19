import javax.swing.*;
import java.awt.*;

public class ScalingImage extends JPanel {

    private Image image;

    ScalingImage(Image image){
        super();
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void setImage(Image image){
        this.image = image;
    }

    public int getImageWidth(){
        return this.image.getWidth(this);
    }

    public int getImageHeight(){
        return this.image.getHeight(this);
    }

}