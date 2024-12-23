package com.audio.speakbuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "audio")
public class Audio {
    @EmbeddedId
    private AudioId id;

    @Column(name = "audio_path")
    private String audioPath;

    public Audio() {
    }

    public AudioId getId() {
        return id;
    }

    public void setId(AudioId id) {
        this.id = id;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
