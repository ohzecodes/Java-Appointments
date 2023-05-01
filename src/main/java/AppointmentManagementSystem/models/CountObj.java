package AppointmentManagementSystem.models;
/**
 this glass is used to define account object that is used for reports.
@author Ohzecodes
@version 1.0 
 */
public class CountObj {
    private int count;
    private String Type;

    /**
     * this class represents a count object
     * @param count an integer
     * @param type  the name of the object
     */
    public CountObj(int count, String type) {
        this.count = count;
        Type = type;
    }
    /**
    * return the count of the object
        @return the count of the object
    */
        public int getCount() {
            return count;
        }
    /**
    returns the name of the object
    @return name of the object
    */
        public String getType() {
            return Type;
        }
}
