import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
/*
Create class Pawnshop that allows store information about items of pawnshop such as itemID (to make all items unique), name,
description, pawnLoan, customerID and date.
Class contains custom constructor that takes data entered by user in the main method and store it in variables,
and one method insertDB() to store new items data into the database.
In addition to that, there are getters that are used to get data of class variables for tables.
*/
public class PawnshopItem {
    //declare class variables to store information about product
    String name, description, itemID, customerID, date;
    double pawnLoan;
    //create custom constructor that receives data for variables as parameters
    PawnshopItem(String itemID, String name, String description, double pawnLoan, String customerID, String date) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.pawnLoan = pawnLoan;
        this.customerID = customerID;
        this.date = date;
    }//end custom Product constructor
    //insertDB() method allows save new objects of pawnshop class into database
    public void insertDB() {
        //defines Connection con as null
        Connection con = MainWindow.dbConnection();
        try {
            //tries to create a SQL statement to insert values of a pawnshop item object into the database
            String query = "INSERT INTO Pawnshop VALUES ('" + itemID + "','" + name + "','" + description + "'," +
                    pawnLoan + ",'" + customerID + "','" + date + "');";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            //shows a message that a new item has been added successfully
            System.out.println("new item has been added into the data base successfully");
            Message message = new Message("Confirmation", "new item has been added into the data base successfully");
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
    public String getItemID() {
        return itemID;
    }
    public double getPawnLoan() {
        return pawnLoan;
    }
    public String getCustomerID() {
        return customerID;
    }
    public String getDate() {
        return date;
    }
}//end PawnshopItem class
