import java.io.*;
import java.util.*;

/*
 BookMyStayApp
 Demonstrates persistence and recovery of booking and inventory state.
 */

public class BookMyStayApp {

    private static final String DATA_FILE = "bookmystay_state.dat";

    private Map<String, Integer> inventory = new HashMap<>();
    private List<String> bookings = new ArrayList<>();

    private PersistenceService persistenceService = new PersistenceService();

    public static void main(String[] args) {

        BookMyStayApp app = new BookMyStayApp();

        // System startup
        app.restoreState();

        // Simulate system operations
        app.addRoomType("Deluxe", 10);
        app.addRoomType("Suite", 5);

        app.bookRoom("Deluxe");
        app.bookRoom("Suite");

        // System shutdown
        app.saveState();

        System.out.println("System shut down safely with persisted state.");
    }

    // ---------------------------
    // Business Logic
    // ---------------------------

    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + count);
        System.out.println("Added " + count + " rooms to " + roomType);
    }

    public void bookRoom(String roomType) {

        if (!inventory.containsKey(roomType)) {
            System.out.println("Room type does not exist.");
            return;
        }

        int available = inventory.get(roomType);

        if (available <= 0) {
            System.out.println("No rooms available for " + roomType);
            return;
        }

        inventory.put(roomType, available - 1);
        bookings.add(roomType);

        System.out.println("Booking successful for " + roomType);
    }

    // ---------------------------
    // Persistence Operations
    // ---------------------------

    public void saveState() {

        System.out.println("Preparing system for shutdown...");

        SystemState state = new SystemState(inventory, bookings);

        persistenceService.save(DATA_FILE, state);

        System.out.println("State successfully persisted.");
    }

    public void restoreState() {

        System.out.println("System starting... Restoring state.");

        SystemState state = persistenceService.load(DATA_FILE);

        if (state != null) {
            inventory = state.inventory;
            bookings = state.bookings;
            System.out.println("State restored successfully.");
        } else {
            System.out.println("No valid persisted state found. Starting fresh.");
        }
    }

}

/*
 Serializable snapshot of application state
 */

class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<String> bookings;

    public SystemState(Map<String, Integer> inventory, List<String> bookings) {
        this.inventory = new HashMap<>(inventory);
        this.bookings = new ArrayList<>(bookings);
    }
}

/*
 Persistence Service
 Handles file-based persistence
 */

class PersistenceService {

    public void save(String fileName, SystemState state) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {

            out.writeObject(state);

        } catch (IOException e) {
            System.out.println("Failed to persist state: " + e.getMessage());
        }
    }

    public SystemState load(String fileName) {

        File file = new File(fileName);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (SystemState) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Persistence file corrupted or unreadable.");
            return null;
        }
    }
}