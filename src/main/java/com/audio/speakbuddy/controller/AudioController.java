package com.audio.speakbuddy.controller;

import com.audio.speakbuddy.service.AudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class AudioController implements AudioApi {
    private final AudioService audioService;

    public AudioController(@Autowired AudioService audioService) {
        this.audioService = audioService;
    }

    @Override
    public ResponseEntity<String> submit(String userId, String phraseId, MultipartFile audioFile) {
        log.info("Request received userId: {}, phraseId: {}, audioFile: {}", userId, phraseId, audioFile.getOriginalFilename());

        if (!StringUtils.hasText(userId) || !StringUtils.hasText(phraseId) || audioFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing request payload");
        }

        try {
            if (Boolean.FALSE.equals(audioService.saveAudio(userId, phraseId, audioFile))) {
                return ResponseEntity.ok("Audio failed to store");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok("Audio successfully stored");
    }

    @Override
    public ResponseEntity<?> get(String userId, String phraseId, String audioFormat) {
        log.info("Request received userId: {}, phraseId: {}, audioFormat: {}", userId, phraseId, audioFormat);
        try {
            Resource resource = audioService.getAudio(userId, phraseId, audioFormat);

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/" + audioFormat))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
