package com.music.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Data
@Table(name = "music_tracks")
public class MusicTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "track_data", length = 100000)
    private String trackData;

    @Lob
    @Column(name = "mp4_data")
    private byte[] mp4Data;

    @Override
    public String toString() {
        return "MusicTrack{" +
                "id=" + id +
                ", trackData='" + trackData + '\'' +
                ", mp4Data=" + Arrays.toString(mp4Data) +
                ", createdAt=" + createdAt +
                '}';
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrackData() { return trackData; }
    public void setTrackData(String trackData) { this.trackData = trackData; }

    public byte[] getMp4Data() { return mp4Data; }
    public void setMp4Data(byte[] mp4Data) { this.mp4Data = mp4Data; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
