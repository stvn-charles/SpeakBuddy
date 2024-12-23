package com.audio.speakbuddy.service;

import com.audio.speakbuddy.exception.FailedAudioConversionException;
import com.audio.speakbuddy.exception.MissingAudioFileException;
import com.audio.speakbuddy.exception.UnsupportedAudioFormatException;
import com.audio.speakbuddy.model.Audio;
import com.audio.speakbuddy.model.AudioId;
import com.audio.speakbuddy.repository.AudioRepository;
import com.audio.speakbuddy.service.impl.AudioServiceImpl;
import com.audio.speakbuddy.util.AudioConverter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestAudioService {
    @Mock
    private AudioRepository audioRepository;

    @InjectMocks
    private AudioServiceImpl audioService;

    @Value("classpath:test_audio.mp3")
    private Resource testResource;

    @Test
    void testSaveAudio_Success() throws IOException {
        String userId = "user1";
        String phraseId = "phrase1";
        MockMultipartFile audioFile = new MockMultipartFile(
            "file", "audio.mp3", "audio/mpeg", testResource.getContentAsByteArray()
        );

        AudioId audioId = new AudioId();
        audioId.setUserId(userId);
        audioId.setPhraseId(phraseId);

        Audio audio = new Audio();
        audio.setId(audioId);
        audio.setAudioPath(System.getProperty("user.dir") + "/audio/" + userId + "_" + phraseId + ".wav");

        when(audioRepository.save(any(Audio.class))).thenReturn(audio);

        Boolean result = audioService.saveAudio(userId, phraseId, audioFile);

        assertTrue(result);
        verify(audioRepository, times(1)).save(any(Audio.class));
    }

    @Test
    void testSaveAudio_FailedToConvertAudio() throws IOException {
        String userId = "user1";
        String phraseId = "phrase1";
        MockMultipartFile audioFile = new MockMultipartFile(
            "file", "audio.mp3", "audio/mpeg", testResource.getContentAsByteArray()
        );

        try (MockedStatic<AudioConverter> mockedAudioConverter = Mockito.mockStatic(AudioConverter.class)) {
            mockedAudioConverter.when(() -> AudioConverter.convertAudio(any(File.class), any(File.class))).thenThrow(IOException.class);

            assertThrows(FailedAudioConversionException.class, () -> audioService.saveAudio(userId, phraseId, audioFile));

            verify(audioRepository, never()).save(any(Audio.class));
        }
    }

    @Test
    void testGetAudio_Success() throws IOException {
        String userId = "user1";
        String phraseId = "phrase1";
        String audioFormat = "mp3";

        String filePath = System.getProperty("user.dir") + "/audio/" + userId + "_" + phraseId + ".wav";
        File tempFile = File.createTempFile("temp", ".mp3");
        tempFile.deleteOnExit();

        AudioId audioId = new AudioId();
        audioId.setUserId(userId);
        audioId.setPhraseId(phraseId);

        Audio audio = new Audio();
        audio.setId(audioId);
        audio.setAudioPath(filePath);

        when(audioRepository.findById(any(AudioId.class))).thenReturn(Optional.of(audio));

        Resource result = audioService.getAudio(userId, phraseId, audioFormat);

        assertNotNull(result);
        verify(audioRepository, times(1)).findById(any(AudioId.class));
    }

    @Test
    void testGetAudio_FileNotFoundInDatabase() {
        String userId = "user1";
        String phraseId = "phrase1";
        String audioFormat = "mp3";

        when(audioRepository.findById(any(AudioId.class))).thenReturn(Optional.empty());

        assertThrows(MissingAudioFileException.class, () -> audioService.getAudio(userId, phraseId, audioFormat));

        verify(audioRepository, times(1)).findById(any(AudioId.class));
    }

    @Test
    void testGetAudio_UnsupportedFormat() {
        String userId = "user1";
        String phraseId = "phrase1";
        String audioFormat = "wav";

        assertThrows(UnsupportedAudioFormatException.class, () -> audioService.getAudio(userId, phraseId, audioFormat));

        verify(audioRepository, never()).findById(any(AudioId.class));
    }
}
