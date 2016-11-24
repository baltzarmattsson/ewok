package app.util;


import app.model.ButtonInfo;
import app.model.ConfigFileSections;
import app.model.Configuration;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public static HashMap<Integer, String> parseConfigToString(Configuration configToBeParsed) {

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

        // Reading button color
        configText.put(indexCounter++, ConfigFileSections.BUTCOL_START.get());
        configText.put(indexCounter++, Util.colorToHex(configToBeParsed.getButtonColor()));
        configText.put(indexCounter++, ConfigFileSections.BUTCOL_END.get());

        // Reading button text color
        configText.put(indexCounter++, ConfigFileSections.BUTTEXTCOL_START.get());
        configText.put(indexCounter++, Util.colorToHex(configToBeParsed.getButtonTextColor()));
        configText.put(indexCounter++, ConfigFileSections.BUTTEXTCOL_END.get());

        // Reading button font
        configText.put(indexCounter++, ConfigFileSections.BUTFONT_START.get());
        configText.put(indexCounter++, Util.fontToString(configToBeParsed.getButtonFont()));
        configText.put(indexCounter++, ConfigFileSections.BUTFONT_END.get());

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
        configText.put(indexCounter++, configToBeParsed.getHomeScreenURL() == null ? "" : configToBeParsed.getHomeScreenURL());
        configText.put(indexCounter++, ConfigFileSections.HOMESCREENURL_END.get());

        return configText;
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

    public static Configuration parseStringToConfig(HashMap<Integer, String> configAsStrings) {
        Configuration parsedConfig = new Configuration();

        String currentLine = null;
        for (int i = 0; i < configAsStrings.size(); i++) {
            currentLine = configAsStrings.get(i);

            // Read buttoninfo
            HashMap<Integer, String> biAsText = new HashMap<Integer, String>();
            int biAsTextIndexCounter = 0;

            if (currentLine.equals(ConfigFileSections.BUTINF_START.get())) {
                biAsText.put(biAsTextIndexCounter++, ConfigFileSections.BI_START.get());

                // As long as the read line is not the end of button info, we add the line to the biAsText
                while (currentLine.contains(ConfigFileSections.BUTINF_END.get()) == false) {
                    biAsText.put(biAsTextIndexCounter++, currentLine);
                    currentLine = configAsStrings.get(++i);
                }
                // Now all, or none, button info as texts has been added, we mark the end
                biAsText.put(biAsTextIndexCounter++, ConfigFileSections.BI_END.get());

                // Using the stringToButtonInfo method top parse it to button info-objects, and assigning to our config instance
                parsedConfig.setButtonInfo(Util.stringToButtonInfo(biAsText));
            }

            // Read idle time
            if (currentLine.equals(ConfigFileSections.IDLE_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.IDLE_END.get()) == false) {
                    int idleTimeInSeconds = Integer.parseInt(currentLine);
                    parsedConfig.setIdleTimeInSeconds(idleTimeInSeconds);
                }
            }

            // Read button color
            if (currentLine.equals(ConfigFileSections.BUTCOL_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.BUTCOL_END.get()) == false) {
                    Color buttonColor = Util.hexToColor(currentLine);
                    parsedConfig.setButtonColor(buttonColor);
                }
            }

            // Read button text color
            if (currentLine.equals(ConfigFileSections.BUTTEXTCOL_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.BUTTEXTCOL_END.get()) == false) {
                    Color butTextCol = Util.hexToColor(currentLine);
                    parsedConfig.setButtonTextColor(butTextCol);
                }
            }

            // Read button font
            if (currentLine.equals(ConfigFileSections.BUTFONT_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.BUTFONT_END.get()) == false) {
                    Font butFont = Util.stringToFont(currentLine);
                    parsedConfig.setButtonFont(butFont);
                }
            }

            // Read bg color
            if (currentLine.equals(ConfigFileSections.BGCOL_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.BGCOL_END.get()) == false) {
                    Color bgColor = Util.hexToColor(currentLine);
                    parsedConfig.setBgColor(bgColor);
                }
            }

            // Read first col width
            if (currentLine.equals(ConfigFileSections.FIRSTCOLWIDTH_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.FIRSTCOLWIDTH_END.get()) == false) {
                    double firstColWidth = Double.parseDouble(currentLine);
                    parsedConfig.setFirstColumnPercentWidth(firstColWidth);
                }
            }

            // Read isFirstButtonHomescreen
            if (currentLine.equals(ConfigFileSections.FIRSTBUTTONISHOME_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.FIRSTBUTTONISHOME_END.get()) == false) {
                    boolean firstButtonIsHome = Boolean.parseBoolean(currentLine);
                    parsedConfig.setFirstButtonIsHomescreen(firstButtonIsHome);
                }
            }
            // Read homescreen URL
            if (currentLine.equals(ConfigFileSections.HOMESCREENURL_START.get())) {
                currentLine = configAsStrings.get(++i);
                if (currentLine.equals(ConfigFileSections.HOMESCREENURL_END.get()) == false) {
                    parsedConfig.setHomeScreenURL(currentLine);
                }
            }
        }

        return parsedConfig;
    }


    private static HashMap<Integer, ButtonInfo> stringToButtonInfo(HashMap<Integer, String> biAsText) {
        HashMap<Integer, ButtonInfo> buttonInfos = new HashMap<Integer, ButtonInfo>();

        for (int i = 0; i < biAsText.size(); i++) {
            String currentLine = biAsText.get(i);

            if (currentLine.equals(ConfigFileSections.BI_START.get())) {

                currentLine = biAsText.get(++i);
                String index = null, text = null, url = null;
                ButtonInfo bi = null;

                while (!currentLine.equals(ConfigFileSections.BI_END.get())) {
                    if (currentLine.contains(ConfigFileSections.BI_INDEX.get()))
                        index = currentLine.replaceFirst(ConfigFileSections.BI_INDEX.get(), "");
                    else if (currentLine.contains(ConfigFileSections.BI_TEXT.get()))
                        text = currentLine.replaceFirst(ConfigFileSections.BI_TEXT.get(), "");
                    else if (currentLine.contains(ConfigFileSections.BI_URL.get()))
                        url = currentLine.replaceFirst(ConfigFileSections.BI_URL.get(), "");
                    currentLine = biAsText.get(++i);
                }
                if (index != null && text != null && url != null) {
                    bi = new ButtonInfo(text, url);
                    buttonInfos.put(Integer.parseInt(index), bi);
                }
            }

        }
        return buttonInfos;
    }


    public static String colorToHex(Color c) {
        String hex = String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
        return hex;
    }

    public static Color hexToColor(String hex) {
        return Color.web(hex);
    }

    public static String fontToString(Font f) {
//    family style size
        String fontAsString = "";
        fontAsString += f.getName() + "\t" + f.getSize();
        return fontAsString;
    }

    public static Font stringToFont(String s) {
        String[] stringSplit = s.split("\t");
        Font retFont = new Font(stringSplit[0], Double.parseDouble(stringSplit[1]));
        return retFont;
    }

}
