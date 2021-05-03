import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
Create class Customer that allows store information about customers such as fist and last names, gender, birth date,
age, customerID (to make all customers unique), number of a phone and userID (ID of a seller, who found this customer).
Class contains custom constructor to allow user to enter data and two methods: insertDB to store Customers data into the database and
getSellerName() to retrieve first and last names of a seller to show them in a common table of all customers.
In addition to that, there are getters that are used to get data of class variables for tables.
*/
public class Customer {
    //declare variables to store information about customer
    private String customerID, firstName, lastName, gender, dateOfBirth;
    private int age;
    private long phoneNumber;
    private String userID;

    //create custom constructor that receives data for variables as parameters
    Customer(String customerID, String firstName, String lastName, String gender, int age, long phoneNumber, String dateOfBirth, String userID) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.userID = userID;
    }//end custom Customer constructor
    //insertDB() method allows save new objects of Customer class into database
    public void insertDB() {
        //defines Connection con as null
        Connection con = MainWindow.dbConnection();
        try {
            //tries to create a SQL statement to insert values of a Customer object into the database
            String query = "INSERT INTO Customer VALUES ('" + customerID + "','" + firstName + "','" + lastName + "','" +
                    gender + "'," + age + "," + phoneNumber + ",'" + dateOfBirth + "','" + userID + "');";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            //shows a message that a new customer has been added successfully
            System.out.println("new customer has been added into the data base successfully");
            Message message = new Message("Confirmation", "new customer has been added into the data base successfully");
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Error happened, when data had been inserted");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        MainWindow.closeConnection(con);
    }//end insertDB() method
    //get() methods to get class variables for tables
    public String getCustomerID() {
        return customerID;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getGender() {
        return gender;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public int getAge() {
        return age;
    }
    public long getPhoneNumber() {
        return phoneNumber;
    }
    //this method returns full name of a seller instead of userID
    public String getUserID() {
        //creates an instance of StringProperty to store full name
        StringProperty sellerName = new SimpleStringProperty();
        //connects to the database and retrieves first name and last name of a seller.
        Connection con = MainWindow.dbConnection();
        try {
            //tries to create a SQL statement to retrieve data of a Customer object from the database
            String query = "SELECT * FROM User WHERE userID = '" + this.userID + "';";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            sellerName = new SimpleStringProperty(result.getString("lastName") + " " + result.getString("firstName"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        MainWindow.closeConnection(con);
        return sellerName.get();
    }//end getUserID() method
}//end Customer class
