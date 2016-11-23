package app;

import app.util.AltTabStopper;
import app.util.ConfigReader;
import app.util.IdleListener;
import app.view.ConfigViewController;
import app.view.ViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private GridPane rootLayout;

    private final String WINDOW_TITLE = "Appfinder";
    private ViewController controller;
    private ConfigViewController configController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(WINDOW_TITLE);

        this.loadWebView();
    }

    // Loading the main kiosk view layout
    public void loadWebView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/View.fxml"));
        try {
            this.rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.controller = loader.getController();
        this.controller.setMainApp(this);

        // Getting CSS
        String style = Main.class.getResource("view/css/styling.css").toExternalForm();

        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(style);


        this.primaryStage.setScene(scene);
        this.primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

//        primaryStage.setOnCloseRequest(e -> {
//            e.consume();
//            if (primaryStage.isFullScreen() == false)
//                primaryStage.setFullScreen(true);
//        });
//
//        primaryStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
//            if(newValue != null && !newValue.booleanValue()) {
//                primaryStage.setFullScreen(true);
//            }
//        });

//        this.primaryStage.setAlwaysOnTop(true);
        this.primaryStage.setFullScreen(true);
        this.primaryStage.show();

        // Starting AltTabStopper
//        AltTabStopper.create();
    }

    public Scene getWebScene() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/View.fxml"));
        try {
            this.rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Getting CSS
        String style = Main.class.getResource("view/css/styling.css").toExternalForm();

        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(style);

        return scene;
    }

    public void loadConfigView() {
        // Loading the root layout
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/ConfigView.fxml"));
        try {
            this.rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.configController = loader.getController();
        this.configController.setMainApp(this);
        Scene scene = new Scene(rootLayout);
        this.primaryStage.setScene(scene);

//        this.primaryStage.setFullScreen(true);
        this.primaryStage.show();
    }

    public static void main(String[] args) {
        ConfigReader.readConfigurationFile();
        launch(args);
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }
}
