package AppointmentManagementSystem;

import AppointmentManagementSystem.models.Appointment;
import AppointmentManagementSystem.database.DbCommunications;
import AppointmentManagementSystem.models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
This is the page that is used to add an appointment
@author Ohzecodes
@version 1.0 
 */
public class AppointmentAddOrModCtrl implements Initializable, Modifiable_Page {

    public Text errorsText;
    public ComboBox<String> customerNameCombo;

    @FXML
    public Button CancelBtn, SaveBtn;
    @FXML
    public TextField AppointmentId;
    @FXML
    public TextField AppointmentTitle;
    @FXML
    public TextField AppointmentLocation;
    public DatePicker StartDate,EndDate;

    public ComboBox<Integer> StartHour, StartMin;
    public ComboBox<Integer> EndHour, EndMin;
    public Text PageTitle;
    public ComboBox<String> AppointmentTypeCombo;


    private Mode mode=Mode.unset;


    public void set(Appointment appointment){
        ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);

    if(appointment==null) {
        mode=Mode.add;
        PageTitle.setText(rr.getString((mode.name()+"Appointment").toUpperCase()));
        AppointmentId.setText(idOnAdd);

       return;
    }
        mode=Mode.modify;
    util.GenerateICS(appointment);
        PageTitle.setText(rr.getString(mode.name().toUpperCase()) +appointment.getTitle());
        AppointmentId.setText(appointment.getAppointmentId()+"");
        customerNameCombo.setValue(appointment.getCustomerName());
        AppointmentTitle.setText(appointment.getTitle());
        AppointmentLocation.setText(appointment.getLocation());
        AppointmentTypeCombo.setValue( appointment.getTYPE());
        StartDate.setValue(appointment.getStartDate());
        StartHour.setValue(appointment.getStartTime().getHour());
        StartMin.setValue(appointment.getStartTime().getMinute());
        EndDate.setValue(appointment.getEndDate());
        EndHour.setValue(appointment.getEndTime().getHour());
        EndMin.setValue(appointment.getEndTime().getMinute());

    }

    @Override
    public void blockId() {
        AppointmentId.setDisable(true);

    }


    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorsText.setText("");
        blockId();
        initTime();
        ObservableList<String> customerNames = getCustomerNames();
        customerNameCombo.setItems(customerNames);
        StartMin.setValue(0);
        StartHour.setValue(0);
        EndMin.setValue(0);
        EndHour.setValue(0);
        AppointmentTypeCombo.setItems(vars.AppointmentTypes);
        CancelBtn.setOnAction(util::backToDashBoard);
        SaveBtn.setOnAction(this::save);

            StartDate.valueProperty().addListener((obs, oldVal, newVal) -> {
                    EndDate.setValue(newVal);

            });

    }




    private ObservableList<String> getCustomerNames(){
      return  FXCollections.observableArrayList(
                DbCommunications.customerObservableList.stream()
                        .map(Customer::getCUSTOMER_NAME)
                        .collect(Collectors.toList())
        );
    }
    private void initTime(){
        ObservableList<Integer> Hours = FXCollections.observableArrayList();
        ObservableList<Integer> Minutes = FXCollections.observableArrayList();
        for (int i = 0; i <= 23; i++) {
            Hours.add(i);
        }
        for (int i = 0; i <= 59; i += 5) {
            Minutes.add(i);
        }

        StartMin.setItems(Minutes);
        StartHour.setItems(Hours);
        EndMin.setItems(Minutes);
        EndHour.setItems(Hours);
        StartMin.setVisibleRowCount(5);
        EndMin.setVisibleRowCount(5);
        StartHour.setVisibleRowCount(5);
        EndHour.setVisibleRowCount(5);
    }

    ObservableList<String> errorsList = FXCollections.observableArrayList();





    @Override
    public int validate() {
        errorsList.removeAll(errorsList);
        ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);

        setControlStyle(customerNameCombo, customerNameCombo.getValue() != null);
        setControlStyle(AppointmentTitle, !AppointmentTitle.getText().trim().isEmpty());
        setControlStyle(AppointmentLocation, !AppointmentLocation.getText().trim().isEmpty());
        setControlStyle(AppointmentTypeCombo, AppointmentTypeCombo.getValue() != null);
        setControlStyle(StartDate, StartDate.getValue() != null);
        setControlStyle(EndDate, EndDate.getValue() != null);

        ZonedDateTime start = null, end = null;
        boolean hasDateRange = StartDate.getValue() != null && EndDate.getValue() != null;
        if (hasDateRange) {
            start = ZonedDateTime.of(StartDate.getValue(), LocalTime.of(StartHour.getValue(), StartMin.getValue(), 0), ZoneId.of(util.getmyTZ()));
            end = ZonedDateTime.of(EndDate.getValue(), LocalTime.of(EndHour.getValue(), EndMin.getValue(), 0), ZoneId.of(util.getmyTZ()));

            if (end.isBefore(start)) {
                errorsList.add("ENDCANTBEBEFORESTART");
            }

        int aid=  Modifiable_Page.parseToInt(AppointmentId.getText().trim(),0);
            if (util.OverlapingintheArray(start, end, DbCommunications.CurrentUser.getAllAppointments(),aid )) {
                errorsList.add("OVERLAPPINGDATE");
            }

            if (!util.isInOfficeHours(start)) {
                errorsList.add("STARTNOTINOFFICE");
            }

            if (!util.isInOfficeHours(end)) {
                errorsList.add("ENDNOTINOFFICE");
            }
        }
        if (errorsList.isEmpty()) {
            return 0;
        } else {

            String i =errorsList.stream().map(e-> {
                        e=e.replaceAll("Appointment","").replaceAll("Combo","");
                        if (rr.containsKey(e.toUpperCase())){
                      return  rr.getString(e.toUpperCase());
                            }else return e;}
                    )
                    .collect(Collectors.joining(", "));

            errorsText.setText("Errors: "+i);
            return errorsList.size();
        }
    }


    private void setControlStyle(Control control, boolean isValid) {
        if (isValid) {
            control.setStyle(null);
            return;
        }
            errorsList.add(control.getId());
            control.setStyle( util.getStyle(util.StyleEnum.invalidvaluetf));

    }


    @Override
    public void save(ActionEvent actionEvent) {
    try {
        if (validate() != 0) {
            return;
        }
        int appointmentid = Modifiable_Page.parseToInt(AppointmentId.getText().trim(), 0);
        String customerName = customerNameCombo.getValue();
        int userid = DbCommunications.CurrentUser.getUid();
        String title = AppointmentTitle.getText();
        String location = AppointmentLocation.getText();
        Customer customer = DbCommunications.customerObservableList.filtered(c -> c.getCUSTOMER_NAME() == customerName).get(0);
        ZonedDateTime localStartDateTime = ZonedDateTime.of(StartDate.getValue(), LocalTime.of(StartHour.getValue(), StartMin.getValue(), 00), ZoneId.of(util.getmyTZ()));
        ZonedDateTime localEndDateTime = ZonedDateTime.of(StartDate.getValue(), LocalTime.of(EndHour.getValue(), EndMin.getValue(), 00), ZoneId.of(util.getmyTZ()));
        String type = AppointmentTypeCombo.getValue().trim();
        Appointment a = new Appointment(appointmentid, title, location, type,
                localStartDateTime, localEndDateTime, customer, userid);

        if (mode == Mode.add) {

            int rs = DbCommunications.addAppointment(a);
            if (rs > 0) {
                a.setId(rs);
                if (userid == DbCommunications.CurrentUser.getUid()) {
                    DbCommunications.CurrentUser.getAllAppointments().add(a);
                    util.backToDashBoard(actionEvent);
                }


            }
        }
        else if (mode == Mode.modify) {
            int rs = DbCommunications.updateAppointment(a);
            if (rs >= 0) {
                if (userid == DbCommunications.CurrentUser.getUid()) {
                    DbCommunications.CurrentUser.updateOneAppointment(a);
                    util.backToDashBoard(actionEvent);
                }


            }

        }
}
    catch (Exception ex){
        ex.printStackTrace();
}finally {

}


    }




}


