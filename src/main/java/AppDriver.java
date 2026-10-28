/*import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppDriver extends Application {

    @Override
    public void start(Stage stage) {

        var scene = new Scene(new StackPane(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}*/

//Save and load the entire system data: XML
//Implement suitable JavaFX graphical user interface to interact with your system.
//You should implement a set of JUnit classes to test code

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AppDriver {
    private final Scanner input;
    private LinkedList<Campsite> campsites;
    private LinkedList<Guest> guests;
    private int nextBookingID;

    public AppDriver() {
        input = new Scanner(System.in);
        campsites = new LinkedList<>();
        guests = new LinkedList<>();
        nextBookingID = 1;
    }

    public static void main(String[] args) {
        AppDriver driver = new AppDriver();
        driver.run();
    }

    // --------------------------------------------------
    // Main program loop
    // --------------------------------------------------

    public void run() {
        boolean running = true;
        System.out.println("CAMPSITE MANAGEMENT SYSTEM");

        while (running) {
        displayMainMenu();

        int option = readInteger("Select an option: ");

        while (option != 0) {
            switch (option) {
                case 1 -> addCampsite();
                case 2 -> addCampingArea();
                case 3 -> addPlot();
                case 4 -> addGuest();
                case 5 -> createBooking();
                case 6 -> cancelBooking();
                case 7 -> completeBooking();
                case 8 -> displayCampsites();
                case 9 -> displayGuests();
                case 10 -> displayGuestRecord();
                case 11 -> smartFind();
                case 12 -> removePlot();
                case 13 -> resetSystem();
                case 0 -> running = false;
                default -> System.out.println("Invalid menu option.");
                }
            }
        }

        input.close();
        System.out.println("Campsite management system closed.");
    }

    private void displayMainMenu() {
        System.out.println("\n----MAIN MENU ----");
        System.out.println("1. Add campsite");
        System.out.println("2. Add camping area");
        System.out.println("3. Add plot");
        System.out.println("4. Add guest");
        System.out.println("5. Create booking");
        System.out.println("6. Cancel booking");
        System.out.println("7. Complete booking");
        System.out.println("8. Display campsites");
        System.out.println("9. Display guests");
        System.out.println("10. Display guest record");
        System.out.println("11. Smart find");
        System.out.println("12. Remove plot");
        System.out.println("13. Reset system");
        System.out.println("0. Exit");
    }

    // --------------------------------------------------
    // Campsite methods
    // --------------------------------------------------

    private void addCampsite() {
        System.out.println("\nADD CAMPSITE");
        String name = readRequiredString("Campsite name: ");

        if (findCampsite(name) != null) {
            System.out.println("A campsite with that name already exists.");
            return;
        }

        String address = readRequiredString("Address: ");
        int stars = readInteger("Number of stars (1-5): ");
        String phoneNumber = readRequiredString("Phone number: ");
        Campsite campsite = new Campsite(name, address, stars, phoneNumber);
        campsites.add(campsite);
        boolean addingFacilities = true;

        while (addingFacilities) {
            String facility = readString("Enter a facility or press Enter to finish: ");

            if (facility.isBlank()) {
                addingFacilities = false;
            }
            else {
                boolean added = campsite.addFacility(facility);

                if (!added) {
                    System.out.println("That facility is already recorded.");
                }
            }
        }
        System.out.println("Campsite added successfully.");
    }

    private Campsite findCampsite(String campsiteName) {
        if (campsiteName == null || campsiteName.isBlank()) {
            return null;
        }

        for (int i = 0; i < campsites.size(); i++) {
            Campsite campsite = campsites.get(i);

            if (campsite.getName().equalsIgnoreCase(campsiteName.trim())) {
                return campsite;
            }
        }
        return null;
    }

    private void displayCampsites() {
        System.out.println("\nCAMPSITES");

        if (campsites.isEmpty()) {
            System.out.println("No campsites have been added.");
            return;
        }

        for (int i = 0; i < campsites.size(); i++) {
            System.out.println(campsites.get(i));
        }
    }

    // --------------------------------------------------
    // Camping-area methods
    // --------------------------------------------------

    private void addCampingArea() {
        System.out.println("\nADD CAMPING AREA");
        Campsite campsite = selectCampsite();

        if (campsite == null) {
            return;
        }

        String areaID = readRequiredString("Camping area identifier: ");
        String description = readString("Description: ");
        CampingArea campingArea = new CampingArea(areaID, description);
        boolean added = campsite.addCampingArea(campingArea);

        if (added) {
            System.out.println("Camping area added successfully.");
        }
        else {
            System.out.println("That camping area identifier already exists in the campsite.");
        }
    }

    private CampingArea selectCampingArea(Campsite campsite) {
        if (campsite == null) {
            return null;
        }

        if (campsite.getCampingAreas().isEmpty()) {
            System.out.println("This campsite has no camping areas.");
            return null;
        }

        displayCampingAreas(campsite);
        String areaID = readRequiredString("Enter camping area ID: ");
        CampingArea area = campsite.findCampingArea(areaID);

        if (area == null) {
            System.out.println("Camping area not found.");
        }
        return area;
    }

    private void displayCampingAreas(Campsite campsite) {
        System.out.println("\nCamping areas at " + campsite.getName() + ":");
        LinkedList<CampingArea> areas = campsite.getCampingAreas();

        for (int i = 0; i < areas.size(); i++) {
            CampingArea area = areas.get(i);

            System.out.println((i + 1) + ". " + area.getCampingAreaID() + " - " + area.getDescription());
        }
    }

    // --------------------------------------------------
    // Plot methods
    // --------------------------------------------------

    private void addPlot() {
        System.out.println("\nADD PLOT");
        Campsite campsite = selectCampsite();

        if (campsite == null) {
           return;
        }

        CampingArea area = selectCampingArea(campsite);

        if (area == null) {
            return;
        }

        String plotID = readRequiredString("Plot ID containing numbers only: ");

        if (area.findPlot(plotID) != null) {
            System.out.println("A plot with that ID already exists.");
            return;
        }

        String name = readRequiredString("Plot name: ");
        displayPlotTypes();
        String plotType = readRequiredString("Plot type: ");
        boolean isStaticDwelling = isStaticPlotType(plotType);

        String staticDwelling;
        String dwelling;

        if (isStaticDwelling) {
            staticDwelling = plotType;
            dwelling = null;
        }
        else {
            staticDwelling = null;
            dwelling = plotType;
        }

        boolean powered;

        if (isStaticDwelling) {
            powered = true;
            System.out.println("Static dwellings automatically have power.");
        }
        else {
            powered = readBoolean("Does the plot have power? (yes/no): ");
        }

        int bedrooms;

        if (isStaticDwelling) {
            bedrooms = readNonNegativeInteger("Number of bedrooms: ");
        }
        else {
            bedrooms = 0;
        }

        int size = readPositiveInteger("Size in square metres: ");
        int cost = readNonNegativeInteger("Cost per night: €");
        String facilities = readString("Facilities: ");
        int beds = readNonNegativeInteger(isStaticDwelling ? "Number of beds: " : "Maximum number of guests: ");
        Plot plot = new Plot(plotID, name, staticDwelling, dwelling, powered, bedrooms, size, cost, facilities, beds);
        boolean added = area.addPlot(plot);

        if (added) {
            System.out.println("Plot added successfully.");
        }
        else {
            System.out.println("The plot could not be added.");
        }
    }

    private void displayPlotTypes() {
        System.out.println("\nValid plot types:");
        System.out.println("- Mobile Home");
        System.out.println("- Bungalow");
        System.out.println("- Apartment");
        System.out.println("- Empty Pitch");
        System.out.println("- Tent");
        System.out.println("- Caravan");
        System.out.println("- Campervan");
        System.out.println("- Motor home");
    }

    private boolean isStaticPlotType(String plotType) {
        return plotType.equalsIgnoreCase("Mobile Home") || plotType.equalsIgnoreCase("Bungalow") || plotType.equalsIgnoreCase("Apartment");
    }

    private boolean isValidPlotType(String plotType) {
        return isStaticPlotType(plotType) || plotType.equalsIgnoreCase("Empty Pitch") || plotType.equalsIgnoreCase("Tent") || plotType.equalsIgnoreCase("Caravan") || plotType.equalsIgnoreCase("Campervan") || plotType.equalsIgnoreCase("Motor home");
    }

    // --------------------------------------------------
    // Guest methods
    // --------------------------------------------------

    private void addGuest() {
        System.out.println("\nADD GUEST");

        int guestID = readInteger("Guest ID (1-9999): ");

        if (findGuest(guestID) != null) {
            System.out.println("A guest with that ID already exists.");
            return;
        }

        String name = readRequiredString("Guest name: ");
        LocalDate dateOfBirth = readDate("Date of birth");
        String phoneNumber = readRequiredString("Phone number: ");
        String ancillaryInfo = readString("Ancillary information: ");
        Guest guest = new Guest(name, dateOfBirth, guestID, phoneNumber, ancillaryInfo);
        guests.add(guest);
        System.out.println("Guest added successfully.");
    }

    private Guest findGuest(int guestID) {
        for (int i = 0; i < guests.size(); i++) {
            Guest guest = guests.get(i);

            if (guest.getGuestID() == guestID) {
                return guest;
            }
        }
        return null;
    }

    private void displayGuests() {
        System.out.println("\nGUESTS");

        if (guests.isEmpty()) {
            System.out.println("No guests have been added.");
            return;
        }

        for (int i = 0; i < guests.size(); i++) {
            System.out.println("\n------------------------------");
            System.out.println(guests.get(i));
        }
    }

    private void displayGuestRecord() {
        int guestID = readInteger("Enter guest ID: ");
        Guest guest = findGuest(guestID);

        if (guest == null) {
            System.out.println("Guest not found.");
            return;
        }

        System.out.println("GUEST RECORD");
        System.out.println(guest);
        LinkedList<Booking> bookings = guest.getBookings();

        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings recorded.");
            return;
        }

        System.out.println("\nBOOKING HISTORY");

        for (int i = 0; i < bookings.size(); i++) {
            System.out.println(bookings.get(i));
        }
    }

    // --------------------------------------------------
    // Booking creation
    // --------------------------------------------------

    private void createBooking() {
        System.out.println("\nCREATE BOOKING");
        Campsite campsite = selectCampsite();

        if (campsite == null) {
            return;
        }

        CampingArea area = selectCampingArea(campsite);

        if (area == null) {
            return;
        }

        int guestID = readInteger("Guest ID: ");

        Guest guest = findGuest(guestID);

        if (guest == null) {
            System.out.println("Guest not found.");
            return;
        }

        LocalDate startDate = readDate("Start date");
        LocalDate endDate = readDate("End date");
        displayPlotTypes();
        String preferredPlotType = readRequiredString("Preferred plot type: ");

        if (!isValidPlotType(preferredPlotType)) {
            System.out.println("Invalid plot type.");
            return;
        }

        int numberOfGuests = readPositiveInteger("Number of guests: ");
        boolean powerRequired = readBoolean("Is power required? (yes/no): ");
        int minimumSize = readNonNegativeInteger("Minimum size in m2: ");
        int minimumBedrooms = readNonNegativeInteger("Minimum bedrooms: ");
        int maximumBudget = readNonNegativeInteger("Maximum total budget: €");
        Booking booking = campsite.createBooking(nextBookingID, area.getCampingAreaID(), guest, startDate, endDate, preferredPlotType, numberOfGuests, powerRequired, minimumSize, minimumBedrooms, maximumBudget);

        if (booking == null) {
            System.out.println("No suitable available plot was found.");
            return;
        }

        nextBookingID++;
        System.out.println("\nBooking created successfully.");
        System.out.println("\n" + booking);
    }

    // --------------------------------------------------
    // Booking cancellation
    // --------------------------------------------------

    private void cancelBooking() {
        int bookingID = readInteger("Enter booking ID: ");
        Campsite campsite = findCampsiteForBooking(bookingID);

        if (campsite == null) {
            System.out.println("Booking not found.");
            return;
        }

        boolean cancelled = campsite.cancelBooking(bookingID);

        if (cancelled) {
            System.out.println("Booking cancelled successfully.");
        }
        else {
            System.out.println("The booking could not be cancelled.");
        }
    }

    // --------------------------------------------------
    // Booking completion
    // --------------------------------------------------

    private void completeBooking() {
        int bookingID = readInteger("Enter booking ID: ");
        Campsite campsite = findCampsiteForBooking(bookingID);

        if (campsite == null) {
            System.out.println("Booking not found.");
            return;
        }

        boolean completed = campsite.completeBooking(bookingID);

        if (completed) {
            System.out.println("Booking completed successfully.");
        }
        else {
            System.out.println("The booking could not be completed.");
        }
    }

    private Campsite findCampsiteForBooking(int bookingID) {
        for (int i = 0; i < campsites.size(); i++) {
            Campsite campsite =campsites.get(i);

            if (campsite.findBooking(bookingID) != null) {
                return campsite;
            }
        }
        return null;
    }

    // --------------------------------------------------
    // Basic smart find
    // --------------------------------------------------

    private void smartFind() {
        System.out.println("\nSMART FIND");

        if (campsites.isEmpty()) {
            System.out.println("No campsites are available.");
            return;
        }

        int guestID = readInteger("Guest ID: ");
        Guest guest = findGuest(guestID);

        if (guest == null) {
            System.out.println("Guest not found.");
            return;
        }

        LocalDate startDate = readDate("Desired start date");
        LocalDate endDate = readDate("Desired end date");
        int minimumStars = readInteger("Minimum campsite stars: ");
        displayPlotTypes();
        String plotType = readRequiredString("Preferred plot type: ");
        int numberOfGuests = readPositiveInteger("Number of guests: ");
        boolean powerRequired = readBoolean("Power required? (yes/no): ");
        int minimumSize = readNonNegativeInteger("Minimum size in m2: ");
        int minimumBedrooms = readNonNegativeInteger("Minimum bedrooms: ");
        int maximumBudget = readNonNegativeInteger("Maximum total budget: €");
        String requiredCampsiteFacility = readString("Required campsite facility (press Enter for none): ");
        String requiredPlotFacility = readString("Required plot facility (press Enter for none): ");

        Campsite bestCampsite = null;
        CampingArea bestArea = null;
        Plot bestPlot = null;

        for (int i = 0; i < campsites.size(); i++) {
            Campsite campsite = campsites.get(i);

            if (campsite.getNumberOfStars() < minimumStars) {
                continue;
            }

            if (!requiredCampsiteFacility.isBlank() && !campsite.hasFacility(requiredCampsiteFacility)) {
                continue;
            }

            LinkedList<CampingArea> areas = campsite.getCampingAreas();

            for (int j = 0; j < areas.size(); j++) {
                CampingArea area = areas.get(j);
                Plot candidate = area.findSuitablePlot(startDate, endDate, plotType, numberOfGuests, powerRequired, minimumSize, minimumBedrooms, maximumBudget);

                if (candidate == null) {
                    continue;
                }

                if (!requiredPlotFacility.isBlank() && !containsIgnoreCase(candidate.getFacilities(), requiredPlotFacility)) {
                    continue;
                }

                if (bestPlot == null || candidate.getCost() < bestPlot.getCost()) {
                    bestCampsite = campsite;
                    bestArea = area;
                    bestPlot = candidate;
                }
            }
        }

        if (bestPlot == null) {
            System.out.println("No suitable holiday was found.");
            return;
        }

        System.out.println("\nSuitable holiday found:");
        System.out.println("Campsite: " + bestCampsite.getName());
        System.out.println("Camping area: " + bestArea.getCampingAreaID());
        System.out.println("Plot: " + bestPlot.getPlotID());
        System.out.println("Plot name: " + bestPlot.getName());
        System.out.println("Cost per night: €" + bestPlot.getCost());

        long nights = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);

        System.out.println("Estimated total: €" + (nights * bestPlot.getCost()));

        boolean createBooking = readBoolean("Create this booking? (yes/no): ");

        if (createBooking) {
            Booking booking = bestCampsite.createBooking(nextBookingID, bestArea.getCampingAreaID(), guest, startDate, endDate, plotType, numberOfGuests, powerRequired, minimumSize, minimumBedrooms, maximumBudget);

            if (booking != null) {
                nextBookingID++;
                System.out.println("Smart-find booking created.");
                System.out.println(booking);
            }
        }
    }

    private boolean containsIgnoreCase(String text, String searchText) {
        if (text == null || searchText == null) {
            return false;
        }
        return text.toLowerCase().contains(searchText.toLowerCase());
    }

    // --------------------------------------------------
    // Plot removal
    // --------------------------------------------------

    private void removePlot() {
        System.out.println("\nREMOVE PLOT");
        Campsite campsite = selectCampsite();

        if (campsite == null) {
            return;
        }

        CampingArea area = selectCampingArea(campsite);

        if (area == null) {
            return;
        }

        String plotID = readRequiredString("Plot ID to remove: ");

        boolean removed = campsite.removePlotAndReassignBookings(area.getCampingAreaID(), plotID);

        if (removed) {
            System.out.println("Plot removed successfully.");
        }
        else {
            System.out.println("Plot could not be removed. One or more bookings may not have a suitable replacement.");
        }
    }

    // --------------------------------------------------
    // Reset system
    // --------------------------------------------------

    private void resetSystem() {
        boolean resetConfirmed = readBoolean("Clear all system data? (yes/no): ");

        if (!resetConfirmed) {
            System.out.println("Reset cancelled.");
            return;
        }

        campsites.clear();
        guests.clear();
        nextBookingID = 1;
        System.out.println("All system data has been cleared.");
    }

    // --------------------------------------------------
    // Selection helpers
    // --------------------------------------------------

    private Campsite selectCampsite() {
        if (campsites.isEmpty()) {
            System.out.println("No campsites have been added.");
            return null;
        }

        System.out.println("\nAvailable campsites:");

        for (int i = 0; i < campsites.size(); i++) {
            Campsite campsite = campsites.get(i);

            System.out.println((i + 1) + ". " + campsite.getName());
        }

        String campsiteName = readRequiredString("Enter campsite name: ");
        Campsite campsite = findCampsite(campsiteName);

        if (campsite == null) {
            System.out.println("Campsite not found.");
        }
        return campsite;
    }

    // --------------------------------------------------
    // Input helpers
    // --------------------------------------------------

    private String readString(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    private String readRequiredString(String prompt) {
        while (true) {
            String value = readString(prompt);

            if (!value.isBlank()) {
                return value;
            }
            System.out.println("A value must be entered.");
        }
    }

    private int readInteger(String prompt) {
        while (true) {
            String value = readString(prompt);

            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException exception) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private int readPositiveInteger(String prompt) {
        while (true) {
            int value = readInteger(prompt);

            if (value > 0) {
                return value;
            }
            System.out.println("The value must be greater than zero.");
        }
    }

    private int readNonNegativeInteger(String prompt) {
        while (true) {
            int value = readInteger(prompt);

            if (value >= 0) {
                return value;
            }
            System.out.println("The value cannot be negative.");
        }
    }

    private boolean readBoolean(String prompt) {
        while (true) {
            String value = readString(prompt);

            if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("true")) {
                return true;
            }

            if (value.equalsIgnoreCase("no") || value.equalsIgnoreCase("n") || value.equalsIgnoreCase("false")) {
                return false;
            }
            System.out.println("Please enter yes or no.");
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String value = readString(prompt + " (YYYY-MM-DD): ");

            try {
                return LocalDate.parse(value);
            }
            catch (DateTimeParseException exception) {
                System.out.println("Enter the date in YYYY-MM-DD format."
                );
            }
        }
    }
}