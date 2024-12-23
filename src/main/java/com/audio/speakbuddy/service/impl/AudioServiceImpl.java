package com.audio.speakbuddy.service.impl;

import com.audio.speakbuddy.exception.FailedAudioConversionException;
import com.audio.speakbuddy.exception.MissingAudioFileException;
import com.audio.speakbuddy.exception.UnsupportedAudioFormatException;
import com.audio.speakbuddy.model.Audio;
import com.audio.speakbuddy.model.AudioId;
import com.audio.speakbuddy.repository.AudioRepository;
import com.audio.speakbuddy.service.AudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.audio.speakbuddy.util.AudioConverter.convertAudio;

@Service
@Slf4j
public class AudioServiceImpl implements AudioService {
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String AUDIO_PATH = System.getProperty("user.dir") + "/audio";
    private static final String SERVER_AUDIO_FORMAT = ".wav";
    private static final String CLIENT_AUDIO_FORMAT = ".mp3";

    private final AudioRepository audioRepository;

    public AudioServiceImpl(@Autowired AudioRepository audioRepository) {
        this.audioRepository = audioRepository;
    }

    @Override
    public Boolean saveAudio(String userId, String phraseId, MultipartFile audioFile) throws IOException {
        String outputFileName = String.format("%s_%s%s", userId, phraseId, SERVER_AUDIO_FORMAT);
        String outputFilePath = AUDIO_PATH + File.separator + outputFileName;

        try {
            File inputFile = File.createTempFile("tempInput", CLIENT_AUDIO_FORMAT);
            audioFile.transferTo(inputFile);
            File outputFile = new File(AUDIO_PATH, outputFileName);
            convertAudio(inputFile, outputFile);
        } catch (IOException e) {
            log.error("Failed to convert the audio, userId: {}, phraseId: {}, audioFile: {}", userId, phraseId, audioFile.getOriginalFilename(), e);
            throw new FailedAudioConversionException("Failed to convert the audio");
        }

        AudioId audioId = new AudioId();
        audioId.setUserId(userId);
        audioId.setPhraseId(phraseId);

        Audio audio = new Audio();
        audio.setId(audioId);
        audio.setAudioPath(outputFilePath);

        try {
            audioRepository.save(audio);
        } catch (Exception e) {
            log.error("Failed to insert into database, userId: {}, phraseId: {}", userId, phraseId, e);
            return false;
        }
        return true;
    }

    @Override
    public Resource getAudio(String userId, String phraseId, String audioFormat) throws IOException {
        if (!CLIENT_AUDIO_FORMAT.toLowerCase().contains(audioFormat.toLowerCase())) {
            log.error("Unsupported output file format");
            throw new UnsupportedAudioFormatException("Application only supports mp3 for now");
        }

        AudioId audioId = new AudioId();
        audioId.setUserId(userId);
        audioId.setPhraseId(phraseId);

        Optional<Audio> audio = audioRepository.findById(audioId);

        if (audio.isEmpty()) {
            throw new MissingAudioFileException("File is not exist in database");
        }

        String filePath = audio.get().getAudioPath();
        File inputFile = new File(filePath);
        File outputFile = new File(TEMP_DIR + String.format("%s_%s%s", userId, phraseId, CLIENT_AUDIO_FORMAT));

        try{
            convertAudio(inputFile, outputFile);
        } catch (IOException e) {
            log.error("Failed to convert the audio, userId: {}, phraseId: {}, audioFile: {}", userId, phraseId, audio.get().getAudioPath(), e);
            throw new FailedAudioConversionException("Failed to convert the audio");
        }

        Resource resource = new UrlResource(outputFile.toURI());
        if (!resource.exists()) {
            throw new MissingAudioFileException("File is not exist");
        }
        return resource;
    }
}
