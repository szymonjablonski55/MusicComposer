package com.music.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
public class MusicController {
    @RequestMapping("/music")
    public String music() {
        return "music_composer";
    }
}
