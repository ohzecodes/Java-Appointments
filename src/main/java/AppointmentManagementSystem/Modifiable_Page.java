package AppointmentManagementSystem;

import javafx.event.ActionEvent;

import java.util.Objects;

public interface Modifiable_Page {
    enum Mode{unset,
        add, modify
    }
    String idOnAdd ="auto-generated";

    abstract int validate();

    void blockId();

    abstract void save(ActionEvent actionEvent);
    public static int parseToInt(String stringToParse, int defaultValue) {
        if(Objects.equals(stringToParse, "")  || stringToParse.equalsIgnoreCase(idOnAdd)){
            return defaultValue;
        }
        try {
            return Integer.parseInt(stringToParse);
        } catch(NumberFormatException ex) {
            return defaultValue; //Use default value if parsing failed
        }
    }
}
