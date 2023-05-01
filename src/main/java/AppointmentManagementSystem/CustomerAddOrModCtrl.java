package AppointmentManagementSystem;

import AppointmentManagementSystem.database.DbCommunications;
import AppointmentManagementSystem.models.Customer;
import AppointmentManagementSystem.models.Representative;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.*;

/**
This is the page that is used to Modify a customer
@author Ohzecodes
@version 1.0  */
public class CustomerAddOrModCtrl implements Initializable, Modifiable_Page {
    public Text PageTitle;



    private Mode mode=Mode.unset;
    public Button SaveBtn;
    public Button CancelBtn;
    public TextField Postal;
    public Text errors;

    @FXML TextField CUSTOMER_ID, CUSTOMER_NAME;
    @FXML
     ComboBox<String> STATE,COUNTRY;

    @FXML TextField RepName,RepEmail,RepPhone,RepDept;
/**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blockId();

        COUNTRY.setItems(vars.CountriesAndStates.getCountries());
        STATE.setVisibleRowCount(5);
        COUNTRY.setVisibleRowCount(5);
        COUNTRY.getSelectionModel().selectedItemProperty().addListener((observableValue, old, newlySelected) -> {
            if(!Objects.equals(old, newlySelected)){
                STATE.setValue("");
                Postal.setText("");
            }
        STATE.setItems(vars.CountriesAndStates.getStates(newlySelected));
        });

        CancelBtn.setOnAction(util::backToDashBoard);
        SaveBtn.setOnAction(this::save);

    }
    @Override
    public void blockId(){
        CUSTOMER_ID.setDisable(true);
        CUSTOMER_ID.setStyle(util.getStyle(util.StyleEnum.Disabletextbox));
    }
    /**
     * sets the fields using the given customer
     * @param c  the given customer
     */
    public void set(Customer c ){
        ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);

        if (c==null){
            mode=Mode.add;
            CUSTOMER_ID.setText(idOnAdd);
            PageTitle.setText(rr.getString((mode.name()+"CUSTOMER").toUpperCase()));
            return;
        }
        mode = Mode.modify;
        PageTitle.setText(rr.getString((mode.name()+"CUSTOMER").toUpperCase())+" "+ c.getCUSTOMER_NAME());
        CUSTOMER_ID.setText(c.getCUSTOMER_ID()+"");
        CUSTOMER_NAME.setText(c.getCUSTOMER_NAME());
        Postal.setText(c.getPOSTAL());
        COUNTRY.setValue(c.getCountry());
        STATE.setValue(c.getSTATE());
        Postal.setText(c.getPOSTAL());
        RepName.setText(c.getRepName());
        RepEmail.setText(c.getRepEmail());
        RepPhone.setText(c.getRepPhone());
        RepDept.setText(c.getRepDepartment());

    }

    /**
     * validated fields and Saves the appointment
     * @param actionEvent an ActionEvent
     */
    @Override
    public void save(ActionEvent actionEvent) {

        if(validate()!=0){
            return;
        }
            String CNAME=CUSTOMER_NAME.getText();
            String CSTATE=STATE.getValue()+"";
            String CCOUNTRY=COUNTRY.getValue()+"";
            String CPOSTAL= Postal.getText();
            Representative rep= new Representative(-1,
                    RepName.getText().trim()
                    ,RepDept.getText().trim()
                    ,RepPhone.getText().trim()
                    ,RepEmail.getText().trim()
            );

            Customer newCustomer=  new Customer(-1,CNAME,CSTATE,CCOUNTRY, CPOSTAL,null);

            if(mode==Mode.modify) {
                int CID = Integer.parseInt(CUSTOMER_ID.getText());
                Customer old = DbCommunications.customerObservableList.filtered(r -> r.getCUSTOMER_ID() == CID).get(0);

                rep.setId(old.getRepresentative().getRepresentativeID());
                newCustomer.setRepresentative(rep);
                newCustomer.setID(CID);

                int rs = DbCommunications.ModifyCustomer(newCustomer);
                if (rs!=-1) {
                    int oldIndex = DbCommunications.customerObservableList.indexOf(old);
                    DbCommunications.customerObservableList.set(oldIndex, newCustomer);
                    DbCommunications.CurrentUser.getAllAppointments().filtered(appointment -> appointment.getCustomerId()== newCustomer.getCUSTOMER_ID())
                            .forEach(appointment -> {

                         appointment.setCustomer(newCustomer);
                                appointment.setRepresentative((rep));
                    });


                }
            }
            else if (mode==Mode.add){
                newCustomer.setRepresentative(rep);
               int id= DbCommunications.addCustomers(newCustomer);

               if(id!=-1) {
                   newCustomer.setID(id);
                   DbCommunications.customerObservableList.add(newCustomer);
               }
            }
        util.backToDashBoard(actionEvent);

    }

    ObservableList<String> errorsObList = FXCollections.observableArrayList();
    /**
     * validates the user input
     * @return the number of errors
     */
    @Override
    public int validate() {
//        System.out.println(mode.name());
        ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);


        validateField(CUSTOMER_NAME, "CUSTOMER_NAME");
        validateField(Postal, "POSTAL");
        validateField(STATE, "STATE");
        validateField(COUNTRY, "COUNTRY");
        validateField(RepName,"REPRESENTATIVENAME");
        validateField ( RepEmail,"REPRESENTATIVEemail");
        validateField(RepPhone,"REPRESENTATIVEphone");
        if (Postal.getText().length()>6){
            errorsObList.add("POSTAL");
        }
        if (!errorsObList.isEmpty()) {
            errors.setText(rr.getString("YOUREMISSING") + ":\t");
            errorsObList.forEach(err -> errors.setText(errors.getText() +rr.getString(err.toUpperCase()) + ", "));
        }
        return errorsObList.size();
    }

    private void validateField(Control field, String fieldName) {

        if (field instanceof TextField textField) {
            if (textField.getText().trim().equals("")) {
                errorsObList.add(fieldName);
                textField.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            }else{

                textField.setStyle(null);
            }
        } else if (field instanceof ComboBox comboBox) {
            if (comboBox.getValue() == null||comboBox.getValue().equals("")) {
                errorsObList.add(fieldName);
                comboBox.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            } else {
                comboBox.setStyle(null);
            }
        }
    }






}
