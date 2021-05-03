import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.*;

/*
Class Message creates and shows a new window with a message and a button "OK". Other windows are not active,
while there is this window.
Class Message has one custom constructor that takes two strings as parameters: String title - to set up a title
of a window, String message is a message that will be showed inside the window.
 */
public class Message {
    //Custom constructor that allows to create a new message window with some particular title and content.
    Message(String title, String message) {
        //creates new Stages (window)
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        //sets a title for the window
        window.setTitle(title);
        //sets sizes of the window
        window.setMinWidth(350);
        window.setMinHeight(150);
        //set a message of the window
        Label messageText = new Label();
        messageText.setText(message);
        messageText.setPadding(new Insets(30,30,30,30));
        //button to close the window
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> window.close());
        //layout to place all items in the window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(messageText, okButton);
        layout.setAlignment(Pos.CENTER);
        //create a scene of the window
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("SignInWindowCSS.css");
        window.setScene(scene);
        //shows the window
        window.showAndWait();
    }//end Message constructor
}//end Message class
