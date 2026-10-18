import java.time.LocalDate;

public class Guest {

    private String name;
    private LocalDate dateOfBirth;
    private int guestID = 0;
    private String phoneNumber;
    private String ancillaryInfo;


    public Guest(String name, LocalDate dateOfBirth, int guestID, String phoneNumber, String ancillaryInfo) {
        setName(name);
        setDateOfBirth(dateOfBirth);
        setGuestID(guestID);
        setPhoneNumber(phoneNumber);
        setAncillaryInfo(ancillaryInfo);
    }

    public void setName(String name) {
        if (name.length() <= 30) {
            this.name = name;
        }
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGuestID(int guestID) {
        if (guestID < 1 || guestID > 9999)
            this.guestID = guestID;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (onlyContainsNumbers(phoneNumber))
            this.phoneNumber = phoneNumber;
    }

    public void setAncillaryInfo(String ancillaryInfo) {
        this.ancillaryInfo = ancillaryInfo;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getGuestID() {
        return guestID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAncillaryInformation() {
        return ancillaryInfo;
    }

    private boolean onlyContainsNumbers(String text) {
        return (text.matches("[0-9]+"));
    }

    @Override
    public String toString() {
        return "Guest ID: " + guestID + '\'' + "Guest Name: " + name + '\'' + "Guest DOB: " + dateOfBirth + '\'' + "Guest Phone: " + phoneNumber + '\'' + "Guest Ancillary Info: " + ancillaryInfo;
    }
}

//Guests can have multiple bookings across multiple campsites.

/*
View a given guest’s record by identifier.
This will also show details of all pending and past bookings
 */