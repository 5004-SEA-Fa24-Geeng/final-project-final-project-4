package student.model.Car;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class CarFileRepository implements CarRepository {

    private static final String CAR_FILE_PATH = "data/cars.csv";
    private final List<Car> cars;

    public CarFileRepository() {
        this.cars = loadCarsFromFile();
    }

    @Override
    public List<Car> getAllCars() {
        return cars;
    }

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
