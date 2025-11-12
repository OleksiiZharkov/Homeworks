package hw13;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AirlineBookingSystem {
    public static void main(String[] args) {
        ConsoleInterface console = new ConsoleInterface();
        console.start();
    }
}

class ConsoleInterface {
    private final FlightController flightController;
    private final BookingController bookingController;
    private final Scanner scanner;

    public ConsoleInterface() {
        this.flightController = new FlightController();
        this.bookingController = new BookingController(flightController);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            printMainMenu();
            int choice = getIntInput("Оберіть пункт меню: ");

            switch (choice) {
                case 1 -> showOnlineBoard();
                case 2 -> showFlightInfo();
                case 3 -> searchAndBookFlight();
                case 4 -> cancelBooking();
                case 5 -> showMyFlights();
                case 6 -> {
                    System.out.println("Дякуємо за використання нашої системи!");
                    return;
                }
                default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\nГоловне меню:");
        System.out.println("1. Онлайн-табло");
        System.out.println("2. Інформація про рейс");
        System.out.println("3. Пошук та бронювання рейсу");
        System.out.println("4. Скасувати бронювання");
        System.out.println("5. Мої рейси");
        System.out.println("6. Вихід");
    }

    private void showOnlineBoard() {
        System.out.println("\nОнлайн-табло (рейси з Києва на найближчі 24 години):");
        List<Flight> flights = flightController.getFlightsInNext24Hours();
        flights.forEach(System.out::println);
    }

    private void showFlightInfo() {
        String flightId = getStringInput("Введіть ID рейсу: ");
        Optional<Flight> flight = flightController.getFlightById(flightId);

        if (flight.isPresent()) {
            System.out.println("\nІнформація про рейс:");
            System.out.println(flight.get().getDetailedInfo());
        } else {
            System.out.println("Рейс з ID " + flightId + " не знайдено.");
        }
    }

    private void searchAndBookFlight() {
        String destination = getStringInput("Введіть місце призначення: ");
        String date = getStringInput("Введіть дату (yyyy-MM-dd): ");
        int passengers = getIntInput("Введіть кількість пасажирів: ");

        List<Flight> foundFlights = flightController.searchFlights(destination, date, passengers);

        if (foundFlights.isEmpty()) {
            System.out.println("Рейсів за вашими критеріями не знайдено.");
            return;
        }

        System.out.println("\nЗнайдені рейси:");
        for (int i = 0; i < foundFlights.size(); i++) {
            System.out.println((i + 1) + ". " + foundFlights.get(i));
        }

        int choice = getIntInput("Оберіть рейс для бронювання (0 - повернутися): ");
        if (choice == 0) return;
        if (choice < 1 || choice > foundFlights.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        Flight selectedFlight = foundFlights.get(choice - 1);
        List<Passenger> passengersList = new ArrayList<>();

        for (int i = 0; i < passengers; i++) {
            System.out.println("\nВведіть дані пасажира #" + (i + 1));
            String firstName = getStringInput("Ім'я: ");
            String lastName = getStringInput("Прізвище: ");
            passengersList.add(new Passenger(firstName, lastName));
        }

        String bookingId = bookingController.bookFlight(selectedFlight, passengersList);
        System.out.println("Бронювання успішно створено. ID бронювання: " + bookingId);
    }

    private void cancelBooking() {
        String bookingId = getStringInput("Введіть ID бронювання для скасування: ");
        boolean success = bookingController.cancelBooking(bookingId);

        if (success) {
            System.out.println("Бронювання успішно скасовано.");
        } else {
            System.out.println("Бронювання з ID " + bookingId + " не знайдено.");
        }
    }

    private void showMyFlights() {
        String firstName = getStringInput("Введіть ваше ім'я: ");
        String lastName = getStringInput("Введіть ваше прізвище: ");

        List<Booking> bookings = bookingController.getUserBookings(firstName, lastName);

        if (bookings.isEmpty()) {
            System.out.println("У вас немає активних бронювань.");
            return;
        }

        System.out.println("\nВаші бронювання:");
        bookings.forEach(booking -> {
            System.out.println("ID бронювання: " + booking.getId());
            System.out.println("Рейс: " + booking.getFlight().getDetailedInfo());
            System.out.println("Пасажири: " + booking.getPassengers());
            System.out.println();
        });
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число.");
            }
        }
    }
}

class FlightController {
    private final FlightService flightService;

    public FlightController() {
        this.flightService = new FlightService();
    }

    public List<Flight> getFlightsInNext24Hours() {
        return flightService.getFlightsInNext24Hours();
    }

    public Optional<Flight> getFlightById(String id) {
        return flightService.getFlightById(id);
    }

    public List<Flight> searchFlights(String destination, String date, int passengers) {
        return flightService.searchFlights(destination, date, passengers);
    }
}

