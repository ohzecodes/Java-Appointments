package AppointmentManagementSystem.models;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
/**
This class represents a Appointment in a scheduling system.
 It stores information about the appointment ie type, contact, title, Description, location, start and time .etc
@author Ohzecodes
@version 1.0 
 */


public class Appointment {


    private String TYPE;
    private int appointmentId;
    private String title;

    private String location;

    private int userid;
    Customer customer;


// todo: use cusotmer's REP

    private ZonedDateTime localStartDateTime;
    private ZonedDateTime localEndDateTime;

    /**
     * This class represents an appointment in a scheduling system.
     *
     * @param appointmentId      The unique identifier for this appointment.
     * @param title              The title of the appointment.
     * @param location           The location of the appointment.
     * @param TYPE               The type of appointment (e.g. "meeting", "call", etc.)
     * @param localStartDateTime The start time of the appointment in the local timezone.
     * @param localEndDateTime   The end time of the appointment in the local timezone.
     * @param userid             The unique identifier for the user associated with this appointment.
     */


    public Appointment(int appointmentId, String title, String location, String TYPE,
                       ZonedDateTime localStartDateTime, ZonedDateTime localEndDateTime,
                       Customer c, int userid) {
        this.TYPE = TYPE;
        this.appointmentId = appointmentId;
        this.title = title;
        this.location = location;
        this.userid = userid;
        this.localStartDateTime = localStartDateTime;
        this.localEndDateTime = localEndDateTime;
        this.customer = c;


    }

    public String getCustomerName() {
        return customer.getCUSTOMER_NAME();
    }

    public String getRepresentativeName() {
        return this.customer.getRepresentative().getRepName();
    }

    public String getRepresentativeEmail() {
        return this.customer.getRepresentative().getEmail();
    }

    public String getRepresentativePhone() {
        return this.customer.getRepresentative().getPhone();
    }

    /**
     * Returns a string representation of this Appointment object.
     *
     * @return A string containing the appointment ID, title, description, location, customer ID, user ID, start date and time, and end date and time.
     */
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentid=" + appointmentId +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", userid=" + userid +
                ", localStartDateTime=" + localStartDateTime +
                ", localEndDateTime=" + localEndDateTime +
                ", client={" + customer + "}" +
                ", Representative={" + this.customer.getRepresentative() + "}" +
                '}';
    }

    public String getDetails() {
        return "appointmentid=" + appointmentId + ", " +
                "title=" + getTitle() + ", " +
                "location=" + getLocation() + ", " +
                "CUSTOMER_ID=" + getCustomerId() + ", " +
                "CUSTOMER_NAME=" + getCustomerName() + ", " +
                "RepName=" + this.customer.getRepresentative().getRepName() + ", " +
                "RepEmail=" + this.customer.getRepresentative().getEmail() + ", " +
                "RepPhone=" + this.customer.getRepresentative().getPhone();
    }

    /**
     * Returns the unique identifier for this appointment.
     *
     * @return The appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Returns the title of this appointment.
     *
     * @return The appointment title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this appointment.
     *
     * @param title The new appointment title.
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Returns the location of this appointment.
     *
     * @return The appointment location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of this appointment.
     *
     * @param location The new appointment location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the unique identifier for the customer associated with this appointment.
     *
     * @return The customer ID.
     */
    public int getCustomerId() {
        return customer.getCUSTOMER_ID();
    }


    /**
     * Returns the unique identifier for the user associated with this appointment.
     *
     * @return The user ID.
     */
    public int getUserid() {
        return userid;
    }

    /**
     * Sets the unique identifier for the user associated with this appointment.
     *
     * @param userid The new user ID.
     */

    //    ------------------------- Start

    /**
     * Returns the start time of this appointment in the local timezone.
     *
     * @return The start time as a ZonedDateTime object.
     */
    public ZonedDateTime getLocalStartDateTime() {
        return localStartDateTime;
    }

    /**
     * Sets the start time of this appointment in the local timezone.
     *
     * @param localStartDateTime The new start time as a Zoned
     */
    public void setLocalStartDateTime(ZonedDateTime localStartDateTime) {
        this.localStartDateTime = localStartDateTime;
    }

    /**
     * Returns the date of the start time of this appointment.
     *
     * @return The start date as a LocalDate object.
     */
    public LocalDate getStartDate() {
        return localStartDateTime.toLocalDate();
    }

    /**
     * Returns the time of the start time of this appointment.
     *
     * @return The start time as a LocalTime object.
     */
    public LocalTime getStartTime() {
        return localStartDateTime.toLocalTime();
    }


    //    -------------------------- End

    /**
     * Returns the end time of this appointment in the local timezone.
     *
     * @return The end time as a ZonedDateTime object.
     */
    public ZonedDateTime getLocalEndDateTime() {
        return localEndDateTime;
    }

    /**
     * Sets the end time of this appointment in the local timezone.
     *
     * @param localEndDateTime The new end time as a ZonedDateTime object.
     */
    public void setLocalEndDateTime(ZonedDateTime localEndDateTime) {
        this.localEndDateTime = localEndDateTime;
    }

    /**
     * Returns the date of the end time of this appointment.
     *
     * @return The end date as a LocalDate object.
     */
    public LocalDate getEndDate() {
        return localEndDateTime.toLocalDate();
    }

    /**
     * Returns the time of the end time of this appointment.
     *
     * @return The end time as a LocalDate object.
     */
    public LocalTime getEndTime() {
        return localEndDateTime.toLocalTime();
    }

    /**
     * Returns the time of the end time of this appointment.
     *
     * @return The end time as a LocalTime object.
     */
    public String getTYPE() {
        return TYPE;
    }


    public void setId(int id) {
        appointmentId = id;
    }


    public void setCustomer(Customer newCustomer) {
        customer = newCustomer;
    }
    public void setRepresentative(Representative representative) {
        this.customer.setRepresentative(representative);
    }


}
