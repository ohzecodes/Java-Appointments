package AppointmentManagementSystem.database;

/**
 * this file consists of variables that are important to establish database connection
 * dbuserName,dbpassword, Port,Db_name, dbconfig
 */
public class dbstrings {

    //    ---------------------- Db Information-------------------------
    protected static final String dbUserName = "root";
    protected static final String dbPassword = "TOOR";
    protected static final int Port=3306;
    protected static final String DbName ="Capstone_Project_DB";
    protected static final String DbHost ="127.0.0.1";
    protected static final String dbConfig = String.format("jdbc:mysql://%s:%d/%s", DbHost,Port, DbName);


}
