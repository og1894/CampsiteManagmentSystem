public class Guest {

    private String name;
    private int dateOfBirth;
    private int id = 0;
    private String phoneNumber = "0000000000";

    public Guest(String name, int dateOfBirth, int id, String phoneNumber) {
        setName(name);
        setDateOfBirth(dateOfBirth);
        setId(id);
        setPhoneNumber(phoneNumber);
    }

    public void setName(String name) {
        if (name.length() <= 30) {
            this.name = name;
        }
    }

    public void setDateOfBirth(int dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setId(int id) {
        if (id >=0 && id <= 9999)
            this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (onlyContainsNumbers(phoneNumber))
            this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }
    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private boolean onlyContainsNumbers(String text) {
        return (text.matches("[0-9]+"));
    }

    @Override
    public String toString() {
        return
                "Guest ID: " + id + ", Guest Name: " + name + ", Guest DOB: " + dateOfBirth + ", Guest Phone: " + phoneNumber;

    }
}

//Guests can have multiple bookings across multiple campsites.

/*
View a given guest’s record by identifier.
This will also show details of all pending and past bookings
 */