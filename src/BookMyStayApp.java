import java.util.*;

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor();

        // Simulate multiple guests (threads)
        Thread t1 = new Thread(() -> processor.processBooking("RES401", "Alice", "DELUXE"));
        Thread t2 = new Thread(() -> processor.processBooking("RES402", "Bob", "DELUXE"));
        Thread t3 = new Thread(() -> processor.processBooking("RES403", "Charlie", "DELUXE"));
        Thread t4 = new Thread(() -> processor.processBooking("RES404", "David", "DELUXE"));

        // Start threads concurrently
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}

// Booking Processor (Thread-safe)
class ConcurrentBookingProcessor {

    private Map<String, Integer> inventory = new HashMap<>();
    private Queue<BookingRequest> bookingQueue = new LinkedList<>();

    public ConcurrentBookingProcessor() {
        inventory.put("DELUXE", 2); // Only 2 rooms available
    }

    // Public method called by multiple threads
    public void processBooking(String reservationId, String guestName, String roomType) {

        BookingRequest request = new BookingRequest(reservationId, guestName, roomType);

        // Add request to shared queue (synchronized)
        synchronized (bookingQueue) {
            bookingQueue.add(request);
        }

        // Process request
        handleRequest();
    }

    // Critical section handling
    private void handleRequest() {

        BookingRequest request;

        // Safely retrieve request
        synchronized (bookingQueue) {
            if (bookingQueue.isEmpty()) return;
            request = bookingQueue.poll();
        }

        // 🔹 Critical section: inventory update
        synchronized (inventory) {

            int available = inventory.getOrDefault(request.roomType, 0);

            if (available > 0) {
                inventory.put(request.roomType, available - 1);

                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: " + request.reservationId +
                        " | Remaining: " + (available - 1));
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No rooms available for " + request.reservationId);
            }
        }
    }
}

// Booking Request Model
class BookingRequest {
    String reservationId;
    String guestName;
    String roomType;

    public BookingRequest(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}