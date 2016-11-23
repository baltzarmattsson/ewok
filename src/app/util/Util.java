package app.util;


import app.model.ButtonInfo;
import app.model.ConfigFileSections;
import app.model.Configuration;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public static HashMap<Integer, String> parseConfigToString(Configuration configToBeParsed) {

//        private HashMap<Integer, ButtonInfo> buttonInfo = new HashMap<Integer, ButtonInfo>();
//        private int idleTimeInSeconds;
//        private Color bgColor;
//        private double firstColumnPercentWidth;
//        private String homeScreenURL;
//        private boolean firstButtonIsHomescreen;

        HashMap<Integer, String> configText = new HashMap<Integer, String>();
        int indexCounter = 0;

        // Reading buttoninfo
        configText.put(indexCounter++, ConfigFileSections.BUTINF_START.get());

        HashMap<Integer, String> biAsText = buttonInfoToString(configToBeParsed.getButtonInfo());
        for (int i = 0; i < biAsText.size(); i++) {
            configText.put(indexCounter++, biAsText.get(i));
        }

        configText.put(indexCounter++, ConfigFileSections.BUTINF_END.get());

        // Reading idle time
        configText.put(indexCounter++, ConfigFileSections.IDLE_START.get());
        configText.put(indexCounter++, Integer.toString(configToBeParsed.getIdleTimeInSeconds()));
        configText.put(indexCounter++, ConfigFileSections.IDLE_END.get());

        // Reading bg color
        configText.put(indexCounter++, ConfigFileSections.BGCOL_START.get());
        configText.put(indexCounter++, Util.colorToHex(configToBeParsed.getBgColor()));
        configText.put(indexCounter++, ConfigFileSections.BGCOL_END.get());

        // Reading first col width
        configText.put(indexCounter++, ConfigFileSections.FIRSTCOLWIDTH_START.get());
        configText.put(indexCounter++, Double.toString(configToBeParsed.getFirstColumnPercentWidth()));
        configText.put(indexCounter++, ConfigFileSections.FIRSTCOLWIDTH_END.get());

        // Reading is first button homescreen
        configText.put(indexCounter++, ConfigFileSections.FIRSTBUTTONISHOME_START.get());
        configText.put(indexCounter++, Boolean.toString(configToBeParsed.isFirstButtonHomescreen()));
        configText.put(indexCounter++, ConfigFileSections.FIRSTBUTTONISHOME_END.get());

        // Reading homescreen url
        configText.put(indexCounter++, ConfigFileSections.HOMESCREENURL_START.get());
        configText.put(indexCounter++, configToBeParsed.getHomeScreenURL());
        configText.put(indexCounter++, ConfigFileSections.HOMESCREENURL_END.get());

        return null;
    }

    private static HashMap<Integer, String> buttonInfoToString(HashMap<Integer, ButtonInfo> buttonInfos) {
        HashMap<Integer, String> biAsText = new HashMap<Integer, String>();
        int indexCounter = 0;

        for (Map.Entry<Integer, ButtonInfo> map : buttonInfos.entrySet()) {
            ButtonInfo bi = map.getValue();

            String index, text, url;
            index = Integer.toString(map.getKey());
            text = bi.getText();
            url = bi.getURL();

            biAsText.put(indexCounter++, ConfigFileSections.BI_START.get());
            biAsText.put(indexCounter++, ConfigFileSections.BI_INDEX.get() + index);
            biAsText.put(indexCounter++, ConfigFileSections.BI_TEXT.get() + text);
            biAsText.put(indexCounter++, ConfigFileSections.BI_URL.get() + url);
            biAsText.put(indexCounter++, ConfigFileSections.BI_END.get());
        }

        return biAsText;
    }

    // TODO stringToButton, stringToConfig


    public static String colorToHex(Color c) {
        String hex = String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
        return hex;
    }

}
