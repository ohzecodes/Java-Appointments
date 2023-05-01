package AppointmentManagementSystem;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class vars {


     public static ObservableList<String> AppointmentTypes = FXCollections.observableArrayList(
            Arrays.asList(
            "Sprint planning meeting",
            "Sprint Retrospective meeting",
            "Product review meeting",
            "One on one meeting",
            "Team building meeting",
            "Board meeting"));

    public static Map<String,String> timings = new HashMap<>() {{
        put("Start", "8");
        put("End", "20");
        put("ZoneId","America/New_York");
    }};


    public static class CountriesAndStates {


        public static HashMap<String, String> getStateAndCountryFromAbbr(String stateAbbr, String countryAbbr) {
            for (Map.Entry<Object, Object> countryEntry : Countries.entrySet()) {
                Map<String, Object> countryData = (Map<String, Object>) countryEntry.getValue();
                String abbr = (String) countryData.get("Abbr");
                if (abbr.equals(countryAbbr)) {
                    Map<String, String> states = (Map<String, String>) countryData.get("states");
                    for (Map.Entry<String, String> stateEntry : states.entrySet()) {
                        String stateCode =  stateEntry.getValue();
                        if (stateCode.equals(stateAbbr)) {
                            HashMap<String, String> result=new HashMap<>();
                            result.put("country", (String) countryEntry.getKey());
                            result.put("state",stateEntry.getKey());
                            return result;
                        }
                    }
                }
            }

            return null;
        }





        private static final Map<String, Object> usa = new HashMap<>();
        static {
            Map<String, String> state = new HashMap<>();
            usa.put("Abbr","USA");
            state.put( "California","CA");
            state.put( "Oregon","OR");
            state.put( "Washington","WA");
            state.put( "Idaho","ID");
            state.put( "Nevada","NV");
            state.put( "Arizona","AZ");
            state.put( "Utah","UT");
            state.put( "Montana","MT");
            state.put( "Wyoming","WY");
            state.put( "Colorado","CO");
            state.put( "New Mexico","NM");
            state.put( "North Dakota","ND");
            state.put( "South Dakota","SD");
            state.put( "Nebraska","NE");
            state.put( "Kansas","KS");
            state.put( "Oklahoma","OK");
            state.put( "Texas","TX");
            state.put( "Minnesota","MN");
            state.put( "Iowa","IA");
            state.put( "Missouri","MO");
            state.put( "Arkansas","AR");
            state.put( "Louisiana","LA");
            state.put( "Wisconsin","WI");
            state.put( "Illinois","IL");
            state.put( "Michigan","MI");
            state.put( "Indiana","IN");
            state.put( "Ohio","OH");
            state.put( "Kentucky","KY");
            state.put( "Tennessee","TN");
            state.put( "Mississippi","MS");
            state.put( "Alabama","AL");
            state.put( "Georgia","GA");
            state.put( "Florida","FL");
            state.put( "South Carolina","SC");
            state.put( "North Carolina","NC");
            state.put( "Virginia","VA");
            state.put( "West Virginia","WV");
            state.put( "Maryland","MD");
            state.put( "Delaware","DE");
            state.put( "Pennsylvania","PA");
            state.put( "New York","NY");
            state.put( "New Jersey","NJ");
            state.put( "Connecticut","CT");
            state.put( "Rhode Island","RI");
            state.put( "Massachusetts","MA");
            state.put( "Vermont","VT");
            state.put( "New Hampshire","NH");
            state.put( "Maine","ME");
            state.put( "Alaska","AK");
            state.put( "Hawaii","HI");
            state.put( "District of Columbia","DC");

            usa.put("states",state);
        }

        private static final Map<String, Object> Canada = new HashMap<>();
        static {
            Map<String, String> state = new HashMap<>();
            Canada.put("Abbr","CAN");
            state.put( "Ontario","ON");
            state.put( "British Columbia","BC");
            state.put( "Alberta","AB");
            state.put( "Saskatchewan","SK");
            state.put( "Manitoba","MB");
            state.put( "Quebec","QC");
            state.put( "New Brunswick","NB");
            state.put( "Nova Scotia","NS");
            state.put( "Prince Edward Island","PE");
            state.put( "Newfoundland and Labrador","NL");
            state.put( "Yukon","YT");
            state.put( "Northwest Territories","NT");
            state.put( "Nunavut","NU");
            Canada.put("states",state);
        }
        private static final Map<String, Object> Mexico = new HashMap<>();
        static {
            Map<String, String> state = new HashMap<>();
            Mexico.put("Abbr","MEX");
            state.put("Aguascalientes","AG");
            state.put("Baja California Norte","BC");
            state.put("Baja California Sur","BS");
            state.put("San Luis Potosi","SL");
            state.put("Distrito Federal","DF");
            state.put("Chihuahua","CH");
            state.put("Colima","CL");
            state.put("Campeche","CM");
            state.put("Coahuila","CO");
            state.put("Chiapas","CS");
            state.put("Durango","DG");
            state.put("Guerrero","GR");
            state.put("Guanajuato","GT");
            state.put("Hidalgo","HG");
            state.put("Jalisco","JA");
            state.put("Michoacan","MI");
            state.put("Morelos","MO");
            state.put("Nayarit","NA");
            state.put("Nuevo Leon","NL");
            state.put("Oaxaca","OA");
            state.put("Puebla","PU");
            state.put("Quintana Roo","QR");
            state.put("Queretaro","QT");
            state.put("Sinaloa","SI");
            state.put("Sonora","SO");
            state.put("Tabasco","TB");
            state.put("Tlaxcala","TL");
            state.put("Tamaulipas","TM");
            state.put("Veracruz","VE");
            state.put("Yucatan","YU");
            state.put("Zacatecas","ZA");
            state.put("México","EM");

            Mexico.put("states",state);
        }

        public static Map<Object, Object> Countries =new HashMap<>();
        static {
            Countries.put("CANADA", Canada) ;
            Countries.put("USA", usa) ;
            Countries.put ("Mexico",Mexico);

        }


        public static ObservableList<String>  getCountries() {
            ObservableList<String> CountriesList=FXCollections.observableArrayList();
            for ( Object countries: Countries.keySet()) {
                CountriesList.add((String) countries);
            }
            return CountriesList;
        }

        public static String getCountryAbbr(String countryName){
            Map<String, Object> country = (Map<String, Object>) Countries.get(countryName);
//            System.out.println(country);
            return (String) country.get("Abbr");
        }

        public static ObservableList<String> getStates(String countryName) {

            Map<String, Object> country = (Map<String, Object>) Countries.get(countryName);
            if(country==null) return null;
            HashMap<String, String>states= (HashMap<String, String>) country.get("states");
            return FXCollections.observableArrayList(states.keySet()).sorted() ;
        }


        public static String getStateAbbr( String countryName,String State){
            Map<String, Object> country = (Map<String, Object>) Countries.get(countryName);
            HashMap<String, String>states= (HashMap<String, String>) country.get("states");
            return states.get(State) ;
        }
    }




}

