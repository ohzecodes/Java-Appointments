package AppointmentManagementSystem.models;

public class Representative {
    private int representativeID;
    private String repName;
    private String department;
    private String phone;
    private String email;

    public Representative(int representativeID, String repName, String department, String phone, String email) {
        this.representativeID = representativeID;
        this.repName = repName;
        this.department = department;
        this.phone = phone;
        this.email = email;
    }
    public Representative( int repId, String repName, String repPhone, String repEmail) {
        this.representativeID=repId;
        this.repName = repName;
        this.phone = repPhone;
        this.email = repEmail;
    }

    public int getRepresentativeID() {
        return representativeID;
    }

    @Override
    public String toString() {
        return "Representative{ " +
                "rep ID=" + representativeID +
                " ,rep Name=" + repName +
                " ,phone=" + phone +
                " ,email=" + email +
                '}';
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int representativeID) {
        this.representativeID=representativeID;
    }
}

