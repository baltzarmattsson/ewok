package app.view;

// Controls all the user events in the buttons-browser-view

import app.Main;
import app.model.ButtonInfo;
import app.util.ConfigReader;
import app.model.Configuration;
import app.util.IdleListener;
import app.util.Util;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import sun.awt.PlatformFont;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
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

        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);

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
        if (webView != null) {
            webView.addEventHandler(MouseEvent.ANY, e -> {
                if (idleListener != null)
                    idleListener.resetTimeAtLastAction();
                this.idleActionIsPerformed = false;
            });

            // Sending logs to the console
            WebConsoleListener.setDefaultListener((webview, message, lineNumber, source) -> {
//                System.out.println("Console: [" + source + ":" + lineNumber + "] " + message);
            });

        }

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
                rowConst.setPercentHeight(100.0 / nbrOfButtons);
                rowConst.setVgrow(Priority.ALWAYS);
                rootHolder.getRowConstraints().add(rowConst);
            }
        } else {
            // If there's no buttons = no rows, but we still want at least one row to work with, thus adding this below
            rootHolder.getRowConstraints().add(new RowConstraints());
        }

        // Adding bgColor, converting javafx Color to CSS hex
        Color c = config.getBgColor();
        String hex = Util.colorToHex(c != null ? c : Color.WHITE);
        rootHolder.setStyle("-fx-background-color:" + hex + ";");

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
                config.setButtonFont(Font.font("Helvetica", 13.0));

            String butColHex = Util.colorToHex(config.getButtonTextColor());
            String fontFamily = config.getButtonFont().getFamily();
            int fontSize = (int) config.getButtonFont().getSize();

            final ObjectProperty<Color> color = new SimpleObjectProperty<Color>(config.getButtonColor());
            final StringBinding cssColorSpec = Bindings.createStringBinding(() -> String.format("" +
                            "-fx-body-color: rgb(%d, %d, %d);" +
                            "-fx-font-size: %dpx;" +
                            "-fx-text-fill: %s;" +
                            "-fx-font-family: \"%s\";" +
                            "-fx-background-radius: 10px;",
                    (int) (256 * color.get().getRed()),
                    (int) (256 * color.get().getGreen()),
                    (int) (256 * color.get().getBlue()),
                    fontSize,
                    butColHex,
                    //fontName
                    fontFamily
            ), color);
            button.styleProperty().bind(cssColorSpec);

            final Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(color, Color.DARKGRAY)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(color, config.getButtonColor())));

            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                timeline.play();
                showWebsite(bInfo.getURL());
            });
            // Setting sizing
            button.setMinSize(150.0, 150.0);
            button.setMaxSize(170.0, 170.0);
            button.wrapTextProperty().setValue(true);
            button.textAlignmentProperty().setValue(TextAlignment.CENTER);
            rootHolder.add(button, 0, i);
        }

        // Adding web view
        webView = new WebView();
        webEngine = webView.getEngine();

//        webEngine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) -> {
//            if (newState == Worker.State.SUCCEEDED) {
//
//                EventListener listener = new EventListener() {
//                    @Override
//                    public void handleEvent(Event evt) {
//                        String href = ((Element) evt.getTarget()).getAttribute("href");
//                        System.out.println();
//                    }
//                };
//
//                Document doc = webView.getEngine().getDocument();
//                NodeList nodeList = doc.getElementsByTagName("a");
//                ArrayList<EventTarget> castedList = new ArrayList<EventTarget>();
//
//                for (int i = 0; i < nodeList.getLength(); i++) {
//                    castedList.add((org.w3c.dom.events.EventTarget)nodeList.item(i));
////                    ((EventTarget)nodeList.item((i)).addEventListener("click", listener, false);
//                }
//                for (EventTarget t : castedList) {
//                    t.addEventListener("click", listener, false);
//                }
//            }
//        });

        webView.setVisible(true);
        rootHolder.add(webView, 1, 0, 1, nbrOfButtons == 0 ? 1 : nbrOfButtons);

        webEngine.locationProperty().addListener((arg0, oldLocation, newLocation) -> {

            int indexOfTextParam = newLocation.indexOf("&text=");

            if (indexOfTextParam != -1 && newLocation.contains("web.wagnerguide.com")) {
                final String newLoc = newLocation.substring(0, indexOfTextParam);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        webEngine.load(newLoc);
                    }
                });
            }
        });

    }

    private void applyGridpaneRules() {
//        // Centers the buttons horizontally
//        rootHolder.getColumnConstraints().get(0).setHalignment(HPos.CENTER);

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
//            System.out.println("Switching to default");
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

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}