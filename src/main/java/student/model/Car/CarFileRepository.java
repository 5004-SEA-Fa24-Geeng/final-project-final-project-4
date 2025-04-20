package student.model.Car;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Implementation of {@link CarRepository} that loads car data from a CSV file.
 * <p>
 * This class reads car information from {@code data/cars.csv} during construction,
 * and stores all loaded {@link Car} objects in memory.
 * It supports reading brand, registration number, price, electric status, and model.
 * </p>
 *
 * <p><strong>Note:</strong> This class does not support saving changes back to the file.</p>
 */
public class CarFileRepository implements CarRepository {

    /**
     * The relative file path to the car data CSV file.
     */
    private static final String CAR_FILE_PATH = "data/cars.csv";

    /**
     * The in-memory list of all cars loaded from the CSV file.
     */
    private final List<Car> cars;

    /**
     * Constructs a new {@code CarFileRepository} and loads car data from file.
     * If the file does not exist or fails to parse, the internal list will be empty.
     */
    public CarFileRepository() {
        this.cars = loadCarsFromFile();
    }

    /**
     * Returns the list of all cars loaded from the file.
     *
     * @return a list of {@link Car} objects
     */
    @Override
    public List<Car> getAllCars() {
        return cars;
    }

    /**
     * Loads cars from the {@code data/cars.csv} file and constructs {@link Car} objects.
     * <p>
     * Expected CSV format: regNumber,brand,price,isElectric,model
     * </p>
     *
     * @return a list of cars parsed from the file; empty if the file is missing or invalid
     * @throws IllegalStateException if the file cannot be read
     */
    private List<Car> loadCarsFromFile() {
        List<Car> list = new ArrayList<>();
        File file = new File(CAR_FILE_PATH);

        if (!file.exists()) {
            System.err.println("⚠️ cars.csv not found at " + CAR_FILE_PATH);
            return list;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(",");
                if (split.length < 5) continue;

                String regNumber = split[0];
                Brand brand = Brand.valueOf(split[1]);
                BigDecimal price = new BigDecimal(split[2]);
                boolean isElectric = Boolean.parseBoolean(split[3]);
                String model = split[4];

                list.add(new Car(regNumber, price, brand, isElectric, model));
            }
        } catch (IOException e) {
            throw new IllegalStateException("❌ Failed to read cars.csv", e);
        }

        return list;
    }
}
