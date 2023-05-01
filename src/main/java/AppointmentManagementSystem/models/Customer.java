package AppointmentManagementSystem.models;

/**
This class represents a customer in a scheduling system. It stores information about the customer's
name, address, phone number, location, and other details.
@author Ohzecodes
@version 1.0
/ */
public class Customer {
    private Representative representative;
    private int CUSTOMER_ID;
    private String CUSTOMER_NAME;


    private String STATE;

    private String Country;
    private String Postal;

    public void setID(int CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public Customer(int CUSTOMER_ID, String CUSTOMER_NAME, String STATE, String country,
                    String Postal, Representative rep ) {
        this.CUSTOMER_ID = CUSTOMER_ID;
        this.CUSTOMER_NAME = CUSTOMER_NAME;
        this.STATE = STATE;
        this.Country=country;
        this.Postal=Postal;
        this.representative=rep;

    }

    public Customer(int clientID, String clientName) {
        this.CUSTOMER_ID = clientID;
        this.CUSTOMER_NAME = clientName;

    }

    /**
     * the string representation of the object
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "Customer{" +
                "CUSTOMER_ID=" + CUSTOMER_ID +
                ", CUSTOMER_NAME='" + CUSTOMER_NAME + '\'' +
                ", STATE='" + STATE + '\'' +
                ", Country='" + Country + '\'' +
                ", Postal='" + Postal + '\'' +
                '}';
    }

    /**
     * STATE or Division of the customer
     * @return STATE or Division of the customer
     */
    public String getSTATE() {
        return STATE;
    }

    /**
     *
     * @return the country where the customer lives
     */
    public String  getCountry() {
        return Country;
    }

    /**
     * sets the country of the customer
     * @param country
     */
    public void setCountry(String country) {
        this.Country = country;
    }

    /**
     *
     * @return the id of customer
     */
    public int getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    /**
     *
     * @return the name of costumer
     */
    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    /**
     * sets the name of customer
     * @param CUSTOMER_NAME  new c name
     */
    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }
    /**
     *
     * @return the address of CUSTOMER
     */


    /**
     *
     * @return the PHONE NUMBER of CUSTOMER
     */


    /**
     * @return  CUSTOMER's postal  code
     */
    public String getPOSTAL() {
        return this.Postal;
    }

    public String getRepName() {
        return representative.getRepName();
    }
    public String getRepEmail() {
        return representative.getEmail();
    }
    public String getRepPhone() {
        return representative.getPhone();
    }
    public String getRepDepartment(){
        return representative.getDepartment();
    }
    public Representative getRepresentative(){
        return this.representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative=representative;
    }


    /**
     *
     * @return name of user who created the customer
     */

}
