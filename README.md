# SpeakBUDDY Take Home Test
https://docs.google.com/document/d/19IV7EREMiYK6amIYYONmTUIDR3VqjYmKED0Ht45-IDo/edit?tab=t.0

## App Specification

* Language: `Java 21`
* Framework: `Spring Boot 3.4.1-SNAPSHOT`
* Database: `PostgreSQL`

## Database Connection

* Host: `localhost`
* Port: `5432`
* Username: `username`
* Password: `password`
* Database: `speakbuddy`
* Table: `audio`

## External Dependencies

* Audio Converter: `FFMPEG`
* Link: https://www.ffmpeg.org/download.html

## APIs

* POST `/audio/user/{user_id}/phrase/{phrase_id}`
* GET `/audio/user/{user_id}/phrase/{phrase_id}/{audio_format}`

## Converter

* Right now the audio converter only support _MP3_ and _WAV_
* _MP3_ as the client input and output audio format
* _WAV_ as the server audio format

## Guide

* To run the application you need to run `docker-compose up`
* The application will listen to port `8080`
* Only run `./gradlew publishJar` if needed or if changes have been made before `docker-compose up`
* There is sample audio for testing in the resources directory `test_audio.mp3`

## Sample cURL

* Submit Audio (POST API)
```
curl --location 'localhost:8080/audio/user/111/phrase/user1' \
--form 'audio_file=@"./test_audio.mp3"'
```

* Retrieve Audio (GET API)
```

curl --location 'localhost:8080/audio/user/111/phrase/user1/mp3' -o './test_response_file_1_1.mp3'
```