import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable {
       
    HumanPlayer(String name, Sprite sprite, PlayerColor color){
        super(name, sprite, color);
    }

}
