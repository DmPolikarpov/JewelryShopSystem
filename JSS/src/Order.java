import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
Create class Order that allows store information about orders such as orderID (to make all orders unique), date,
customerID, productID (to connect order with product and customer) and userID (to know who created this order).
Class contains one custom constructor (to allow user to enter data).
Class contains two methods: insertDB to store Orders data into the database and
getSellerName() to retrieve first and last names of a seller to show them in a common table of all orders.
In addition to that, there are getters that are used to get data of class variables for tables.
*/
public class Order {
    //declare class variables to store information about order
    private String orderID, date, customerID, productID, userID;

    //create custom constructor that receives data for variables as parameters
    Order(String orderID, String productID, String customerID, String date, String userID) {
        this.orderID = orderID;
        this.productID = productID;
        this.customerID = customerID;
        this.date = date;
        this.userID = userID;
    }//end custom Order constructor
    //insertDB() method allows save new objects of Order class into database
    public void insertDB() {
        //defines Connection con as null
        Connection con = MainWindow.dbConnection();
        try {
            //tries to create a SQL statement to insert values of a Order object into the database
            String query = "INSERT INTO CustomerOrder VALUES ('" + orderID + "','" + productID + "','" + customerID + "','" +
                    date + "','" + userID + "');";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            //shows a message that a new Order has been added successfully
            System.out.println("new order has been added into the data base successfully");
            Message message = new Message("Confirmation", "new order has been added into the data base successfully");
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Error happened, when data had been inserted");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        MainWindow.closeConnection(con);
    }//end insertDB() method
    //get() methods to get class variables for tables
    public String getOrderID() {
        return orderID;
    }
    public String getDate() {
        return date;
    }
    public String getCustomerID() {
        return customerID;
    }
    public String getProductID() {
        return productID;
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
}//end Order class
