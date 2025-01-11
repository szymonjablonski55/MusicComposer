package com.music.consumer;

import com.music.dto.TrackEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TrackEventConsumer {

    @KafkaListener(topics = "${kafka.topic.tracks}", groupId = "music-group")
    public void listen(TrackEvent event) {
        System.out.println("TrackEventConsumer -> : " + event.getTrackId());
    }
}
