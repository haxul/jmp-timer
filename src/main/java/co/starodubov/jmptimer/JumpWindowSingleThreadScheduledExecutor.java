package co.starodubov.jmptimer;

import java.util.concurrent.*;

public class JumpWindowSingleThreadScheduledExecutor {
    private final int timePeriod;
    private final int winSize;
    private final ScheduledExecutorService tickPool = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService actionPool = Executors.newSingleThreadExecutor();

    private int lt;
    private int rt;
    private int actionTime;
    private volatile boolean stop = false;
    private int tick = 0;

    public void reset() {
        init();
        stop = false;
    }

    public void stop() {
        stop = true;
    }

    private void init() {
        lt = 0;
        rt = lt + this.winSize - 1;
        actionTime = getNextActionTime();
    }

    public JumpWindowSingleThreadScheduledExecutor(final int timePeriod, final int winSize) {
        this.timePeriod = timePeriod;
        this.winSize = winSize;
        init();
        System.out.println("next action time " + actionTime);
        System.out.println("lt " + lt);
        System.out.println("rt " + rt);
    }

    private int getNextActionTime() {
        return ThreadLocalRandom.current().nextInt(lt, rt + 1);
    }

    public void exec(final Runnable task) {
        if (task == null) throw new IllegalArgumentException("task is null");

        tickPool.scheduleAtFixedRate(() -> {
            if (stop) {
                tickPool.shutdown();
                return;
            }

            if (tick == actionTime) {
                actionPool.execute(task);
            }

            if (tick >= rt) {
                lt = rt;
                rt += winSize - 1;
                if (rt > timePeriod) {
                    rt = timePeriod;
                }
                actionTime = getNextActionTime();

                System.out.println("next action time " + actionTime);
                System.out.println("lt " + lt);
                System.out.println("rt " + rt);
            }
            tick++;
            System.out.println(tick + " sec"); //DEV ONLY
        }, 0, 1, TimeUnit.SECONDS);
    }
}
