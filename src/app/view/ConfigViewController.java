package app.view;

// Controls all the user events in the view

import app.util.ButtonInfo;
import app.util.ConfigReader;
import app.util.Configuration;
import app.util.IdleListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.HashMap;
import java.util.Map;

public class ConfigViewController {

    // Configurations
    private HashMap<Integer, ButtonInfo> buttonInfo;
    private Color bgColor;
    private int idleTimeInSeconds;
    private double firstColumnPercentWidth;


    @FXML
    private GridPane rootHolder;
    @FXML
    private VBox sideBarVbox;
    private VBox buttonVbox;

    @FXML
    private void initialize() {

        buttonInfo = new HashMap<Integer, ButtonInfo>();


        this.initializeView();

    }

    private void initializeView() {

        // Button for adding buttons
        sideBarVbox.getChildren().add(new Label("Knappar"));
        Button addNewButton = new Button("Lägg till knapp");

        buttonVbox = new VBox();

        addNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            HBox hbox = new HBox();
            TextField buttonText = new TextField(), buttonURL = new TextField();
            buttonText.setPromptText("Namn");
            buttonURL.setPromptText("URL");
            Button removeButton = new Button("X");

            removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e2 -> {
                for (int i = 0; i < buttonVbox.getChildren().size(); i++) {
                    HBox iteratedHbox = (HBox) buttonVbox.getChildren().get(i);
                    Button iteratedButton = (Button) iteratedHbox.getChildren().get(2);
                    if (removeButton == iteratedButton)
                        buttonVbox.getChildren().remove(i);
                }

            });
            hbox.getChildren().addAll(buttonText, buttonURL, removeButton);

            this.buttonVbox.getChildren().add(hbox);
        });
        sideBarVbox.getChildren().add(addNewButton);
        sideBarVbox.getChildren().add(buttonVbox);


        // Chosing background color
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> {
            if (colorPicker.getValue() != null)
                this.bgColor = colorPicker.getValue();
            System.out.println(bgColor);
        });

        sideBarVbox.getChildren().add(new Label("Välj bakgrundsfärg"));
        sideBarVbox.getChildren().add(colorPicker);

        // Adding idle-time slider
        Slider idleSlider = new Slider();
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
        Slider widthSlider = new Slider();
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

    }

    private Configuration generateConfiguration() {

        Configuration retConfig = new Configuration();
        retConfig.setButtonInfo(this.buttonInfo);

        return retConfig;

    }
}
