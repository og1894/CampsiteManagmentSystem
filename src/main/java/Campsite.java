public class Campsite {

    private String campsiteName;
    private String address;
    private int numOfStars;
    private String phoneNumber = "0000000000";
    private LinkedList<String> facilities;
    private LinkedList<CampingArea> campingArea;

    public Campsite(String campsiteName, String address, int numOfStars, String phoneNumber) {
        setCampsiteName(campsiteName);
        setAddress(address);
        setNumOfStars(numOfStars);
        setPhoneNumber(phoneNumber);
        campingArea = new LinkedList<>();
        facilities = new LinkedList<>();
    }

    public void setCampsiteName(String campsiteName) {
        if (campsiteName.length() < 30) {
            this.campsiteName = campsiteName;
        }
    }
    public void setAddress(String address) {
        if (address.length() < 60) {
            this.address = address;
        }
    }

    public void setNumOfStars(int numOfStars) {
       if (numOfStars <= 1 && numOfStars >= 5) {
           this.numOfStars = numOfStars;
       }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (onlyContainsNumbers(phoneNumber))
            this.phoneNumber = phoneNumber;
    }

    public String getCampsiteName() {
        return campsiteName;
    }

    public String getAddress() {
        return address;
    }

    public int getNumOfStars() {
        return numOfStars;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private boolean onlyContainsNumbers(String text) {
        return (text.matches("[0-9]+"));
    }
}

/*
add new campsite:
It should also be possible to record facilities available at the campsite, such as:
restaurants, swimming pools, shops, spa, gym, WiFi, laundry, medical centre, etc.
*/
