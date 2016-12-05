package app.view;

// Controls all the user events in the buttons-browser-view

import app.Main;
import app.model.ButtonInfo;
import app.util.ConfigReader;
import app.model.Configuration;
import app.util.IdleListener;
import app.util.Util;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class ViewController extends Controller {

    private Main mainApp;

    @FXML
    private WebView webView;
    private WebEngine webEngine;

    private IdleListener idleListener;
    private boolean idleActionIsPerformed;
    private int idleTimeInSeconds;
    private String defaultSiteURL;

    private boolean listenersAreStopped = false;
    private boolean passwordPromptIsShowing = false;

    @FXML
    private GridPane rootHolder;

    @FXML
    private void initialize() {
        Configuration config = ConfigReader.getConfigInstance();
        if (config != null) {
            this.setupFromConfig(config);
            this.showDefaultSite();
        }
        this.addListeners();
    }

    private void addListeners() {
        // Adding listeners to both mouse and keyboard-events, resets timeSinceLastAction in the listener thread
        // and tells us that the idle action (switching to default welcome-screen) is false, the user might
        // have switched page
        if (rootHolder != null)
            rootHolder.addEventHandler(MouseEvent.ANY, e -> {
                if (idleListener != null)
                    idleListener.resetTimeAtLastAction();
                this.idleActionIsPerformed = false;
            });
        if (webView != null)
            webView.addEventHandler(MouseEvent.ANY, e -> {
                if (idleListener != null)
                    idleListener.resetTimeAtLastAction();
                this.idleActionIsPerformed = false;
            });

        if (rootHolder != null) {
            // Adding listener to shortcut for Settings-password-prompt
            rootHolder.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if ((e.getCode() == KeyCode.F9 && e.isShiftDown()) /**|| (e.getCode() == KeyCode.ESCAPE && e.isShiftDown()) || (e.getCode() == KeyCode.ESCAPE && e.isAltDown())**/) {
                    this.showPasswordPrompt();
                }
            });
        }
    }

    private void setupFromConfig(Configuration config) {

        if (config.getIdleTimeInSeconds() > 0) {
            this.idleListener = new IdleListener(this, config.getIdleTimeInSeconds());
            this.idleTimeInSeconds = config.getIdleTimeInSeconds();
        }
        rootHolder.getRowConstraints().clear();
        rootHolder.getColumnConstraints().clear();

        // Adding two columns to the view, first with getFirstColumnPercentWidth from ConfigReader
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(config.getFirstColumnPercentWidth());
        cc.setHalignment(HPos.CENTER);
        rootHolder.getColumnConstraints().add(cc);
        rootHolder.getColumnConstraints().add(new ColumnConstraints());

        HashMap<Integer, ButtonInfo> buttonInfo = null;
        int nbrOfButtons = 0;
        if (config.getButtonInfo() != null) {
            buttonInfo = config.getButtonInfo();
            nbrOfButtons = buttonInfo.size();
        }

        // Setting the default URL to the first buttons URL
        if (buttonInfo != null && buttonInfo.get(0) != null) {
            if (config.isFirstButtonHomescreen())
                this.defaultSiteURL = buttonInfo.get(0).getURL();
            else
                this.defaultSiteURL = config.getHomeScreenURL();
        }

        // Setting default URL page, according to if the first button is homeURL or not
        if (config.isFirstButtonHomescreen() && buttonInfo != null && buttonInfo.get(0) != null) {
            this.defaultSiteURL = buttonInfo.get(0).getURL();
        } else if (config.getHomeScreenURL() != null) {
            this.defaultSiteURL = config.getHomeScreenURL();
        } else {
            this.defaultSiteURL = "";
        }

        // Adding row constraints
        if (nbrOfButtons > 0) {
            for (int i = 0; i < nbrOfButtons; i++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setValignment(VPos.CENTER);
                rowConst.setPercentHeight(90.0 / nbrOfButtons); // 90.0 since we want 10% for settings and logo in the bottom
                rowConst.setVgrow(Priority.ALWAYS);
                rootHolder.getRowConstraints().add(rowConst);
            }
        } else {
            // If there's no buttons = no rows, but we still want at least one row to work with, thus adding this below
            rootHolder.getRowConstraints().add(new RowConstraints());
        }

        // Adding settings and logo-row
        RowConstraints setLogConst = new RowConstraints();
        setLogConst.setValignment(VPos.CENTER);
        setLogConst.setPercentHeight(10.0);
        setLogConst.setVgrow(Priority.ALWAYS);
        rootHolder.getRowConstraints().add(setLogConst);

        // Adding bgColor, converting javafx Color to CSS hex
        Color c = config.getBgColor();
        String hex = Util.colorToHex(c != null ? c : Color.WHITE);
        rootHolder.setStyle("-fx-background-color:" + hex + ";");

        double buttonAndLogoSize = 180 - (nbrOfButtons * 10);
        if (buttonAndLogoSize < 10)
            buttonAndLogoSize = 10;

        // Adding buttons
        for (int i = 0; i < nbrOfButtons; i++) {
            ButtonInfo bInfo = buttonInfo.get(i);
            Button button = new Button();

            button.setText(bInfo.getText());

            // Adding listener, applying color and animation for buttons
            if (config.getButtonColor() == null)
                config.setButtonColor(Color.GRAY);
            if (config.getButtonTextColor() == null)
                config.setButtonTextColor(Color.WHITE);
            if (config.getButtonFont() == null)
                config.setButtonFont(Font.getDefault());


            String butTextColHex = Util.colorToHex(config.getButtonTextColor());
            String fontFamily = config.getButtonFont().getFamily();
            int fontSize = (int) config.getButtonFont().getSize();

            Color color = config.getButtonColor();

            String bodyColor = String.format("rgb(%d, %d, %d)",
                    (int) (256 * color.getRed()),
                    (int) (256 * color.getGreen()),
                    (int) (256 * color.getBlue()));

            String darkerBodyColor = String.format("rgb(%d, %d, %d)",
                    (int) (0.8 * 256 * color.getRed()),
                    (int) (0.8 * 256 * color.getGreen()),
                    (int) (0.8 * 256 * color.getBlue()));


            button.styleProperty().bind(
                    Bindings
                            .when(button.pressedProperty())
                            .then(
                                    new SimpleStringProperty(String.format("" +
                                                    "-fx-body-color: %s;" +
                                                    "-fx-font-size: %dpx;" +
                                                    "-fx-text-fill: %s;" +
                                                    "-fx-font-family: \"%s\";" +
                                                    "-fx-background-radius: 10px;",
                                            darkerBodyColor,
                                            fontSize,
                                            butTextColHex,
                                            fontFamily
                                    ))
                            )
                            .otherwise(
                                    new SimpleStringProperty(String.format("" +
                                                    "-fx-body-color: %s;" +
                                                    "-fx-font-size: %dpx;" +
                                                    "-fx-text-fill: %s;" +
                                                    "-fx-font-family: \"%s\";" +
                                                    "-fx-background-radius: 10px;",
                                            bodyColor,
                                            fontSize,
                                            butTextColHex,
                                            fontFamily
                                    ))
                            )
            );

            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showWebsite(bInfo.getURL()));
            // Setting sizing
//            button.setMinSize(150.0, 150.0);
//            button.setMaxSize(150.0, 150.0);
            button.setMinSize(buttonAndLogoSize, buttonAndLogoSize);
            button.setMaxSize(buttonAndLogoSize, buttonAndLogoSize);
            button.wrapTextProperty().setValue(true);
            button.textAlignmentProperty().setValue(TextAlignment.CENTER);
            rootHolder.add(button, 0, i);
        }
        // Adding logo to bottom of sidebar
        final ImageView wagnerLogo = new ImageView(new Image(Main.class.getResourceAsStream("view/images/wagnerGUIDE.png")));
        wagnerLogo.setFitWidth(buttonAndLogoSize);
        wagnerLogo.setPreserveRatio(true);
        wagnerLogo.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> showPasswordPrompt());
        rootHolder.add(wagnerLogo, 0, nbrOfButtons);

        // Adding web view
        webView = new WebView();
        webEngine = webView.getEngine();
        webView.setVisible(true);

        rootHolder.add(webView, 1, 0, 1, nbrOfButtons == 0 ? 2 : nbrOfButtons + 1);
    }

    @FXML
    private void showDefaultSite() {
        this.showWebsite(defaultSiteURL);
    }

    private void showWebsite(String URL) {
        webEngine.load(URL);
    }

    @Override
    public void performIdleAction() {
        if (idleActionIsPerformed == false) {
            Platform.runLater(() -> showDefaultSite());
            idleActionIsPerformed = true;
        }
        if (this.listenersAreStopped == false)
            this.idleListener = new IdleListener(this, this.idleTimeInSeconds);
        else
            this.clearListeners();
    }

    @FXML
    private void showPasswordPrompt() {
        if (passwordPromptIsShowing == false) {
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
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.getIcons().add(new Image(Main.class.getResourceAsStream("view/images/ww.gif")));
            this.passwordPromptIsShowing = true;
            dialogStage.showAndWait();
            if (controller.isPasswordOk()) {
                this.clearListeners();
                this.mainApp.loadConfigView();
            }
            this.passwordPromptIsShowing = false;
        }
    }

    // Used for clearing listeners in the example view, else they continue til the run() is done
    public void clearListeners() {
        if (this.idleListener != null) {
            this.idleListener.setStop(true);
            this.idleListener.interrupt();
        }
        this.idleListener = null;
        this.listenersAreStopped = true;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}