package app.util;

// Used to hold information about buttons, i.e. text, URL
public class ButtonInfo {

    private String text;
    private String URL;
    private String helperText;

    public ButtonInfo(String text, String URL, String helperText) {
        this.text = text;
        this.URL = URL;
        this.helperText = helperText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getHelperText() {
        return helperText;
    }

    public void setHelperText(String helperText) {
        this.helperText = helperText;
    }
}
