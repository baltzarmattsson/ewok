package app;

import app.util.ConfigReader;
import app.util.IdleListener;
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

public class Main extends Application {

    private Stage primaryStage;
    private GridPane rootLayout;

    private final String WINDOW_TITLE = "Appfinder";
    private ViewController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(WINDOW_TITLE);

        // Loading the root layout
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/View.fxml"));
        this.rootLayout = loader.load();

        this.controller = loader.getController();

        // Getting CSS
        String style = Main.class.getResource("view/css/styling.css").toExternalForm();

        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(style);

        this.primaryStage.setScene(scene);
        this.primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            if (primaryStage.isFullScreen() == false)
                primaryStage.setFullScreen(true);
        });

        primaryStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                if(newValue != null && !newValue.booleanValue()) {
                    primaryStage.setFullScreen(true);
                }
            }
        });

        primaryStage.setAlwaysOnTop(true);
        this.primaryStage.setFullScreen(true);
        this.primaryStage.show();


//
//        frame.setUndecorated(true);
//        // Make frame topmost
//        frame.setAlwaysOnTop(true);
//        // Disable Alt+F4 on Windows
//        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//        // Make frame full-screen
//        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
//        // Display frame
//        frame.setVisible(true);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    //in my initial try i didn't add sleep,
//                    //and i ended up,turning off the pc,lost this post for a while
//                    try {
//                        Thread.sleep(100); //buy little millieseconds
//                    } catch (InterruptedException e) {}
//
//                    Platform.runLater(()->{
//                        primaryStage.toFront();
//                        //bring your UI on top of everyone
//                    });
//                }
//
//            }
//        }).start();


    }


    public static void main(String[] args) {
        ConfigReader.readConfig();
        launch(args);
    }
}
