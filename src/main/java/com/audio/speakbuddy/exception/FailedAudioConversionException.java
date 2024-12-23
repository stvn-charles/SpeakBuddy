package com.audio.speakbuddy.exception;

import java.io.IOException;

public class FailedAudioConversionException extends IOException {
    public FailedAudioConversionException(String message) {
        super(message);
    }
}
