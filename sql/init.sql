CREATE TABLE IF NOT EXISTS audio (
    user_id VARCHAR(100) NOT NULL,
    phrase_id VARCHAR(100) NOT NULL,
    audio_path VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(user_id, phrase_id)
);
