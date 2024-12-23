package com.audio.speakbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeakBuddyApplication {

	public static void main(String[] args) {
		System.setProperty("ffmpeg.location", "/usr/bin/ffmpeg");
		SpringApplication.run(SpeakBuddyApplication.class, args);
	}

}
