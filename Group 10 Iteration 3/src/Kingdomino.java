public class Kingdomino {
    private Game game;
    private MainWindow mainWindow;
    private Settings settings;
    private Archivist memory;

    Kingdomino(){
    }

    public void start(){
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

}
