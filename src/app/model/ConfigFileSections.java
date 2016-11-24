package app.model;


public enum ConfigFileSections {

    BUTINF_START ("#BUTINF_START"),
    BUTINF_END ("#BUTINF_END"),

    IDLE_START ("#IDLE_START"),
    IDLE_END ("#IDLE_END"),

    BGCOL_START ("#BGCOL_START"),
    BGCOL_END ("#BGCOL_END"),

    BUTCOL_START ("#BUTCOL_START"),
    BUTCOL_END("#BUTCOL_END"),

    BUTFONT_START ("#BUTFONT_START"),
    BUTFONT_END ("#BUTFONT_END"),

    BUTTEXTCOL_START ("#BUTTEXTCOL_START"),
    BUTTEXTCOL_END ("#BUTTEXTCOL_END"),

    FIRSTCOLWIDTH_START ("#FIRSTCOLWIDTH_START"),
    FIRSTCOLWIDTH_END ("#FIRSTCOLWIDTH_END"),

    FIRSTBUTTONISHOME_START ("#FIRSTBUTTONISHOME_START"),
    FIRSTBUTTONISHOME_END ("#FIRSTBUTTONISHOME_END"),

    HOMESCREENURL_START ("#HOMESCREENURL_START"),
    HOMESCREENURL_END ("#HOMESCREENURL_END"),

    // BUTTONINFO instances
    BI_START ("###BI_START###"),
    BI_END ("###BI_END###"),
    BI_INDEX ("##BI_INDEX "), // Is written in this order, index, text, url
    BI_TEXT ("##BI_TEXT "),
    BI_URL ("##BI_URL ");

    private final String name;

    ConfigFileSections(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }

    public String get() {
        return this.name;
    }

}
