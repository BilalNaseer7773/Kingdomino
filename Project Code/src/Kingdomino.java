public class Kingdomino {
    private Game game;
    private MainWindow mainWindow;
    private Settings settings;
    private Archivist memory;
    private AudioPlayer player;

    Kingdomino(){
    }

    public void start(){
        player = new AudioPlayer();
        mainWindow = new MainWindow(this);
        memory = Archivist.getArchivist();
        settings = memory.loadSettings();
        mainWindow.setTheme(settings.getTheme());
        mainWindow.setPalette(settings.getPalette());
        mainWindow.resizeTo(settings.getDimensions());
        mainWindow.setup();
        applySettings();
    }


    public void StartGame(Game newGame){
        player.setCurrentSong("Battle Theme V1.wav");
        game = newGame;
        mainWindow.toMenu(MainWindow.GAME, false);
        GameController g = (GameController) mainWindow.getController();
        Facade fc = new Facade(game, g);
        g.setFacade(fc);
        game.setFacade(fc);
        game.playGame();
    }

    public Game getGame(){
        return game;
    }

    public void applySettings(){
        mainWindow.setTheme(settings.getTheme());
        mainWindow.changeColorPalette(settings.getPalette());
        mainWindow.resizeTo(settings.getDimensions());
        player.setVolume(settings.getVolume());
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void saveSettings(){
        memory.saveSettings(settings);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void endGame(VictoryRecord victory){
        mainWindow.toEndScreen(victory);
    }

    public void titleSong(){
        if(player.getCurrentSong()==null||!player.getCurrentSong().equals("Title Theme V2.wav")){
            player.setCurrentSong("Title Theme V2.wav");;
        }
    }
}