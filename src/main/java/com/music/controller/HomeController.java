package com.music.controller;

import com.music.dto.TrackEvent;
import com.music.model.MusicTrack;
import com.music.repository.MusicTrackRepository;
import com.music.services.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Controller
public class HomeController {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.tracks}")
    private String tracksTopic;

    public HomeController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    private MusicTrackRepository musicTrackRepository;

    @Autowired
    private MusicService musicService;

    @RequestMapping(value = {"", "/", "/home"})

    public String home(Model model) {

        return "home.html";

    }

    @RequestMapping("/music")
    public String music() {
        return "music_composer";
    }


    @PostMapping("/save-track")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> saveTrack(@RequestParam("audioData") MultipartFile audioFile,
                                       @RequestParam("trackData") String trackData) {
        try {
            MusicTrack track = new MusicTrack();
            track.setTrackData(trackData);
            track.setCreatedAt(LocalDateTime.now());

            // Convert WAV to MP4
            byte[] mp4Data = musicService.convertToMp4(audioFile.getBytes());
            track.setMp4Data(mp4Data);

            TrackEvent event = new TrackEvent();
            event.setTrackId(1L);
            event.setTrackData(trackData);
            event.setCreatedAt(track.getCreatedAt());
            event.setEventType("TRACK_CREATED");
            kafkaTemplate.send(tracksTopic, String.valueOf(1), event);

            musicTrackRepository.save(track);
            return ResponseEntity.ok().body(track.getId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving track: " + e.getMessage());
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadTrack(@PathVariable Long id) {
        MusicTrack track = musicTrackRepository.findById(id).orElse(null);
        if (track == null || track.getMp4Data() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mp4"));
        headers.setContentDispositionFormData("attachment", "track-" + id + ".mp4");

        return ResponseEntity.ok()
                .headers(headers)
                .body(track.getMp4Data());
    }
}
