package com.suzukiplan.emulator.nes.core;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * audio capturing service
 */
public class NESAudioCaptureService {
    private final NESView nesView;
    private final int interval;
    private final int bufferSize;
    private PipedInputStream inputStream = null;
    private PipedOutputStream outputStream = null;

    /**
     * create the audio capturing service
     *
     * @param nesView  `NESView` of creating audio capturing service
     * @param interval capturing interval
     */
    public NESAudioCaptureService(@Nullable NESView nesView, int interval) {
        this.nesView = nesView;
        this.interval = interval;
        this.bufferSize = (int) (interval * 4 / 1000.0 * 88200 * 2);
    }

    /**
     * open the pipe
     *
     * @return input stream of the pipe
     * @throws IOException I/O error
     */
    @Nullable
    public InputStream open() throws IOException {
        if (null == nesView) return null;
        close();
        inputStream = new PipedInputStream(bufferSize);
        outputStream = new PipedOutputStream(inputStream);
        nesView.setOnCaptureAudioListener(new NESView.OnCaptureAudioListener() {
            @Override
            public void onCaptureAudio(byte[] pcm) {
                if (null != outputStream) {
                    try {
                        outputStream.write(pcm);
                    } catch (IOException e) {
                        Logger.printStackTrace(e);
                    }
                }
            }
        }, interval, null);
        return inputStream;
    }

    /**
     * close the pipe
     */
    public void close() {
        if (null == nesView) return;
        nesView.setOnCaptureAudioListener(null);
        if (null != outputStream) {
            try {
                outputStream.close();
            } catch (IOException e) {
                Logger.printStackTrace(e);
            }
            outputStream = null;
        }
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Logger.printStackTrace(e);
            }
            inputStream = null;
        }
    }
}
