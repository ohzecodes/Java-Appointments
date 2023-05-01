package AppointmentManagementSystem;

import AppointmentManagementSystem.database.DbCommunications;
import AppointmentManagementSystem.models.Appointment;
import AppointmentManagementSystem.models.Customer;
import AppointmentManagementSystem.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;



/**
    This is a page that a user sees after signing in.
    It also features a language changing functionality English or french.
    @author Ohzecodes
    @version 1.0
 */
public class Dashboard implements Initializable {

    public static ObservableList<Stage> stages = FXCollections.observableArrayList();
    @FXML
    public Button logoutbtn;
    @FXML public Text UserNameText, UserIdText, timeZoneText;

    @FXML public TableView<Appointment> AppointmentTable;
    @FXML  public TableColumn<Object, Object> AppointmentId,Title,Location,TYPE,StarTime,EndTime,StartDate,EndDate, CustomerName,RepName,RepEmail,RepPhone;

    public Button AddAppointment,ModifyAppointment,DeleteAppointment;

    //customer Table cols
    public TableView<Customer> CustomerTable;
    public TableColumn<Object, Object> CUSTOMER_ID,CUSTOMER_NAME,State,POSTAL,COUNTRY,REPNAME,REPEMAIL,REPPHONE,REPDEPT;
    //   buttons
    public Button AddCustomer,ModifyCustomer,DeleteCustomer;
    public ToggleGroup FilterBy;
    public Button ReportByType,ReportsByCustomer;

    public TextField AppointmentSearch, CustomerSearch;
    public TitledPane reportsPane;
    public Button MultipleReport;


    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        timeZoneText.setText("Timezone"+"\n"+ util.getTZcity());
        logoutbtn.setOnAction(this::logout);
        setUserInfo(DbCommunications.CurrentUser);
        DeleteAppointment.setOnAction(this::DeleteAppointment);
        ModifyAppointment.setOnAction(this::changeScreenToModifyAppointment);

        DeleteCustomer.setOnAction(e->DeleteCustomer());
        ModifyCustomer.setOnAction(this::Modify_customer);
        AddAppointment.setOnAction(this::AddAppointment);
        AddCustomer.setOnAction(this::AddCustomer);
        ReportsByCustomer.setOnAction(e->    ChangeScreen("Report_By_Customer.fxml",null));
        ReportByType.setOnAction(e-> ChangeScreen("Report_By_Type.fxml",null));
        MultipleReport.setOnAction(e->ChangeScreen("MulipleReports.fxml",null));
        FilterBy.getToggles().forEach(e -> {
            RadioButton e1 = (RadioButton) e;
            e1.setOnAction(this::RadioButtonFiltering);
        } );

        //hides the appointment search fields when the reportsPane is visible
        reportsPane.expandedProperty().addListener((obs, oldVal, newVal) -> AppointmentSearch.setVisible(!newVal));


        AppointmentSearch.setOnKeyPressed(key->{
                if (!key.getCode().equals(KeyCode.ENTER)) {return;}
                    String searchText = AppointmentSearch.getText().trim();
                    AppointmentFiltering(searchText);

        });
        CustomerSearch.setOnKeyPressed(key-> {
            if (!key.getCode().equals(KeyCode.ENTER)){return;}
                String  searchText= CustomerSearch.getText().trim();
                CustomerFiltering(searchText);

        });

