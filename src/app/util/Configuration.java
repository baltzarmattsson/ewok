package app.util;

import javafx.scene.paint.Color;

import java.util.HashMap;


public class Configuration {

    private HashMap<Integer, ButtonInfo> buttonInfo = new HashMap<Integer, ButtonInfo>();
    private int idleTimeInSeconds;
    private Color bgColor;
    private double firstColumnPercentWidth;
    private String homeScreenURL;
    private boolean firstButtonIsHomescreen;

    public Configuration(HashMap<Integer, ButtonInfo> buttonInfo, int idleTimeInSeconds, Color bgColor, double firstColumnPercentWidth, String homeScreenURL, boolean firstButtonIsHomescreen) {
        this.buttonInfo = buttonInfo;
        this.idleTimeInSeconds = idleTimeInSeconds;
        this.bgColor = bgColor;
        this.firstColumnPercentWidth = firstColumnPercentWidth;
        this.homeScreenURL = homeScreenURL;
        this.firstButtonIsHomescreen = firstButtonIsHomescreen;
    }

    public Configuration() {
    }

    public HashMap<Integer, ButtonInfo> getButtonInfo() {
        return buttonInfo;
    }

    public void setButtonInfo(HashMap<Integer, ButtonInfo> buttonInfo) {
        this.buttonInfo = buttonInfo;
    }

    public int getIdleTimeInSeconds() {
        return idleTimeInSeconds;
    }

    public void setIdleTimeInSeconds(int idleTimeInSeconds) {
        this.idleTimeInSeconds = idleTimeInSeconds;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public double getFirstColumnPercentWidth() {
        return firstColumnPercentWidth;
    }

    public void setFirstColumnPercentWidth(double firstColumnPercentWidth) {
        this.firstColumnPercentWidth = firstColumnPercentWidth;
    }

    public String getHomeScreenURL() {
        return homeScreenURL;
    }

    public void setHomeScreenURL(String homeScreenURL) {
        this.homeScreenURL = homeScreenURL;
    }

    public boolean isFirstButtonIsHomescreen() {
        return firstButtonIsHomescreen;
    }

    public void setFirstButtonIsHomescreen(boolean firstButtonIsHomescreen) {
        this.firstButtonIsHomescreen = firstButtonIsHomescreen;
    }
}
