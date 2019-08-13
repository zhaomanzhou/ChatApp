package foreground.ui;

import foreground.core.ClientContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/Main.fxml"));
        primaryStage.setScene(new Scene(root,1200, 810));
        primaryStage.setOnCloseRequest((e)->{
            ClientContext.getTcpClient().exit();
        });
        primaryStage.show();

    }
}
