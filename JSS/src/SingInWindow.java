import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.control.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/*
signInWindow is showed up when the main application starts running. It allows user to login and
use the application with their credentials.
 */
public class SingInWindow {
    //creates a stage of SignInWindow
    Stage window;
    //creates to Scene objects: scene1 - for start window, scene2 - a window with form to enter credentials
    Scene scene1, scene2;
    //boolean variable that is used to run the main window
    static boolean result;
    /*
    Default constructor to create an object of SignInWindow class. It contents all window's features.
     */
    SingInWindow () throws FileNotFoundException {
        //creates new window
        window = new Stage();
        //sets a title for the window
        window.setTitle("Welcome");
        //sets sizes of the window
        window.setWidth(550);
        window.setHeight(700);
        //invokes startWindow() method to create the first scene
        scene1 = startWindow();
        //invokes loginForm() method to create the second scene
        scene2 = loginForm();
        //sets scene1 as a scene for the window
        window.setScene(scene1);
        //makes the window show up
        window.showAndWait();
    }//end SingInWindow constructor
    //startWindow() method creates and returns a new scene for start window
    public Scene startWindow() throws FileNotFoundException {
        /*
        layout 1 - welcomes user and asks to choose options:
         - sign in
         - exit
         */
        //creates a new VBox and sets up its features
        VBox layout1 = new VBox(15);
        layout1.setAlignment(Pos.CENTER);
        //creates a scene
        Scene scene = new Scene(layout1);
        //assigns SignInWindowCSS.css file as a stylesheet for layout1
        scene.getStylesheets().add("SignInWindowCSS.css");
        //creates a new label
        Label greeting = new Label("Welcome to Jewelry Shop System");
        //receives an image for logo from a file and uses it as a logotype
        Image logo = new Image(new FileInputStream("static/images/logo.png"));
        ImageView logoView = new ImageView();
        logoView.setImage(logo);
        //creates a button "SIGN IN"
        Button signIn = new Button("SIGN IN");
        signIn.setPrefWidth(250);
        //creates a button "EXIT"
        Button exit = new Button("EXIT");
        exit.setPrefWidth(250);
        //creates button events handler for "SIGN IN" button
        signIn.setOnAction(e -> {
            //sets scene2 as a scene for the window (stage)
            window.setScene(scene2);
        });
        //creates button events handler for "EXIT" button
        exit.setOnAction(e -> {
            //defines variable result as False
            result = false;
            //closes launch window and finishes application execution
            window.close();
        });
        //adds all items into the layout1
        layout1.getChildren().addAll(greeting, logoView, signIn, exit);
        return scene;
    }//end startWindow() method
    //loginForm() method creates and returns a new scene with login form
    public Scene loginForm() {
        /*
        layout2 - layout with a form to enter the application with users credentials
         */
        //creates new VBox for layout2 and sets all its features
        VBox layout2 = new VBox();
        layout2.setAlignment(Pos.TOP_CENTER);
        layout2.setSpacing(15);
        //creates a new scene for this layout2
        Scene scene = new Scene(layout2);
        //custom style class for this scene
        scene.getStylesheets().add("SignInWindowCSS.css");
        //creates a new label and its features
        Label loginForm = new Label("Login Form");
        loginForm.setPadding(new Insets(100,0,100,0));
        //custom style class for loginForm label
        loginForm.getStyleClass().clear();
        loginForm.getStyleClass().add("loginForm");
        //GridPane to place a form to sign in
        GridPane signInForm = new GridPane();
        //custom style class for this GridPane
        signInForm.getStyleClass().clear();
        signInForm.getStyleClass().add("signInForm");
        //sets features of the GridForm
        signInForm.setPadding(new Insets(0, 30, 100, 30));
        signInForm.setAlignment(Pos.CENTER);
        signInForm.setVgap(20);
        signInForm.setHgap(20);
        //UserID label
        Label usernameLabel = new Label("UserID");
        //defines a place of the label inside the grid
        GridPane.setConstraints(usernameLabel, 0, 0);
        //UserID input
        TextField userID = new TextField();
        userID.setPrefWidth(250);
        userID.setPrefHeight(40);
        //defines a place of the textField inside the grid
        GridPane.setConstraints(userID, 1, 0);
        //custom style class for this userID
        userID.getStyleClass().clear();
        userID.getStyleClass().add("textPasswordField");
        //password label
        Label passwordLabel = new Label("Password");
        //defines a place of the passwordLabel inside the grid
        GridPane.setConstraints(passwordLabel, 0, 1);
        //password input
        PasswordField password = new PasswordField();
        password.setPrefWidth(250);
        password.setPrefHeight(40);
        //defines a place of the password inside the grid
        GridPane.setConstraints(password, 1, 1);
        //custom style class for this password
        password.getStyleClass().clear();
        password.getStyleClass().add("textPasswordField");
        //add all items into GridPane
        signInForm.getChildren().addAll(usernameLabel, userID, passwordLabel, password);
        //button to check users credentials and redirect to the main page of the application
        Button confirm = new Button("CONFIRM");
        confirm.setPrefWidth(250);
        //creates button events handler for confirm button
        confirm.setOnAction(e -> {
            //retrieves data entered by user
            String inputLogin = userID.getText();
            String inputPassword = password.getText();
            //creates new User object
            User newUser = new User();
            //checks if checkCredentials() method returns true or false and runs some particular part of code for each case
            if(newUser.checkCredentials(inputLogin, inputPassword)) {
                //defines boolean variable result as True
                result = true;
                //closes the window
                window.close();
            } else {
                //creates a new object of class Message to show error
                Message errorMessage = new Message("Error", "UserID or password is incorrect.\nPlease, check them and try again.");
            };
        });
        //button to go to the start page
        Button back = new Button("BACK");
        back.setPrefWidth(250);
        //creates button events handler for BACK button
        back.setOnAction(e -> window.setScene(scene1));
        //adds all items into layout2
        layout2.getChildren().addAll(loginForm, signInForm, confirm, back);
        return scene;
    }//end loginForm() method

}//end SingInWindow class
