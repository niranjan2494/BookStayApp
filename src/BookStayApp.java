import java.util.HashMap;
import java.util.Map;

/**
 * BookStayApp
 * Entry point of the BookStay Hotel Booking application.
 */
public class BookStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to BookStay Hotel Booking System\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room domain objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize search service
        SearchService searchService = new SearchService(inventory);

        // Guest searches for available rooms
        searchService.searchAvailableRooms(single, doubleRoom, suite);
    }
}

/**
 * Abstract Room class representing room characteristics
 */
abstract class Room {

    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: $" + price);
    }
}

/**
 * Single Room
 */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 80);
    }
}

/**
 * Double Room
 */
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 120);
    }
}

/**
 * Suite Room
 */
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 250);
    }
}

/**
 * Centralized Inventory using HashMap
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // example unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

/**
 * SearchService handles read-only search operations.
 */
class SearchService {

    private RoomInventory inventory;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room... rooms) {

        System.out.println("Available Rooms:\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getType());

            // Defensive check: show only available rooms
            if (available > 0) {

                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("----------------------");
            }
        }
    }
}