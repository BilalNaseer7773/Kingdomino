public class Game {

    private Kingdomino program;
    private int numberOfPlayers;
    private Player[] players;
    private int playerIndex;

    Game(Kingdomino program, int numberOfPlayers){
        this.program = program;
        this.numberOfPlayers = numberOfPlayers;
        players = new Player[numberOfPlayers];
        playerIndex = 0;
    }

    public void addPlayer(Player newPlayer){
        if( playerIndex < numberOfPlayers) players[playerIndex] = newPlayer;
        else; // max number of players
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

}


