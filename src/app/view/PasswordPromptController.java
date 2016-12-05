package app.view;

import app.util.ConfigReader;
import app.util.IdleListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class PasswordPromptController extends Controller {

    private Stage dialogStage;
    private boolean passwordOk = false;

    private IdleListener idleListener;
    private final int IDLE_TIME_IN_SECONDS = 2; // Doesn't really wait for 2s, more like 5

    @FXML
    private PasswordField passwordField;

    @FXML
    private void initialize() {

        this.idleListener = new IdleListener(this, IDLE_TIME_IN_SECONDS);

        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE)
                this.handleCancel();
            else if (e.getCode() == KeyCode.ENTER)
                this.handleOK();
            else
                this.idleListener.resetTimeAtLastAction();
        });

        if (this.passwordField != null)
            this.passwordField.addEventHandler(MouseEvent.ANY, e -> this.idleListener.resetTimeAtLastAction());
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

    @Override
    public void performIdleAction() {
        Platform.runLater(() -> this.dialogStage.close());
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isPasswordOk() {
        return passwordOk;
    }
}
