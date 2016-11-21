package app.view;

// Controls all the user events in the view

import app.util.ButtonInfo;
import app.util.ConfigReader;
import app.util.Configuration;
import app.util.IdleListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.HashMap;
import java.util.Map;

public class ConfigViewController {

    private HashMap<Integer, ButtonInfo> buttonInfo;


    @FXML
    private GridPane rootHolder;
    @FXML
    private VBox sideBarVbox;
    @FXML
    private

    @FXML
    private void initialize() {

        buttonInfo = new HashMap<Integer, ButtonInfo>();

        this.updateSidebar();

    }

    private void updateSidebar() {

        sideBarVbox.getChildren().clear();
        sideBarVbox.getChildren().add(new Label("Knappar"));

        Button addNewButton = new Button("Lägg till knapp");
        addNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            HBox hbox = new HBox();
            TextField buttonText = new TextField(), buttonURL = new TextField();
            buttonText.setPromptText("Namn");
            buttonURL.setPromptText("URL");
            Button removeButton = new Button("Ta bort");
            removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e2 -> {
                int indexKey = sideBarVbox.getChildren().size()-1;
                sideBarVbox.getChildren().remove(indexKey);
            });
            hbox.getChildren().addAll(buttonText, buttonURL, removeButton);

            sideBarVbox.getChildren().add(hbox);
            updateSidebar();
            System.out.println(sideBarVbox.getChildren().size());
        });
        sideBarVbox.getChildren().add(addNewButton);



        for (Map.Entry<Integer, ButtonInfo> buttonMap : buttonInfo.entrySet()) {
            int key = buttonMap.getKey();
            ButtonInfo bi = buttonMap.getValue();

            sideBarVbox.getChildren().add(new TextField());
        }

    }

}
