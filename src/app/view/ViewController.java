package app.view;

// Controls all the user events in the view

import app.util.ButtonInfo;
import app.util.ConfigReader;
import app.util.Configuration;
import app.util.IdleListener;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.util.HashMap;

public class ViewController {

    @FXML
    private WebView webView;
    private WebEngine webEngine;

    private IdleListener idleListener;
    private boolean idleActionIsPerformed;
    private int idleTimeInSeconds;

    private String defaultSiteURL;

    @FXML
    private GridPane rootHolder;

    @FXML
    private void initialize() {
        Configuration config = ConfigReader.getConfiguration();
        this.setupFromConfig(config);

        this.idleListener = new IdleListener(this, config.getIdleTimeInSeconds());
        this.idleTimeInSeconds = config.getIdleTimeInSeconds();

        // Adding listeners to both mouse and keyboard-events, resets timeSinceLastAction in the listener thread
        // and tells us that the idle action (switching to default welcome-screen) is false, the user might
        // have switched page
        rootHolder.addEventHandler(MouseEvent.ANY, e -> {
            idleListener.resetTimeAtLastAction();
            this.idleActionIsPerformed = false;
        });
        // Removing this because of the robot realising buttons and triggering KeyEvents
//        rootHolder.addEventHandler(KeyEvent.ANY, e -> {
//            idleListener.resetTimeAtLastAction();
//            this.idleActionIsPerformed = false;
//        });
        webView.addEventHandler(MouseEvent.ANY, e -> {
            idleListener.resetTimeAtLastAction();
            this.idleActionIsPerformed = false;
        });

        this.showDefaultSite();

    }

    private void setupFromConfig(Configuration config) {

        rootHolder.getRowConstraints().clear();
        rootHolder.getColumnConstraints().clear();

        // Adding two columns to the view, first with getFirstColumnPercentWidth from ConfigReader
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(config.getFirstColumnPercentWidth());
        rootHolder.getColumnConstraints().add(cc);
        rootHolder.getColumnConstraints().add(new ColumnConstraints());

        HashMap<Integer, ButtonInfo> buttonInfo = config.getButtonInfo();
        int nbrOfButtons = buttonInfo.size();

        // Setting the default URL to the first buttons URL
        this.defaultSiteURL = buttonInfo.get(0).getURL();

        // Adding row constraints
        for (int i = 0; i < nbrOfButtons; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setValignment(VPos.CENTER);
            rowConst.setPercentHeight(100.0 / nbrOfButtons);
            rowConst.setVgrow(Priority.ALWAYS);
            rootHolder.getRowConstraints().add(rowConst);

            // Converting javafx Color to CSS hex
            Color c = config.getBgColor();
            String hex = String.format("#%02X%02X%02X",
                    (int) (c.getRed() * 255),
                    (int) (c.getGreen() * 255),
                    (int) (c.getBlue() * 255));
            rootHolder.setStyle("-fx-background-color:" + hex + ";");
        }
        // Adding buttons
        for (int i = 0; i < nbrOfButtons; i++) {
            ButtonInfo bInfo = buttonInfo.get(i);
            Button button = new Button();

            button.setText(bInfo.getText());
            // Applying CSS
            button.setId("navigationbutton");
            // Adding listener to buttons
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showWebsite(bInfo.getURL()));
            rootHolder.add(button, 0, i);
            // Adding helper text to the main view
            // Disabled for now
//            rootHolder.add(new Label(bInfo.getHelperText()), 1, i);
        }

        // Centers the buttons horizontally
        rootHolder.getColumnConstraints().get(0).setHalignment(HPos.CENTER);

        // Adding web view
        webView = new WebView();
        webEngine = webView.getEngine();
        webView.setVisible(true);

        rootHolder.add(webView, 1, 0, 1, nbrOfButtons);
    }

    @FXML
    private void showDefaultSite() {
        this.showWebsite(defaultSiteURL);
    }

    private void showWebsite(String URL) {
        webEngine.load(URL);
        rootHolder.getChildren();
    }


    public void performIdleAction() {
        if (idleActionIsPerformed == false) {
            Platform.runLater(() -> showDefaultSite());
            idleActionIsPerformed = true;
            System.out.println("Switching to default");
        }
        this.idleListener = new IdleListener(this, this.idleTimeInSeconds);
    }
}
