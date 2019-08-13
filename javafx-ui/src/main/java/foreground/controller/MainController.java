package foreground.controller;

import bean.ServerInfo;
import bean.po.User;
import bean.vo.Message;
import bean.vo.MessageType;
import com.alibaba.fastjson.JSON;
import foreground.componment.SelfMessagePane;
import foreground.componment.TalkerMessagePane;
import foreground.componment.UserTab;
import foreground.core.ClientContext;
import foreground.net.TCPClient;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable, TCPClient.OnMessageArrivedCallback, UserTab.UserTabHandler
{
    private @FXML ImageView selfHeadImage;
    private @FXML ImageView currentMeaage;
    private @FXML ImageView friend;
    private @FXML ImageView group;
    private @FXML ImageView location;
    private @FXML ImageView setting;
    private @FXML VBox contactListVbox;
    private @FXML VBox messageVbox;
    private @FXML TextArea inputTextArea;
    private @FXML Label curChaterName;
    private @FXML ScrollPane scrollPane;

    private UserTab currentUserTab;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        //连接socket服务器
        TCPClient tcpClient = null;
        try
        {
            tcpClient = TCPClient.startWith(ServerInfo.get());
            ClientContext.setTcpClient(tcpClient);
            tcpClient.setCallback(this);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //当前为陌生人聊天
        locationClick();


        refreshChatPane();



    }

















    //最上面消息列表点击事件处理
    public void currentMessageClick()
    {
        setDarkImage();
        currentMeaage.imageProperty().setValue(new Image("images/message_active.png"));
    }

    //好友列表点击事件处理
    public void friendClick()
    {
        setDarkImage();
        friend.imageProperty().setValue(new Image("images/contact_active.png"));
    }

    public void selfHeadImageClick()
    {

    }


    //群聊消息列表点击事件处理
    public void groupClick()
    {
        setDarkImage();
        group.imageProperty().setValue(new Image("images/group_chat_active.png"));
    }


    //陌生人列表点击事件处理
    public void locationClick()
    {
        setDarkImage();
        location.imageProperty().setValue(new Image("images/online_active.png"));

    }


    //设置点击事件处理
    public void settingClick()
    {
        setDarkImage();
        setting.imageProperty().setValue(new Image("images/setting_active.png"));

    }

    //给全部图标设置灰色图片
    private void setDarkImage()
    {
        currentMeaage.imageProperty().setValue(new Image("images/message.png"));
        friend.imageProperty().setValue(new Image("images/contact.png"));
        group.imageProperty().setValue(new Image("images/group_chat.png"));
        location.imageProperty().setValue(new Image("images/online.png"));
        setting.imageProperty().setValue(new Image("images/setting.png"));
    }


    //点击发送按钮后的事件处理
    public void sendMessage()
    {
        if(currentUserTab == null)
            return;
        String s = inputTextArea.textProperty().get();
        Message message = new Message();
        message.setFromId(ClientContext.getMe().getId());
        message.setMessage(s);
        message.setDate(LocalDateTime.now());
        message.setType(MessageType.SIMPLE_MESSAGE);
        message.setToId(currentUserTab.getUser().getId());
        //更新聊天面板
        TalkerMessagePane pane = new TalkerMessagePane();
        pane.setText(message.getMessage());
        pane.setHeadImage(new Image("images/avatar-367-456319.png"));

        messageVbox.getChildren().add(pane);
        scrollPane.setVvalue(1.0);

        currentUserTab.getMessageList().add(message);
        ClientContext.getTcpClient().send(JSON.toJSONString(message));
        inputTextArea.setText("");
    }


    //更换当前聊天人后刷新面板
    void refreshChatPane()
    {
        if(currentUserTab == null)
        {
            curChaterName.setText("还没有聊天人");
            return;
        }
        messageVbox.getChildren().clear();
        curChaterName.setText(currentUserTab.getUser().getName());

        List<Message> messageList = currentUserTab.getMessageList();

        for(Message m: messageList)
        {
            if(!m.getFromId().equals(currentUserTab.getUser().getId()))
            {
                TalkerMessagePane pane = new TalkerMessagePane();
                pane.setText(m.getMessage());
                pane.setHeadImage(new Image("images/avatar-367-456319.png"));
                messageVbox.getChildren().add(pane);
            }else
            {
                SelfMessagePane pane = new SelfMessagePane();
                pane.setText(m.getMessage());
                pane.setHeadImage(currentUserTab.getHeadImage());
                messageVbox.getChildren().add(pane);
                scrollPane.setVvalue(1.0);
            }
        }


    }

    @Override
    public void onOnlineUserArrived(List<User> users)
    {
        List<UserTab> collect = users.stream()
                .map(i -> new UserTab(i, this))
                .sorted()
                .collect(Collectors.toList());
        Platform.runLater(()->{
            ObservableList<Node> children = contactListVbox.getChildren();
            children.addAll(collect);
        });

        if(collect.size() != 0 && currentUserTab == null)
        {
            currentUserTab = collect.get(0);
        }


    }




    @Override
    public void onNewMessageArrived(Message message)
    {

        Platform.runLater(()->{

            System.out.println("处理新消息");
            System.out.println("User Id" + currentUserTab.getUser().getId());
            if(message.getToId().equals(ClientContext.getMe().getId()))
            {
                System.out.println("equal");
                SelfMessagePane pane = new SelfMessagePane();
                pane.setText(message.getMessage());
                pane.setHeadImage(currentUserTab.getHeadImage());
                messageVbox.getChildren().add(pane);
                scrollPane.setVvalue(1.0);
                currentUserTab.getMessageList().add(message);
            }else
            {
                ObservableList<Node> children = contactListVbox.getChildren();
                for(Node n: children)
                {
                    UserTab cur = (UserTab)n;
                    if(cur.getUser().getId().equals(message.getToId()))
                    {
                        cur.getMessageList().add(message);
                        //更新 未读消息，排序
                        cur.addUnreadMessage();
                    }
                }
            }
        });
    }

    @Override
    public void onMouseClicked(MouseEvent e)
    {
        Platform.runLater(()->{
            ObservableList<Node > children = contactListVbox.getChildren();
            for(Node n: children)
            {
                if(n == e.getSource())
                {
                    currentUserTab = (UserTab) e.getSource();
                    currentUserTab.resetUnreadMessage();
                    currentUserTab.setCurrentClicked(true);
                    children.sorted();
                    refreshChatPane();
                    return;
                }
            }
        });
    }
}
