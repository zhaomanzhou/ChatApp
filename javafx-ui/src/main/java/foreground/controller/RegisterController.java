package foreground.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import util.AlertUtil;
import util.StringUtil;

import java.io.IOException;


public class RegisterController
{
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private RadioButton sexMan;
    @FXML
    private RadioButton sexWoman;

    private final ToggleGroup toggleGroup = new ToggleGroup();
    public void initialize()
    {
        sexMan.setToggleGroup(toggleGroup);
        sexWoman.setToggleGroup(toggleGroup);
    }

    public void register()
    {
        String name = username.getText();
        String passwd = password.getText();
        if(StringUtil.isEmpty(name) || StringUtil.isEmpty(passwd) || toggleGroup.getSelectedToggle() == null)
        {
            AlertUtil.error("请先完善注册信息");
        }
    }

    public void login() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/Login.fxml"));
        Stage primaryStage = (Stage) username.getScene().getWindow();
        primaryStage.setScene(new Scene(root,665, 507));
        primaryStage.setTitle("注册");
    }

}
