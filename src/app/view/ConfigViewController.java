package app.view;

// Controls all the user events in the view

import app.Main;
import app.util.ButtonInfo;
import app.util.ConfigReader;
import app.util.Configuration;
import app.util.IdleListener;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigViewController {

    private Main mainApp;

    // Configurations
    private HashMap<Integer, ButtonInfo> buttonInfo = new HashMap<Integer, ButtonInfo>();
    private Color bgColor;
    private int idleTimeInSeconds;
    private double firstColumnPercentWidth;
    private boolean firstButtonIsHomescreen;
    private String homeScreenURL;

    @FXML
    private GridPane rootHolder;

    @FXML
    private GridPane exampleView;
    private ViewController exampleController;
    @FXML
    private VBox sideBarVbox;
    private VBox buttonVbox;
    private ColorPicker colorPicker;
    private Slider idleSlider;
    private Slider widthSlider;
    private CheckBox homeScreenIsFirstButtonCheckbox;
    private TextField homescreenURLTextField;
    private Label homescreenResponseLabel;

    @FXML
    public void initialize() {
        this.initializeView();
        ConfigReader.getTempConfig();
        this.initializeFromConfig(ConfigReader.getConfigInstance());
//        this.showExample();
    }

    public void initializeFromConfig(Configuration config) {
        if (config != null) {
            this.buttonInfo = config.getButtonInfo();
            this.bgColor = config.getBgColor();
            this.idleTimeInSeconds = config.getIdleTimeInSeconds();
            this.firstColumnPercentWidth = config.getFirstColumnPercentWidth();
            this.firstButtonIsHomescreen = config.isFirstButtonIsHomescreen();
            this.homeScreenURL = config.getHomeScreenURL();

            // Adding buttons
            for (Map.Entry<Integer, ButtonInfo> map : this.buttonInfo.entrySet()) {
                int index = map.getKey();
                ButtonInfo bi = map.getValue();

                HBox hbox = this.generateButtonEntryHBox(bi.getText(), bi.getURL());
                this.buttonVbox.getChildren().add(hbox);
            }

            // Adding bg color
            this.colorPicker.setValue(config.getBgColor());
            this.bgColor = config.getBgColor();

            // Adding idle time
            this.idleSlider.setValue(config.getIdleTimeInSeconds());
            this.idleTimeInSeconds = config.getIdleTimeInSeconds();

            // Adding first column width
            this.widthSlider.setValue(config.getFirstColumnPercentWidth());
            this.firstColumnPercentWidth = config.getFirstColumnPercentWidth();

            // Adding first button is homescreen
            this.homeScreenIsFirstButtonCheckbox.setSelected(config.isFirstButtonIsHomescreen());
            if (config.isFirstButtonIsHomescreen() == false) {
                homescreenURLTextField.setVisible(true);
                homeScreenURL = config.getHomeScreenURL();
                homescreenResponseLabel.setText(" Nej");
            }
            this.firstButtonIsHomescreen = config.isFirstButtonIsHomescreen();

            // Adding text to homeescreen textfield
            this.homescreenURLTextField.setText(config.getHomeScreenURL());
            this.homeScreenURL = config.getHomeScreenURL();
        }
    }

    private void initializeView() {

        sideBarVbox.setSpacing(5.0);

        // Button for adding buttons
        sideBarVbox.getChildren().add(new Label("Knappar"));
        Button addNewButton = new Button("Lägg till knapp");

        buttonVbox = new VBox();

        addNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            HBox hbox = this.generateButtonEntryHBox(null, null);
            this.buttonVbox.getChildren().add(hbox);
        });
        sideBarVbox.getChildren().add(addNewButton);
        sideBarVbox.getChildren().add(buttonVbox);

        // Choosing background color
        colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> {
            if (colorPicker.getValue() != null)
                this.bgColor = colorPicker.getValue();
            System.out.println(bgColor);
        });

        sideBarVbox.getChildren().add(new Label("Välj bakgrundsfärg"));
        sideBarVbox.getChildren().add(colorPicker);

        // Adding idle-time slider
        idleSlider = new Slider();
        idleSlider.setOrientation(Orientation.HORIZONTAL);
        idleSlider.setMax(1000);
        Label idleLabel = new Label("Inaktivitetstid (0 sekunder):");
        idleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            idleTimeInSeconds = newValue.intValue();
            idleLabel.setText("Inaktivitetstid (" + idleTimeInSeconds + " sekunder):");
        });

        sideBarVbox.getChildren().add(idleLabel);
        sideBarVbox.getChildren().add(idleSlider);

        // Adding sidebar width slider
        widthSlider = new Slider();
        widthSlider.setOrientation(Orientation.HORIZONTAL);
        widthSlider.setMax(100);
        Label widthLabel = new Label("Bredd sidomeny (0% bredd):");
        widthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            firstColumnPercentWidth = newValue.intValue();
            widthLabel.setText("Bredd sidomeny (" + firstColumnPercentWidth + "% bredd):");
        });

        sideBarVbox.getChildren().add(widthLabel);
        sideBarVbox.getChildren().add(widthSlider);

        // Homescreen
        Label homeScreenIsFirstButtonLabel = new Label("Första knappen är hemskärm:");
        homeScreenIsFirstButtonLabel.setWrapText(true);

        homeScreenIsFirstButtonCheckbox = new CheckBox();
        homeScreenIsFirstButtonCheckbox.setSelected(true);

        HBox homescreenCheckBoxAndLabel = new HBox();
        homescreenResponseLabel = new Label(" Ja");

        homescreenURLTextField = new TextField();
        homescreenURLTextField.setPromptText("URL");
        homescreenURLTextField.setVisible(false);

        homeScreenIsFirstButtonCheckbox.setOnAction(e -> {
            firstButtonIsHomescreen = homeScreenIsFirstButtonCheckbox.isSelected();
            homescreenResponseLabel.setText(firstButtonIsHomescreen ? " Ja" : " Nej");
            homescreenURLTextField.setVisible(!firstButtonIsHomescreen);
        });

        homescreenURLTextField.addEventHandler(KeyEvent.ANY, e -> {
            this.homeScreenURL = homescreenURLTextField.getText();
        });

        homescreenCheckBoxAndLabel.getChildren().addAll(homeScreenIsFirstButtonCheckbox, homescreenResponseLabel);
        sideBarVbox.getChildren().add(homeScreenIsFirstButtonLabel);
        sideBarVbox.getChildren().add(homescreenCheckBoxAndLabel);

