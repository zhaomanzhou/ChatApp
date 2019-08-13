package foreground.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegisterStage extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/Register.fxml"));
        primaryStage.setScene(new Scene(root,728, 632));
        primaryStage.setTitle("注册");
        primaryStage.show();
    }

}
