package app.util;

import app.Main;
import app.view.ViewController;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

// Thread: Used for listening to user inactivity.

public class IdleListener extends Thread {

    private final long IDLE_TIME_THRESHOLD_SECONDS = ConfigReader.getIdleTimeInSeconds();
    private long timeAtLastAction = System.currentTimeMillis();

    private ViewController controller;

    public IdleListener(ViewController controller) {
        this.setDaemon(true);
        this.start();
        this.controller = controller;
    }

    // Listens for user inactivity. If user is idle for IDLE_TIME_THRESHOLD_SECONDS, it requests
    // the default idle action from the ViewController
    public void run() {
        long timeSinceLastAction = timeAtLastAction - System.currentTimeMillis();

        while (TimeUnit.MILLISECONDS.toSeconds(timeSinceLastAction) < IDLE_TIME_THRESHOLD_SECONDS) {
            try {
                this.sleep(TimeUnit.SECONDS.toMillis(1));
                timeSinceLastAction = System.currentTimeMillis() - timeAtLastAction;
                System.out.println(TimeUnit.MILLISECONDS.toSeconds(timeSinceLastAction) + "s since last action");
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Requesting idle action from Controller @IdleListener:38");
        this.controller.performIdleAction();

    }

    public void resetTimeAtLastAction() {
        this.timeAtLastAction = System.currentTimeMillis();
    }
}
