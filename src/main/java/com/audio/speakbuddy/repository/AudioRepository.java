package com.audio.speakbuddy.repository;

import com.audio.speakbuddy.model.Audio;
import com.audio.speakbuddy.model.AudioId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepository extends JpaRepository<Audio, AudioId> {
}
