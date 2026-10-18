import java.time.LocalDate;

public class Guest {

    private String name;
    private LocalDate dateOfBirth;
    private int guestID = 0;
    private String phoneNumber;
    private String ancillaryInfo;
    private LinkedList<Booking> bookings;


    public Guest(String name, LocalDate dateOfBirth, int guestID, String phoneNumber, String ancillaryInfo) {
        bookings = new LinkedList<>();
        setName(name);
        setDateOfBirth(dateOfBirth);
        setGuestID(guestID);
        setPhoneNumber(phoneNumber);
        setAncillaryInfo(ancillaryInfo);
    }

    // --------------------------------------------------
    // Name
    // --------------------------------------------------

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Guest name cannot be empty.");
        }

        if (name.length() > 30) {
            throw new IllegalArgumentException("Guest name cannot exceed 30 characters.");
        }
        this.name = name.trim();
    }
    public String getName() {
        return name;
    }

    // --------------------------------------------------
    // Date of birth
    // --------------------------------------------------

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null.");
        }

        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    // --------------------------------------------------
    // Guest ID
    // --------------------------------------------------

    public void setGuestID(int guestID) {
        if (guestID < 1 || guestID > 9999) {
            throw new IllegalArgumentException("Guest ID must be between 1 and 9999.");
        }
        this.guestID = guestID;
    }

    public int getGuestID() {
        return guestID;
    }

    // --------------------------------------------------
    // Phone number
    // --------------------------------------------------

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }

        if (!onlyContainsNumbers(phoneNumber)) {
            throw new IllegalArgumentException("Phone number must contain numbers only.");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // --------------------------------------------------
    // Ancillary info
    // --------------------------------------------------

    public void setAncillaryInfo(String ancillaryInfo) {
        if (ancillaryInfo == null || ancillaryInfo.isBlank()) {
            this.ancillaryInfo = "None";
        }
        else {
            this.ancillaryInfo = ancillaryInfo.trim();
        }
    }
    public String getAncillaryInfo() {
        return ancillaryInfo;
    }

    // --------------------------------------------------
    // Booking
    // --------------------------------------------------

    public LinkedList<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }
        bookings.add(booking);
    }

    public Booking findBooking(int bookingID) {
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            if (booking.getBookingID() == bookingID) {
                return booking;
            }
        }
        return null;
    }

    // --------------------------------------------------
    // Helper methods
    // --------------------------------------------------

    private boolean onlyContainsNumbers(String text) {
        return (text.matches("[0-9]+"));
    }

    // --------------------------------------------------
    // toString
    // --------------------------------------------------

    @Override
    public String toString() {
        return "Guest ID: " + guestID + "\nGuest Name: " + name + "\nGuest DOB: " + dateOfBirth + "\nGuest Phone: " + phoneNumber + "\nGuest Ancillary Info: " + ancillaryInfo;
    }
}