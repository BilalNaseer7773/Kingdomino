import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.net.URL;
import java.net.*;
import javax.sound.sampled.*;

public class AudioPlayer {
    
    Clip clip;
    float volumeVal; // max volume is 0, increasing values decrease the volume. (volume values multiplied by -1)
    String currentSong;

    AudioPlayer(){

    }

    public void stopSong(){
        clip.stop();
    }

    //Worker Method
    private void playSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {
        
        URL url = getClass().getResource(currentSong);
        //File file1 = new File(url.toURI()); // do not forget to replace the file, use double slash
        InputStream in = getClass().getResourceAsStream(currentSong);
        InputStream bufferedIn = new BufferedInputStream(in);
        AudioInputStream audioStream1 = AudioSystem.getAudioInputStream(bufferedIn);
        clip = AudioSystem.getClip();
        clip.open(audioStream1);
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(volumeVal);
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }


    public void setCurrentSong(String filename){
        currentSong = filename;
        if(clip != null) clip.stop();
        try{
            playSong();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setVolume(float volume){
        volumeVal = volume;
        if(clip != null) clip.stop();
        try{
            playSong();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    public String getCurrentSong() {
        return currentSong;
    }
}
