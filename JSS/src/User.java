import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
/*
Class User is a class of employees who work in the jewelry shop. It contents variables:
-userID;
- password;
- firstName;
- lastName;
This class has two constructors: default and custom to let create new custom instances of the class User.
Class User contents two methods: insertDB() to store an object of a user into a database and checkCredentials()
to check if this user is allowed to enter or not.
 */
public class User {
    //declares class variables
    String userID, password, firstName, lastName;
    //creates default constructor
    User() {
        userID = " ";
        password = " ";
        firstName = " ";
        lastName = " ";
    }//end default User() constructor
    //creates custom constructor to let create new objects of this class
    User(String userID, String password, String firstName, String lastName) {
        this.userID = userID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }//end custom User() constructor
    //insertDB() method allows save new objects of User class into database
    public void insertDB() {
        //defines Connection con as null
        Connection con = MainWindow.dbConnection();
        try {
            //tries to create a SQL statement to insert values of a User object into the database
            String query = "INSERT INTO User VALUES ('" + userID + "','" + password + "','" + firstName + "','" +
                    lastName + "');";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            //shows a message that a new user has been added successfully
            System.out.println("new user has been added into the data base successfully");
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Something wrong happened, when there was a attempt to add user into the database. Try again later.");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        MainWindow.closeConnection(con);
    }//end insertDB() method
    /*
    checkCredentials() method checks if there is data entered by user in the database or not and returns True or False.
     */
    public boolean checkCredentials(String inputLogin, String inputPassword) {
        //creates boolean variable flag and defines it as True
        boolean flag = false;
        //defines Connection con as null
        Connection con = MainWindow.dbConnection();
        try {
            //tries to create a SQL statement to retrieve values of a User object from the database
            String query = "SELECT * FROM User WHERE userID = '" + inputLogin + "' AND password = '" + inputPassword + "';";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            /*
            if statement compares data entered by user with data in the database. If there is such data in the database
            users credentials are put into MainWindow variables and defines flag variable as True
             */
            if(result.getString("userID").equals(inputLogin) && result.getString("password").equals(inputPassword)) {
                flag = true;
                MainWindow.userLastName = result.getString("lastName");
                MainWindow.userFirstName = result.getString("firstName");
                MainWindow.userID = inputLogin;
                MainWindow.userPassword = inputPassword;
            }//end if statement
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        MainWindow.closeConnection(con);
        return flag;
    }//end checkCredentials() method
}//end User class