class FlightService {
    private final FlightDao flightDao;

    public FlightService() {
        this.flightDao = new FlightDao();
    }

    public List<Flight> getFlightsInNext24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusHours(24);
        return flightDao.getAllFlights().stream()
                .filter(f -> {
                    assert f.getDepartureTime() != null;
                    return f.getDepartureTime().isAfter(ChronoLocalDateTime.from(Instant.from(now))) && f.getDepartureTime().isBefore(ChronoLocalDateTime.from(Instant.from(tomorrow)));
                })
                .toList();
    }

    public Optional<Flight> getFlightById(String id) {
        return flightDao.getAllFlights().stream()
                .filter(f -> {
                    assert f.getId() != null;
                    return f.getId().equals(id);
                })
                .findFirst();
    }

    public List<Flight> searchFlights(String destination, String date, int passengers) {
        return flightDao.getAllFlights().stream()
                .filter(f -> {
                    assert f.getDestination() != null;
                    return f.getDestination().equalsIgnoreCase(destination);
                })
                .filter(f -> f.getDepartureTime().format(DateTimeFormatter.ISO_LOCAL_DATE).equals(date))
                .filter(f -> f.getAvailableSeats() >= passengers)
                .toList();
    }
}

class FlightDao {
    private final List<Flight> flights;

    public FlightDao() {
        this.flights = generateSampleFlights();
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    private List<Flight> generateSampleFlights() {
        List<Flight> sampleFlights = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        String[] destinations = {"Лондон", "Париж", "Берлін", "Рим", "Мадрид", "Варшава"};

        for (int i = 0; i < 20; i++) {
            String id = "FL" + (100 + i);
            String destination = destinations[i % destinations.length];
            LocalDateTime departureTime = now.plusHours(i * 2).plusMinutes(i * 15);
            int totalSeats = 150 + (i * 10) % 100;
            int availableSeats = totalSeats - (i * 5) % 50;

            sampleFlights.add(new Flight(id, "Київ", destination, departureTime, totalSeats, availableSeats));
        }

        return sampleFlights;
    }
}

class BookingController {
    private final BookingService bookingService;

    public BookingController(FlightController ignoredFlightController) {
        this.bookingService = new BookingService();
    }

    public String bookFlight(Flight flight, List<Passenger> passengers) {
        return bookingService.createBooking(flight, passengers);
    }

    public boolean cancelBooking(String id) {
        return bookingService.cancelBooking(id);
    }

    public List<Booking> getUserBookings(String firstName, String lastName) {
        return bookingService.getUserBookings(firstName, lastName);
    }
}

class BookingService {
    private final BookingDao bookingDao;

    public BookingService() {
        this.bookingDao = new BookingDao();
    }

    public String createBooking(Flight flight, List<Passenger> passengers) {
        String bookingId = "BK" + System.currentTimeMillis();
        Booking booking = new Booking(bookingId, flight, passengers);
        bookingDao.saveBooking(booking);
        return bookingId;
    }

    public boolean cancelBooking(String id) {
        return bookingDao.deleteBooking(id);
    }

    public List<Booking> getUserBookings(String firstName, String lastName) {
        return bookingDao.getAllBookings().stream()
                .filter(b -> b.getPassengers().stream()
                        .anyMatch(p -> {
                            assert p.getFirstName() != null;
                            if (!p.getFirstName().equalsIgnoreCase(firstName)) return false;
                            assert p.getLastName() != null;
                            return p.getLastName().equalsIgnoreCase(lastName);
                        }))
                .toList();
    }
}

class BookingDao {
    private final Map<String, Booking> bookings = new HashMap<>();

    public void saveBooking(Booking booking) {
        bookings.put(booking.getId(), booking);
    }

    public boolean deleteBooking(String id) {
        return bookings.remove(id) != null;
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }
}

record Flight(String id, String origin, String destination, LocalDateTime departureTime,
              int totalSeats, int availableSeats) {

    public String getDetailedInfo() {
        return String.format("Рейс %s: %s -> %s, %s, Місць: %d/%d",
                id, origin, destination,
                departureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                availableSeats, totalSeats);
    }

    @Override
    public String toString() {
        return String.format("%s: %s -> %s, %s",
                id, origin, destination,
                departureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}

record Passenger(String firstName, String lastName) {
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return null;
    }

    public String getLastName() {
        return null;
    }
}

class Booking {
    private final String id;
    private final Flight flight;
    private final List<Passenger> passengers;
    public LocalDateTime bookingTime;

    public Booking(String id, Flight flight, List<Passenger> passengers) {
        this.id = id;
        this.flight = flight;
        this.passengers = new ArrayList<>(passengers);
        this.bookingTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

}