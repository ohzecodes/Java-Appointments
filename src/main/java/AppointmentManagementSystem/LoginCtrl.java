package AppointmentManagementSystem;

import AppointmentManagementSystem.database.DbCommunications;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static AppointmentManagementSystem.database.DbCommunications.appLocale;
/**
 This is a sign in page that Ask a user for username and page  for logging the user in.
 It also features a language changing functionality English or french.

@author Ohzecodes
@version 1.0 
 */
public class LoginCtrl implements Initializable {
    public TextField UserNameField;
    public Button LoginButton;
    public PasswordField PasswordField;
    @FXML
    private Text TimeZoneText;
    @FXML
    private ChoiceBox<String> LanguageChoiceBox;
    public Text UPassText, UNameText,LangText,SchedulerTxt;

    /**
    * used to signing the user
    * @params ScreenName
     */


    private void timeZoneTextValue() {
        TimeZoneText.setText(TimeZoneText.getText()+"\n"+util.getTZcity());

    }
    /**
        * This function is used to handle the language change on the Login page
    */
    public void handleLocaleChange(){
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", appLocale);
        UPassText.setText(rr.getString("PASSWORD"));
        UNameText.setText(rr.getString("USERNAME"));
        LangText.setText(rr.getString("LANGUAGE"));
        TimeZoneText.setText(rr.getString("TIMEZONE"));

        timeZoneTextValue();
        LoginButton.setText(rr.getString("LOGIN"));
        SchedulerTxt.setText(rr.getString("SCHEDULER"));
        ((Stage) LanguageChoiceBox.getScene().getWindow()).setTitle(rr.getString("SCHEDULER"));
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeZoneTextValue();
        ObservableList<String> Language= FXCollections.observableArrayList("English","Spanish","French");
        LanguageChoiceBox.setItems(Language);
//        System.out.println(appLocale.getDisplayLanguage());
        LanguageChoiceBox.setValue(appLocale.getDisplayLanguage());
        LanguageChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equalsIgnoreCase("French")) {
                DbCommunications.appLocale = new Locale("FR", "CA");
            }else if(t1.equalsIgnoreCase("Spanish")){
                DbCommunications.appLocale = new Locale("ES", "MX");
            }
            else {
                DbCommunications.appLocale = new Locale("EN", "US");
            }
            handleLocaleChange();
        });

        LoginButton.setOnAction(this::login);
    }

    private void login(ActionEvent actionEvent) {
        if(!validate()){
       ( new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty")).showAndWait();
            return;
        }
        DbCommunications.loginWithUserNameAndPassword(actionEvent,UserNameField.getText(), PasswordField.getText());
    }


        public boolean validate() {
            String username = UserNameField.getText();
            String password = PasswordField.getText();
            return (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty());
        }




}