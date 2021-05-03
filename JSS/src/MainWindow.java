import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

/*
Class MainWindow creates a main window of the application and sets up all its features and behaviour.
Class MainWindow has a custom constructor that receives Stage object as a parameter to make this class the primary
stage (the main window) of the application.
Class MainWindow has the next methods:
- mainWindowContent() to set up and show all items inside the window;
- newTab() to create new Tab object with particular features;
- greetings(), customers(), newCustomerForm(), newOrderForm(), newProductForm(), order(), products(), pawnshop(),
  allMenuButtons(), getCustomers(), getOrders(), getProducts(), getItems(), getID(), addPawnshopItem(), dbConnection(),
  closeConnection()  to create a content of all tabs and menu items as well as to get some particular information
  from the database;
 */
public class MainWindow {
    //declares all class variables, the stage of the main window, TabPane to place all tabs.
    Stage window;
    static String userFirstName, userLastName, userID, userPassword;
    User currentUser;
    TabPane allTabs;
    //a custom constructor of the main window that takes Stage object as a parameter and creates a new window with a title and sizes.
    MainWindow(Stage stage) {
        window = stage;
        //create a title for the main window
        window.setTitle("Jewelry Shop System");
        window.setWidth(1200);
        window.setHeight(800);
    }//end MainWindow constructor
    //method that creates a scene of the main window and places all content inside the scene.
    public void mainWindowContent() throws FileNotFoundException {
        //creates an object of current user
        currentUser = new User(userID, userPassword, userFirstName, userLastName);
        //left menu
        VBox leftMenu = new VBox();
        leftMenu.setPrefWidth(150);
        //custom class of left menu for stylesheet
        leftMenu.getStyleClass().clear();
        leftMenu.getStyleClass().add("leftMenu");
        leftMenu.setAlignment(Pos.CENTER);
        leftMenu.setSpacing(15);
        /*
        creates an array of buttons for left menu, by invoking allMenuButtons() method and passing sizes
        of image inside buttons as parameters
         */
        Button[] menuItems = allMenuButtons(50, 50);
        for(int i = 0; i < menuItems.length; i++) {
            leftMenu.getChildren().add(menuItems[i]);
        }//end for loop
        //the main content of the main window
        VBox workPlace = new VBox();
        //custom class of work place for stylesheet
        workPlace.getStyleClass().clear();
        workPlace.getStyleClass().add("workPlace");
        workPlace.setPadding(new Insets(25,25,25,25));
        //creates new TabPane object to show all menu items as tabs
        allTabs = new TabPane();
        allTabs.setPrefWidth(1000);
        allTabs.setPrefHeight(window.getHeight()-50);
        //the first greeting tab
        Tab greetingTab = new Tab("Greeting", greeting());
        //adds greetingTab to allTabs
        allTabs.getTabs().add(greetingTab);
        //adds tabs to workPlace
        workPlace.getChildren().add(allTabs);
        //BorderPane to arrange all items in the window
        BorderPane windowPane = new BorderPane();
        windowPane.setLeft(leftMenu);
        windowPane.setRight(workPlace);
        //Scene of the main window
        Scene scene = new Scene(windowPane);
        //custom stylesheet for the main window
        scene.getStylesheets().add("MainWindowCSS.css");
        //set scene as a scene of the window (Stage)
        window.setScene(scene);
    }//end mainWindowContent() method
    //greeting() method creates the greeting tab and places the whole content inside it
    private VBox greeting() throws FileNotFoundException {
        //VBox to place all content of the tab
        VBox greeting = new VBox();
        greeting.setPadding(new Insets(10,10,10,10));
        greeting.setAlignment(Pos.TOP_CENTER);
        //Greeting label
        Label greetingLabel = new Label("Welcome, " + currentUser.firstName + " " + currentUser.lastName + "!");
        //custom css class for greeting label
        greetingLabel.getStyleClass().clear();
        greetingLabel.getStyleClass().add("greetingLabel");
        //GridPane to place all buttons
        GridPane menuOptions = new GridPane();
        menuOptions.getStyleClass().clear();
        menuOptions.getStyleClass().add("menuOptions");
        menuOptions.setAlignment(Pos.CENTER);
        menuOptions.setPadding(new Insets(50, 0,0,0));
        menuOptions.setVgap(20);
        menuOptions.setHgap(20);
        //String array of text inside buttons
        String[] buttonText = {"All Customers", "New Customer", "My Customers",
                "All Orders", "New Order", "My Orders",
                "All jewelry", "New jewelry", "Pawnshop"};
        /*
        creates an array of buttons for greeting tab, by invoking allMenuButtons() method and passing sizes
        of image inside buttons as parameters
         */
        Button[] buttons = allMenuButtons(100, 100);
        int count = 0;
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GridPane.setConstraints(buttons[count], j, i);
                menuOptions.getChildren().add(buttons[count]);
                buttons[count].setPrefWidth(300);
                buttons[count].setText(buttonText[count]);
                count += 1;
            }//end for loop
        }//end for loop
        //add all items into the VBox
        greeting.getChildren().addAll(greetingLabel, menuOptions);
        return greeting;
    }//end greeting() method
    //customers() method creates content of "all customers" and "my customers" sections
    private VBox customers(String category) {
        //creates new VBox customers to place and arrange all items
        VBox customers = new VBox();
        customers.setAlignment(Pos.CENTER);
        //creates label of a section and depending on section sets up relevant text
        Label customerLabel = new Label();
        customerLabel.getStyleClass().add("newLabel");
        if(category.equals("all")) {
            customerLabel.setText("All customers");
        } else if (category.equals("my")) {
            customerLabel.setText("My customers");
        }//end if statement
        //creates new table to display data in it
        TableView<Customer> customersTable = new TableView<>();
        //String array of column names where datatype is String
        String [] allStringColumns = {"customerID", "firstName", "lastName", "gender", "dateOfBirth"};
        //String array of column names where datatype is Integer
        String [] allIntegerColumns = {"age", "phoneNumber"};
        //for loop creates columns where data type is String
        for(int i = 0; i < allStringColumns.length; i++) {
            TableColumn<Customer, String> newColumn = new TableColumn<>(allStringColumns[i]);
            newColumn.setMinWidth(100);
            newColumn.setCellValueFactory(new PropertyValueFactory<>(allStringColumns[i]));
            customersTable.getColumns().add(newColumn);
        }//end for loop
        //for loop creates columns where data type is Integer
        for(int i = 0; i < allIntegerColumns.length; i++) {
            TableColumn<Customer, Integer> newColumn = new TableColumn<>(allIntegerColumns[i]);
            newColumn.setMinWidth(100);
            newColumn.setCellValueFactory(new PropertyValueFactory<>(allIntegerColumns[i]));
            customersTable.getColumns().add(newColumn);
        }//end for loop
        //if statement fills tables with relevant data depending on section
        if(category.equals("all")) {
            customersTable.setItems(getCustomers("all"));
        } else if (category.equals("my")) {
            customersTable.setItems(getCustomers("my"));
        }//end if statement
        //creates one more table column for seller name and makes it the last column in the table
        TableColumn<Customer, String> sellerColumn = new TableColumn<>("Seller");
        sellerColumn.setMinWidth(100);
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        customersTable.getColumns().add(sellerColumn);
        //places label and table inside customers VBox
        customers.getChildren().addAll(customerLabel, customersTable);
        return customers;
    }//end customers() method
    //newCustomerForm() method creates a section with form to add new customer into the database
    private VBox newCustomerForm() {
        VBox newCustomer = new VBox();
        //creates a grid to arrange form inside the window
        GridPane form = new GridPane();
        form.setPadding(new Insets(30, 30, 30, 30));
        form.setAlignment(Pos.CENTER);
        form.setVgap(20);
        form.setHgap(20);
        Label newCustomerLabel = new Label("New Customer Form");
        //custom css class for newCustomerLabel
        newCustomerLabel.getStyleClass().clear();
        newCustomerLabel.getStyleClass().add("newItemLabel");
        newCustomerLabel.setAlignment(Pos.CENTER);
        //String array to create labels for each text field
        String[] fields = {"firstName", "lastName", "gender", "dateOfBirth", "phoneNumber"};
        //for loop that creates labels for text fields and adds them into the gridPane
        for(int i = 0; i < fields.length; i++) {
            Label newLabel = new Label(fields[i]);
            newLabel.getStyleClass().clear();
            newLabel.getStyleClass().add("newLabel");
            GridPane.setConstraints(newLabel, 0, (i + 1), 1, 1);
            form.getChildren().add(newLabel);
        }//end for loop
        //first name text field
        TextField firstName = new TextField();
        firstName.getStyleClass().clear();
        firstName.getStyleClass().add("newField");
        GridPane.setConstraints(firstName, 1, 1, 1, 1);
        //last name text field
        TextField lastName = new TextField();
        lastName.getStyleClass().clear();
        lastName.getStyleClass().add("newField");
        GridPane.setConstraints(lastName, 1, 2, 1, 1);
        //gender field
        ChoiceBox<String> gender = new ChoiceBox<>();
        gender.getStyleClass().add("gender");
        gender.getItems().addAll("Female", "Male");
        gender.setValue("Female");
        gender.setPrefWidth(250);
        GridPane.setConstraints(gender, 1, 3, 1, 1);
        //date of birth field
        DatePicker birthDate = new DatePicker();
        //setDayCellFactory method is used to set limits for a calendar to not allow user to choose future date
        birthDate.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(LocalDate.now()));
                    }});
        //custom css class for DatePicker birthDate
        birthDate.getStyleClass().add("birthDate");
        GridPane.setConstraints(birthDate, 1, 4, 1, 1);
        //phone number text field
        TextField phoneNumber = new TextField();
        phoneNumber.getStyleClass().clear();
        phoneNumber.getStyleClass().add("newField");
        GridPane.setConstraints(phoneNumber, 1, 5, 1, 1);
        //add all fields into gridPane
        form.getChildren().addAll(firstName, lastName, gender, birthDate, phoneNumber);
        //button "Submit" to insert data entered by user into the database
        Button addButton = new Button("Submit");
        //button event handler to process click on button event
        addButton.setOnAction(e -> {
            try {
                //retrieve data entered by user from all fields of the form
                String customerID = getID("Customer");
                String inputFirstName = firstName.getText();
                String inputLastName = lastName.getText();
                String inputGender = gender.getValue();
                LocalDate ld = birthDate.getValue();
                String inputDateOfBirth = ld.toString();
                //calculate age of a customer as a difference between current date and date of the birth
                int inputAge = LocalDate.now().getYear() - birthDate.getValue().getYear();
                long inputPhoneNumber = Long.parseLong(phoneNumber.getText());
                String inputUserID = userID;
                //creates a new object of Customer and passes data entered by user as parameters
                Customer newGuy = new Customer(customerID, inputFirstName, inputLastName, inputGender, inputAge, inputPhoneNumber, inputDateOfBirth, inputUserID);
                //invokes insertDB() method to add data of new customer into database
                newGuy.insertDB();
            } catch (Exception g) {
                //catches exceptions and shows message about them
                System.out.println(g.getMessage());
                Message errorMessage = new Message("Error", "Please, check data entered and try again");
            }//end try/catch
        });//end button event handler
        //custom css class for addButton
        addButton.getStyleClass().clear();
        addButton.getStyleClass().add("addButton");
        addButton.setPrefWidth(250);
        addButton.setAlignment(Pos.CENTER);
        newCustomer.setAlignment(Pos.CENTER);
        newCustomer.getChildren().addAll(newCustomerLabel, form, addButton);
        return newCustomer;
    }//end newCustomerForm() method
    //newOrderForm() method creates a section with form to add new order into the database
    private VBox newOrderForm() {
        VBox newOrder = new VBox();
        //creates a grid to arrange form inside the window
        GridPane form = new GridPane();
        form.setPadding(new Insets(30, 30, 30, 30));
        form.setAlignment(Pos.CENTER);
        form.setVgap(20);
        form.setHgap(20);
        Label newOrderLabel = new Label("New Order Form");
        //custom css class for newOrderLabel
        newOrderLabel.getStyleClass().clear();
        newOrderLabel.getStyleClass().add("newItemLabel");
        newOrderLabel.setAlignment(Pos.CENTER);
        //String array to create labels for each text field
        String[] fields = {"productID", "customerID"};
        //for loop that creates labels for text fields and adds them into the gridPane
        for(int i = 0; i < fields.length; i++) {
            Label newLabel = new Label(fields[i]);
            newLabel.getStyleClass().clear();
            newLabel.getStyleClass().add("newLabel");
            GridPane.setConstraints(newLabel, 0, (i + 1), 1, 1);
            form.getChildren().add(newLabel);
        }//end for loop
        //product ID field
        TextField productID = new TextField();
        productID.getStyleClass().clear();
        productID.getStyleClass().add("newField");
        GridPane.setConstraints(productID, 1, 1, 1, 1);
        //last name text field
        TextField customerID = new TextField();
        customerID.getStyleClass().clear();
        customerID.getStyleClass().add("newField");
        GridPane.setConstraints(customerID, 1, 2, 1, 1);
        //add all fields into gridPane
        form.getChildren().addAll(productID, customerID);
        //button "Submit" to insert data entered by user into the database
        Button addButton = new Button("Submit");
        //button event handler to process click on button event
        addButton.setOnAction(e -> {
            try {
                //retrieve data entered by user from all fields of the form
                String orderID = getID("CustomerOrder");
                String inputProductID = productID.getText();
                String inputCustomerID = customerID.getText();
                String inputDate = LocalDate.now().toString();
                String inputUserID = userID;
                //creates a new object of Order and passes data entered by user as parameters
                Order order = new Order(orderID, inputProductID, inputCustomerID, inputDate, inputUserID);
                //invokes insertDB() method to add data of new customer into database
                order.insertDB();
            } catch (Exception g) {
                //catches exceptions and shows message about them
                System.out.println(g.getMessage());
                Message errorMessage = new Message("Error", "Please, check data entered and try again");
            }//end try/catch
        });//end button event handler
        //custom css class for addButton
        addButton.getStyleClass().clear();
        addButton.getStyleClass().add("addButton");
        addButton.setPrefWidth(250);
        addButton.setAlignment(Pos.CENTER);
        newOrder.setAlignment(Pos.CENTER);
        //adds all items into VBox newOrder
        newOrder.getChildren().addAll(newOrderLabel, form, addButton);
        return newOrder;
    }//end newOrderForm() methos
    //newProductForm() method creates a section with form to add new product into the database
    private VBox newProductForm() {
        VBox newProduct = new VBox();
        //creates a grid to arrange form inside the window
        GridPane form = new GridPane();
        form.setPadding(new Insets(30, 30, 30, 30));
        form.setAlignment(Pos.CENTER);
        form.setVgap(20);
        form.setHgap(20);
        Label newProductLabel = new Label("New jewelry Form");
        //custom css class for newProductLabel
        newProductLabel.getStyleClass().clear();
        newProductLabel.getStyleClass().add("newItemLabel");
        newProductLabel.setAlignment(Pos.CENTER);
        //String array to create labels for each text field
        String[] fields = {"name", "description", "price", "availability"};
        //for loop that creates labels for text fields and adds them into the gridPane
        for(int i = 0; i < fields.length; i++) {
            Label newLabel = new Label(fields[i]);
            newLabel.getStyleClass().clear();
            newLabel.getStyleClass().add("newLabel");
            GridPane.setConstraints(newLabel, 0, (i + 1), 1, 1);
            form.getChildren().add(newLabel);
        }//end for loop
        //product name field
        TextField name = new TextField();
        name.getStyleClass().clear();
        name.getStyleClass().add("newField");
        GridPane.setConstraints(name, 1, 1, 1, 1);
        //description text field
        TextField description = new TextField();
        description.getStyleClass().clear();
        description.getStyleClass().add("newField");
        GridPane.setConstraints(description, 1, 2, 1, 1);
        //price text field
        TextField price = new TextField();
        price.getStyleClass().clear();
        price.getStyleClass().add("newField");
        GridPane.setConstraints(price, 1, 3, 1, 1);
        //availability text field
        TextField availability = new TextField();
        availability.getStyleClass().clear();
        availability.getStyleClass().add("newField");
        GridPane.setConstraints(availability, 1, 4, 1, 1);
        //add all fields into gridPane
        form.getChildren().addAll(name, description, price, availability);
        //button "Submit" to insert data entered by user into the database
        Button addButton = new Button("Submit");
        //button event handler to process click on button event
        addButton.setOnAction(e -> {
            try {
                //retrieve data entered by user from all fields of the form
                String productID = getID("Product");
                String inputName = name.getText();
                String inputDescription = description.getText();
                double inputPrice = Double.parseDouble(price.getText());
                int inputAvailability = Integer.parseInt(availability.getText());
                //creates a new object of Product and passes data entered by user as parameters
                Jewelry product = new Jewelry(productID, inputName, inputDescription, inputPrice, inputAvailability);
                //invokes insertDB() method to add data of new customer into database
                product.insertDB();
            } catch (Exception g) {
                //catches exceptions and shows message about them
                System.out.println(g.getMessage());
                Message errorMessage = new Message("Error", "Please, check data entered and try again");
            }//end try/catch
        });//end button event handler
        //custom css class for addButton
        addButton.getStyleClass().clear();
        addButton.getStyleClass().add("addButton");
        addButton.setPrefWidth(250);
        addButton.setAlignment(Pos.CENTER);
        newProduct.setAlignment(Pos.CENTER);
        //adds all items into newProduct VBox
        newProduct.getChildren().addAll(newProductLabel, form, addButton);
        return newProduct;
    }//end newProductForm() method
    //orders() method creates content of "all orders" and "my orders" sections
    private VBox orders(String category) {
        VBox orders = new VBox();
        orders.setAlignment(Pos.CENTER);
        //creates label of a section and depending on section sets up relevant text
        Label ordersLabel = new Label();
        ordersLabel.getStyleClass().add("newLabel");
        if(category.equals("all")) {
            ordersLabel.setText("All orders");
        } else if (category.equals("my")) {
            ordersLabel.setText("My orders");
        }//end if statement
        //creates new table to display data in it
        TableView<Order> orderTable = new TableView<>();
        //String array of column names
        String [] columnNames = {"orderID", "productID", "customerID", "date"};
        //for loop creates columns
        for(int i = 0; i < columnNames.length; i++) {
            TableColumn<Order, String> newColumn = new TableColumn<>(columnNames[i]);
            newColumn.setMinWidth(100);
            newColumn.setCellValueFactory(new PropertyValueFactory<>(columnNames[i]));
            orderTable.getColumns().add(newColumn);
        }//end for loop
        //if statement fills tables with relevant data depending on section
        if(category.equals("all")) {
            orderTable.setItems(getOrders("all"));
        } else if (category.equals("my")) {
            orderTable.setItems(getOrders("my"));
        }//end if statement
        //creates one more table column for seller name and makes it the last column in the table
        TableColumn<Order, String> sellerColumn = new TableColumn<>("Seller");
        sellerColumn.setMinWidth(100);
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        orderTable.getColumns().add(sellerColumn);
        //places items inside orders VBox
        orders.getChildren().addAll(ordersLabel, orderTable);
        return orders;
    }//end orders() method
    //orders() method creates content of "product" section
    private VBox products() {
        VBox products = new VBox();
        products.setAlignment(Pos.CENTER);
        //creates label of a section
        Label productsLabel = new Label();
        productsLabel.getStyleClass().add("newLabel");
        productsLabel.setText("All jewelry");
        //creates new table to display data in it
        TableView<Jewelry> productTable = new TableView<>();
        //String array of column names
        String [] columnNames = {"productID", "name", "description"};
        //for loop creates columns
        for(int i = 0; i < columnNames.length; i++) {
            TableColumn<Jewelry, String> newColumn = new TableColumn<>(columnNames[i]);
            newColumn.setMinWidth(100);
            newColumn.setCellValueFactory(new PropertyValueFactory<>(columnNames[i]));
            productTable.getColumns().add(newColumn);
        }//end for loop
        //creates a column for price with data type Double
        TableColumn<Jewelry, Double> priceColumn = new TableColumn<>("price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        //creates a column for availability with data type Integer
        TableColumn<Jewelry, Integer> availabilityColumn = new TableColumn<>("availability");
        availabilityColumn.setMinWidth(100);
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        productTable.getColumns().addAll(priceColumn, availabilityColumn);
        //fills the table with data from the database
        productTable.setItems(getProducts());
        //places items inside orders VBox
        products.getChildren().addAll(productsLabel, productTable);
        return products;
    }//end product() method
    //pawnshop() method creates content of "pawnshop" section
    private VBox pawnshop() {
        VBox pawnshop = new VBox();
        pawnshop.setAlignment(Pos.CENTER);
        //creates label of a section
        Label pawnshopLabel = new Label();
        pawnshopLabel.getStyleClass().add("newLabel");
        pawnshopLabel.setText("Pawnshop");
        //creates new table to display data in it
        TableView<PawnshopItem> pawnshopTable = new TableView<>();
        //String array of column names
        String [] columnNames = {"itemID", "name", "description", "customerID", "date"};
        //for loop creates columns
        for(int i = 0; i < columnNames.length; i++) {
            TableColumn<PawnshopItem, String> newColumn = new TableColumn<>(columnNames[i]);
            newColumn.setMinWidth(100);
            newColumn.setCellValueFactory(new PropertyValueFactory<>(columnNames[i]));
            pawnshopTable.getColumns().add(newColumn);
        }//end for loop
        //creates a column for pawnLoan with data type Double
        TableColumn<PawnshopItem, Double> loanColumn = new TableColumn<>("pawnLoan");
        loanColumn.setMinWidth(100);
        loanColumn.setCellValueFactory(new PropertyValueFactory<>("pawnLoan"));
        pawnshopTable.getColumns().addAll(loanColumn);
        //fills the table with data from the database
        pawnshopTable.setItems(getItems());
        //creates form to allow insert new pawnshop items
        HBox addNewItem = addPawnshopItem();
        //places items inside orders VBox
        pawnshop.getChildren().addAll(pawnshopLabel, pawnshopTable, addNewItem);
        return pawnshop;
    }//end pawnshop() method
    //allMenuButtons() method creates all menu items and buttons for the left menu and greeting tab
    private Button[] allMenuButtons(int imageHeinght, int imageWidth) throws FileNotFoundException {
        //all customers menu button
        Button allCustomersButton = new Button();
        allCustomersButton.setOnAction(e -> {
            Tab newTab = new Tab("allCustomers", customers("all"));
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //"add new customer" menu button
        Button newCustomerButton = new Button();
        newCustomerButton.setOnAction(e -> {
            Tab newTab = new Tab("newCustomer", newCustomerForm());
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //"my customers" menu button
        Button myCustomersButton = new Button();
        myCustomersButton.setOnAction(e -> {
            Tab newTab = new Tab("myCustomers", customers("my"));
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //all orders menu button
        Button allOrdersButton = new Button();
        allOrdersButton.setOnAction(e -> {
            Tab newTab = new Tab("allOrders", orders("all"));
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //"add new order" menu button
        Button newOrderButton = new Button();
        newOrderButton.setOnAction(e -> {
            Tab newTab = new Tab("newOrder", newOrderForm());
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //"my orders" menu button
        Button myOrdersButton = new Button();
        myOrdersButton.setOnAction(e -> {
            Tab newTab = new Tab("myOrders", orders("my"));
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //all products menu button
        Button allProductsButton = new Button();
        allProductsButton.setOnAction(e -> {
            Tab newTab = new Tab("allProducts", products());
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //"add new product" menu button
        Button newProductButton = new Button();
        newProductButton.setOnAction(e -> {
            Tab newTab = new Tab("newProduct", newProductForm());
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //"pawnshop" menu button
        Button pawnshopButton = new Button();
        pawnshopButton.setOnAction(e -> {
            Tab newTab = new Tab("pawnshop", pawnshop());
            allTabs.getTabs().add(newTab);
            allTabs.getSelectionModel().select(newTab);
        });
        //button array contents all menu buttons and is used to assign names, icons and button events handler for each button
        Button[] allMenuButtons = {allCustomersButton, newCustomerButton, myCustomersButton,
                allOrdersButton, newOrderButton, myOrdersButton,
                allProductsButton, newProductButton, pawnshopButton};
        //String array contents all button names
        String[] iconNames = {"allCustomers", "newCustomer", "myCustomers",
                "allOrders", "newOrder", "myOrders",
                "allProducts", "newProduct", "pawnshop"};
        //for loop adds names, button events handler and icons for each button
        for(int i = 0; i < allMenuButtons.length; i++) {
            //creates icon for eacn button
            Image icon = new Image(new FileInputStream("static/images/icons/" + iconNames[i] + ".png"), imageHeinght, imageWidth, false, false);
            ImageView imageView = new ImageView(icon);
            allMenuButtons[i].setGraphic(imageView);
        }//end for loop
        return allMenuButtons;
    }//end allMenuButtons() method
    /*
    getCustomers() method receives kind of customer section as a parameter ("all" - for all customers section
    and "my" - for my customers section) and returns relevant ObservableList of Customer objects.
     */
    public ObservableList<Customer> getCustomers(String kindOfCustomers) {
        //creates new observable array list
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        //connect to the database to get relevant Customer objects depending on section
        Connection con = dbConnection();
        try {
            //creates SQL statement to retrieve relevant data from the database
            String query = " ";
            if (kindOfCustomers.equals("all")) {
                query = "SELECT * FROM Customer;";
            } else if (kindOfCustomers.equals("my")) {
                query = "SELECT * FROM Customer WHERE userID=" + "'" + userID + "';";
            }//end if statement
            Statement statement = con.createStatement();
            //creates ResultSet with all data received from the database
            ResultSet result = statement.executeQuery(query);
            //iterates the resultSet, creates Customer objects and adds them into the observable array list
            while (result.next()) {
                customers.add(new Customer(result.getString("customerID"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getInt("age"),
                        result.getInt("phonenumber"),
                        result.getString("dateOfBirth"),
                        result.getString("userID")));
            }//end while loop
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Something wrong happened. Try again later.");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        closeConnection(con);
        return customers;
    }//end getCustomer() method
    /*
    getOrders() method receives kind of orders section as a parameter ("all" - for all orders section
    and "my" - for my orders section and returns relevant ObservableList of Order objects.
     */
    public ObservableList<Order> getOrders(String kindOfOrders) {
        //creates new observable array list
        ObservableList<Order> orders = FXCollections.observableArrayList();
        //connect to the database to get relevant Order objects depending on section
        Connection con = dbConnection();
        try {
            //creates SQL statement to retrieve relevant data from the database
            String query = " ";
            if (kindOfOrders.equals("all")) {
                query = "SELECT * FROM CustomerOrder;";
            } else if (kindOfOrders.equals("my")) {
                query = "SELECT * FROM CustomerOrder WHERE userID=" + "'" + userID + "';";
            }//end if statement
            Statement statement = con.createStatement();
            //creates ResultSet with all data received from the database
            ResultSet result = statement.executeQuery(query);
            //iterates the resultSet, creates Order objects and adds them into the observable array list
            while (result.next()) {
                orders.add(new Order(result.getString("orderID"),
                        result.getString("productID"),
                        result.getString("customerID"),
                        result.getString("date"),
                        result.getString("userID")));
            }//end while loop
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Something wrong happened. Try again later.");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        closeConnection(con);
        return orders;
    }//end getOrders() method
    /*
    getProducts() method receives kind of product section as a parameter ("all" - for all products section
    and "my" - for my products section and returns relevant ObservableList of Product objects.
     */
    public ObservableList<Jewelry> getProducts() {
        //creates new observable array list
        ObservableList<Jewelry> products = FXCollections.observableArrayList();
        //connect to the database to get relevant Product objects depending on section
        Connection con = dbConnection();
        try {
            //creates SQL statement to retrieve relevant data from the database
            String query = "SELECT * FROM Product;";
            Statement statement = con.createStatement();
            //creates ResultSet with all data received from the database
            ResultSet result = statement.executeQuery(query);
            //iterates the resultSet, creates Product objects and adds them into the observable array list
            while (result.next()) {
                products.add(new Jewelry(result.getString("productID"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getDouble("price"),
                        result.getInt("availability")));
            }//end while loop
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Something wrong happened. Try again later.");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        closeConnection(con);
        return products;
    }//end getProducts() method
    /*
    getItems() method  returns relevant ObservableList of PawnshopItem objects.
     */
    public ObservableList<PawnshopItem> getItems() {
        //creates new observable array list
        ObservableList<PawnshopItem> items = FXCollections.observableArrayList();
        //connect to the database to get relevant PawnshopItem objects depending on section
        Connection con = dbConnection();
        try {
            //creates SQL statement to retrieve relevant data from the database
            String query = "SELECT * FROM Pawnshop;";
            Statement statement = con.createStatement();
            //creates ResultSet with all data received from the database
            ResultSet result = statement.executeQuery(query);
            //iterates the resultSet, creates PawnshopItem objects and adds them into the observable array list
            while (result.next()) {
                items.add(new PawnshopItem(result.getString("itemID"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getDouble("pawnLoan"),
                        result.getString("customerID"),
                        result.getString("date")));
            }
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Something wrong happened. Try again later.");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        closeConnection(con);
        return items;
    }//end getItems() method
    //getID() method receives table name of the database as a parameter and returns the next ID to make it unique
    public String getID(String tableName) {
        //declare method variables and assigns their initial values
        String id = " ";
        int rowNumber = 0;
        String columnID = " ";
        //depending on table names defines columnID variable with relevant value
        if (tableName.equals("CustomerOrder")) {
            columnID = "orderID";
        } else if ((tableName.equals("Customer")) || (tableName.equals("Product"))) {
            columnID = tableName.toLowerCase() + "ID";
        } else if (tableName.equals("Pawnshop")) {
            columnID = "itemID";
        }//end if statement
        //connect to the database to get numbers of rows in relevant table
        Connection con = dbConnection();
        try {
            //creates SQL statement to retrieve relevant data from the database
            String query = "SELECT COUNT(" + columnID + ") FROM " + tableName;
            Statement statement = con.createStatement();
            //creates ResultSet with all data received from the database
            ResultSet result = statement.executeQuery(query);
            result.next();
            rowNumber = result.getInt(1);
        } catch (Exception e) {
            //catches exceptions and show message about them
            System.out.println(e.getMessage());
            Message errorMessage = new Message("Error", "Something wrong happened. Try again later.");
        }//end try/catch
        //checks if Connection con is equaled null or not and closes it.
        closeConnection(con);
        //depending on table name creates the next available ID
        if (tableName.equals("Customer")) {
            id = "C-" + (rowNumber + 1);
        } else if (tableName.equals("CustomerOrder")) {
            id = "O-" + (rowNumber + 1);
        } else if (tableName.equals("Product")) {
            id = "R01-" + (rowNumber + 1);
        } else if (tableName.equals("Pawnshop")) {
            id = "P-" + (rowNumber + 1);
        }//end if statement
        return id;
    }//end getID() method
    /*
    addPawnshopItem() method creates a form bar to fill for adding a new pawnshop item into the database
    and returns HBox with this form bar.
     */
    public HBox addPawnshopItem() {
        //creates new HBox to place all elements inside it
        HBox newItem = new HBox();
        newItem.setPadding(new Insets(10,10,10,10));
        newItem.setSpacing(10);
        //item name text field
        TextField nameField = new TextField();
        nameField.setPromptText("Item name");
        //description text field
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        //pawn loan text field
        TextField pawnLoanField = new TextField();
        pawnLoanField.setPromptText("pawn loan");
        //customer ID text field
        TextField customerIDField = new TextField();
        customerIDField.setPromptText("customer ID");
        //button to add new record to the database
        Button addButton = new Button("Add");
        //creates button events handler for addButton
        addButton.setOnAction(e -> {
            try {
                //retrieves data entered by user
                String itemID = getID("Pawnshop");
                String name = nameField.getText();
                String description = descriptionField.getText();
                double pawnLoan = Double.parseDouble(pawnLoanField.getText());
                String customerID = customerIDField.getText();
                String date = LocalDate.now().toString();
                //creates new PawnshopItem object passing data entered by user as parameters
                PawnshopItem item = new PawnshopItem(itemID, name, description, pawnLoan, customerID, date);
                //invokes insertDB() method to insert new item into the database
                item.insertDB();
            } catch (Exception g) {
                //catches exceptions and shows a message about them
                System.out.println(g.getMessage());
                Message errorMessage = new Message("Error", "Check data entered or try again later.");
            }//end try/catch
        });//end button events handler
        //adds all elements into the bar (HBox)
        newItem.getChildren().addAll(nameField, descriptionField, pawnLoanField, customerIDField, addButton);
        return newItem;
    }//end addPawnshopItem() method
    //dbConnection() method creates Connection object and connects to the database
    public static Connection dbConnection() {
        String url = "jdbc:sqlite:db/test.db";
        //connect to the database
        Connection con = null;
        try {
            //tries to establish connection to the database
            con = DriverManager.getConnection(url);
            System.out.println("connection has been established");
        } catch (Exception e) {
            //exception handler
            System.out.println(e.getMessage());
            //shows message about the exception
            Message errorMessage = new Message("Error", "Connection has not been established. Try again later.");
        }//end try/catch
        return con;
    }//end dbConnection() method
    //closeConnection() method receives Connection object as a parameter, checks if this object equals zero or not and
    //closes the connection to the database
    public static void closeConnection(Connection conName) {
        if (conName != null) {
            //tries to close connection and show message about that
            try {
                conName.close();
                System.out.println("connection has been closed");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Message errorMessage = new Message("Error", "Error with the database.");
            }//end try/catch
        }//end if statement
    }//end closeConnection() method
}//end MainWindow class
