package foreground.componment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class SelfMessagePane extends Pane
{
    @FXML
    private ImageView headImage;
    @FXML
    private Text text;

    public SelfMessagePane()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(
                "views/customer/SelfMessagePane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public void setHeadImage(Image image)
    {
        this.headImage.imageProperty().setValue(image);
    }

    public void setText(String text)
    {
        this.text.textProperty().set(text);
    }

}
