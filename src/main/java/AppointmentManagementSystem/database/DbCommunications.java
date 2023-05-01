package AppointmentManagementSystem.database;

import AppointmentManagementSystem.exceptions.InvalidCredentialException;
import AppointmentManagementSystem.models.Appointment;
import AppointmentManagementSystem.models.Customer;
import AppointmentManagementSystem.models.Representative;
import AppointmentManagementSystem.models.User;
import AppointmentManagementSystem.util;
import AppointmentManagementSystem.vars;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/** 
 * This is a utility file that is supposed to communicate with the database and store essential information ie: Customers list, locale,and User.
 */
public class DbCommunications extends dbstrings {

    public static User CurrentUser = null; // this is the signed in
    public static Locale appLocale = Locale.getDefault();// locale for language settings
    public static ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(); //a list of customer

    //    ------------------------Login Methods --------------------------
    public static boolean checkConnection() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException ex){
                throw ex;
            }
            // Open a connection
            conn = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);

            // Execute a query to test the connection
            stmt = conn.createStatement();
            stmt.executeQuery("SELECT 1");

            return true;

        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            // Handle errors for Class.forName

            throw ex;
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                throw ex;
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                throw ex;
            }
        }



    }

    //    ------------------------Login Methods --------------------------



    /**
     * If the user exists and the password matches, it will change the screen to the login screen and log a success message.
     * If the password does not match, it will throw an exception and log a failure message
     * @param e  an ActionEvent
     * @param username the username form the user input
     * @param Password the password from the user input
     */
    public static void loginWithUserNameAndPassword(ActionEvent e, String username, String Password) {

        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        ObservableList<String>  ARR= FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement("SELECT * FROM USERS WHERE UserName= ?");
            s.setString(1, username);
            r = s.executeQuery();
            if (!r.isBeforeFirst()) {
                ARR.add(ZonedDateTime.now()+ ",Status failed, username does not exist");
                throw new InvalidCredentialException("Invalid UserName Or Password");
            } else {
                while (r.next()) {
                    String retrievedPassword = r.getString("PASSWORD");
                    int UserId = r.getInt("USERID");
                    String UserName = r.getString("UserName");
                    if (retrievedPassword.equals(Password)) {
                        ChangeScreenToLogin(e, UserName, UserId);
                        ARR.add(ZonedDateTime.now()+ ",Status: successful");
                    } else {
                        ARR.add(ZonedDateTime.now()+ ",Status: failed" );
                        throw new InvalidCredentialException("Invalid UserName Or Password");
                    }
                }
            }
        }
        catch (SQLException ex) {
            int errorCode = ex.getErrorCode();
            System.out.println(errorCode);




            ex.printStackTrace();
        } catch (InvalidCredentialException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", appLocale).getString("INVALIDUSERNAMEORPASSWORDERROR")
            );
            a.show();
        } finally {
            try {
                r.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                util.writingToActivityFile(ARR);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Changes Screen To Log in Screen and alert if any appointment between now and 15 minutes
     * @param e an ActionEvent
     * @param name the username of the user
     * @param UserId the userId of the user
     */
    private static void ChangeScreenToLogin(ActionEvent e, String name, int UserId) {
        try
        {
            customerObservableList=getClients();
            CurrentUser=setCurentUser(UserId);
            ObservableList<Appointment> appointmentinFifteen = CurrentUser.getAllAppointments().filtered(an -> util.in15minutes(an.getLocalStartDateTime()) && an.getUserid() == UserId);


            ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", appLocale);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            util.changeScreen("Dashboard.fxml",null,stage, DbCommunications.class.getName());


            String AlertString;
            if (appointmentinFifteen.size() != 0) {
                AlertString = rr.getString("YOUHAVE") + " " + appointmentinFifteen.size() + " " +
                        rr.getString("APPOINTMENTS") + ":\n";

                for (Appointment an : appointmentinFifteen) {
                    AlertString += "id: "+ an.getAppointmentId()
                            + " Title: "+ an.getTitle()
                            + " Start Date: "+ an.getLocalStartDateTime().toLocalDate()
                            + " Start Time: "+an.getLocalStartDateTime().toLocalTime()
                            + " End Date: "+ an.getLocalEndDateTime().toLocalDate()
                            + " End Time: "+ an.getLocalEndDateTime().toLocalTime()
                            + "\n----------------------------------------\n";
                }
                Alert alert=  new Alert(Alert.AlertType.INFORMATION, AlertString);

                alert.showAndWait();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static User setCurentUser(int userId) {
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement(" SELECT UserID, UserName  FROM USERS  where UserID=?;");
            s.setInt(1,userId);
            r = s.executeQuery();
            if (r.isBeforeFirst()) {

                while (r.next()) {

                    int USER_ID = r.getInt("USERID");
                    String NAME = r.getString("UserName");
                    User u =new User(USER_ID, NAME);

                    u.setAllAppointments(getAppointmentsFromDb(new ActionEvent(),USER_ID));
                    return u;
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null ;

    }

    public static void logout(ActionEvent e) {
        CurrentUser.Signout();
        customerObservableList.removeAll(customerObservableList);
        System.out.println("_______________ USER SIGNED OUT_______________________");
        try {
           Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            util.changeScreen("Login.fxml",null,stage, DbCommunications.class.getName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


//  ------------------------Appointment Methods --------------------------
    public static ObservableList<Appointment> getAppointmentsFromDb(ActionEvent e, int UserId) {

        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        ObservableList<Appointment> a = FXCollections.observableArrayList();
        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement("SELECT * FROM APPOINTMENT INNER JOIN " +
                    "(SELECT c.ClientID, c.ClientName,r.RepresentativeID , r.RepName, r.Phone as RepPhone, r.email as RepEmail,r.Department as RepDept " +
                    "FROM CLIENTS c LEFT JOIN REPRESENTATIVE r ON c.RepresentativeID = r.RepresentativeID WHERE r.RepresentativeID IS NOT NULL OR c.RepresentativeID IS NULL) " +
                    "AS subquery ON APPOINTMENT.ClientID = subquery.ClientID WHERE UserID=?"
            );
                s.setInt(1,UserId);
            r = s.executeQuery();
            if (r.isBeforeFirst()) {
                while (r.next()) {

//
                    int AppointmentId = r.getInt("AppointmentID");
                    String title = r.getString("Title");
                    String Type= r.getString("Type");
                    String location = r.getString("Location");


                    LocalDate Startdate = r.getDate("StartTime").toLocalDate();
                    LocalTime StartTime = r.getTime("StartTime").toLocalTime();

                    LocalDate EndDate = r.getDate("EndTime").toLocalDate();
                    LocalTime EndTime = r.getTime("EndTime").toLocalTime();

                    ZonedDateTime LocalStartDateTime = util.UTCtolocal(util.UTCDateTime(Startdate, StartTime));
                    ZonedDateTime LocalEndDateTime = util.UTCtolocal(util.UTCDateTime(EndDate, EndTime));

//                    Client
                    int clientID = r.getInt("ClientID");

                    // representativeInfo
                    int repId=r.getInt("RepresentativeID");
                    String RepName=r.getString("RepName");
                    String RepEmail=r.getString("RepEmail");
                    String RepPhone=r.getString("RepPhone");
                    String RepDept=r.getString("RepDept");

                  Customer customer=  customerObservableList.filtered(c ->c.getCUSTOMER_ID()==clientID).get(0);
                  Representative representative=new Representative(repId,RepName,RepDept,RepPhone,RepEmail);
                  customer.setRepresentative(representative);
                    Appointment appointment = new Appointment(AppointmentId, title, location,Type,
                            LocalStartDateTime, LocalEndDateTime, customer,
                            UserId);
                    a.add(appointment);

                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return a;

    }
    public static int addAppointment(Appointment a) {
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            String sql= "INSERT INTO `APPOINTMENT`(`Title`,`Type`,`Location`,`StartTime`,`EndTime`,`ClientID`,`UserID`,`CreateDateTime`)" +
                    "VALUES(?,?,?,?,?,?,?,?)";
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            s.setString(1,a.getTitle());
            s.setString(2,a.getTYPE());
            s.setString(3,a.getLocation());

            s.setTimestamp(4, Timestamp.valueOf(util.localtoUTC((a.getLocalStartDateTime())).toLocalDateTime()));
            s.setTimestamp(5,Timestamp.valueOf(util.localtoUTC((a.getLocalEndDateTime())).toLocalDateTime()));
            s.setInt(6,a.getCustomerId());
            s.setInt(7,CurrentUser.getUid());
            s.setTimestamp(8,Timestamp.valueOf(util.localtoUTC(ZonedDateTime.now()).toLocalDateTime()));

            int affectedRows = s.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = s.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                  return id;
                } else {
                    throw new SQLException("Creating appointment failed, no ID obtained.");
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", appLocale).getString("CANTUPDATEAPPOINTMENT")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }


        return -1;
    }

    public static int updateAppointment(Appointment a) {
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement("UPDATE `APPOINTMENT` " +
                    "SET `Title` = ?,`Type` = ?,`Location` = ?,`" +
                    "StartTime` = ?,`EndTime` = ?,`ClientID` = ?," +
                    "`UserID` = ? WHERE `AppointmentID` = ?;");

            s.setString(1, a.getTitle());
            s.setString(2, a.getTYPE());
            s.setString(3, a.getLocation());
            s.setTimestamp(4, Timestamp.valueOf(util.localtoUTC((a.getLocalStartDateTime())).toLocalDateTime()));
            s.setTimestamp(5,Timestamp.valueOf(util.localtoUTC((a.getLocalEndDateTime())).toLocalDateTime()));

            s.setInt(6,a.getCustomerId());
            s.setInt(7, a.getUserid());
            s.setInt(8,a.getAppointmentId());
            rs = s.executeUpdate();


        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", appLocale).getString("CANTUPDATEAPPOINTMENT")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return rs;
    }
    public static int DeleteAppointment(ActionEvent e, int selected) {
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement("DELETE FROM APPOINTMENT  WHERE AppointmentID = ?");
            s.setInt(1, selected);
            rs = s.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        }
    }


    //    ------------------------Customer Methods --------------------------

    public static ObservableList<Customer> getClients() {
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement("SELECT * FROM CLIENTS LEFT JOIN REPRESENTATIVE ON `CLIENTS`.`RepresentativeID` = `REPRESENTATIVE`.`RepresentativeID`" +
                    " WHERE `REPRESENTATIVE`.`RepresentativeID` IS NOT NULL" +
                    " OR CLIENTS.RepresentativeID IS NULL");

            r = s.executeQuery();
            if (r.isBeforeFirst()) {
                while (r.next()) {
                    int Client_ID = r.getInt("ClientID");
                    String ClientName=r.getString("ClientName");
                    String PostalCode=r.getString("PostalCode");
                    String StateAbbr=r.getString("StateName");
                    String CountryAbbr=r.getString("CountryName");
                    int repID=r.getInt("RepresentativeID");
                    String repName=r.getString("RepName");
                    String repPhone=r.getString("Phone");
                    String repDepartment=r.getString("Department");
                    String repEmail=r.getString("email");

                    String countryName,stateName;
                HashMap<String,String >CountryAndStates= vars.CountriesAndStates.getStateAndCountryFromAbbr(StateAbbr,CountryAbbr);
                 if (CountryAndStates!=null){
                     countryName=  CountryAndStates.getOrDefault("country",CountryAbbr);
                     stateName= CountryAndStates.getOrDefault("state",StateAbbr);
                 }else{
                      countryName= CountryAbbr;
                      stateName= StateAbbr;
                 }

                    Representative rep=new Representative(repID,repName,repDepartment,repPhone,repEmail);
                   Customer customer= new Customer(Client_ID,ClientName,stateName, countryName,PostalCode,rep);
                   customerObservableList.add(customer);
                }

                while (r.next()) {

//                    int CUSTOMER_ID = r.getInt("CUSTOMER_ID");
//                    String CUSTOMER_NAME = r.getString("CUSTOMER_NAME");
//                    String CUSTOMER_ADDRESS = r.getString("Address");
//                    String postal=r.getString("Postal_Code");
//
//                    String PHONE = r.getString("Phone");
//
//
//
//                    Date CREATED_ON_DATE = r.getDate("Create_Date");
//                    Time CREATED_ON_TIME = r.getTime("Create_Date");
//
//                    ZonedDateTime CREATED_ON = UTCtolocal(UTCDateTime(CREATED_ON_DATE.toLocalDate(), CREATED_ON_TIME.toLocalTime()));
//
//                    Date LAST_UPDATE_ON_DATE = r.getDate("Last_Update");
//                    Time LAST_UPDATE_ON_TIME = r.getTime("Last_Update");
//
//                    ZonedDateTime LAST_UPDATE_ON = UTCtolocal(UTCDateTime(LAST_UPDATE_ON_DATE.toLocalDate(), LAST_UPDATE_ON_TIME.toLocalTime()));
//                    Division STATE=FindDivisionById(r.getInt("Division_ID"));
//                    Country country=Countries.filtered(e->e.getId()==STATE.getCountryId()).get(0);
//
//
//                    String CREATED_BY  = r.getString("CREATED_BY");
//                    String LAST_UPDATED_BY  = r.getString("LAST_UPDATED_BY");
//                    Customer customer = new Customer(CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_ADDRESS, PHONE, STATE,
//                            country,postal, CREATED_ON, LAST_UPDATE_ON,LAST_UPDATED_BY,CREATED_BY);

//                    c.add(customer);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return customerObservableList;

    }
    public static void deleteCustomerAppointmentLocal(int cid){
            ObservableList<Appointment> a =CurrentUser.getAllAppointments().filtered(ap -> ap.getCustomerId()!=cid);
            CurrentUser.setAllAppointments(a);

    }
    private static int DeleteAppointmentByClient(int clientId ){
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement("DELETE FROM APPOINTMENT WHERE ClientID = ?");
            s.setInt(1, clientId);
            rs = s.executeUpdate();
            if(rs!=0){
                deleteCustomerAppointmentLocal(clientId);
            }
        } catch (SQLIntegrityConstraintViolationException e) {} catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        }
    }
    public static int deleteClient(int SelectedId) {
        DeleteAppointmentByClient(SelectedId);
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
            s = connection.prepareStatement("DELETE FROM CLIENTS  WHERE ClientID = ?");
            s.setInt(1, SelectedId);
            rs = s.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {

            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", appLocale).getString("CANTDELETECUSTOMERSERROR")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        }
    }
    public static int addCustomers(Customer c) {
        Connection connection = null;
        PreparedStatement s = null;

        try {
            String countryAbbr=vars.CountriesAndStates.getCountryAbbr(c.getCountry());
            String statesAbbr=vars.CountriesAndStates.getStateAbbr(c.getCountry(),c.getSTATE());

            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
             String SQL="INSERT INTO `CLIENTS`(`ClientName`,`PostalCode`,`StateName`,`CountryName`,`RepresentativeID`)" +
                                    "VALUES(?           ,?          , ?,            ?,              ?)";
          int repId= addRep(c.getRepresentative());
            s = connection.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);


            s.setString(1,c.getCUSTOMER_NAME());
            s.setString(2,c.getPOSTAL());
            s.setString(3,statesAbbr);
            s.setString(4,countryAbbr);
            s.setInt(5,repId);


           int rows = s.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = s.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                     return id;
                    }
                }
            }
            return -1;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", appLocale).getString("CANTCREATECUSTOMER")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {

            try {
                if (s!=null)
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }
    public static int ModifyCustomer(Customer c) {

        Connection connection = null;
        PreparedStatement s = null;

        try {
            modifyRep(c.getRepresentative());
            String countryAbbr=vars.CountriesAndStates.getCountryAbbr(c.getCountry());
            String statesAbbr=vars.CountriesAndStates.getStateAbbr(c.getCountry(),c.getSTATE());

            connection = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);

            s = connection.prepareStatement("UPDATE `CLIENTS` SET" +
                    "`ClientName` = ?," +
                    "`PostalCode` = ?," +
                    "`StateName` = ?," +
                    "`CountryName` =?" +
                    "WHERE `ClientID` = ?");

            s.setString(1,c.getCUSTOMER_NAME());

            s.setString(2,c.getPOSTAL());
            s.setString(3,statesAbbr);
            s.setString(4,countryAbbr);
            s.setInt(5,c.getCUSTOMER_ID());

          return s.executeUpdate();
        }catch (SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            new Alert (Alert.AlertType.ERROR,ResourceBundle.getBundle("Internationalization.lang", appLocale).getString("CANTUPDATECUSTOMER")
            ).showAndWait();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }  finally {
            try {
                s.close();
            } catch (SQLException ex) {ex.printStackTrace();}

            try {connection.close();} catch (SQLException ex) {ex.printStackTrace();}

        }

        return -1;


    }
    public static int addRep(Representative r) throws SQLException{

        String insertQuery = "INSERT INTO REPRESENTATIVE (`RepName`,`Department`,`Phone`, `email`)VALUES (? ,?,?,?);";
        try (Connection conn =DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            {
            pstmt.setString(1, r.getRepName());
            pstmt.setString(2,r.getDepartment());
            pstmt.setString(3, r.getPhone());
            pstmt.setString(4,r.getEmail());
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                    return id;
                    }
                }
            }
            return -1;
        }

        }
    }
    public static int modifyRep(Representative r) throws SQLException{
        try(
                Connection connection  = DriverManager.getConnection(dbConfig, dbUserName, dbPassword);
                PreparedStatement s  = connection.prepareStatement("UPDATE `Capstone_Project_DB`.`REPRESENTATIVE` " +
                    "SET`RepName` =?,`Department` = ?," +
                    "`Phone` = ?," +
                    "`email` = ?" +
                    "WHERE `RepresentativeID` = ?")) {
            s.setString(1, r.getRepName());
            s.setString(2, r.getDepartment());
            s.setString(3, r.getPhone());
            s.setString(4, r.getEmail());
            s.setInt(5, r.getRepresentativeID());

            return s.executeUpdate();
        }




    }
}

