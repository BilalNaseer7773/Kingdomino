import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Archivist {

    private static final String path = System.getProperty("user.home") + "/Kingdomino/";
    private static final String saveName = "gameFile";

    private static Archivist me;
    public static Archivist getArchivist(){
        if(me==null){
            me = new Archivist();
        }
        return me;
    }

    private Archivist(){
        new File(path).mkdirs();
    }

    public void saveSettings(Settings settings){
        try{
            FileOutputStream out = new FileOutputStream(path + "settings.ser");
            ObjectOutputStream objstrm = new ObjectOutputStream(out);
            objstrm.writeObject(settings);
            objstrm.close();
            out.close();
        } catch(Throwable e){
            e.printStackTrace();
        }
    }

    public Settings loadSettings(){
        Settings settings;
        try{
            FileInputStream input = new FileInputStream(path + "settings.ser");
            ObjectInputStream inpstrm = new ObjectInputStream(input);
            settings = (Settings) inpstrm.readObject();
            inpstrm.close();
            input.close();
            return settings;
        } catch(Throwable e){
            settings = new Settings();
            settings.setTheme(ColorPalette.Light);
            settings.setPalette(ColorPalette.ROYAL);
            settings.setVolume(-6.02059991328f);
            settings.setDimensions(new ScreenSize(1200, 700, false));
            return settings;
        }
    }

    public void saveGame(String name, Game game){
        FilePreview preview = generatePreview(name, game);
        try{
            FileOutputStream out = new FileOutputStream(path + preview.getFilename());
            ObjectOutputStream stream = new ObjectOutputStream(out);
            Serializable[] objects = {preview, game};
            stream.writeObject(objects);
            stream.close();
            out.close();
        } catch(Throwable e){
            e.printStackTrace();
        }
    }

    public Game loadGame(String filename, Kingdomino program){
        Game game = null;
        try{
            FileInputStream input = new FileInputStream(path + filename);
            ObjectInputStream stream = new ObjectInputStream(input);
            game = (Game) ((Serializable[]) stream.readObject())[1];
            stream.close();
            input.close();
        } catch(Throwable e){
            e.printStackTrace();
        }
        if(game!=null){
            game.setProgram(program);
        }
        return game;
    }

    public FilePreview[] loadPreviews(){
        File[] files = getSaveFiles();
        if(files==null){
            return null;
        }
        ArrayList<FilePreview> previews = new ArrayList<>(files.length);
        FileInputStream input;
        ObjectInputStream stream;
        try{
            for(File f:files){
                input = new FileInputStream(f);
                stream = new ObjectInputStream(input);
                previews.add((FilePreview) ((Serializable[]) stream.readObject())[0]);
                stream.close();
                input.close();
            }
        } catch(Throwable e){
            e.printStackTrace();
        }
        return previews.toArray(FilePreview[]::new);
    }
    public void deleteSave(String filename){
        File file = new File(path + filename);
        if(file.exists()){
            file.delete();
        }
    }

    public FilePreview generatePreview(String name, Game game){
        File[] files = getSaveFiles();
        int num = 0;
        if(files!=null){
            num = files.length;
            for(int i=0; i<files.length; i++){
                int finalI = i;
                if(Arrays.stream(files).noneMatch(e -> e.getName().equals(saveName + finalI + ".ser"))){
                    num = i;
                    break;
                }
            }
        }
        Player[] temp = game.getPlayers();
        Sprite[] sprites;
        if(temp!=null && temp.length!=0) {
            sprites = Arrays.stream(game.getPlayers()).map(Player::getSprite).toArray(Sprite[]::new);
        }else{
            sprites = null;
        }
        return new FilePreview(name, saveName + num + ".ser", game.getRound(), sprites);
    }

    private File[] getSaveFiles(){
        File direct = new File(path);
        File[] files = direct.listFiles();
        if(files==null){
            return null;
        }
        return Arrays.stream(files).filter(e->e.getName().contains(saveName)).toArray(File[]::new);
    }
}
