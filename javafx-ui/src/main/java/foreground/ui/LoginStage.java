package foreground.ui;

import bean.ServerInfo;
import foreground.net.UDPSearcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginStage extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        ServerInfo info = UDPSearcher.searchServer(10000);
        ServerInfo.setServerInfo(info);
        System.out.println("Server:" + info);

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/Login.fxml"));
        primaryStage.setScene(new Scene(root,665, 507));
        primaryStage.setTitle("登陆");
        primaryStage.show();
    }
}
