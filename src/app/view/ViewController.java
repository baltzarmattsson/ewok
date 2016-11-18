package app.view;

// Controls all the user events in the view

import app.util.ButtonInfo;
import app.util.ConfigReader;
import app.util.IdleListener;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    @FXML
    private GridPane rootHolder;

    @FXML
    private void initialize() {

        this.setupFromConfig();
        this.showDefaultSite();

        this.idleListener = new IdleListener(this);

        // Adding listeners to both mouse and keyboard-events, resets timeSinceLastAction in the listener thread
        // and tells us that the idle action (switching to default welcome-screen) is false, the user might
        // have switched page
        rootHolder.addEventHandler(MouseEvent.ANY, e -> {
            idleListener.resetTimeAtLastAction();
            this.idleActionIsPerformed = false;
        });
        rootHolder.addEventHandler(KeyEvent.ANY, e -> {
            idleListener.resetTimeAtLastAction();
            this.idleActionIsPerformed = false;
        });
        webView.addEventHandler(MouseEvent.ANY, e -> {
            idleListener.resetTimeAtLastAction();
            this.idleActionIsPerformed = false;
        });

    }

    private void setupFromConfig() {

        rootHolder.getRowConstraints().clear();

        HashMap<Integer, ButtonInfo> buttonInfo = ConfigReader.getButtonInfo();
        int nbrOfButtons = buttonInfo.size();

        // Adding row constraints
        for (int i = 0; i < nbrOfButtons; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setValignment(VPos.CENTER);
            rowConst.setPercentHeight(100 / nbrOfButtons);
            rowConst.setVgrow(Priority.ALWAYS);
            rootHolder.getRowConstraints().add(rowConst);

            Color c = (Color) ConfigReader.getSideBarColor();
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

            // Adding listener to buttons, doing differently for Home-button
            if (i == 0) { // Is home button
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    showDefaultSite();
                });
            } else {
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    showWebsite(bInfo.getURL());
                });
            }

            rootHolder.add(button, 0, i);

            // Adding helper text to the main view
            rootHolder.add(new Label(bInfo.getHelperText()), 1, i);

        }

        // Centers the buttons horizontally
        rootHolder.getColumnConstraints().get(0).setHalignment(HPos.CENTER);

        // Adding main header text
        rootHolder.add(new Label(ConfigReader.getMainText()), 1, 0);

        // Adding web view
        webView = new WebView();
        webEngine = webView.getEngine();

        // Adding drag-handler for pekskärm
//        webView.setOnDragDetected(e -> {
//            System.out.println(e.getX() + " " + e.getY());
//            webEngine.executeScript("window.scrollTo(" + e.getY() + ", " + e.getX() + ");");
////            webView.setTranslateX(e.getX());
////            webView.setTranslateY(e.getY());
//
//        });
        rootHolder.add(webView, 1, 0, 1, nbrOfButtons + 2);
    }

    @FXML
    private void showDefaultSite() {
        webView.setVisible(false);
    }

    private void showWebsite(String URL) {
        webView.setVisible(true);
        webEngine.load(URL);
    }


    public void performIdleAction() {
        if (idleActionIsPerformed == false) {
            Platform.runLater(() -> showDefaultSite());
            idleActionIsPerformed = true;
            System.out.println("Switching to default");
        }
        this.idleListener = new IdleListener(this);
    }
}
