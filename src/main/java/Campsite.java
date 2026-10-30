import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Campsite {
    private String name;
    private String address;
    private int numberOfStars;
    private String phoneNumber;
    private LinkedList<String> facilities;
    private LinkedList<CampingArea> campingAreas;

    public Campsite(String name, String address, int numberOfStars, String phoneNumber) {
        facilities = new LinkedList<>();
        campingAreas = new LinkedList<>();
        setName(name);
        setAddress(address);
        setNumberOfStars(numberOfStars);
        setPhoneNumber(phoneNumber);
    }

    // --------------------------------------------------
    // Campsite name
    // --------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Campsite name cannot be empty.");
        }

        if (name.length() > 50) {
            throw new IllegalArgumentException("Campsite name cannot exceed 50 characters.");
        }
        this.name = name.trim();
    }

    // --------------------------------------------------
    // Address
    // --------------------------------------------------

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Campsite address cannot be empty.");
        }

        if (address.length() > 100) {
            throw new IllegalArgumentException("Campsite address cannot exceed 100 characters.");
        }
        this.address = address.trim();
    }

    // --------------------------------------------------
    // Number of stars
    // --------------------------------------------------

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(int numberOfStars) {
        if (numberOfStars < 1 || numberOfStars > 5) {
            throw new IllegalArgumentException("Number of stars must be between 1 and 5.");
        }
        this.numberOfStars = numberOfStars;
    }

    // --------------------------------------------------
    // Phone number
    // --------------------------------------------------

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }

        if (!phoneNumber.matches("[0-9]+")) {
            throw new IllegalArgumentException("Phone number must contain numbers only.");
        }
        this.phoneNumber = phoneNumber;
    }

    // --------------------------------------------------
    // Facilities
    // --------------------------------------------------

    public LinkedList<String> getFacilities() {
        return facilities;
    }

    public int getNumberOfFacilities() {
        return facilities.size();
    }

    public boolean addFacility(String facility) {
        if (facility == null || facility.isBlank()) {
            throw new IllegalArgumentException("Facility cannot be empty.");
        }

        if (hasFacility(facility)) {
            return false;
        }

        facilities.add(facility.trim());
        return true;
    }

    public boolean hasFacility(String facility) {
        if (facility == null || facility.isBlank()) {
            return false;
        }

        for (int i = 0; i < facilities.size(); i++) {
            String currentFacility = facilities.get(i);

            if (currentFacility.equalsIgnoreCase(facility.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean removeFacility(String facility) {
        if (facility == null || facility.isBlank()) {
            return false;
        }

        for (int i = 0; i < facilities.size(); i++) {
            String currentFacility = facilities.get(i);

            if (currentFacility.equalsIgnoreCase(facility.trim())) {
                facilities.remove(i);
                return true;
            }
        }
        return false;
    }

    public String getFacilitiesReport() {
        if (facilities.isEmpty()) {
            return "None";
        }

        String report = "";

        for (int i = 0; i < facilities.size(); i++) {
            report += facilities.get(i);

            if (i < facilities.size() - 1) {
                report += ", ";
            }
        }
        return report;
    }

    // --------------------------------------------------
    // Camping-area management
    // --------------------------------------------------

    public LinkedList<CampingArea> getCampingAreas() {
        return campingAreas;
    }

    public int getNumberOfCampingAreas() {
        return campingAreas.size();
    }

    public boolean addCampingArea(CampingArea campingArea) {
        if (campingArea == null) {
            throw new IllegalArgumentException("Camping area cannot be null.");
        }

        String areaID = campingArea.getCampingAreaID();

        if (findCampingArea(areaID) != null) {
            return false;
        }
        campingAreas.add(campingArea);
        return true;
    }

    public CampingArea findCampingArea(String campingAreaID) {
        if (campingAreaID == null || campingAreaID.isBlank()) {
            return null;
        }

        String cleanedID = campingAreaID.trim();

        for (int i = 0; i < campingAreas.size(); i++) {
            CampingArea area = campingAreas.get(i);

            if (area.getCampingAreaID().equalsIgnoreCase(cleanedID)) {
                return area;
            }
        }
        return null;
    }

    public boolean removeCampingArea(String campingAreaID) {
        if (campingAreaID == null || campingAreaID.isBlank()) {
            return false;
        }

        for (int i = 0; i < campingAreas.size(); i++) {
            CampingArea area = campingAreas.get(i);

            if (area.getCampingAreaID().equalsIgnoreCase(campingAreaID.trim())) {
                if (area.getNumberOfPendingBookings() > 0) {
                    return false;
                }

                campingAreas.remove(i);
                return true;
            }
        }
        return false;
    }

    // --------------------------------------------------
    // Plot management
    // --------------------------------------------------

    public boolean addPlot(String campingAreaID, Plot plot) {
        CampingArea area = findCampingArea(campingAreaID);

        if (area == null) {
            return false;
        }
        return area.addPlot(plot);
    }

    public Plot findPlot(String campingAreaID, String plotID) {
        CampingArea area = findCampingArea(campingAreaID);

        if (area == null) {
            return null;
        }
        return area.findPlot(plotID);
    }

    // --------------------------------------------------
    // Booking creation
    // --------------------------------------------------

    public Booking createBooking(int bookingID, String campingAreaID, Guest guest, LocalDate startDate, LocalDate endDate, String preferredPlotType, int numberOfGuests, boolean powerRequired, int minimumSize, int minimumBedrooms, int maximumBudget) {
        CampingArea area = findCampingArea(campingAreaID);

        if (area == null) {
            throw new IllegalArgumentException("Camping area does not exist.");
        }

        if (findBooking(bookingID) != null) {
            throw new IllegalArgumentException("A booking with this ID already exists in this campsite.");
        }
        return area.createBooking(bookingID, guest, name, startDate, endDate, preferredPlotType, numberOfGuests, powerRequired, minimumSize, minimumBedrooms, maximumBudget);
    }

    // --------------------------------------------------
    // Booking search
    // --------------------------------------------------

    public Booking findBooking(int bookingID) {
        for (int i = 0; i < campingAreas.size(); i++) {
            CampingArea area = campingAreas.get(i);

            Booking booking = area.findBooking(bookingID);

            if (booking != null) {
                return booking;
            }
        }
        return null;
    }

    public CampingArea findAreaForBooking(int bookingID) {

        for (int i = 0; i < campingAreas.size(); i++) {
            CampingArea area = campingAreas.get(i);

            if (area.findBooking(bookingID) != null) {
                return area;
            }
        }
        return null;
    }

    // --------------------------------------------------
    // Booking cancellation
    // --------------------------------------------------

    public boolean cancelBooking(int bookingID) {
        CampingArea area = findAreaForBooking(bookingID);

        if (area == null) {
            return false;
        }
        return area.cancelBooking(bookingID);
    }

    // --------------------------------------------------
    // Booking completion
    // --------------------------------------------------

    public boolean completeBooking(int bookingID) {
        CampingArea area = findAreaForBooking(bookingID);

        if (area == null) {
            return false;
        }
        return area.completeBooking(bookingID);
    }

    // --------------------------------------------------
    // Plot removal and booking reassignment
    // --------------------------------------------------

    public boolean removePlotAndReassignBookings(String campingAreaID, String plotID) {
        CampingArea originalArea = findCampingArea(campingAreaID);

        if (originalArea == null) {
            return false;
        }

        Plot plotToRemove = originalArea.findPlot(plotID);

        if (plotToRemove == null) {
            return false;
        }

        LinkedList<Booking> pendingBookings = getPendingBookings(plotToRemove);

        for (int i = 0; i < pendingBookings.size(); i++) {
            Booking booking = pendingBookings.get(i);

            CampingArea replacementArea = findReplacementArea(booking, plotToRemove);

            if (replacementArea == null) {
                return false;
            }

            Plot replacementPlot = findReplacementPlot(replacementArea, booking, plotToRemove);

            if (replacementPlot == null) {
                return false;
            }

            Plot oldPlot = booking.getAssignedPlot();
            booking.reassignPlot(replacementPlot, replacementArea.getCampingAreaID());
            oldPlot.removeBooking(booking.getBookingID());
            replacementPlot.addBooking(booking);
        }
        return originalArea.removePlot(plotID);
    }

    private LinkedList<Booking> getPendingBookings(Plot plot) {
        LinkedList<Booking> pendingBookings = new LinkedList<>();
        LinkedList<Booking> plotBookings = plot.getBookings();

        for (int i = 0; i < plotBookings.size(); i++) {
            Booking booking = plotBookings.get(i);

            if (booking.isPending()) {
                pendingBookings.add(booking);
            }
        }
        return pendingBookings;
    }

    private CampingArea findReplacementArea(Booking booking, Plot excludedPlot) {

        for (int i = 0; i < campingAreas.size(); i++) {
            CampingArea area = campingAreas.get(i);
            Plot replacement = findReplacementPlot(area, booking, excludedPlot);

            if (replacement != null) {
                return area;
            }
        }
        return null;
    }

    //continue skips remainder of current loop iteration and starts next iteration
    private Plot findReplacementPlot(CampingArea area, Booking booking, Plot excludedPlot) {
        LinkedList<Plot> plots = area.getPlots();
        Plot bestPlot = null;

        for (int i = 0; i < plots.size(); i++) {
            Plot candidate = plots.get(i);

            if (candidate == excludedPlot) {
                continue;
            }

            if (!candidate.isAvailable(booking.getStartDate(), booking.getEndDate())) {
                continue;
            }

            if (!plotMatchesBooking(candidate, booking)) {
                continue;
            }

            if (bestPlot == null || candidate.getCost() < bestPlot.getCost()) {
                bestPlot = candidate;
            }
        }
        return bestPlot;
    }

    private boolean plotMatchesBooking(Plot plot, Booking booking) {
        String plotType = plot.getPlotType();

        if (plotType == null) {
            return false;
        }

        if (!plotType.equalsIgnoreCase(booking.getPreferredPlotType())) {
            return false;
        }

        if (plot.getBeds() < booking.getNumberOfGuests()) {
            return false;
        }

        if (booking.isPowerRequired() && !plot.isPowered()) {
            return false;
        }

        if (plot.getSize() < booking.getMinimumSize()) {
            return false;
        }

        if (plot.getBedrooms() < booking.getMinimumBedrooms()) {
            return false;
        }

        long nights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());

        long totalCost = nights * plot.getCost();

        return totalCost <= booking.getMaximumBudget();
    }

    // --------------------------------------------------
    // Pending booking totals
    // --------------------------------------------------

    public int getNumberOfPendingBookings() {
        int numberOfPendingBookings = 0;

        for (int i = 0; i < campingAreas.size(); i++) {
            CampingArea area = campingAreas.get(i);
            numberOfPendingBookings += area.getNumberOfPendingBookings();
        }
        return numberOfPendingBookings;
    }

    public int getPendingBookingTotal() {
        int total = 0;

        for (int i = 0; i < campingAreas.size(); i++) {
            CampingArea area = campingAreas.get(i);
            total += area.getPendingBookingTotal();
        }
        return total;
    }

    // --------------------------------------------------
    // toString
    // --------------------------------------------------

    @Override
    public String toString() {
        return "Campsite name: " + name + "\nAddress: " + address + "\nNumber of stars: " + numberOfStars + "\nPhone number: " + phoneNumber + "\nFacilities: " + getFacilitiesReport() + "\nNumber of camping areas: " + campingAreas.size() + "\nPending bookings: " + getNumberOfPendingBookings() + "\nPending booking total: €" + getPendingBookingTotal();
    }
}