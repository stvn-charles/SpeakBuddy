package com.audio.speakbuddy.exception;

import java.io.IOException;

public class UnsupportedAudioFormatException extends IOException {
    public UnsupportedAudioFormatException(String message) {
        super(message);
    }
}