        setCustomersTable(DbCommunications.customerObservableList);
        setAppointmentColumns(DbCommunications.CurrentUser.getAllAppointments());

    }
    /*Todo: write docs
    * using contains ignore case
    * */
    private void CustomerFiltering(String searchText){
        FilteredList<Customer> filteredByName= DbCommunications.customerObservableList.filtered(c->util.containsIgnoreCase(c.getCUSTOMER_NAME(),searchText));
        FilteredList<Customer> filteredByState= DbCommunications.customerObservableList.filtered(c->util.containsIgnoreCase(c.getSTATE(),searchText) );
        ObservableList<Customer> SearchedCustomers= FXCollections.observableArrayList( FXCollections.concat(filteredByName,filteredByState).stream().distinct().toList() );

        setCustomersTable(
                !searchText.isEmpty()? SearchedCustomers : DbCommunications.customerObservableList);
    }

    /* Todo: write docs
    Using strict equals ignore case
     */
    private  void AppointmentFiltering(String searchText){
        ObservableList<Appointment> filteredByTitle = DbCommunications.CurrentUser.getAllAppointments().filtered(a ->searchText.equalsIgnoreCase(a.getTitle()));
        ObservableList<Appointment> filteredByCustomerName = DbCommunications.CurrentUser.getAllAppointments().filtered(a -> searchText.equalsIgnoreCase(a.getCustomerName()));
        ObservableList<Appointment> SearchedAppointments = FXCollections.observableArrayList( FXCollections.concat(filteredByTitle,filteredByCustomerName).stream().distinct().toList() );

        setAppointmentColumns(
                !searchText.isEmpty()? SearchedAppointments
                        : DbCommunications.CurrentUser.getAllAppointments());

    }
    /**
     * A function that runs when you toggle the radio buttons (monthly, weekly, all). It sets the table columns accordingly
     * @param e Action event
     */

    private void RadioButtonFiltering(ActionEvent e) {

        RadioButton e1=(RadioButton)e.getSource();
        if(e1.getText().equalsIgnoreCase("ALL")){
            AppointmentTable.setItems(DbCommunications.CurrentUser.getAllAppointments());
        }
        else if(e1.getText().equalsIgnoreCase("Month")) {
            System.out.println(ZonedDateTime.now().getMonth());
            AppointmentTable.setItems(DbCommunications.CurrentUser.getAllAppointments()
                    .filtered(f -> f.getLocalStartDateTime().getMonth().equals(ZonedDateTime.now().getMonth()))
            );
        }
        else if(e1.getText().equalsIgnoreCase("Week"))
            AppointmentTable.setItems(
                    DbCommunications.CurrentUser.getAllAppointments()
                            .filtered(f->util.isinthisweek(f.getLocalStartDateTime()))
            );

    }

    /**
    *opens the add apointment page
    * @param e Action event
     */
    private void  AddAppointment(ActionEvent e) {

        try {
            ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);
            if (DbCommunications.customerObservableList.size()<1) {
               new Alert(Alert.AlertType.WARNING,rr.getString("YOUCANTADDANAPPOINTMENTWITHNOCUSTOMER")).showAndWait();
                return;
            }
            ChangeScreen("Add_Or_Mod_Appointment.fxml",(Appointment) null);

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    /**
    * opens  modify Appointment page
     * @param e Action event
     */
    public void changeScreenToModifyAppointment(ActionEvent e)  {
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);
        Appointment appointment =AppointmentTable.getSelectionModel().getSelectedItem();
        if (appointment==null){
            new Alert(Alert.AlertType.ERROR, rr.getString("NOELEMENTSELECTED")).showAndWait();
            return;
        }
        ChangeScreen("Add_Or_Mod_Appointment.fxml",(Appointment) appointment);
    }
    /**
    * Deletes the selected appointment in the table after showing a Confirmation message
     * @param e Action event
    */
    private void DeleteAppointment(ActionEvent e) {
        Appointment SelectedAppointment=  AppointmentTable.getSelectionModel().getSelectedItem();
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);

        if(SelectedAppointment!=null) {
            Alert A=  new Alert(Alert.AlertType.CONFIRMATION,rr.getString("APPOINTMENTCONFIRMATIONDELETE")+
                    "\n Id: " +SelectedAppointment.getAppointmentId()+
                    "\n Of Type: "+SelectedAppointment.getTYPE()+
//                    "\n Title:"+SelectedAppointment.getTitle()
                    "\n AND WILL BE CANCELLED"
            );
            Optional< ButtonType> res=A.showAndWait();
            if (res.isPresent()&&res.get()==ButtonType.OK) {
                int rs = DbCommunications.DeleteAppointment(e, SelectedAppointment.getAppointmentId());
                DbCommunications.CurrentUser.getAllAppointments().remove(SelectedAppointment);
                if (rs < 1)
                    new Alert(Alert.AlertType.ERROR, rr.getString("NODATAEXISTS")).showAndWait();
            }
            }else{
            new Alert(Alert.AlertType.ERROR,rr.getString("NOELEMENTSELECTED") ).showAndWait();

        }

    }
    /**
        * logouts the user, switches to the login screen
        * expects the user to close all windows before logout
        * @param e Action event
    */
    private void logout(ActionEvent e) {
        DbCommunications.logout(e);
        stages.forEach(stage -> stage.close());
    }

    /**
        * Search the table columns of the appointment table by using the observable list
        * @param a The observable list of appointments
    */

    public void setAppointmentColumns(ObservableList<Appointment> a ) {

        if(a==null) {return;}
            AppointmentTable.setItems(a);
            CustomerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
            RepName.setCellValueFactory(new PropertyValueFactory<>("RepresentativeName"));
            RepEmail.setCellValueFactory(new PropertyValueFactory<>("RepresentativeEmail"));
            RepPhone.setCellValueFactory(new PropertyValueFactory<>("RepresentativePhone"));

            TYPE.setCellValueFactory(new PropertyValueFactory<>("TYPE"));
            AppointmentId.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
            Title.setCellValueFactory(new PropertyValueFactory<>("title"));

            Location.setCellValueFactory(new PropertyValueFactory<>("location"));

            StartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
            EndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));

            StarTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
            EndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));


    }

    /**
    * Sets the user info on the page
    * @param U curently signin user
     */
    public void setUserInfo(User U) {
        UserNameText.setText("UserName\n"+U.getName());
        UserIdText.setText("UserId\n"+U.getUid());

    }
    /**
    * opens  the add customer page
     * @param e Action event
     */
    private void AddCustomer(ActionEvent e) {
        ChangeScreen("Add_Or_Mod_Customer.fxml",(Customer) null);

    }

    /**
    * Opens the modify customer page
    * @param e Action event
    */
   
    public void Modify_customer(ActionEvent e) {
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);
       Customer c = CustomerTable.getSelectionModel().getSelectedItem();
       if (c==null){
           new Alert(Alert.AlertType.ERROR, rr.getString("NOELEMENTSELECTED")).showAndWait();
           return;
       }
        ChangeScreen("Add_Or_Mod_Customer.fxml", c);


    }

    /**
    * search the customer columns  using the given list
    * @param c  A list of customers that will be used to set the columns of the customer table
    */

    public void setCustomersTable(ObservableList<Customer> c) {
        if(c==null) {return;}
        CustomerTable.setItems(c);
        CUSTOMER_ID.setCellValueFactory(new PropertyValueFactory<>("CUSTOMER_ID"));
        CUSTOMER_NAME.setCellValueFactory(new PropertyValueFactory<>("CUSTOMER_NAME"));
        POSTAL.setCellValueFactory(new PropertyValueFactory<>("POSTAL"));
        State.setCellValueFactory(new PropertyValueFactory<>("STATE"));
        COUNTRY.setCellValueFactory(new PropertyValueFactory<>("Country"));
        REPNAME.setCellValueFactory(new PropertyValueFactory<>("RepName"));
        REPEMAIL.setCellValueFactory(new PropertyValueFactory<>("RepEmail"));
        REPPHONE.setCellValueFactory(new PropertyValueFactory<>("RepPhone"));
        REPDEPT.setCellValueFactory(new PropertyValueFactory<>("RepDepartment"));


    }
    /**
    * delete the customer after showing a confirmation message
    *
    */
    public void DeleteCustomer(){
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);
       Customer Selected= CustomerTable.getSelectionModel().getSelectedItem();
       if(Selected!=null){
           Alert A=  new Alert(Alert.AlertType.CONFIRMATION,rr.getString("CUSTOMERCONFIRMATIONDELETE") +
                   "\n"+Selected.getCUSTOMER_NAME());
           Optional< ButtonType> res=A.showAndWait();
           if (res.isPresent()&&res.get()==ButtonType.OK) {
               int SelectedId = Selected.getCUSTOMER_ID();
               int rs = DbCommunications.deleteClient(SelectedId);
               if(rs==1) {
                   DbCommunications.customerObservableList.remove(Selected);
                   setAppointmentColumns(DbCommunications.CurrentUser.getAllAppointments());
               } }
       }else{
           new Alert(Alert.AlertType.ERROR, rr.getString("NOELEMENTSELECTED")).showAndWait();
       }
    }


    /**
    * Change the screen to the given screenName
    * @param ScreenName the namee of the screen Including the extension for example something.fxml
     */
    private void ChangeScreen(String ScreenName, Object object) {
        Stage stage =(Stage) logoutbtn.getScene().getWindow();

        util.changeScreen(ScreenName,object,stage,this.getClass().getName());
    }





}