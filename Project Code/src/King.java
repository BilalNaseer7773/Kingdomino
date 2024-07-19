import java.io.Serializable;

public class King implements Comparable<King> , Serializable {
    private Player owner;
    private Domino selectedDomino;

    King(Player owner){
        this.owner = owner;
    }

    @Override public int compareTo(King k){
        if(k.selectedDomino.getNumber() > this.selectedDomino.getNumber()) return -1;
        else return 1;
    }

    public Player getOwner(){
        return owner;
    }

    public void setSelectedDomino(Domino domino){
        selectedDomino = domino;
    }

    public Domino getSelectedDomino(){
        return selectedDomino;
    }

}
