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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigViewController {

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
    private VBox sideBarVbox;
    private VBox buttonVbox;
    private ColorPicker colorPicker;
    private Slider idleSlider;
    private Slider widthSlider;
    private CheckBox homeScreenIsFirstButtonCheckbox;
    private TextField homescreenURLTextField;

    @FXML
    public void initialize() {
        this.initializeView();
        this.initializeFromConfig(ConfigReader.getConfigInstance());
        this.showExample();
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
        Label homeScreenIsFirstButtonLabel = new Label("Första knappen är hemskärm\nJa:");
        homeScreenIsFirstButtonLabel.setWrapText(true);

        homeScreenIsFirstButtonCheckbox = new CheckBox();
        homeScreenIsFirstButtonCheckbox.setSelected(true);

        homescreenURLTextField = new TextField();
        homescreenURLTextField.setPromptText("URL");
        homescreenURLTextField.setVisible(false);

        homeScreenIsFirstButtonCheckbox.setOnAction(e -> {
            firstButtonIsHomescreen = homeScreenIsFirstButtonCheckbox.isSelected();
            String isHomescreenText = firstButtonIsHomescreen ? "Ja" : "Nej";
            homeScreenIsFirstButtonLabel.setText("Första knappen är hemskärm\n" + isHomescreenText + ":");
            homescreenURLTextField.setVisible(!firstButtonIsHomescreen);
        });

        homescreenURLTextField.addEventHandler(KeyEvent.ANY, e -> {
            this.homeScreenURL = homescreenURLTextField.getText();
        });

        sideBarVbox.getChildren().add(homeScreenIsFirstButtonLabel);
        sideBarVbox.getChildren().add(homeScreenIsFirstButtonCheckbox);
        sideBarVbox.getChildren().add(homescreenURLTextField);

        // Adding update view button
        Button updateViewButton = new Button("Uppdatera exempel");
        updateViewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            updateExample();
        });

        sideBarVbox.setMargin(updateViewButton, new Insets(50, 0, 0, 0));
        sideBarVbox.getChildren().add(updateViewButton);
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

    private Configuration generateConfiguration() {

        Configuration retConfig = new Configuration();

        retConfig.setButtonInfo(this.buttonInfo);
        retConfig.setFirstColumnPercentWidth(this.firstColumnPercentWidth);
        retConfig.setIdleTimeInSeconds(this.idleTimeInSeconds);
        retConfig.setBgColor(this.bgColor);
        retConfig.setHomeScreenURL(this.homeScreenURL);
        retConfig.setFirstButtonIsHomescreen(this.firstButtonIsHomescreen);
        return retConfig;
    }

    private void updateExample() {
        Configuration config = this.generateConfiguration();
        ConfigReader.setConfigInstance(config);
    }

    private void showExample() {
        this.updateExample();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/View.fxml"));
        GridPane exampleView = null;
        try {
            exampleView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ViewController viewController = loader.getController();
//        Scene scene = new Scene(exampleView);

        this.rootHolder.add(exampleView, 1, 0);
    }
}
