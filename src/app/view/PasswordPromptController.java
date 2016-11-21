package app.view;

import app.util.ConfigReader;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;


public class PasswordPromptController {

    private Stage dialogStage;
    private boolean passwordOk = false;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleOK() {
        this.passwordOk = passwordField.getText().equals(ConfigReader.settingsPwd);
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isPasswordOk() {
        return passwordOk;
    }
}
