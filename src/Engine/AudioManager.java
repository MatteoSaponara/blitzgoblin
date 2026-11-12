package Engine;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class AudioManager {
    private static final AudioManager INSTANCE = new AudioManager();
    private static final String AUDIO_PATH = Config.RESOURCES_PATH + "Audio" + File.separator;
    private static final String SOUND_EFFECT_PATH = AUDIO_PATH + "sfx" + File.separator;
    private static final String MUSIC_PATH = AUDIO_PATH + "music" + File.separator;
    private static final float SOUND_EFFECT_GAIN_DB = -8.0f;
    private static final int INITIAL_SOUND_EFFECT_VOICES = 2;
    private static final int PITCH_VARIANT_COUNT = 5;

    private final Map<String, List<Clip>> soundEffects = new HashMap<>();
    private final Map<String, PitchVariation> soundEffectPitchVariations = new HashMap<>();
    private final Map<String, Float> soundEffectGains = new HashMap<>();
    private Clip music;
    private String musicFileName;
    private Clip playbackKeepAlive;

    private AudioManager() { }

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    public synchronized void initializeSoundEffects() {
        File soundEffectDirectory = new File(SOUND_EFFECT_PATH);
        File[] soundEffectFiles = soundEffectDirectory.listFiles(
                (directory, name) -> name.toLowerCase(java.util.Locale.ROOT).endsWith(".wav"));
        if (soundEffectFiles == null) {
            throw new IllegalStateException("Unable to list sound effects in: " + soundEffectDirectory);
        }

        Map<String, SoundEffectSettings> settings = loadSoundEffectSettings(soundEffectDirectory);
        for (File soundEffectFile : soundEffectFiles) {
            String fileName = soundEffectFile.getName();
            SoundEffectSettings effectSettings = settings.remove(fileName);
            if (effectSettings != null) {
                if (effectSettings.gainDb != null) {
                    setSoundEffectGain(fileName, effectSettings.gainDb);
                }
                if (effectSettings.pitchMin != null || effectSettings.pitchMax != null) {
                    if (effectSettings.pitchMin == null || effectSettings.pitchMax == null) {
                        throw new IllegalStateException("Both pitchMin and pitchMax are required for " + fileName);
                    }
                    setSoundEffectPitchVariation(fileName, effectSettings.pitchMin, effectSettings.pitchMax);
                }
            }
            preloadSoundEffect(fileName);
        }
        if (!settings.isEmpty()) {
            throw new IllegalStateException("Sound effect settings reference missing WAV files: " + settings.keySet());
        }
    }

    public synchronized void preloadSoundEffect(String fileName) {
        if (!soundEffects.containsKey(fileName)) {
            List<Clip> clips = new ArrayList<>();
            PitchVariation variation = soundEffectPitchVariations.get(fileName);
            int voiceCount = variation == null || !variation.hasVariation()
                    ? INITIAL_SOUND_EFFECT_VOICES
                    : PITCH_VARIANT_COUNT;
            for (int i = 0; i < voiceCount; i++) {
                float pitchSemitones = variation == null
                        ? 0
                        : variation.getVariantSemitones(i, voiceCount);
                clips.add(loadClip(SOUND_EFFECT_PATH, fileName, true, pitchSemitones));
            }
            if (playbackKeepAlive == null) {
                warmUpPlaybackLine(clips.get(0).getFormat());
            }
            soundEffects.put(fileName, clips);
        }
    }

    public synchronized void setSoundEffectGain(String fileName, float gainDb) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Sound effect file name must not be empty.");
        }
        validateGain(gainDb);
        soundEffectGains.put(fileName, gainDb);
        List<Clip> clips = soundEffects.get(fileName);
        if (clips != null) {
            for (Clip clip : clips) {
                applySoundEffectGain(clip, gainDb);
            }
        }
    }

    public synchronized void playSoundEffect(String fileName) {
        playSoundEffect(fileName, soundEffectGains.getOrDefault(fileName, SOUND_EFFECT_GAIN_DB));
    }

    public synchronized void playSoundEffect(String fileName, float gainDb) {
        validateGain(gainDb);
        preloadSoundEffect(fileName);
        List<Clip> clips = soundEffects.get(fileName);
        Clip clip = null;
        List<Clip> availableClips = new ArrayList<>();
        for (Clip availableClip : clips) {
            if (!availableClip.isRunning()) {
                availableClips.add(availableClip);
            }
        }
        if (availableClips.isEmpty()) {
            PitchVariation variation = soundEffectPitchVariations.get(fileName);
            float pitchSemitones = variation == null ? 0 : variation.getRandomSemitones();
            clip = loadClip(SOUND_EFFECT_PATH, fileName, true, pitchSemitones);
            clips.add(clip);
        } else {
            clip = availableClips.get(ThreadLocalRandom.current().nextInt(availableClips.size()));
        }
        applySoundEffectGain(clip, gainDb);
        clip.setFramePosition(0);
        clip.start();
    }

    public synchronized void setSoundEffectPitchVariation(
            String fileName, float minimumSemitones, float maximumSemitones) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Sound effect file name must not be empty.");
        }
        if (Float.isNaN(minimumSemitones) || Float.isInfinite(minimumSemitones)
                || Float.isNaN(maximumSemitones) || Float.isInfinite(maximumSemitones)
                || minimumSemitones > maximumSemitones
                || minimumSemitones < -24 || maximumSemitones > 24) {
            throw new IllegalArgumentException("Pitch variation must be an ordered range between -24 and 24 semitones.");
        }
        soundEffectPitchVariations.put(fileName, new PitchVariation(minimumSemitones, maximumSemitones));
        List<Clip> existingClips = soundEffects.remove(fileName);
        if (existingClips != null) {
            closeClips(existingClips);
            preloadSoundEffect(fileName);
        }
    }

    public synchronized void playMusic(String fileName) {
        if (musicFileName != null && musicFileName.equals(fileName) && music != null) {
            if (!music.isRunning()) {
                music.setFramePosition(0);
                music.loop(Clip.LOOP_CONTINUOUSLY);
            }
            return;
        }

        Clip nextMusic = loadClip(MUSIC_PATH, fileName, false, 0);
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
        if (playbackKeepAlive != null) {
            playbackKeepAlive.stop();
            playbackKeepAlive.close();
            playbackKeepAlive = null;
        }
        for (List<Clip> clips : soundEffects.values()) {
            closeClips(clips);
        }
        soundEffects.clear();
    }

    private Map<String, SoundEffectSettings> loadSoundEffectSettings(File soundEffectDirectory) {
        File settingsFile = new File(soundEffectDirectory, "effects.txt");
        Map<String, SoundEffectSettings> settings = new HashMap<>();
        if (!settingsFile.isFile()) {
            return settings;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }
                parseSoundEffectSettingLine(settings, trimmedLine, settingsFile, lineNumber);
            }
            return settings;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load sound effect settings: " + settingsFile, e);
        }
    }

    private void parseSoundEffectSettingLine(
            Map<String, SoundEffectSettings> settings, String line, File settingsFile, int lineNumber) {
        String[] tokens = line.split("\\s+");
        if (tokens[0].contains(File.separator) || tokens[0].contains("/")
                || !tokens[0].toLowerCase(java.util.Locale.ROOT).endsWith(".wav")) {
            throw new IllegalStateException(
                    "Expected a WAV filename followed by settings at " + settingsFile + ":" + lineNumber);
        }
        SoundEffectSettings effectSettings = new SoundEffectSettings();
        for (int i = 1; i < tokens.length; i++) {
            String[] setting = tokens[i].split("=", 2);
            if (setting.length != 2 || setting[1].isEmpty()) {
                throw new IllegalStateException(
                        "Expected key=value setting at " + settingsFile + ":" + lineNumber);
            }
            float value;
            try {
                value = Float.parseFloat(setting[1]);
            } catch (NumberFormatException e) {
                throw new IllegalStateException(
                        "Invalid number at " + settingsFile + ":" + lineNumber + ": " + setting[1], e);
            }
            switch (setting[0]) {
                case "gain":
                    if (effectSettings.gainDb != null) {
                        throw duplicateSetting(settingsFile, lineNumber, setting[0]);
                    }
                    effectSettings.gainDb = value;
                    break;
                case "pitchMin":
                    if (effectSettings.pitchMin != null) {
                        throw duplicateSetting(settingsFile, lineNumber, setting[0]);
                    }
                    effectSettings.pitchMin = value;
                    break;
                case "pitchMax":
                    if (effectSettings.pitchMax != null) {
                        throw duplicateSetting(settingsFile, lineNumber, setting[0]);
                    }
                    effectSettings.pitchMax = value;
                    break;
                default:
                    throw new IllegalStateException(
                            "Unknown setting at " + settingsFile + ":" + lineNumber + ": " + setting[0]);
            }
        }
        if (settings.put(tokens[0], effectSettings) != null) {
            throw new IllegalStateException(
                    "Duplicate sound effect settings at " + settingsFile + ":" + lineNumber + ": " + tokens[0]);
        }
    }

    private IllegalStateException duplicateSetting(File settingsFile, int lineNumber, String settingName) {
        return new IllegalStateException(
                "Duplicate setting at " + settingsFile + ":" + lineNumber + ": " + settingName);
    }

    private void validateGain(float gainDb) {
        if (Float.isNaN(gainDb) || Float.isInfinite(gainDb) || gainDb < -80 || gainDb > 6) {
            throw new IllegalArgumentException("Sound effect gain must be between -80 and 6 dB.");
        }
    }

    private void closeClips(List<Clip> clips) {
        for (Clip clip : clips) {
            clip.stop();
            clip.close();
        }
    }

    private void applySoundEffectGain(Clip clip, float gainDb) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(Math.max(
                    gainControl.getMinimum(),
                    Math.min(gainDb, gainControl.getMaximum())));
        }
    }

    private Clip loadClip(String audioPath, String fileName, boolean soundEffect, float pitchSemitones) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Audio file name must not be empty.");
        }

        File audioFile = new File(audioPath, fileName);
        Clip clip = null;
        try {
            try (AudioInputStream sourceStream = AudioSystem.getAudioInputStream(audioFile)) {
                AudioFormat sourceFormat = sourceStream.getFormat();
                int channels = sourceFormat.getChannels();
                float sampleRate = sourceFormat.getSampleRate();
                AudioFormat playbackFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        sampleRate,
                        16,
                        channels,
                        channels * 2,
                        sampleRate,
                        false);
                AudioInputStream convertedStream = sourceStream;
                if (!sourceFormat.matches(playbackFormat)) {
                    if (!AudioSystem.isConversionSupported(playbackFormat, sourceFormat)) {
                        throw new IllegalStateException(
                                "Unable to convert audio format for playback: " + sourceFormat);
                    }
                    convertedStream = AudioSystem.getAudioInputStream(playbackFormat, sourceStream);
                }
                try (AudioInputStream audioStream = convertedStream) {
                    byte[] audioData = readAudioData(audioStream);
                    if (pitchSemitones != 0) {
                        audioData = shiftPitch(audioData, playbackFormat, pitchSemitones);
                    }
                    clip = AudioSystem.getClip();
                    clip.open(playbackFormat, audioData, 0, audioData.length);
                }
            }
            if (soundEffect) {
                applySoundEffectGain(clip, soundEffectGains.getOrDefault(fileName, SOUND_EFFECT_GAIN_DB));
            }
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            if (clip != null) {
                clip.close();
            }
            throw new IllegalStateException("Unable to load audio file: " + audioFile, e);
        }
    }

    private void warmUpPlaybackLine(AudioFormat format) {
        int frameSize = format.getFrameSize();
        if (frameSize <= 0 || format.getSampleRate() <= 0
                || !format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
            return;
        }
        int frameCount = Math.max(1, Math.round(format.getSampleRate() * 0.05f));
        byte[] silence = new byte[frameCount * frameSize];
        try {
            playbackKeepAlive = AudioSystem.getClip();
            playbackKeepAlive.open(format, silence, 0, silence.length);
            playbackKeepAlive.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException e) {
            if (playbackKeepAlive != null) {
                playbackKeepAlive.close();
                playbackKeepAlive = null;
            }
            throw new IllegalStateException("Unable to initialize audio playback.", e);
        }
    }

    private byte[] readAudioData(AudioInputStream audioStream) throws IOException {
        ByteArrayOutputStream audioData = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = audioStream.read(buffer)) != -1) {
            audioData.write(buffer, 0, bytesRead);
        }
        return audioData.toByteArray();
    }

    private byte[] shiftPitch(byte[] audioData, AudioFormat format, float semitones) {
        int frameSize = format.getFrameSize();
        int channels = format.getChannels();
        if (frameSize != channels * 2 || audioData.length < frameSize
                || audioData.length % frameSize != 0) {
            throw new IllegalArgumentException("Audio data is not aligned to 16-bit PCM frames.");
        }
        int inputFrameCount = audioData.length / frameSize;
        double playbackRate = Math.pow(2.0, semitones / 12.0);
        long outputFrameCount = Math.max(1, Math.round(inputFrameCount / playbackRate));
        if (outputFrameCount > Integer.MAX_VALUE / frameSize) {
            throw new IllegalArgumentException("Pitch variation produces an audio clip that is too large.");
        }

        byte[] pitchedData = new byte[(int) outputFrameCount * frameSize];
        for (int outputFrame = 0; outputFrame < outputFrameCount; outputFrame++) {
            double inputPosition = outputFrame * playbackRate;
            int firstInputFrame = Math.min((int) inputPosition, inputFrameCount - 1);
            int secondInputFrame = Math.min(firstInputFrame + 1, inputFrameCount - 1);
            double interpolation = inputPosition - firstInputFrame;
            for (int channel = 0; channel < channels; channel++) {
                int firstSample = readSample(audioData, firstInputFrame * frameSize + channel * 2);
                int secondSample = readSample(audioData, secondInputFrame * frameSize + channel * 2);
                int sample = (int) Math.round(firstSample + (secondSample - firstSample) * interpolation);
                writeSample(pitchedData, outputFrame * frameSize + channel * 2, sample);
            }
        }
        return pitchedData;
    }

    private int readSample(byte[] audioData, int offset) {
        return (short) ((audioData[offset + 1] << 8) | (audioData[offset] & 0xff));
    }

    private void writeSample(byte[] audioData, int offset, int sample) {
        short boundedSample = (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, sample));
        audioData[offset] = (byte) (boundedSample & 0xff);
        audioData[offset + 1] = (byte) ((boundedSample >> 8) & 0xff);
    }

    private static final class PitchVariation {
        private final float minimumSemitones;
        private final float maximumSemitones;

        private PitchVariation(float minimumSemitones, float maximumSemitones) {
            this.minimumSemitones = minimumSemitones;
            this.maximumSemitones = maximumSemitones;
        }

        private boolean hasVariation() {
            return minimumSemitones != maximumSemitones;
        }

        private float getVariantSemitones(int index, int variantCount) {
            if (variantCount == 1) {
                return minimumSemitones;
            }
            float progress = index / (float) (variantCount - 1);
            return minimumSemitones + (maximumSemitones - minimumSemitones) * progress;
        }

        private float getRandomSemitones() {
            return minimumSemitones == maximumSemitones
                    ? minimumSemitones
                    : (float) ThreadLocalRandom.current().nextDouble(minimumSemitones, maximumSemitones);
        }
    }

    private static final class SoundEffectSettings {
        private Float gainDb;
        private Float pitchMin;
        private Float pitchMax;
    }
}
