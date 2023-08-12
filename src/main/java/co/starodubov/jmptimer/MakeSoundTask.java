package co.starodubov.jmptimer;

import javafx.scene.layout.Pane;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class MakeSoundTask {
    private final int timePeriod;
    private final int winSize;
    private final File soundFile;

    private final ScheduledExecutorService tickPool = Executors.newSingleThreadScheduledExecutor(r -> {
        var t = new Thread(r);
        t.setName("tick-thread");
        t.setDaemon(true);
        return t;
    });

    private int lt;
    private int rt;
    private int actionTime;
    private volatile boolean cancel = false;
    private int tick = 0;

    public void cancel() {
        cancel = true;
    }

    private void init() {
        lt = 0;
        rt = lt + this.winSize - 1;
        actionTime = getNextActionTime();
    }

    public MakeSoundTask(final int timePeriod, final int winSize, final File soundFile) {
        this.timePeriod = timePeriod;
        this.winSize = winSize;
        this.soundFile = soundFile;
        init();
        System.out.println("next action time " + actionTime);
        System.out.println("lt " + lt);
        System.out.println("rt " + rt);
    }

    private int getNextActionTime() {
        return ThreadLocalRandom.current().nextInt(lt, rt + 1);
    }

    private void close(AudioInputStream stream) {
        if (stream == null) {
            System.out.println("stream is null");
            return;
        }
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AudioInputStream getAudioStream() {
        try {
            return getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Clip getClip() {
        try {
            return AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void openClip(Clip clip, AudioInputStream stream) {
        try {
            clip.open(stream);
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startAsync() {
        final var as = getAudioStream();
        final var clip = getClip();
        openClip(clip, as);
        final var countDownLatch = new CountDownLatch(1);
        final var superVisor = new Thread(() -> {
            try {
                tickPool.scheduleAtFixedRate(() -> {
                    if (lt >= timePeriod || cancel) {
                        tickPool.shutdown();
                        countDownLatch.countDown();
                        return;
                    }

                    if (tick == actionTime) {
                        clip.setMicrosecondPosition(0);
                        clip.start();
                        System.out.println("sound");
                    }

                    if (tick >= rt) {
                        lt = rt;
                        rt += winSize - 1;
                        if (rt > timePeriod) {
                            rt = timePeriod;
                        }
                        actionTime = getNextActionTime();

                        System.out.println("-------------- next action time " + actionTime);
                        System.out.println("-------------- lt " + lt);
                        System.out.println("-------------- rt " + rt);
                    }
                    tick++;
                    System.out.println(tick + " sec"); //DEV ONLY
                }, 0, 1, TimeUnit.SECONDS);
                countDownLatch.await(10, TimeUnit.MINUTES);
                System.out.println("supervisor is done with task");
            } catch (Exception e) {
                System.err.println(e);
                tickPool.shutdown();
            } finally {
                close(as);
                if (clip != null) clip.close();
                System.out.println("clean resources");
            }
        });

        superVisor.setDaemon(true);
        superVisor.setName("supervisor-thread");
        superVisor.start();
    }
}
