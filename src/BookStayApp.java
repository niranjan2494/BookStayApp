import java.util.*;

/**
 * BookStayApp
 * Main application entry point.
 */
public class BookStayApp {

    public static void main(String[] args) {

        // Initialize services
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Guests submit booking requests
        requestQueue.addRequest(new Reservation("Arun", "Single Room"));
        requestQueue.addRequest(new Reservation("Meena", "Double Room"));
        requestQueue.addRequest(new Reservation("Rahul", "Single Room"));

        System.out.println("\nProcessing Booking Requests...\n");

        // Process queue in FIFO order
        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.getNextRequest();
            bookingService.processReservation(reservation);
        }

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();
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
}

/**
 * Queue that stores booking requests (FIFO).
 */
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Request received from " + reservation.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO retrieval
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Inventory Service
 * Maintains room availability.
 */
class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int current = inventory.get(roomType);
        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " rooms available");
        }
    }
}

/**
 * BookingService handles room allocation safely.
 */
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs
    private HashSet<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> assigned room IDs
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    // Room ID counter
    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processReservation(Reservation reservation) {

        String roomType = reservation.getRoomType();

        // Check availability
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("No rooms available for " + reservation.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Ensure uniqueness using Set
        allocatedRoomIds.add(roomId);

        // Track room allocation by type
        roomAllocations.putIfAbsent(roomType, new HashSet<>());
        roomAllocations.get(roomType).add(roomId);

        // Update inventory immediately
        inventory.decrementRoom(roomType);

        // Confirm reservation
        System.out.println("Reservation confirmed for " + reservation.getGuestName()
                + " | Room Type: " + roomType
                + " | Assigned Room ID: " + roomId);
    }

    private String generateRoomId(String roomType) {

        String prefix = roomType.replace(" ", "").substring(0, 2).toUpperCase();
        String roomId;

        do {
            roomId = prefix + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}