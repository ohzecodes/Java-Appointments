package AppointmentManagementSystem;

import AppointmentManagementSystem.database.DbCommunications;
import javafx.application.Application;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;


import static java.lang.System.exit;

/**
This class runs the main application
@author Ohzecodes
@version 1.0  
*/
public class ScheduleApp extends Application {

    public static void setLocale(){
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("fr")){
            DbCommunications.appLocale = Locale.CANADA_FRENCH;
        }if (Locale.getDefault().getLanguage().equalsIgnoreCase("es")){
            DbCommunications.appLocale = new Locale("es", "MX");
        }

    }

    /**
    This function starts the program
     If you change you your language you will need to restart the program
    @param stage
     */
    @Override
    public void start(Stage stage) throws IOException {

       setLocale();
       util.changeScreen("Login.fxml",null,stage,this.getClass().getName());
    }

    public static void preCheck(){

        try {
            DbCommunications.checkConnection();
        }catch ( ClassNotFoundException classNotFoundException){
            System.err.println("Can't find the jdbc Driver \n Read the Documentation.");
            exit(1);
        }
        catch (SQLException e){
            System.err.println("Can't Talk to the database \nRead the Documentation. ");
            exit(1);
        }

    }


    /**
    The main function of the application
    @param args
    */
    public static void main(String[] args) {
        preCheck();
        launch();

    }
}