import java.util.Queue;
import java.util.LinkedList;

/**
 * BookStayApp
 * Entry point of the BookStay Hotel Booking system.
 */
public class BookStayApp {

    public static void main(String[] args) {

        System.out.println("BookStay Booking Request System\n");

        // Initialize booking request queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Guests submit booking requests
        Reservation r1 = new Reservation("Arun", "Single Room");
        Reservation r2 = new Reservation("Meena", "Double Room");
        Reservation r3 = new Reservation("Rahul", "Suite Room");

        // Add requests to queue
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Display queued requests
        requestQueue.displayRequests();
    }
}

/**
 * Reservation represents a guest booking request.
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

/**
 * BookingRequestQueue manages booking requests using FIFO queue.
 */
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    /**
     * Add booking request to queue
     */
    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    /**
     * Display all queued booking requests
     */
    public void displayRequests() {
        System.out.println("\nCurrent Booking Request Queue:\n");

        for (Reservation r : queue) {
            r.displayReservation();
        }
    }
}