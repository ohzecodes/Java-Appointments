package AppointmentManagementSystem.models;

import javafx.collections.ObservableList;
/**
This class holds a user information.
@author Ohzecodes
@version 1.0  */
public class User {
    private String name;
    private int uid;
    private ObservableList<Appointment> allAppointments;
    /**
    Create a user with a NO NAME, -1 AS THE USERID, AND NULL Appointments.
    We will use the setter methods to fill the user
     */
    public User( ) {
        this.name = null;
        this.uid=-1;
        this.allAppointments=  null;
    }
    /**
    Create a User with the given name, id and appointments
    @param name The name of the User
    @param  uid The user ID of the user
    @param  allAppointments  The appointment of the User, if known
     */
    public User(String name, int uid, ObservableList<Appointment> allAppointments ) {
        this.name = name;
        this.uid=uid;
        this.allAppointments=  allAppointments;
    }
 /**
    Create a User with the given name,and  id 
    @param name The name of the User
    @param  uid The user ID of the user
  
     */
    public User(int uid,String name){
        this.name = name;
        this.uid=uid;
    }

    /** 
    a string representation of the object
     */
    @Override
    public String toString() {

        return "User{" +
                "name='" + name + '\'' +
                ", uid=" + uid +
                ", Size of allAppointments=" + getAppointmentSize()+
                '}';
    }
    /**
    Returns  the appointment size
    @return the appointment size
     */
    public int getAppointmentSize(){
        return allAppointments!=null? allAppointments.size():-1;
    }
    /**
    Returns  the user's name 
    @return the user's name 
    */
    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    /**
    Returns  the user's uid 
    @return the user's uid 
    */
    public int getUid() {
        return uid;
    }



    public ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public void setAllAppointments(ObservableList<Appointment> allAppointments) {
        this.allAppointments = allAppointments;
    }

    public void updateOneAppointment(Appointment newAppointment ){
       Appointment old= this.allAppointments.filtered(app->app.getAppointmentId()==newAppointment.getAppointmentId()).get(0);
      int oldIndex= allAppointments.indexOf(old);
        allAppointments.set(oldIndex,newAppointment);
    }



    /**
    set the user's name to null, uid to -1, Appointments to null;
    */
    public void Signout(){
     name=null;
     uid=-1;
     allAppointments=null;
    }


}
