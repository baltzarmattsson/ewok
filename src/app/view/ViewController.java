package app.view;

// Controls all the user events in the buttons-browser-view

import app.Main;
import app.model.ButtonInfo;
import app.util.ConfigReader;
import app.model.Configuration;
import app.util.IdleListener;
import app.util.Util;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ViewController {

    private Main mainApp;

    @FXML
    private WebView webView;
    private WebEngine webEngine;

    private IdleListener idleListener;
    private boolean idleActionIsPerformed;
    private int idleTimeInSeconds;
    private String defaultSiteURL;

    private boolean listenersAreStopped = false;

    @FXML
    private GridPane rootHolder;

    @FXML
    private void initialize() {
//        Configuration config = ConfigReader.getConfigInstance();
        Configuration config = ConfigReader.getTempConfig();
        if (config != null) {
            this.setupFromConfig(config);

            this.addListeners();
            this.showDefaultSite();

        }
    }

    private void addListeners() {
        // Adding listeners to both mouse and keyboard-events, resets timeSinceLastAction in the listener thread
        // and tells us that the idle action (switching to default welcome-screen) is false, the user might
        // have switched page
        rootHolder.addEventHandler(MouseEvent.ANY, e -> {
            if (idleListener != null)
                idleListener.resetTimeAtLastAction();
            this.idleActionIsPerformed = false;
        });
        webView.addEventHandler(MouseEvent.ANY, e -> {
            if (idleListener != null)
                idleListener.resetTimeAtLastAction();
            this.idleActionIsPerformed = false;
        });

        // Adding listener to shortcut for Settings-password-prompt
        rootHolder.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F9 && e.isShiftDown()) {
                this.showPasswordPrompt();
            }
        });
    }

    private void setupFromConfig(Configuration config) {

        this.idleListener = new IdleListener(this, config.getIdleTimeInSeconds());
        this.idleTimeInSeconds = config.getIdleTimeInSeconds();

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
        if (buttonInfo != null && buttonInfo.get(0) != null) {
            if (config.isFirstButtonHomescreen())
                this.defaultSiteURL = buttonInfo.get(0).getURL();
            else
                this.defaultSiteURL = config.getHomeScreenURL();
        }

        // Adding row constraints, + 1 for settings button
        for (int i = 0; i < nbrOfButtons; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setValignment(VPos.CENTER);
            rowConst.setPercentHeight(100.0 / nbrOfButtons);
            rowConst.setVgrow(Priority.ALWAYS);
            rootHolder.getRowConstraints().add(rowConst);
        }

        // Adding bgColor, converting javafx Color to CSS hex
        Color c = config.getBgColor();
        String hex = Util.colorToHex(c);
//        String hex = String.format("#%02X%02X%02X",
//                (int) (c.getRed() * 255),
//                (int) (c.getGreen() * 255),
//                (int) (c.getBlue() * 255));
        rootHolder.setStyle("-fx-background-color:" + hex + ";");

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
        }

        // Centers the buttons horizontally
        rootHolder.getColumnConstraints().get(0).setHalignment(HPos.CENTER);

        // Adding web view
        webView = new WebView();
        webEngine = webView.getEngine();
        webView.setVisible(true);

        rootHolder.add(webView, 1, 0, 1, nbrOfButtons == 0 ? 1 : nbrOfButtons);

    }

    @FXML
    private void showDefaultSite() {
        this.showWebsite(defaultSiteURL);
    }

    private void showWebsite(String URL) {
        webEngine.load(URL);
        System.gc();
    }


    public void performIdleAction() {
        System.gc();
        if (idleActionIsPerformed == false) {
            Platform.runLater(() -> showDefaultSite());
            idleActionIsPerformed = true;
            System.out.println("Switching to default");
        }
        if (this.listenersAreStopped == false)
            this.idleListener = new IdleListener(this, this.idleTimeInSeconds);
        else
            this.clearListeners();
    }

    @FXML
    private void showPasswordPrompt() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/PasswordPrompt.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.NONE);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        PasswordPromptController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.initOwner(mainApp.getPrimaryStage());
        dialogStage.showAndWait();
        if (controller.isPasswordOk()) {
            System.out.println("Pass OK");
            this.clearListeners();
            this.mainApp.loadConfigView();
        }
    }

    // Used for clearing listeners in the example view, else they continue til the run() is done
    public void clearListeners() {
        if (this.idleListener != null)
            this.idleListener.setStop(true);
        this.idleListener = null;
        this.listenersAreStopped = true;
    }

    public Main getMainApp() {
        return mainApp;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}