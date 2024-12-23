package com.audio.speakbuddy.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AudioService {
    Boolean saveAudio(String userId, String phraseId, MultipartFile audioFile) throws IOException;

    Resource getAudio(String userId, String phraseId, String audioFormat) throws IOException;
}