//        sideBarVbox.getChildren().add(homeScreenIsFirstButtonLabel);
//        sideBarVbox.getChildren().add(homeScreenIsFirstButtonCheckbox);
        sideBarVbox.getChildren().add(homescreenURLTextField);

        // Adding update view button
        Button updateViewButton = new Button("Uppdatera exempel");
        updateViewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            updateExample();
        });

        sideBarVbox.setMargin(updateViewButton, new Insets(20, 0, 0, 0));
        sideBarVbox.getChildren().add(updateViewButton);

        // Adding go back to main view-button
        Button saveAndExitButton = new Button("Gå till webbskal");
        saveAndExitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (this.exampleController != null)
                this.exampleController.clearListeners();
            this.saveConfiguration();
            mainApp.loadWebView();
        });

        sideBarVbox.getChildren().add(saveAndExitButton);

        // Adding exit application button
        Button exitApplicationButton = new Button("Avsluta applikation");
        exitApplicationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            // TODO save unfinished work
            System.exit(0);
        });

        sideBarVbox.getChildren().add(exitApplicationButton);

        rootHolder.getColumnConstraints().get(0).setHgrow(Priority.NEVER);

        // Adding a clean gridpane as exampleview
        this.exampleView = new GridPane();
        this.exampleView.prefHeightProperty().bind(this.rootHolder.heightProperty());
        this.exampleView.prefWidthProperty().bind(this.rootHolder.widthProperty());
        this.rootHolder.add(exampleView, 1, 0, 1, 1);

    }

    private void addListenerToRemoveButton(Button removeButton) {
        removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e2 -> {
            for (int i = 0; i < buttonVbox.getChildren().size(); i++) {
                HBox iteratedHbox = (HBox) buttonVbox.getChildren().get(i);
                Button iteratedButton = (Button) iteratedHbox.getChildren().get(2);
                if (removeButton == iteratedButton)
                    buttonVbox.getChildren().remove(i);
            }
        });
    }

    private HBox generateButtonEntryHBox(String text, String URL) {
        HBox hbox = new HBox();
        TextField buttonText = new TextField();
        TextField buttonURL = new TextField();
        buttonText.setPromptText("Text");
        buttonURL.setPromptText("URL");
        if (text != null)
            buttonText.setText(text);
        if (URL != null)
            buttonURL.setText(URL);

        Button removeButton = new Button("X");
        this.addListenerToRemoveButton(removeButton);

        hbox.getChildren().addAll(buttonText, buttonURL, removeButton);
        return hbox;
    }

    private Configuration saveConfiguration() {

        // TODO
        Configuration retConfig = new Configuration();

//        retConfig.setButtonInfo(this.buttonInfo);

        buttonVbox.getChildren();
        this.buttonInfo = new HashMap<Integer, ButtonInfo>();

        for (int i = 0; i < buttonVbox.getChildren().size(); i++) {
            Node n = buttonVbox.getChildren().get(i);
            HBox btnEntryContainer = null;
            ButtonInfo bi = null;
            TextField btnText = null;
            TextField btnURL = null;

            if (n instanceof HBox) {
                btnEntryContainer = (HBox) n;
                btnText = (TextField) btnEntryContainer.getChildren().get(0);
                btnURL = (TextField) btnEntryContainer.getChildren().get(1);
                bi = new ButtonInfo(btnText.getText(), btnURL.getText());
                this.buttonInfo.put(i, bi);
            }
        }
        retConfig.setButtonInfo(this.buttonInfo);
        retConfig.setFirstColumnPercentWidth(this.firstColumnPercentWidth);
        retConfig.setIdleTimeInSeconds(this.idleTimeInSeconds);
        retConfig.setBgColor(this.bgColor);
        retConfig.setHomeScreenURL(this.homeScreenURL);
        retConfig.setFirstButtonIsHomescreen(this.firstButtonIsHomescreen);

        ConfigReader.setConfigInstance(retConfig);
        return retConfig;

//        return ConfigReader.getTempConfig();
    }

    private void updateExample() {

        if (this.exampleController != null)
            this.exampleController.clearListeners();

        Configuration config = this.saveConfiguration();
        ConfigReader.setConfigInstance(config);
        this.showExample();
    }

    private void showExample() {

        this.rootHolder.getChildren().remove(this.exampleView);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/View.fxml"));
        try {
            this.exampleView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exampleController = loader.getController();

        exampleView.prefWidthProperty().bind(rootHolder.widthProperty());
        exampleView.prefHeightProperty().bind(rootHolder.heightProperty());

        String style = Main.class.getResource("view/css/styling.css").toExternalForm();
        this.exampleView.getStylesheets().add(style);

        rootHolder.add(exampleView, 1, 0, 1, 1);

        rootHolder.setGridLinesVisible(true);
        exampleView.getColumnConstraints().get(0).setHgrow(Priority.NEVER);


    }

    public Main getMainApp() {
        return mainApp;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
