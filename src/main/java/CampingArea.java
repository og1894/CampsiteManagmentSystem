import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CampingArea {
    private String campingAreaID;
    private String description;
    private LinkedList<Plot> plots;

    public CampingArea(String campingAreaID, String description) {
        plots = new LinkedList<>();
        setCampingAreaID(campingAreaID);
        setDescription(description);
    }

    // --------------------------------------------------
    // Camping area ID
    // --------------------------------------------------

    public String getCampingAreaID() {
        return campingAreaID;
    }

    public void setCampingAreaID(String campingAreaID) {
        if (campingAreaID == null || campingAreaID.isBlank()) {
            throw new IllegalArgumentException("Camping area ID cannot be empty.");
        }

        if (campingAreaID.length() > 50) {
            throw new IllegalArgumentException("Camping area ID cannot exceed 50 characters.");
        }
        this.campingAreaID = campingAreaID.trim();
    }

    // --------------------------------------------------
    // Description
    // --------------------------------------------------

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            this.description = "No description";
        }
        else {
            this.description = description.trim();
        }
    }

    // --------------------------------------------------
    // Plot management
    // --------------------------------------------------

    public LinkedList<Plot> getPlots() {
        return plots;
    }

    public int getNumberOfPlots() {
        return plots.size();
    }

    public boolean addPlot(Plot plot) {
        if (plot == null) {
            throw new IllegalArgumentException("Plot cannot be null.");
        }

        if (findPlot(plot.getPlotID()) != null) {
            return false;
        }
        plots.add(plot);
        return true;
    }

    public Plot findPlot(String plotID) {
        if (plotID == null || plotID.isBlank()) {
            return null;
        }

        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);

            if (plot.getPlotID().equalsIgnoreCase(plotID.trim())) {
                return plot;
            }
        }
        return null;
    }

    public boolean removePlot(String plotID) {
        if (plotID == null || plotID.isBlank()) {
            return false;
        }

        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);

            if (plot.getPlotID().equalsIgnoreCase(plotID.trim())) {
                if (hasPendingBookings(plot)) {
                    return false;
                }
                plots.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean hasPendingBookings(Plot plot) {
        LinkedList<Booking> bookings = plot.getBookings();

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            if (booking.isPending()) {
                return true;
            }
        }
        return false;
    }

    // --------------------------------------------------
    // Suitable plot search
    // --------------------------------------------------

    public Plot findSuitablePlot(LocalDate startDate, LocalDate endDate, String preferredPlotType, int numberOfGuests, boolean powerRequired, int minimumSize, int minimumBedrooms, int maximumBudget) {
        validateBookingRequest(startDate, endDate, preferredPlotType, numberOfGuests, minimumSize, minimumBedrooms, maximumBudget);

        Plot bestPlot = null;

        for (int i = 0; i < plots.size(); i++) {
            Plot currentPlot = plots.get(i);

            if (plotMatchesRequirements(currentPlot, startDate, endDate, preferredPlotType, numberOfGuests, powerRequired, minimumSize, minimumBedrooms, maximumBudget)) {

                if (bestPlot == null) {
                    bestPlot = currentPlot;
                }
                else if (currentPlot.getCost() < bestPlot.getCost()) {
                    bestPlot = currentPlot;
                }
            }
        }
        return bestPlot;
    }

    private boolean plotMatchesRequirements(Plot plot, LocalDate startDate, LocalDate endDate, String preferredPlotType, int numberOfGuests, boolean powerRequired, int minimumSize, int minimumBedrooms, int maximumBudget) {
        if (plot == null) {
            return false;
        }

        if (!plotTypeMatches(plot, preferredPlotType)) {
            return false;
        }

        if (!plot.isAvailable(startDate, endDate)) {
            return false;
        }

        if (plot.getBeds() < numberOfGuests) {
            return false;
        }

        if (powerRequired && !plot.isPowered()) {
            return false;
        }

        if (plot.getSize() < minimumSize) {
            return false;
        }

        if (plot.getBedrooms() < minimumBedrooms) {
            return false;
        }

        long numberOfNights = ChronoUnit.DAYS.between(startDate, endDate);
        long totalCost = numberOfNights * plot.getCost();
        return totalCost <= maximumBudget;
    }

    private boolean plotTypeMatches(Plot plot, String preferredPlotType) {
        if (preferredPlotType == null) {
            return false;
        }

        String staticType = plot.getStaticDwelling();
        String dwellingType = plot.getDwelling();
        boolean staticTypeMatches = staticType != null && staticType.equalsIgnoreCase(preferredPlotType);
        boolean dwellingTypeMatches = dwellingType != null && dwellingType.equalsIgnoreCase(preferredPlotType);

        return staticTypeMatches || dwellingTypeMatches;
    }

    // --------------------------------------------------
    // Booking creation
    // --------------------------------------------------

    public Booking createBooking(int bookingID, Guest guest, String campsiteName, LocalDate startDate, LocalDate endDate, String preferredPlotType, int numberOfGuests, boolean powerRequired, int minimumSize, int minimumBedrooms, int maximumBudget) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null.");
        }

        if (campsiteName == null || campsiteName.isBlank()) {
            throw new IllegalArgumentException("Campsite name cannot be empty.");
        }

        if (findBooking(bookingID) != null) {
            throw new IllegalArgumentException("A booking with this ID already exists in the camping area.");
        }

        Plot suitablePlot = findSuitablePlot(startDate, endDate, preferredPlotType, numberOfGuests, powerRequired, minimumSize, minimumBedrooms, maximumBudget);

        if (suitablePlot == null) {
            return null;
        }

        Booking booking = new Booking(bookingID, guest, suitablePlot, campsiteName, campingAreaID, startDate, endDate, preferredPlotType, numberOfGuests, powerRequired, minimumSize, minimumBedrooms, maximumBudget);

        suitablePlot.addBooking(booking);
        guest.addBooking(booking);

        return booking;
    }

    // --------------------------------------------------
    // Booking search
    // --------------------------------------------------

    public Booking findBooking(int bookingID) {
        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);

            Booking booking = plot.findBooking(bookingID);

            if (booking != null) {
                return booking;
            }
        }
        return null;
    }

    public Plot findPlotForBooking(int bookingID) {
        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);

            if (plot.findBooking(bookingID) != null) {
                return plot;
            }
        }
        return null;
    }

    // --------------------------------------------------
    // Booking cancellation
    // --------------------------------------------------

    public boolean cancelBooking(int bookingID) {
        Booking booking = findBooking(bookingID);

        if (booking == null || !booking.isPending()) {
            return false;
        }

        Plot assignedPlot = booking.getAssignedPlot();

        booking.cancelBooking();

        return assignedPlot.removeBooking(bookingID);
    }

    // --------------------------------------------------
    // Booking completion
    // --------------------------------------------------

    public boolean completeBooking(int bookingID) {
        Booking booking = findBooking(bookingID);

        if (booking == null || !booking.isPending()) {
            return false;
        }

        booking.completeBooking();
        return true;
    }

    // --------------------------------------------------
    // Booking reassignment within this area
    // --------------------------------------------------

    public boolean reassignBookingWithinArea(int bookingID, String excludedPlotID) {
        Booking booking = findBooking(bookingID);

        if (booking == null || !booking.isPending()) {
            return false;
        }

        Plot oldPlot = booking.getAssignedPlot();
        Plot replacementPlot = null;

        for (int i = 0; i < plots.size(); i++) {
            Plot candidate = plots.get(i);

            boolean isExcludedPlot = candidate.getPlotID().equalsIgnoreCase(excludedPlotID);

            if (!isExcludedPlot && candidate.isAvailable(booking.getStartDate(), booking.getEndDate()) && matchesExistingBooking(candidate, booking)) {
                replacementPlot = candidate;
                break;
            }
        }

        if (replacementPlot == null) {
            return false;
        }

        //Validate and update the booking first.
        booking.reassignPlot(replacementPlot, campingAreaID);

        //Move the booking between the plot lists.
        oldPlot.removeBooking(bookingID);
        replacementPlot.addBooking(booking);

        return true;
    }

    private boolean matchesExistingBooking(Plot candidate, Booking booking) {
        String candidateType = candidate.getPlotType();

        if (candidateType == null || !candidateType.equalsIgnoreCase(booking.getPreferredPlotType())) {
            return false;
        }

        if (candidate.getBeds() < booking.getNumberOfGuests()) {
            return false;
        }

        if (booking.isPowerRequired() && !candidate.isPowered()) {
            return false;
        }

        if (candidate.getSize() < booking.getMinimumSize()) {
            return false;
        }

        if (candidate.getBedrooms() < booking.getMinimumBedrooms()) {
            return false;
        }

        long nights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        long replacementCost = nights * candidate.getCost();
        return replacementCost <= booking.getMaximumBudget();
    }

    // --------------------------------------------------
    // Pending booking information
    // --------------------------------------------------

    public int getNumberOfPendingBookings() {
        int numberOfPendingBookings = 0;

        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);

            LinkedList<Booking> bookings = plot.getBookings();

            for (int j = 0; j < bookings.size(); j++) {
                Booking booking = bookings.get(j);

                if (booking.isPending()) {
                    numberOfPendingBookings++;
                }
            }
        }
        return numberOfPendingBookings;
    }

    public int getPendingBookingTotal() {
        int total = 0;

        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);

            LinkedList<Booking> bookings = plot.getBookings();

            for (int j = 0; j < bookings.size(); j++) {
                Booking booking = bookings.get(j);

                if (booking.isPending()) {
                    total += booking.getTotalCost();
                }
            }
        }
        return total;
    }

    // --------------------------------------------------
    // Booking validation
    // --------------------------------------------------

    private void validateBookingRequest(LocalDate startDate, LocalDate endDate, String preferredPlotType, int numberOfGuests, int minimumSize, int minimumBedrooms, int maximumBudget) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Booking dates cannot be null.");
        }

        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("The start date must be before the end date.");
        }

        if (preferredPlotType == null || preferredPlotType.isBlank()) {
            throw new IllegalArgumentException("Preferred plot type cannot be empty.");
        }

        if (numberOfGuests < 1) {
            throw new IllegalArgumentException("There must be at least one guest.");
        }

        if (minimumSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative.");
        }

        if (minimumBedrooms < 0) {
            throw new IllegalArgumentException("Minimum bedrooms cannot be negative.");
        }

        if (maximumBudget < 0) {
            throw new IllegalArgumentException("Maximum budget cannot be negative.");
        }
    }

    // --------------------------------------------------
    // toString
    // --------------------------------------------------

    @Override
    public String toString() {
        return "Camping Area ID: " + campingAreaID + "\nDescription: " + description + "\nNumber of plots: " + plots.size() + "\nPending bookings: " + getNumberOfPendingBookings() + "\nPending booking total: €" + getPendingBookingTotal();
    }
}