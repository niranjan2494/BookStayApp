import java.util.*;

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingService bookingService = new BookingService();
        CancellationService cancellationService = new CancellationService(bookingService);

        try {
            // Create bookings
            bookingService.bookRoom("RES301", "Alice", "DELUXE");
            bookingService.bookRoom("RES302", "Bob", "STANDARD");

            // Cancel booking
            cancellationService.cancelBooking("RES301");

            // Try cancelling again (should fail)
            cancellationService.cancelBooking("RES301");

        } catch (BookingException e) {
            System.out.println("Operation Failed: " + e.getMessage());
        }

        System.out.println("\nSystem continues running...");
    }
}

// Reservation Model
class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;
    boolean isActive;

    public Reservation(String reservationId, String guestName,
                       String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }
}

// Booking Service (Handles booking + inventory)
class BookingService {

    Map<String, Integer> inventory = new HashMap<>();
    Map<String, Reservation> reservations = new HashMap<>();

    // Track allocated rooms per type
    Map<String, Stack<String>> allocatedRooms = new HashMap<>();

    public BookingService() {
        inventory.put("STANDARD", 2);
        inventory.put("DELUXE", 2);

        allocatedRooms.put("STANDARD", new Stack<>());
        allocatedRooms.put("DELUXE", new Stack<>());
    }

    public void bookRoom(String reservationId, String guestName, String roomType)
            throws BookingException {

        validateRoomType(roomType);

        if (inventory.get(roomType) <= 0) {
            throw new BookingException("No rooms available for type: " + roomType);
        }

        // Allocate room (generate ID)
        String roomId = roomType + "_" + (allocatedRooms.get(roomType).size() + 1);
        allocatedRooms.get(roomType).push(roomId);

        // Reduce inventory
        inventory.put(roomType, inventory.get(roomType) - 1);

        // Store reservation
        Reservation res = new Reservation(reservationId, guestName, roomType, roomId);
        reservations.put(reservationId, res);

        System.out.println("Booking Confirmed: " + reservationId + " | RoomID: " + roomId);
    }

    private void validateRoomType(String roomType) throws BookingException {
        if (!inventory.containsKey(roomType)) {
            throw new BookingException("Invalid room type: " + roomType);
        }
    }
}

// Cancellation Service (Rollback logic)
class CancellationService {

    private BookingService bookingService;

    public CancellationService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void cancelBooking(String reservationId) throws BookingException {

        // 🔹 Validate reservation existence
        Reservation res = bookingService.reservations.get(reservationId);

        if (res == null) {
            throw new BookingException("Reservation does not exist: " + reservationId);
        }

        // 🔹 Prevent duplicate cancellation
        if (!res.isActive) {
            throw new BookingException("Reservation already cancelled: " + reservationId);
        }

        String roomType = res.roomType;

        // 🔹 LIFO rollback using stack
        Stack<String> stack = bookingService.allocatedRooms.get(roomType);

        if (stack.isEmpty()) {
            throw new BookingException("No allocated rooms to rollback.");
        }

        String releasedRoomId = stack.pop();

        // 🔹 Controlled mutation (order matters)
        bookingService.inventory.put(roomType,
                bookingService.inventory.get(roomType) + 1);

        res.isActive = false;

        System.out.println("Booking Cancelled: " + reservationId +
                " | Released RoomID: " + releasedRoomId);
    }
}

// Custom Exception
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}