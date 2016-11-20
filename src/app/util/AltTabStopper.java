package app.util;

import sun.jvm.hotspot.utilities.WorkerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AltTabStopper implements Runnable
{
    private boolean working = true;
    private boolean isWindows;
    private boolean isMac;

    public AltTabStopper(boolean isWindows, boolean isMac) {
        this.isWindows = isWindows;
        this.isMac = isMac;
    }

    public void stop() {
        working = false;
    }

    public static AltTabStopper create() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        boolean isMac = (isWindows) ? false : System.getProperty("os.name").toLowerCase().contains("mac") ? true : false;
        System.out.println("isMac " + isMac);
        AltTabStopper stopper = new AltTabStopper(isWindows, isMac);
        new Thread(stopper, "Alt-Tab Stopper").start();
        return stopper;
    }

    public void run() {
        try {
            Robot robot = new Robot();
            while (working) {
                System.out.println("working");
//                robot.keyRelease(KeyEvent.VK_ALT);
//                robot.keyRelease(KeyEvent.VK_TAB);
//                if (this.isWindows)
//                    robot.keyRelease(KeyEvent.VK_WINDOWS);
//                else if (this.isMac)
//                    robot.keyRelease(KeyEvent.VK_META);
                try { robot.delay(10); } catch(Exception e) {}
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

}