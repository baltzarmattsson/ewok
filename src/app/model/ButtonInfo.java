package app.model;

// Used to hold information about buttons, i.e. text, URL
public class ButtonInfo {

    private String text;
    private String URL;

    public ButtonInfo(String text, String URL) {
        this.text = text;
        this.URL = URL;
    }

    public String getText() {
        return text;
    }

    public String getURL() {
        return URL;
    }
}
