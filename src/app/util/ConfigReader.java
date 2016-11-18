package app.util;

// Used to read config from a file and set the UI and the idle-timer accordingly

import javafx.scene.paint.Color;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.util.HashMap;

public class ConfigReader {

    private static String mainText;
    private static HashMap<Integer, ButtonInfo> buttonInfo = new HashMap<Integer, ButtonInfo>();
    private static int idleTimeInSeconds;
    private static Color bgColor;
    private static Color sideBarColor;

    // Sets temporary data for now
    public static void readConfig() {
        ConfigReader.buttonInfo.put(0, new ButtonInfo("Hem", null, null));
        ConfigReader.buttonInfo.put(1, new ButtonInfo("Wagner", "http://web.wagnerguide.com/2.0/uwl.aspx?Lang=en&Extern=true&Symbol=0x000000&Sub=False&MainButtonId=0&ButtonId=0&Text=", "<-- Tryck för guide"));
        ConfigReader.buttonInfo.put(2, new ButtonInfo("Bibliotek", "https://arenagodemo.axiell.com/web/arena/results?p_auth=STGfxWFC&p_p_id=crDetailWicket_WAR_arenaportlets&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-2&p_p_col_pos=3&p_p_col_count=10&p_r_p_687834046_facet_queries=&_crDetailWicket_WAR_arenaportlets_back_url=https%3A%2F%2Farenagodemo.axiell.com%2Fweb%2Farena%2Fwelcome%3Fp_p_id%3DlistResult_WAR_arenaportlets%26p_p_lifecycle%3D0%26p_p_state%3Dnormal%26p_p_mode%3Dview%26p_p_col_id%3Dcolumn-2%26p_p_col_pos%3D3%26p_p_col_count%3D10%26_listResult_WAR_arenaportlets_facet_queries%3D%26_listResult_WAR_arenaportlets_search_item_no%3D0%26_listResult_WAR_arenaportlets_sort_advice%3Dfield%253DRelevance%2526direction%253DDescending%26_listResult_WAR_arenaportlets_arena_member_id%3D10268539%26_listResult_WAR_arenaportlets_agency_name%3DADEMOSE2013%26_listResult_WAR_arenaportlets_search_type%3Dsolr%26p_r_p_687834046_search_query%3Dauthor%253ALindenbaum%252C%2BPija%2Bmediaclass%253ABook&p_r_p_687834046_search_item_no=0&p_r_p_687834046_sort_advice=field%3DRelevance%26direction%3DDescending&p_r_p_687834046_arena_member_id=10268539&p_r_p_687834046_search_item_id=168532&p_r_p_687834046_agency_name=ADEMOSE2013&p_r_p_687834046_search_type=solr&p_r_p_687834046_search_query=author%3ALindenbaum%2C+Pija+mediaclass%3ABook", "<-- Tryck för Bibliotek"));
        ConfigReader.buttonInfo.put(3, new ButtonInfo("Netloan", "http://netloan.bibliotek.umea.se/Login.aspx?ReturnUrl=%2f", "<-- Tryck för Netloan"));
        ConfigReader.buttonInfo.put(4, new ButtonInfo("Lund", "http://www.lu.se/", "<-- Tryck för Lund"));
        ConfigReader.buttonInfo.put(5, new ButtonInfo("ICA", "https://www.ica.se/", "<-- Tryck för ICA"));



        ConfigReader.bgColor = Color.WHITESMOKE;
        ConfigReader.sideBarColor = Color.WHITESMOKE;
        ConfigReader.mainText = "Välkommen till Bibliotek X";
        ConfigReader.idleTimeInSeconds = 100;
    }

    public static HashMap<Integer, ButtonInfo> getButtonInfo() {
        return buttonInfo;
    }

    public static Color getBgColor() {
        return bgColor;
    }

    public static Color getSideBarColor() {
        return sideBarColor;
    }

    public static String getMainText() {
        return mainText;
    }

    public static int getIdleTimeInSeconds() {
        return idleTimeInSeconds;
    }
}
