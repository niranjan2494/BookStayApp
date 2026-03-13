/**
 * BookStayApp
 * Entry point of the BookStay Hotel Booking application.
 */
public class BookStayApp {

    public static void main(String[] args) {

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("Welcome to BookStay");
        System.out.println("Version 1.0\n");

        single.displayRoomDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable);
    }
}

/**
 * Abstract class representing a room
 */
abstract class Room {

    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public void displayRoomDetails() {
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