import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private int bookingID;
    private Guest guest;
    private Plot assignedPlot;
    private String campsiteName;
    private String campingAreaID;
    private LocalDate startDate;
    private LocalDate endDate;
    private String preferredPlotType;
    private int numberOfGuests;
    private boolean powerRequired;
    private int minimumSize;
    private int minimumBedrooms;
    private int maximumBudget;
    private int totalCost;
    private BookingStatus status;

    public Booking(int bookingID, Guest guest,Plot assignedPlot,String campsiteName,String campingAreaID,LocalDate startDate,LocalDate endDate,String preferredPlotType,int numberOfGuests,boolean powerRequired,int minimumSize,int minimumBedrooms,int maximumBudget) {
        setBookingID(bookingID);
        setGuest(guest);
        setAssignedPlot(assignedPlot);
        setCampsiteName(campsiteName);
        setCampingAreaID(campingAreaID);
        setDates(startDate, endDate);
        setPreferredPlotType(preferredPlotType);
        setNumberOfGuests(numberOfGuests);
        setPowerRequired(powerRequired);
        setMinimumSize(minimumSize);
        setMinimumBedrooms(minimumBedrooms);
        setMaximumBudget(maximumBudget);

        status = BookingStatus.PENDING;
        totalCost = calculateTotalCost();

        if (!assignedPlotMatchesPreferences()) {
            throw new IllegalArgumentException("The assigned plot does not match the booking preferences.");
        }
    }

    // --------------------------------------------------
    // Booking ID
    // --------------------------------------------------

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        if (bookingID < 1 || bookingID > 999999) {
            throw new IllegalArgumentException("Booking ID must be between 1 and 999999.");
        }
        this.bookingID = bookingID;
    }

    // --------------------------------------------------
    // Guest
    // --------------------------------------------------

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("A booking must have a guest.");
        }
        this.guest = guest;
    }

    // --------------------------------------------------
    // Assigned plot
    // --------------------------------------------------

    public Plot getAssignedPlot() {
        return assignedPlot;
    }

    public void setAssignedPlot(Plot assignedPlot) {
        if (assignedPlot == null) {
            throw new IllegalArgumentException("A booking must have an assigned plot.");
        }
        this.assignedPlot = assignedPlot;
    }

    // --------------------------------------------------
    // Campsite
    // --------------------------------------------------

    public String getCampsiteName() {
        return campsiteName;
    }

    public void setCampsiteName(String campsiteName) {
        if (campsiteName == null || campsiteName.isBlank()) {
            throw new IllegalArgumentException("Campsite name cannot be empty.");
        }
        this.campsiteName = campsiteName.trim();
    }

    // --------------------------------------------------
    // Camping area
    // --------------------------------------------------

    public String getCampingAreaID() {
        return campingAreaID;
    }

    public void setCampingAreaID(String campingAreaID) {
        if (campingAreaID == null || campingAreaID.isBlank()) {
            throw new IllegalArgumentException("Camping area ID cannot be empty.");
        }
        this.campingAreaID = campingAreaID.trim();
    }

    // --------------------------------------------------
    // Booking dates
    // --------------------------------------------------

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Booking dates cannot be null.");
        }

        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("The start date must be before the end date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void changeDates(LocalDate newStartDate, LocalDate newEndDate) {
        if (!isPending()) {
            throw new IllegalStateException("Only pending bookings can be changed.");
        }

        if (!assignedPlot.isAvailable(newStartDate, newEndDate)) {
            throw new IllegalArgumentException("The plot is unavailable for the new dates.");
        }

        setDates(newStartDate, newEndDate);
        totalCost = calculateTotalCost();

        if (!isWithinBudget()) {
            throw new IllegalArgumentException("The changed booking exceeds the budget.");
        }
    }

    // --------------------------------------------------
    // Preferred plot type
    // --------------------------------------------------

    public String getPreferredPlotType() {
        return preferredPlotType;
    }

    public void setPreferredPlotType(String preferredPlotType) {
        if (preferredPlotType == null || preferredPlotType.isBlank()) {
            throw new IllegalArgumentException("Preferred plot type cannot be empty.");
        }

        if (!isValidPlotType(preferredPlotType)) {
            throw new IllegalArgumentException("Invalid preferred plot type.");
        }
        this.preferredPlotType = preferredPlotType.trim();
    }

    // --------------------------------------------------
    // Number of guests
    // --------------------------------------------------

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 1) {
            throw new IllegalArgumentException("A booking must contain at least one guest.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    // --------------------------------------------------
    // Power requirement
    // --------------------------------------------------

    public boolean isPowerRequired() {
        return powerRequired;
    }

    public void setPowerRequired(boolean powerRequired) {
        this.powerRequired = powerRequired;
    }

    // --------------------------------------------------
    // Minimum size
    // --------------------------------------------------

    public int getMinimumSize() {
        return minimumSize;
    }

    public void setMinimumSize(int minimumSize) {
        if (minimumSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative.");
        }
        this.minimumSize = minimumSize;
    }

    // --------------------------------------------------
    // Minimum bedrooms
    // --------------------------------------------------

    public int getMinimumBedrooms() {
        return minimumBedrooms;
    }

    public void setMinimumBedrooms(int minimumBedrooms) {
        if (minimumBedrooms < 0) {
            throw new IllegalArgumentException("Minimum bedrooms cannot be negative.");
        }
        this.minimumBedrooms = minimumBedrooms;
    }

    // --------------------------------------------------
    // Maximum budget
    // --------------------------------------------------

    public int getMaximumBudget() {
        return maximumBudget;
    }

    public void setMaximumBudget(int maximumBudget) {
        if (maximumBudget < 0) {
            throw new IllegalArgumentException("Maximum budget cannot be negative.");
        }
        this.maximumBudget = maximumBudget;
    }

    // --------------------------------------------------
    // Status
    // --------------------------------------------------

    public BookingStatus getStatus() {
        return status;
    }

    public boolean isPending() {
        return status == BookingStatus.PENDING;
    }

    public boolean isCompleted() {
        return status == BookingStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }

    public void cancelBooking() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending bookings can be cancelled.");
        }
        status = BookingStatus.CANCELLED;
    }

    public void completeBooking() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending bookings can be completed.");
        }
        status = BookingStatus.COMPLETED;
    }

    // --------------------------------------------------
    // Number of nights and price
    // --------------------------------------------------

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public int calculateTotalCost() {
        if (assignedPlot == null || startDate == null || endDate == null) {
            return 0;
        }
        return Math.toIntExact(getNumberOfNights() * assignedPlot.getCost());
    }

    public int getTotalCost() {
        return totalCost;
    }

    public boolean isWithinBudget() {
        return totalCost <= maximumBudget;
    }

    // --------------------------------------------------
    // Plot matching
    // --------------------------------------------------

    public boolean assignedPlotMatchesPreferences() {
        if (assignedPlot == null) {
            return false;
        }

        if (!plotTypeMatches()) {
            return false;
        }

        if (powerRequired && !assignedPlot.isPowered()) {
            return false;
        }

        if (assignedPlot.getSize() < minimumSize) {
            return false;
        }

        if (assignedPlot.getBedrooms() < minimumBedrooms) {
            return false;
        }

        if (assignedPlot.getBeds() < numberOfGuests) {
            return false;
        }

        return calculateTotalCost() <= maximumBudget;
    }

    private boolean plotTypeMatches() {
        String staticType = assignedPlot.getStaticDwelling();
        String dwellingType = assignedPlot.getDwelling();

        boolean matchesStaticType = staticType != null && staticType.equalsIgnoreCase(preferredPlotType);
        boolean matchesDwellingType = dwellingType != null && dwellingType.equalsIgnoreCase(preferredPlotType);
        return matchesStaticType || matchesDwellingType;
    }

    private boolean isValidPlotType(String plotType) {
        return plotType.equalsIgnoreCase("Mobile Home") || plotType.equalsIgnoreCase("Bungalow") || plotType.equalsIgnoreCase("Apartment") || plotType.equalsIgnoreCase("Empty Pitch") || plotType.equalsIgnoreCase("Tent") || plotType.equalsIgnoreCase("Caravan") || plotType.equalsIgnoreCase("Campervan") || plotType.equalsIgnoreCase("Motor home");
    }

    // --------------------------------------------------
    // Date overlap checking
    // --------------------------------------------------

    public boolean overlaps(LocalDate requestedStart, LocalDate requestedEnd) {
        if (requestedStart == null || requestedEnd == null) {
            throw new IllegalArgumentException("Requested dates cannot be null.");
        }

        if (!requestedStart.isBefore(requestedEnd)) {
            throw new IllegalArgumentException("Requested start date must be before the end date.");
        }

        if (!isPending()) {
            return false;
        }

        return requestedStart.isBefore(endDate) && requestedEnd.isAfter(startDate);
    }

    // --------------------------------------------------
    // Reassigning a plot
    // --------------------------------------------------

    public void reassignPlot(Plot newPlot, String newCampingAreaID) {
        if (!isPending()) {
            throw new IllegalStateException("Only pending bookings can be reassigned.");
        }

        if (newPlot == null) {
            throw new IllegalArgumentException("The replacement plot cannot be null.");
        }

        if (newCampingAreaID == null || newCampingAreaID.isBlank()) {
            throw new IllegalArgumentException("New camping-area ID cannot be empty.");
        }

        if (!newPlot.isAvailable(startDate, endDate)) {
            throw new IllegalArgumentException("The replacement plot is unavailable.");
        }

        Plot previousPlot = assignedPlot;
        String cleanedAreaID = newCampingAreaID.trim();

        assignedPlot = newPlot;
        campingAreaID = newCampingAreaID;

        if (!assignedPlotMatchesPreferences()) {
            assignedPlot = previousPlot;
            campingAreaID = cleanedAreaID;

            throw new IllegalArgumentException("The replacement plot does not match the original booking preferences.");
        }
        totalCost = calculateTotalCost();
    }

    // --------------------------------------------------
    // toString
    // --------------------------------------------------

    @Override
    public String toString() {
        return "Booking ID: " + bookingID +  "\nGuest ID: " + guest.getGuestID() + "\nGuest name: " + guest.getName() + "\nCampsite: " + campsiteName + "\nCamping area: " + campingAreaID + "\nAssigned plot: " + assignedPlot.getPlotID() + "\nStart date: " + startDate + "\nEnd date: " + endDate + "\nNumber of nights: " + getNumberOfNights() + "\nNumber of guests: " + numberOfGuests + "\nPreferred plot type: " + preferredPlotType + "\nPower required: " + powerRequired + "\nMinimum size (m2): " + minimumSize + "\nMinimum bedrooms: " + minimumBedrooms + "\nMaximum budget (€): " + maximumBudget + "\nTotal cost (€): " + totalCost + "\nStatus: " + status;
    }
}