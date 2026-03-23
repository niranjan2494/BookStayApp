import java.util.*;

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        String reservationId = "RES123";

        // Create add-on services
        AddOnService breakfast = new AddOnService("S1", "Breakfast", 500);
        AddOnService airportPickup = new AddOnService("S2", "Airport Pickup", 1200);
        AddOnService spa = new AddOnService("S3", "Spa Access", 2000);

        // Guest selects services
        serviceManager.addService(reservationId, breakfast);
        serviceManager.addService(reservationId, airportPickup);
        serviceManager.addService(reservationId, spa);

        // Display services
        serviceManager.printServices(reservationId);

        // Calculate total add-on cost
        double totalCost = serviceManager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);

        // NOTE: Core booking & inventory logic remains unchanged
    }
}

// Add-On Service Model
class AddOnService {
    private String serviceId;
    private String name;
    private double price;

    public AddOnService(String serviceId, String name, double price) {
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " (₹" + price + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // reservationId -> list of services
    private Map<String, List<AddOnService>> reservationServicesMap = new HashMap<>();

    // Add service
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0;
        for (AddOnService service : getServices(reservationId)) {
            total += service.getPrice();
        }
        return total;
    }

    // Print services
    public void printServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services:");
        for (AddOnService service : services) {
            System.out.println(" - " + service);
        }
    }
}