package util;

import javafx.scene.control.Alert;

public class AlertUtil
{
    public static void error(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }
}
