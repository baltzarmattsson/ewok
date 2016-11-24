package app.util;

import app.Main;
import app.view.ViewController;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

// Thread: Used for listening to user inactivity.

public class IdleListener extends Thread {

    private long IDLE_TIME_THRESHOLD_SECONDS;
    private long timeAtLastAction;
    private boolean stop;

    private ViewController controller;

    public IdleListener(ViewController controller, int idleTimeInSeconds) {
        this.stop = false;
        this.setDaemon(true);
        this.start();
        this.controller = controller;
        this.IDLE_TIME_THRESHOLD_SECONDS = idleTimeInSeconds;
        this.timeAtLastAction = System.currentTimeMillis();
    }

    // Listens for user inactivity. If user is idle for IDLE_TIME_THRESHOLD_SECONDS, it requests
    // the default idle action from the ViewController
    public void run() {
        long timeSinceLastAction = timeAtLastAction - System.currentTimeMillis();

        while (TimeUnit.MILLISECONDS.toSeconds(timeSinceLastAction) < IDLE_TIME_THRESHOLD_SECONDS && this.stop == false) {
            try {
                this.sleep(TimeUnit.SECONDS.toMillis(1));
                timeSinceLastAction = System.currentTimeMillis() - timeAtLastAction;
//                System.out.print(TimeUnit.MILLISECONDS.toSeconds(timeSinceLastAction) + "s ");
            } catch (InterruptedException e) {
            }
        }
//        System.out.println("Requesting idle action from Controller @IdleListener:38");
        this.controller.performIdleAction();
    }

    public void resetTimeAtLastAction() {
        this.timeAtLastAction = System.currentTimeMillis();
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
