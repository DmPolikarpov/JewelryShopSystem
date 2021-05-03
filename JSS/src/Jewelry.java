import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
/*
Create class Product that allows store information about products such as productID (to make all products unique), name,
description, price and availability.
Class contains custom constructor that takes data entered by user in the main method and store it in variables,
and one method insertDB() to store new Product data into the database.
In addition to that, there are getters that are used to get data of class variables for tables.
*/
public class Jewelry {
    //declare variables to store information about product
    String name, description, productID;
    double price;
    int availability;
    //create custom constructor that receives data for variables as parameters
    Jewelry(String productID, String name, String description, double price, int availability) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }//end custom Product constructor
    //insertDB() method allows save new objects of Product class into database
    public void insertDB() {
        //defines Connection con as null
        Connection con = MainWindow.dbConnection();
        try {
            //tries to create a SQL statement to insert values of a Product object into the database
            String query = "INSERT INTO Product VALUES ('" + productID + "','" + name + "','" + description + "'," +
                    price + "," + availability + ");";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            //shows a message that a new Product has been added successfully
            System.out.println("new product has been added into the data base successfully");
            Message message = new Message("Confirmation", "new product has been added into the data base successfully");
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Error happened, when data had been inserted");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        MainWindow.closeConnection(con);
    }//end insertDB() method
    //get() methods to get class variables for tables
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getProductID() {
        return productID;
    }
    public double getPrice() {
        return price;
    }
    public int getAvailability() {
        return availability;
    }
}//end Product class
