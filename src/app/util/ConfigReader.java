package app.util;

// Used to read config from a file and set the UI and the idle-timer accordingly

import app.Main;
import app.model.Configuration;

import javax.swing.text.AttributeSet;
import javax.swing.text.html.CSS;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


public class ConfigReader {

    private static Configuration configInstance;
    public static final String settingsPwd = "hej123";

    private static final String fileName = "config.txt";
    private static final String filePath = "config/";

    public static void readConfigurationFile() {

        File jarFile = null;
        String finalPath = null;
        try {

            BufferedReader br;

            // Used for developing
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(Main.class.getResource("config/config.txt").getFile()), StandardCharsets.UTF_8));

//            // Used from the jarfile
            jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            finalPath = jarFile.getParent() + File.separator + filePath + File.separator + fileName;
            br = new BufferedReader(new FileReader(new File(finalPath)));

            String s;
            HashMap<Integer, String> configAsText = new HashMap<Integer, String>();
            int indexCounter = 0;

            while ((s = br.readLine()) != null) {
                configAsText.put(indexCounter++, s);
            }
            ConfigReader.configInstance = Util.parseStringToConfig(configAsText);

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfigurationFile(Configuration config) {
        BufferedWriter bw = null;
        try {
            // Used for developing
//             bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Main.class.getResource("config/config.txt").getFile()), StandardCharsets.UTF_8));

//            // Used from the jarfile
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String finalPath = jarFile.getParent() + File.separator + filePath + File.separator + fileName;
            bw = new BufferedWriter(new FileWriter(new File(finalPath)));


            // Kolla ifall config är null
            HashMap<Integer, String> configAsText = Util.parseConfigToString(config);
            for (int i = 0; i < configAsText.size(); i++) {
                bw.write(configAsText.get(i) + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
            }
        }

        ConfigReader.configInstance = config;
    }


    public static Configuration getConfigInstance() {
        return configInstance;
    }

    public static void setConfigInstance(Configuration configInstance) {
        ConfigReader.configInstance = configInstance;
    }
}
