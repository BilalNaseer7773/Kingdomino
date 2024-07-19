public class Kingdomino {
    private Game game;
    private MainWindow mainWindow;

    Kingdomino(){
        mainWindow = new MainWindow(this);
    }

    public void StartGame(Game newGame){
        game = newGame;
    }

    public Game getGame(){
        return game;
    }
}
