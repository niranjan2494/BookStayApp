import java.util.*;

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES101", "Alice", 2500);
        Reservation r2 = new Reservation("RES102", "Bob", 4000);
        Reservation r3 = new Reservation("RES103", "Charlie", 3200);

        // Booking confirmed → add to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin views all bookings
        System.out.println("=== Booking History ===");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }

        // Generate reports
        System.out.println("\n=== Booking Report ===");
        reportService.printSummary(history);
    }
}

// Reservation Model
class Reservation {
    private String reservationId;
    private String guestName;
    private double totalCost;

    public Reservation(String reservationId, String guestName, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.totalCost = totalCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Cost: ₹" + totalCost;
    }
}

// Booking History (Storage Layer)
class BookingHistory {

    // Maintains insertion order
    private List<Reservation> reservations = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations); // return copy (safe)
    }
}

// Reporting Service (Read-only logic)
class BookingReportService {

    // Generate summary report
    public void printSummary(BookingHistory history) {

        List<Reservation> reservations = history.getAllReservations();

        int totalBookings = reservations.size();
        double totalRevenue = 0;

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        if (totalBookings > 0) {
            System.out.println("Average Booking Value: ₹" + (totalRevenue / totalBookings));
        }
    }
}