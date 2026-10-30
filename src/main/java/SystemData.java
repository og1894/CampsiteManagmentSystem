public class SystemData {
    private LinkedList<Campsite> campsites;
    private LinkedList<Guest> guests;
    private int nextBookingID;

    public SystemData(LinkedList<Campsite> campsites, LinkedList<Guest> guests, int nextBookingID) {

        if (campsites == null) {
            throw new IllegalArgumentException("Campsite list cannot be null.");
        }

        if (guests == null) {throw new IllegalArgumentException("Guest list cannot be null.");
        }

        this.campsites = campsites;
        this.guests = guests;
        this.nextBookingID = nextBookingID;
    }

    public LinkedList<Campsite> getCampsites() {
        return campsites;
    }

    public LinkedList<Guest> getGuests() {
        return guests;
    }

    public int getNextBookingID() {
        return nextBookingID;
    }
}
