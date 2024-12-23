package com.audio.speakbuddy.exception;

import java.io.IOException;

public class MissingAudioFileException extends IOException {
    public MissingAudioFileException(String message) {
        super(message);
    }
}
