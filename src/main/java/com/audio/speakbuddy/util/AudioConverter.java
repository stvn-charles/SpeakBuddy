package com.audio.speakbuddy.util;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioConverter {
    private static Map<String, String> audioFormat = new HashMap<>();
    private static Map<String, String> audioCodec = new HashMap<>();
    static {
        audioFormat.put(".wav", "wav");
        audioFormat.put(".mp3", "mp3");

        audioCodec.put(".wav", "pcm_s16le");
        audioCodec.put(".mp3", "libmp3lame");
    }

    private static String getFileExtension(File file) {
        String originalName = file.getName();
        return originalName.substring(originalName.lastIndexOf("."));
    }

    public static void convertAudio(File inputFile, File outputFile) throws IOException {
        String fileExtension = getFileExtension(outputFile);

        FFmpeg ffmpeg = new FFmpeg();
        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(inputFile.getAbsolutePath())
            .addOutput(outputFile.getAbsolutePath())
            .setFormat(audioFormat.get(fileExtension))
            .setAudioCodec(audioCodec.get(fileExtension))
            .setAudioChannels(2)
            .setAudioSampleRate(44100)
            .done();
        ffmpeg.run(builder);
    }
}
