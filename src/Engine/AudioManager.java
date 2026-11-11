package Engine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class AudioManager {
    private static final AudioManager INSTANCE = new AudioManager();
    private static final String AUDIO_PATH = Config.RESOURCES_PATH + "Audio" + File.separator;

    private final Map<String, Clip> soundEffects = new HashMap<>();
    private Clip music;
    private String musicFileName;

    private AudioManager() { }

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    public synchronized void preloadSoundEffect(String fileName) {
        if (!soundEffects.containsKey(fileName)) {
            soundEffects.put(fileName, loadClip(fileName));
        }
    }

    public synchronized void playSoundEffect(String fileName) {
        preloadSoundEffect(fileName);
        Clip clip = soundEffects.get(fileName);
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public synchronized void playMusic(String fileName) {
        if (musicFileName != null && musicFileName.equals(fileName) && music != null) {
            if (!music.isRunning()) {
                music.setFramePosition(0);
                music.loop(Clip.LOOP_CONTINUOUSLY);
            }
            return;
        }

        Clip nextMusic = loadClip(fileName);
        if (music != null) {
            music.stop();
            music.close();
        }
        music = nextMusic;
        musicFileName = fileName;
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public synchronized void stopMusic() {
        if (music != null) {
            music.stop();
        }
    }

    public synchronized void close() {
        stopMusic();
        if (music != null) {
            music.close();
            music = null;
            musicFileName = null;
        }
        for (Clip clip : soundEffects.values()) {
            clip.stop();
            clip.close();
        }
        soundEffects.clear();
    }

    private Clip loadClip(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Audio file name must not be empty.");
        }

        File audioFile = new File(AUDIO_PATH + fileName);
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
                clip.open(audioStream);
            }
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            if (clip != null) {
                clip.close();
            }
            throw new IllegalStateException("Unable to load audio file: " + audioFile, e);
        }
    }
}
