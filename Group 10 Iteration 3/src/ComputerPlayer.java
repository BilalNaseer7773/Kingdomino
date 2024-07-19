import java.io.Serializable;

public class ComputerPlayer extends Player implements Serializable {
    
    ComputerPlayer(String name, Sprite sprite, PlayerColor color){
        super(name, sprite, color);
    }
    
}
