package AppointmentManagementSystem;

import AppointmentManagementSystem.database.DbCommunications;
import AppointmentManagementSystem.models.Appointment;
import AppointmentManagementSystem.models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.time.*;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

/**
This class holds utillity function that are needed for the application to run.
@author Ohzecodes
@version 1.0 
 */

public class util {



    public static void changeScreen(String screenName, Object object, Stage stage,String callingClass ) {
        try {
            ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", DbCommunications.appLocale);
            FXMLLoader loader = new FXMLLoader(util.class.getResource(screenName), rr);
            Parent root = loader.load();
            String title;
           if(containsIgnoreCase(callingClass,"DashBoard")) {
               if (object instanceof Appointment || containsIgnoreCase(screenName,"Mod_Appointment")) {
                   AppointmentAddOrModCtrl mod = loader.getController();
                   assert object instanceof Appointment || object == null;
                   mod.set((Appointment) object);
                    title=object == null ? rr.getString("ADDAPPOINTMENT") : rr.getString("MODIFYAPPOINTMENT") + ":" + ((Appointment) object).getTitle();


               } else if (object instanceof Customer || containsIgnoreCase(screenName,"Mod_Customer" )){
                   CustomerAddOrModCtrl mod = loader.getController();
                   assert object instanceof Customer || object == null;
                   mod.set((Customer) object);
                    title =object == null ? rr.getString("ADDCUSTOMER") : rr.getString("MODIFYCUSTOMER") + ":" + ((Customer) object).getCUSTOMER_NAME();


               }
               else {
                   title=screenName.replace(".fxml", "").replace("_", " ");
               }
           }else {
               title=(screenName.replace(".fxml", "").replace("_", " "));
           }
            stage.setTitle(title);
          Scene scene=  new Scene(root);
            String css = Objects.requireNonNull(util.class.getResource("dark.css")).toExternalForm() ;
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void GenerateICS(Appointment a){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        String formattedStartDateTime = a.getLocalStartDateTime().toLocalDateTime().format(formatter);
        String  formattedEndDateTime=a.getLocalEndDateTime().toLocalDateTime().format(formatter);
        String summary=a.getTitle()+" with "+a.getCustomerName()+" ";
        String ics=
          "BEGIN:VCALENDAR\n" +
                  "VERSION:2.0\n" +
                  "PRODID:-//ABI Software//Scheduler Application//EN\n" +
                  "BEGIN:VEVENT\n" +
                  "SUMMARY:"+summary+ "\n" +
                  "DESCRIPTION:"+a.getDetails()+"\n"+
                  "DTSTART;TZID="+getmyTZ()+":"+formattedStartDateTime+"\n" +
                  "DTEND;TZID="+getmyTZ()+":"+formattedEndDateTime+"\n" +
                  "END:VEVENT\n" +
                  "END:VCALENDAR";
    try {
        String filePath = System.getProperty("user.home") + "/Downloads/" + a.getTitle();
        createAnewICSFile(filePath, ics);
    }catch(Exception ex){
        ex.printStackTrace();
    }
    }

    public static boolean containsIgnoreCase(String mainText,String subText) {
     return   mainText.toUpperCase().contains(subText.toUpperCase());
    }

    public static void backToDashBoard(ActionEvent e){
        String className =e.getSource().getClass().getName();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        changeScreen("Dashboard.fxml",null,stage,className);
    }

    public enum StyleEnum {
        Disabletextbox,
        invalidvaluetf,
        invalidvalueLabel

    }
    /**
    Building a UTC time as a ZonedDateTime
    @param  l LocalDate 
    @param  t LocalTime 
     */
    public static ZonedDateTime UTCDateTime(LocalDate l, LocalTime t) {
        return LocalDateTime.of(l, t).atZone(ZoneId.of("UTC"));
    }
    /**
    this method returns a CSS style string based on the given StyleEnum value.
    @param styleEnum The StyleEnum value representing the desired style.
    @return A CSS style string for the given StyleEnum value.
    */
    public static String getStyle(StyleEnum styleEnum) {
        switch (styleEnum) {
            case Disabletextbox:
                return "-fx-background-color: #455a64;-fx-text-fill: #cfd8dc;-fx-font-weight: bold";
            case invalidvaluetf:
                return "-fx-border-color: red";
            case invalidvalueLabel:
                return "-fx-color:red";
            default:
                return null;
        }
    }
    /** 
    This Function checks weather I number is in between the lowest and highest value as given
    @param check  long: The number to check
    @param lowest_value The lowest value in the range
    @param highest_value The highest value in range
    @return If it is in between then true else False
    */
    public static boolean isBetween(long check, long lowest_value, long highest_value) {

        return (check <= highest_value && check >= lowest_value);
    }

    /**
    This function checks whether the given time is in between 15 minutes and now
    @param when The given time
    @return  If it is in between then true else False

     */
    public static boolean in15minutes(ZonedDateTime when) {
        long between = when.toInstant().toEpochMilli();
        long n = ZonedDateTime.now().toInstant().toEpochMilli();
        long Fifteen = ZonedDateTime.now().plusMinutes(15).toInstant().toEpochMilli();
        return (isBetween(between, n, Fifteen));

    }



    public static boolean isInOfficeHours(ZonedDateTime zdt){

        ZoneId EST =ZoneId.of (vars.timings.getOrDefault("TZ","America/New_York"));
        int start= Integer.parseInt(vars.timings.getOrDefault("Start","8"));
        int end =Integer.parseInt( vars.timings.getOrDefault("End","20"));

        ZonedDateTime openingTime = ZonedDateTime.of(zdt.toLocalDate(), LocalTime.of(start , 0), EST);
        ZonedDateTime closingTime = ZonedDateTime.of(zdt.toLocalDate(), LocalTime.of(end, 0), EST);
        ZonedDateTime NowInEst = zdt.withZoneSameInstant(ZoneId.of(getmyTZ())).toOffsetDateTime()
                .toZonedDateTime().toInstant().atZone(EST);


        return (NowInEst.isAfter(openingTime) && NowInEst.isBefore(closingTime));

    }

    /** 
     Converts time from UTC to local
     @param d the given time
     @return The given time in local time 
    */
    public static ZonedDateTime UTCtolocal(ZonedDateTime d) {
        return d.withZoneSameInstant(ZoneId.of(getmyTZ())).toOffsetDateTime()
                .toZonedDateTime().toInstant().atZone(ZoneId.of(getmyTZ()));
    }
     /** 
     Converts time from local time to UTC
     @param d the given time
     @return The given time in UTC time 
    */
    public static ZonedDateTime localtoUTC(ZonedDateTime d) {
        return d.withZoneSameInstant(ZoneId.of(getmyTZ())).toOffsetDateTime()
                .toZonedDateTime().toInstant().atZone(ZoneId.of("UTC"));
    }
    /**
    This function returns the Default time zone as the string
     @return the Default time zone as the string
     */
    public static String getmyTZ() {
        return ZoneId.systemDefault().toString();
    }

    public static String getTZcity(){
        String myTZ = getmyTZ();
        int index = myTZ.lastIndexOf('/');
     return index >= 0 ? myTZ.substring(index + 1) : myTZ;

    }
    /** 
    This function helps append the array into a file. 
    @param ARR The array that you want to write to the file
    */
    public static void writingToActivityFile(ObservableList<String> ARR) throws IOException {

        String FN = "login_activity.txt";
        File file = new File(FN);
        if (!file.exists()) {
                file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(FN));
        ObservableList<String> s = FXCollections.observableArrayList();
        String n;
        while ((n = br.readLine()) != null) {
            s.add(n);
            s.add("\n");
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(FN));
        //        rewrite the old file
        for (String line : s) {
            bw.write(line);
        }
        //       write the new text in the file
        for (String line : ARR) {
            bw.write(line);
            bw.write("\n");
        }

        bw.close();

    }

    private static void createAnewICSFile(String fileName,String TextToWrite) throws IOException {

        String filename = getUniqueFileName( fileName+".ics");
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
     BufferedWriter bw =new BufferedWriter(new FileWriter(filename));
        bw.write(TextToWrite);
        bw.close();

    }
    private static String getUniqueFileName(String originalFileName) {
        File originalFile = new File(originalFileName);

        if (!originalFile.exists()) {
        return originalFileName;  }

        String newFileName;
        int fileNumber = 1;
        String baseFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        do {
            newFileName = baseFileName + "_" + fileNumber + extension;
            fileNumber++;
        } while (new File(newFileName).exists());

        return newFileName;
    }
    /** 
     This function helps figure out whether a given time is in this week or not
     Week is from friday to thursday. for example Friday 6 jan 2023 to 12 jan 2023
    @param z  The date that you want to check
    @return  If it is then true else False
    
    */
    public static boolean isinthisweek(ZonedDateTime z){
        int thisweek=ZonedDateTime.now().getDayOfYear()/7;
        int thatweek=z.getDayOfYear()/7;


        return thisweek==thatweek;

    }
    /**
    This Function is checking whether two time ranges is overlapping
        * @param s1 The start time of the first range
        * @param e1 the end time of the first range
        * @param s2 The start time of the Second range
        * @param e2 the end time of the Second range
        @return  If it is then true else false
    */
    private static boolean isOverlapping( ZonedDateTime s1,  ZonedDateTime e1, ZonedDateTime s2, ZonedDateTime e2) {
        long s1millis= s1.toInstant().toEpochMilli();
        long e1millis= e1.toInstant().toEpochMilli();
        long s2millis= s2.toInstant().toEpochMilli();
        long e2millis= e2.toInstant().toEpochMilli();
        return Math.max(s1millis,s2millis)<=Math.min(e1millis,e2millis);
    }
    /**
    This function checks whether the start and end time is overlapping in a given array
    * @param start  The start time of the range
        * @param end The end time of the range
        * @param A The given array 
        * @param id The id of an Appointment so I can check whether if it's the same appointment or not.
    @return  If it is then true else false
     */

    public static boolean OverlapingintheArray(ZonedDateTime start, ZonedDateTime end, ObservableList<Appointment> A, int id){
        for (Appointment e: A) {
            ZonedDateTime ds= e.getLocalStartDateTime();
            ZonedDateTime dend= e.getLocalEndDateTime();
            if(isOverlapping(start,end,ds,dend)){
                if(e.getAppointmentId()==id){
        //                    The same appointment. It's being modified.So, we will skip this
                    continue;}
                System.out.println(e.getAppointmentId());
        //                yes,There is overlapping
                return true;
            }
        }
        //No, there is no overlapping
        return  false;
    }




}