package com.music.services;

import org.springframework.stereotype.Service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
@Service
public class MusicService {
    @Value("${ffmpeg.path:C:\\ProgramData\\chocolatey\\bin\\ffmpeg.exe}")
    private String ffmpegPath;

    public byte[] convertToMp4(byte[] wavData) throws Exception {
        File inputFile = File.createTempFile("input", ".wav");
        File outputFile = File.createTempFile("output", ".mp4");

        try {
            try (FileOutputStream fos = new FileOutputStream(inputFile)) {
                fos.write(wavData);
            }

            FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputFile.getAbsolutePath())
                    .overrideOutputFiles(true)
                    .addOutput(outputFile.getAbsolutePath())
                    .setAudioCodec("aac")
                    .setAudioBitRate(128000)
                    .setAudioChannels(2)
                    .setAudioSampleRate(44100)
                    .setFormat("mp4")
                    .done();

            // Execute FFmpeg command
            executor.createJob(builder).run();

            // Read the output MP4 file into a byte array
            byte[] mp4Data = new byte[(int) outputFile.length()];
            try (FileInputStream fis = new FileInputStream(outputFile)) {
                fis.read(mp4Data);
            }

            return mp4Data;
        } finally {
            // Clean up by deleting temporary files after conversion
            inputFile.delete();
            outputFile.delete();
        }
    }
}
