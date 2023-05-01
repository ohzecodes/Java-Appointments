package AppointmentManagementSystem;

import AppointmentManagementSystem.database.DbCommunications;
import AppointmentManagementSystem.models.Appointment;
import AppointmentManagementSystem.models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MultipleReports implements Initializable {
    @FXML
    public TableColumn<Object, Object> AppointmentId,Title,Location,TYPE,StarTime,EndTime,StartDate,EndDate,CustomerName,RepName,RepEmail,RepPhone;

    public ObservableList<Appointment> appointmentList;
    public ComboBox<String> whichCombo;
    public HBox hbox;
    public TableView<Appointment> AppointmentTable;
    public Text count;
    public Button BackBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> whichList=FXCollections.observableArrayList("type","between dates", "customer");
        whichCombo.setItems(whichList);
        whichCombo.setOnAction(e -> {
            String selectedItem = whichCombo.getSelectionModel().getSelectedItem();
            switch (selectedItem) {
                case "type" -> initType();
                case "between dates" -> initDates();
                case "customer" -> initCustomer();
            }
        });

    BackBtn.setOnAction(util::backToDashBoard);
    }

    public void initAppointmentTable(){
        if(appointmentList==null) {return;}
        AppointmentTable.setItems(appointmentList);
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

    private void initType() {
        hbox.getChildren().removeAll(hbox.getChildren());
       ComboBox<String> Cb= new ComboBox<String>();

        Cb.setItems(vars.AppointmentTypes);
        Text text=new Text();
        text.setText("Choose Appointment type");
        VBox vbox=new VBox();
        vbox.getChildren().addAll(text,Cb);
        hbox.getChildren().add(vbox);
        Cb.setOnAction(e-> {
            String selectedItem = Cb.getSelectionModel().getSelectedItem();
            appointmentList= DbCommunications.CurrentUser.getAllAppointments()
                    .filtered(f->f.getTYPE().equalsIgnoreCase(selectedItem));
            initAppointmentTable();
            count.setText("Count: "+appointmentList.size());
        });
    }
    private void initDates() {
        hbox.getChildren().removeAll(hbox.getChildren());

        Text text=new Text();
        text.setText("Start");
        DatePicker startDp=new DatePicker();
        VBox startVbox = new VBox();
        startVbox.getChildren().addAll(text,startDp);



        Text EndText=new Text();
        EndText.setText("End");
        DatePicker endDp=new DatePicker();
        VBox endVbox = new VBox();
        endVbox.getChildren().addAll(EndText,endDp);
        Button goButton=new Button();
        goButton.setText("GO");
        goButton.setOnAction(e->{
            if (startDp.getValue()==null ||endDp.getValue()==null  ) {
                if ( endDp.getValue()==null){
                    endDp.setStyle("-fx-border-width: 1; -fx-border-color: red");
                }else{
                    endDp.setStyle("-fx-border-width: 0;");
                }

                if(startDp.getValue()==null) {
                    startDp.setStyle("-fx-border-width: 1; -fx-border-color: red");
                }
               else{
                    startDp.setStyle("-fx-border-width: 0;");
                }
                return;
            }

                endDp.setStyle("-fx-border-width: 0;");
                startDp.setStyle("-fx-border-width: 0;");
            appointmentList= DbCommunications.CurrentUser.getAllAppointments()
                    .filtered(app->app.getStartDate().isAfter(startDp.getValue())
                    && app.getEndDate().isBefore(endDp.getValue()))
                    .sorted(Comparator.comparing(Appointment::getStartDate));
            initAppointmentTable();
            count.setText("Count: "+ appointmentList.size());
        });

        hbox.getChildren().addAll(startVbox, endVbox,goButton);



    }

    private void initCustomer() {
        hbox.getChildren().removeAll(hbox.getChildren());
        ComboBox<String> Cb= new ComboBox<String>();
       ObservableList<String> customerNames= FXCollections.observableArrayList(DbCommunications.customerObservableList.stream().map(Customer::getCUSTOMER_NAME).toList());
        Cb.setItems(customerNames);
        Text text=new Text();
        text.setText("Choose Customer");
        VBox vbox=new VBox();
        vbox.getChildren().addAll(text,Cb);
        hbox.getChildren().add(vbox);
        Cb.setOnAction(e->{
            String selectedItem = Cb.getSelectionModel().getSelectedItem();
            appointmentList= DbCommunications.CurrentUser.getAllAppointments()
                    .filtered(f->f.getCustomerName().equalsIgnoreCase(selectedItem));
            initAppointmentTable();
            count.setText("Count: "+appointmentList.size());
        });

    }
}
