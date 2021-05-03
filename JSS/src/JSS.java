import javafx.application.Application;
import javafx.stage.Stage;
/*
Main class contains main method and start method to run the whole application
 */
public class JSS extends Application {

    public static void main(String[] args) {
        //runs javafx application
        launch(args);
    }//end main method
    //method start create the main window and all its content.
    @Override
    public void start(Stage primaryStage) throws Exception {
        //creates an instance of the mainWindow class
        MainWindow mainWindow = new MainWindow(primaryStage);
        //creates an instance of the SingInWindow class
        SingInWindow launchWindow = new SingInWindow();
        //if boolean variable of launch window is true the main window is showed
        if (launchWindow.result == true) {
            //invokes method mainWindowContent() of the object mainWindow
            mainWindow.mainWindowContent();
            //shows an mainWindow object
            mainWindow.window.show();
        }
    } //end start method
}//end Main class
