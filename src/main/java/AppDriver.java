import javafx.application.Application;
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

}

//ABSTRACT CLASSES Campsite, CampingArea, Plot, Booking, Guest, etc?

/*
In short, there can be many campsites, a campsite can have many
camping areas, a camping area can have many plots/pitches,
a booking is made for a guest for a specific plot/pitch, and
an appointment will create a single vacation record for that guest on completion.
A guest can have many bookings (complete and incomplete).
 */

/*
Interactively “drill down” through campsites, camping areas,
plots/pitches, and bookings using an appropriate GUI
 */

/*
A “smart find” facility for camping holidays. Given appropriate guest information (including number of guests,
budget, min stars, desired dates, required/desired facilities (e.g. pool and air conditioning), etc.)
the system will identify a suitable campsite, camping area, plot/pitch and date to make a booking.
 */

//Reset facility. Clears all system data

//Save and load the entire system data: XML

//You should implement a suitable JavaFX graphical user interface to interact with your system.

//You should implement a set of JUnit classes to test code

