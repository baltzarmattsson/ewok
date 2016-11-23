package app.model;


public enum ConfigFileSections {

    BUTINF_START ("#BUTINF_START"),
    BUTINF_END ("#BUTINF_END"),

    IDLE_START ("#IDLE_START"),
    IDLE_END ("#IDLE_END"),

    BGCOL_START ("#BGCOL_START"),
    BGCOL_END ("#BGCOL_END"),

    FIRSTCOLWIDTH_START ("#FIRSTCOLWIDTH_START"),
    FIRSTCOLWIDTH_END ("#FIRSTCOLWIDTH_END"),

    FIRSTBUTTONISHOME_START ("#FIRSTBUTTONISHOME_START"),
    FIRSTBUTTONISHOME_END ("#FIRSTBUTTONISHOME_END"),

    HOMESCREENURL_START ("#HOMESCREENURL_START"),
    HOMESCREENURL_END ("#HOMESCREENURL_END"),

    // BUTTONINFO instances
    BI_START ("+BI+"),
    BI_END ("-BI-"),
    BI_INDEX ("+BI_INDEX "), // Is written in this order, index, text, url
    BI_TEXT ("+BI_TEXT "),
    BI_URL ("+BI_URL ");

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
