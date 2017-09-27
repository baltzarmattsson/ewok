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
    public static final String settingsPwd = "roslagen#";

    private static final String fileName = "config.txt";
    private static final String filePath = "config/";

//        buttonInfo.put(1, new ButtonInfo("Wagner", "http://web.wagnerguide.com/2.0/uwl.aspx?Lang=en&Extern=true&Symbol=0x000000&Sub=False&MainButtonId=0&ButtonId=0&Text="));
//        buttonInfo.put(2, new ButtonInfo("Bibliotek", "https://arenagodemo.axiell.com/web/arena/results?p_auth=STGfxWFC&p_p_id=crDetailWicket_WAR_arenaportlets&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-2&p_p_col_pos=3&p_p_col_count=10&p_r_p_687834046_facet_queries=&_crDetailWicket_WAR_arenaportlets_back_url=https%3A%2F%2Farenagodemo.axiell.com%2Fweb%2Farena%2Fwelcome%3Fp_p_id%3DlistResult_WAR_arenaportlets%26p_p_lifecycle%3D0%26p_p_state%3Dnormal%26p_p_mode%3Dview%26p_p_col_id%3Dcolumn-2%26p_p_col_pos%3D3%26p_p_col_count%3D10%26_listResult_WAR_arenaportlets_facet_queries%3D%26_listResult_WAR_arenaportlets_search_item_no%3D0%26_listResult_WAR_arenaportlets_sort_advice%3Dfield%253DRelevance%2526direction%253DDescending%26_listResult_WAR_arenaportlets_arena_member_id%3D10268539%26_listResult_WAR_arenaportlets_agency_name%3DADEMOSE2013%26_listResult_WAR_arenaportlets_search_type%3Dsolr%26p_r_p_687834046_search_query%3Dauthor%253ALindenbaum%252C%2BPija%2Bmediaclass%253ABook&p_r_p_687834046_search_item_no=0&p_r_p_687834046_sort_advice=field%3DRelevance%26direction%3DDescending&p_r_p_687834046_arena_member_id=10268539&p_r_p_687834046_search_item_id=168532&p_r_p_687834046_agency_name=ADEMOSE2013&p_r_p_687834046_search_type=solr&p_r_p_687834046_search_query=author%3ALindenbaum%2C+Pija+mediaclass%3ABook"));
//        buttonInfo.put(3, new ButtonInfo("Netloan", "http://netloan.bibliotek.umea.se/Login.aspx?ReturnUrl=%2f"));


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
