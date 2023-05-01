package AppointmentManagementSystem;

import AppointmentManagementSystem.models.Appointment;
import AppointmentManagementSystem.database.DbCommunications;
import AppointmentManagementSystem.models.CountObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
This is a page that a user can use to find out which customer had How many appointments?
@author Ohzecodes
@version 1.0 
 */
public class ReportByCustomerCtrl implements Initializable {



    public TableView<CountObj> reportTbl;
    public TableColumn<Object, Object> type;
    public TableColumn<Object, Object> countTC;

    public TableColumn<Object, Object> customer;
    public Button BackBtn;
    ObservableList<Appointment> App ;

    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App= DbCommunications.CurrentUser.getAllAppointments();
        ObservableList<CountObj> count= FXCollections.observableArrayList();
        CountAllAppointmentByCustomerID(count);
        reportTbl.setItems(count);
        customer.setCellValueFactory(new PropertyValueFactory<>("type"));
        countTC.setCellValueFactory(new PropertyValueFactory<>("count"));

        BackBtn.setOnAction(util::backToDashBoard);
    }
    /** A look up function that returns the corresponding name of the given ID
    * @param id the given id 
    */
    private String getCustomerNameById(int id){
       return DbCommunications.customerObservableList.filtered(c1->c1.getCUSTOMER_ID()==id ).get(0).getCUSTOMER_NAME();
    }
    /**
    * Finds and stores unique customer Name and the number of appointments they have.
     * uses a lambda function to generate the number of each appointment made by each distinct and stores it to count variable
    * @param count An ObservableList of CountObj objects where the unique customer IDs and appointment counts will be stored.
    */

    private void CountAllAppointmentByCustomerID(ObservableList<CountObj> count){

       App.stream().map(Appointment::getCustomerId).distinct().toList().forEach(customerId->{
            int OneCount = countOneAppointmentsByCustomerId(customerId);
            if(OneCount!=0) {
                count.add(new CountObj(OneCount, getCustomerNameById(customerId)));
            }
        });

    }
    /**
    Returns the number of appointments for a given customer.
    @param CustomerId The unique identifier for the customer.
    @return The number of appointments for the specified customer.
    */

    private int countOneAppointmentsByCustomerId(int CustomerId){
        return  App.filtered(a-> Objects.equals(a.getCustomerId(), CustomerId)).size();

    }





}
