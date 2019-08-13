package foreground.componment;

import bean.po.User;
import bean.vo.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserTab extends Pane implements Comparable<UserTab>
{


    //没有头像的用户随机生成头像用的
    private static int avatarNum = 1;

    //对应的用户
    private User user;


    private UserTabHandler userTabHandler;

    private List<Message> messageList = new ArrayList<>();

    //未读消息数目
    private int unreadMessage = 0;

    //最新消息
    private Message lastMessage;

    //当前是否选中该聊天窗口
    private boolean currentClicked = false;

    @FXML
    private ImageView headImage;
    @FXML
    private Label name;
    @FXML
    private Label lastestMessage;
    @FXML
    private Label messageNumLabel;
    @FXML
    private Label timeDuration;

    public Image getHeadImage()
    {
        return headImage.imageProperty().get();
    }











    public UserTab(User user, UserTabHandler handler)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(
                "views/customer/UserTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        this.user = user;
        setName(user.getName());
        setLastestMessage("");
        setMessageNumLabel(0 + "");
        messageNumLabel.setVisible(false);

        if(user.getAvatarUrl() != null)
        {
            setHeadImage(new Image(user.getAvatarUrl()));
        }else
        {
            int i = (++avatarNum)%6 + 1;
            setHeadImage(new Image("images/avatar/" + i+ ".png"));
        }



        //未读消息为0不显示
        messageNumLabel.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue.equals("0分钟前") || newValue.equals(""))
                messageNumLabel.setVisible(false);
            else
            {
                messageNumLabel.setVisible(true);
            }
        });



        this.setOnMouseClicked(handler::onMouseClicked);

    }



    public void refresh()
    {
        if(messageList.size() == 0)
            return;
        Collections.sort(messageList);
        Message message = messageList.get(0);
        setLastestMessage(message.getMessage());
        setMessageNumLabel(unreadMessage +"分钟前");
        lastMessage = message;
    }










    @Override
    public int compareTo(UserTab o)
    {
        if(currentClicked)
            return 1;
        else
        {
            if(lastMessage == null)
                return  -1;
            if(o.lastMessage == null)
                return  1;
            return lastMessage.getDate().compareTo(o.lastMessage.getDate());
        }
    }








    public interface UserTabHandler
    {
        void onMouseClicked(MouseEvent e);
    }


    public boolean isCurrentClicked()
    {
        return currentClicked;
    }

    public void setCurrentClicked(boolean currentClicked)
    {
        this.currentClicked = currentClicked;
    }

    public void addUnreadMessage()
    {
        this.unreadMessage++;
    }

    public int getUnreadMessage()
    {
        return this.unreadMessage;
    }

    public void resetUnreadMessage()
    {
        this.unreadMessage = 0;
    }

    public List<Message> getMessageList()
    {
        return messageList;
    }

    public void setMessageList(List<Message> messageList)
    {
        this.messageList = messageList;
    }

    public void setHeadImage(Image image)
    {
        this.headImage.imageProperty().setValue(image);
    }

    public String getName()
    {
        return name.textProperty().get();
    }

    public void setName(String name)
    {
        this.name.textProperty().set(name);
    }

    public String getLastestMessage()
    {
        return lastestMessage.textProperty().get();
    }

    public void setLastestMessage(String lastestMessage)
    {
        this.lastestMessage.textProperty().set(lastestMessage);
    }

    public String getMessageNumLabel()
    {
        return messageNumLabel.textProperty().get();
    }

    public void setMessageNumLabel(String messageNumLabel)
    {
        this.messageNumLabel.textProperty().set(messageNumLabel);
    }


    public User getUser()
    {
        return user;
    }
}
