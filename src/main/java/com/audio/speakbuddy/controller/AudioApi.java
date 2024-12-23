package com.audio.speakbuddy.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/audio/user")
public interface AudioApi {
    @PostMapping("/{user_id}/phrase/{phrase_id}")
    ResponseEntity<String> submit(@PathVariable("user_id") String userId,
                                  @PathVariable("phrase_id") String phraseId,
                                  @RequestParam("audio_file") MultipartFile audioFile);

    @GetMapping("/{user_id}/phrase/{phrase_id}/{audio_format}")
    ResponseEntity<?> get(@PathVariable("user_id") String userId,
                                 @PathVariable("phrase_id") String phraseId,
                                 @PathVariable("audio_format") String audioFormat);
}
