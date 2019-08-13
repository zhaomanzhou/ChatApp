package foreground.controller;

import bean.ServerInfo;
import bean.po.User;
import com.alibaba.fastjson.JSON;
import constants.HttpServerConstrants;
import foreground.core.ClientContext;
import foreground.ui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import util.AlertUtil;
import util.HttpClientUtil;
import util.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class LoginController
{
    @FXML
    private TextField username;
    @FXML
    private TextField password;



    public void vistor() throws Exception
    {
        User user = new User();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("请输入游客姓名");
        Optional<String> s = dialog.showAndWait();


        if(s.isPresent() )
        {
            String i = s.get();
            if(i.equals(""))
            {
                AlertUtil.error("不能为空");
                return;
            }else
            {
                user.setName(i);
            }

        }else
        {
            AlertUtil.error("不能为空");
            return;
        }

        System.out.println("游客" + user.getName() + "进入系统");

        user.setId(UUID.randomUUID().toString());
        user.setLegal(false);

        ClientContext.setMe(user);
        MainStage mainStage = new MainStage();
        mainStage.start(new Stage());
        ((Stage)username.getScene().getWindow()).close();
    }


    public void login() throws Exception
    {
        String usernameS = username.getText();
        String passwordS = password.getText();
        if(StringUtil.isEmpty(usernameS) || StringUtil.isEmpty(passwordS))
        {
            AlertUtil.error("用户名或密码不能为空");
            return;
        }

        Map<String , String> map = new HashMap<>();
        map.put("name", usernameS);
        map.put("password", passwordS);
        String s = HttpClientUtil.doPost("http://"+ServerInfo.get().getAddress() +":" + HttpServerConstrants.PORT_SERVER + "/login/", map);
        if(s.equals("true"))
        {
            String s1 = HttpClientUtil.doGet("http://"+ServerInfo.get().getAddress() +":" + HttpServerConstrants.PORT_SERVER  + "/user/" + usernameS, null);
            User user = JSON.parseObject(s1, User.class);
            user.setLegal(true);

            System.out.println("服务器端返回的用户信息" + user);

            ClientContext.setMe(user);
            MainStage mainStage = new MainStage();
            Stage primaryStage = new Stage();
            mainStage.start(primaryStage);
            //窗口关闭时关闭tcp连接
            primaryStage.setOnCloseRequest(e->{
                ClientContext.getTcpClient().exit();
            });
            ((Stage)username.getScene().getWindow()).close();

        }else
        {
            AlertUtil.error("用户名或密码错误");
        }


    }



    public void register() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/Register.fxml"));
        Stage primaryStage = (Stage) username.getScene().getWindow();
        primaryStage.setScene(new Scene(root,728, 632));
        primaryStage.setTitle("注册");
    }

}
