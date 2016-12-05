package app.view;

// Controls all the user events in the view

import app.Main;
import app.model.ButtonInfo;
import app.util.ConfigReader;
import app.model.Configuration;
import app.util.fontpicker.FontPicker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigViewController {

    private Main mainApp;

    // Configurations
    private HashMap<Integer, ButtonInfo> buttonInfo = new HashMap<Integer, ButtonInfo>();
    private Color buttonColor;
    private Color buttonTextColor;
    private Color bgColor;
    private int idleTimeInSeconds;
    private double firstColumnPercentWidth;
    private boolean firstButtonIsHomescreen;
    private String homeScreenURL;

    @FXML
    private GridPane rootHolder;

    @FXML
    private GridPane exampleHolder;

    @FXML
    private GridPane exampleView;
    private ViewController exampleController;
    @FXML
    private VBox sideBarVbox;
    private VBox buttonVbox;
    private FontPicker fontPicker;
    private ColorPicker bgColorPicker;
    private ColorPicker buttonColorPicker;
    private ColorPicker buttonTextColorPicker;
    private Slider idleSlider;
    private Slider widthSlider;
    private CheckBox homeScreenIsFirstButtonCheckbox;
    private TextField homescreenURLTextField;
    private Label homescreenResponseLabel;

    @FXML
    public void initialize() {
        this.initializeView();
        ConfigReader.readConfigurationFile();
        this.initializeFromConfig(ConfigReader.getConfigInstance());
    }

    public void initializeFromConfig(Configuration config) {
        if (config != null) {
            this.buttonInfo = config.getButtonInfo();
            this.buttonColor = config.getButtonColor();
            this.buttonTextColor = config.getButtonTextColor();
            this.bgColor = config.getBgColor();
            this.idleTimeInSeconds = config.getIdleTimeInSeconds();
            this.firstColumnPercentWidth = config.getFirstColumnPercentWidth();
            this.firstButtonIsHomescreen = config.isFirstButtonHomescreen();
            this.homeScreenURL = config.getHomeScreenURL();

            // Adding buttons
            for (Map.Entry<Integer, ButtonInfo> map : this.buttonInfo.entrySet()) {
                ButtonInfo bi = map.getValue();

                HBox hbox = this.generateButtonEntryHBox(bi.getText(), bi.getURL());
                this.buttonVbox.getChildren().add(hbox);
            }

            // Adding button color
            this.buttonColorPicker.setValue(config.getButtonColor());

            // Adding button text color
            this.buttonTextColorPicker.setValue(config.getButtonTextColor());

            // Adding button font
            this.fontPicker.setValue(config.getButtonFont());

            // Adding bg color
            this.bgColorPicker.setValue(config.getBgColor());
            this.bgColor = config.getBgColor();

            // Adding idle time
            this.idleSlider.setValue(config.getIdleTimeInSeconds());
            this.idleTimeInSeconds = config.getIdleTimeInSeconds();

            // Adding first column width
            this.widthSlider.setValue(config.getFirstColumnPercentWidth());
            this.firstColumnPercentWidth = config.getFirstColumnPercentWidth();

            // Adding first button is homescreen
            this.homeScreenIsFirstButtonCheckbox.setSelected(config.isFirstButtonHomescreen());
            if (config.isFirstButtonHomescreen() == false) {
                homescreenURLTextField.setVisible(true);
                homeScreenURL = config.getHomeScreenURL();
                homescreenResponseLabel.setText(" Nej");
            }
            this.firstButtonIsHomescreen = config.isFirstButtonHomescreen();

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

        // Choosing button color
        buttonColorPicker = new ColorPicker();
        buttonColorPicker.setOnAction(e -> {
            if (buttonColorPicker.getValue() != null)
                this.buttonColor = buttonColorPicker.getValue();
        });

        buttonColorPicker.setMinHeight(27.0);
        sideBarVbox.getChildren().add(new Label("Välj knappfärg"));
        sideBarVbox.getChildren().add(buttonColorPicker);

        // Choosing button text font/size/weight
        fontPicker = new FontPicker();
        fontPicker.setMinHeight(27.0);
        fontPicker.setPrefWidth(137.0);
        sideBarVbox.getChildren().add(new Label("Välj typsnitt på knapp"));
        sideBarVbox.getChildren().add(fontPicker);


        // Choosing button text color
        buttonTextColorPicker = new ColorPicker();
        buttonTextColorPicker.setOnAction(e -> {
            if (buttonTextColorPicker.getValue() != null)
                this.buttonTextColor = buttonTextColorPicker.getValue();
        });

        buttonTextColorPicker.setMinHeight(27.0);
        sideBarVbox.getChildren().add(new Label("Välj färg på knapptext"));
        sideBarVbox.getChildren().add(buttonTextColorPicker);

        // Choosing background color
        bgColorPicker = new ColorPicker();
        bgColorPicker.setOnAction(e -> {
            if (bgColorPicker.getValue() != null)
                this.bgColor = bgColorPicker.getValue();
        });
        bgColorPicker.setMinHeight(27.0);
        sideBarVbox.getChildren().add(new Label("Välj bakgrundsfärg"));
        sideBarVbox.getChildren().add(bgColorPicker);

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

        sideBarVbox.getChildren().add(homescreenURLTextField);

        // Adding update view button
        Button updateViewButton = new Button("Uppdatera exempel");
        updateViewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.updateExample();
            this.applyRulesToGridpanes();
        });

        sideBarVbox.setMargin(updateViewButton, new Insets(20, 0, 0, 0));
        sideBarVbox.getChildren().add(updateViewButton);

        // Adding go back to main view-button
        Button saveAndExitButton = new Button("Gå till webbskal");
        saveAndExitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            int response = this.saveOrDiscreditDialog();
            // Dont save, but go to web
            if (response == -1 || response == 1) {
                if (this.exampleController != null)
                    this.exampleController.clearListeners();
                if (response == 1)
                    this.saveConfiguration();
                mainApp.loadWebView();
            } else {
                // do nothing, cancel
            }
        });

        sideBarVbox.getChildren().add(saveAndExitButton);

        // Adding exit application button
        Button exitApplicationButton = new Button("Avsluta applikation");
        exitApplicationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.exitApplication();
        });

        sideBarVbox.getChildren().add(exitApplicationButton);

        // Adding a clean gridpane as exampleview
        this.exampleView = new GridPane();
        this.exampleHolder.add(exampleView, 0, 0, 1, 1);

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

    private void saveConfiguration() {
        Configuration generatedConf = this.generateConfiguration();
        ConfigReader.saveConfigurationFile(generatedConf);
    }

    private Configuration generateConfiguration() {

        Configuration retConfig = new Configuration();

        // Adding button info based on the text+url-entries in the view (i.e. added button entries)
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
        retConfig.setButtonTextColor(this.buttonTextColor == null ? Color.WHITE : this.buttonTextColor);
        retConfig.setButtonFont(this.fontPicker.getValue());
//        System.out.println(this.buttonFont);
        retConfig.setButtonColor(this.buttonColor == null ? Color.BLACK : this.buttonColor);
        retConfig.setBgColor(this.bgColor == null ? Color.WHITESMOKE : this.bgColor);
        retConfig.setHomeScreenURL(this.homeScreenURL);
        retConfig.setFirstButtonIsHomescreen(this.firstButtonIsHomescreen);

        return retConfig;
    }

    private void exitApplication() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Säkerställande");
        alert.setHeaderText("Avsluta program");
        alert.setContentText("Vill du spara och skriva över den existerande konfigurationen?");

        ButtonType buttonYes = new ButtonType("Avsluta och spara");
        ButtonType buttonNo = new ButtonType("Avsluta utan att spara");
        ButtonType buttonCancel = new ButtonType("Avbryt");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonYes, buttonNo, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes) {
            this.saveConfiguration();
            System.exit(1);
        } else if (result.get() == buttonNo) {
            System.exit(1);
        }
    }

    private int saveOrDiscreditDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Säkerställande");
        alert.setHeaderText("Spara");
        alert.setContentText("Vill du spara och skriva över den existerande konfigurationen?");

        ButtonType buttonYes = new ButtonType("Spara och gå till webbskal");
        ButtonType buttonNo = new ButtonType("Gå till webbskal utan att spara");
        ButtonType buttonCancel = new ButtonType("Avbryt");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonYes, buttonNo, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes) {
            this.saveConfiguration();
            return 1;
        } else if (result.get() == buttonNo) {
            return -1;
        } else {
            return 0;
        }
    }

    private void updateExample() {

        if (this.exampleController != null)
            this.exampleController.clearListeners();

        Configuration config = this.generateConfiguration();
        ConfigReader.setConfigInstance(config);
        this.showExample();
    }

    private void showExample() {

        this.rootHolder.getChildren().remove(this.exampleView);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/View.fxml"));
        try {
            this.exampleView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exampleController = loader.getController();

        this.exampleView.prefHeightProperty().bind(this.rootHolder.heightProperty());
        this.exampleView.prefWidthProperty().bind(this.rootHolder.widthProperty());
        String style = Main.class.getResource("view/css/styling.css").toExternalForm();
        this.exampleView.getStylesheets().add(style);

        // fillwidth
        GridPane.setFillHeight(exampleView, true);
        GridPane.setFillHeight(exampleView, true);
        GridPane.setHgrow(exampleView, Priority.ALWAYS);
        GridPane.setVgrow(exampleView, Priority.ALWAYS);

//        exampleView.setPrefSize(500, 500);

        rootHolder.add(exampleView, 1, 0, 1, 1);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    private void applyRulesToGridpanes() {

        // Rootholder
        RowConstraints row1 = this.rootHolder.getRowConstraints().get(0);
        row1.setPercentHeight(-1.0);
        row1.setMinHeight(10.0);
        row1.setPrefHeight(30.0);
        row1.setMaxHeight(-1.0);
        row1.setVgrow(Priority.SOMETIMES);
        row1.setFillHeight(true);
        row1.setValignment(null);

        ColumnConstraints col2 = this.rootHolder.getColumnConstraints().get(1);
        col2.setHgrow(Priority.SOMETIMES);
        col2.setFillWidth(true);
        col2.setHalignment(null);

        // Exampleview
        for (RowConstraints row : exampleView.getRowConstraints()) {
            row.setVgrow(Priority.SOMETIMES);
            row.setValignment(null);
            row.setFillHeight(true);
        }
        col2 = this.exampleView.getColumnConstraints().get(1);
        col2.setHgrow(Priority.SOMETIMES);
        col2.setFillWidth(true);
        col2.setHalignment(null);
        ColumnConstraints col1 = this.exampleView.getColumnConstraints().get(0);
        col1.setHgrow(Priority.SOMETIMES);
        col1.setHalignment(HPos.CENTER);
        col1.setFillWidth(true);

    }
}
