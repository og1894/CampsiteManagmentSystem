import java.time.LocalDate;

public class Plot {
    private String plotID;
    private String name;
    private String staticDwelling;
    private String dwelling;
    private boolean hasPower;
    private int bedrooms;
    private int size;
    private int cost;
    private String facilities;
    private int beds;
    private LinkedList<Booking> bookings;

    public Plot(String plotID, String name, String staticDwelling, String dwelling, boolean hasPower, int bedrooms, int size, int cost, String facilities, int beds) {
        bookings = new LinkedList<>();
        setPlotID(plotID);
        setName(name);
        setStaticDwelling(staticDwelling);
        setDwelling(dwelling);
        setHasPower(hasPower);
        setBedrooms(bedrooms);
        setSize(size);
        setCost(cost);
        setFacilities(facilities);
        setBeds(beds);
    }

    // --------------------------------------------------
    // Plot ID
    // --------------------------------------------------

    public String getPlotID() {
        return plotID;
    }

    public void setPlotID(String plotID) {
        if (plotID == null || plotID.isBlank()) {
            throw new IllegalArgumentException("Plot ID cannot be empty.");
        }

        if (!onlyContainsNumbers(plotID)) {
            throw new IllegalArgumentException("Plot ID must contain numbers only.");
        }

        if (plotID.length() > 10) {
            throw new IllegalArgumentException("Plot ID cannot contain more than 10 digits.");
        }
        this.plotID = plotID;
    }

    // --------------------------------------------------
    // Plot name
    // --------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Plot name cannot be empty.");
        }

        if (name.length() > 30) {
            throw new IllegalArgumentException("Plot name cannot be longer than 30 characters.");
        }
        this.name = name.trim();
    }

    // --------------------------------------------------
    // Static dwelling
    // --------------------------------------------------

    public String getStaticDwelling() {
        return staticDwelling;
    }

    public void setStaticDwelling(String staticDwelling) {
        if (staticDwelling == null || staticDwelling.isBlank() || staticDwelling.equalsIgnoreCase("none")) {
            this.staticDwelling = null;
            return;
        }

        if (!isValidStaticDwelling(staticDwelling)) {
            throw new IllegalArgumentException("Static dwelling must be MobileHome, " + "Bungalow, Apartment or None.");
        }
        this.staticDwelling = staticDwelling.trim();
    }

    public boolean isStaticDwelling() {
        return staticDwelling != null && isValidStaticDwelling(staticDwelling);
    }

    private boolean isValidStaticDwelling(String value) {
        return value.equalsIgnoreCase("Mobile Home") || value.equalsIgnoreCase("Bungalow") || value.equalsIgnoreCase("Apartment");
    }

    // --------------------------------------------------
    // Normal dwelling
    // --------------------------------------------------

    public String getDwelling() {
        return dwelling;
    }

    public void setDwelling(String dwelling) {
        if (dwelling == null || dwelling.isBlank() || dwelling.equalsIgnoreCase("none")) {
            this.dwelling = null;
            return;
        }

        if (!isValidDwelling(dwelling)) {
            throw new IllegalArgumentException("Dwelling must be EmptyPitch, Tent, Caravan, " + "Campervan, Motorhome or None.");
        }
        this.dwelling = dwelling.trim();
    }

    private boolean isValidDwelling(String value) {
        return value.equalsIgnoreCase("Empty Pitch") || value.equalsIgnoreCase("Tent") || value.equalsIgnoreCase("Caravan") || value.equalsIgnoreCase("Campervan") || value.equalsIgnoreCase("Motor home");
    }

    // --------------------------------------------------
    // Dwelling power
    // --------------------------------------------------

    public boolean isPowered() {
        return hasPower;
    }

    public void setHasPower(boolean hasPower) {
        if (isStaticDwelling()) {
            this.hasPower = true;
        }
        else {
            this.hasPower = hasPower;
        }
    }

    // --------------------------------------------------
    // Bedrooms
    // --------------------------------------------------

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        if (bedrooms < 0) {
            throw new IllegalArgumentException("Number of bedrooms cannot be negative.");
        }

        if (!isStaticDwelling() && bedrooms > 0) {
            throw new IllegalArgumentException("Only static dwellings can have bedrooms.");
        }
        this.bedrooms = bedrooms;
    }

    // --------------------------------------------------
    // Plot size
    // --------------------------------------------------

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Plot size must be greater than zero.");
        }
        this.size = size;
    }

    // --------------------------------------------------
    // Cost
    // --------------------------------------------------

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }
        this.cost = cost;
    }

    // --------------------------------------------------
    // Facilities
    // --------------------------------------------------

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        if (facilities == null || facilities.isBlank()) {
            this.facilities = "None";
        }
        else {
            this.facilities = facilities.trim();
        }
    }

    // --------------------------------------------------
    // Beds
    // --------------------------------------------------

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        if (beds < 0) {
            throw new IllegalArgumentException("Plot must have a bed.");
        }

        if (isStaticDwelling() && beds == 0) {
            throw new IllegalArgumentException("A static dwelling must have at least one bed.");
        }
        this.beds = beds;
    }

    // --------------------------------------------------
    // Helper methods
    // --------------------------------------------------

    private boolean onlyContainsNumbers(String text) {
        return (text.matches("[0-9]+"));
    }

    public String getPlotType() {
        if (isStaticDwelling()) {
            return staticDwelling;
        }
        return dwelling;
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

    public boolean removeBooking(int bookingID) {
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            if (booking.getBookingID() == bookingID) {
                bookings.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isAvailable(LocalDate requestedStart, LocalDate requestedEnd) {
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            if (booking.overlaps(requestedStart, requestedEnd)) {
                return false;
            }
        }
        return true;
    }

    // --------------------------------------------------
    // toString
    // --------------------------------------------------

    @Override
    public String toString() {
        return "Plot ID: " + plotID + "\nName: " + name + "\nElectricity: " + hasPower + "\nBedrooms: " + bedrooms + "\nSize (m2): " + size + "\nCost (€): " + cost + " per night" + "\nFacilities: " + facilities + "\nBeds: " + beds + "\nPlot Type: " + getPlotType();
    }
}