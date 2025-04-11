package student.model.Car;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

public class CarFileRepository implements CarRepository {

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
        InputStream is = getClass().getClassLoader().getResourceAsStream("cars.csv");
        if (is == null) {
            throw new RuntimeException("cars.csv not found in resources!");
        }

        try (Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(",");
                String regNumber = split[0];
                Brand brand = Brand.valueOf(split[1]);
                BigDecimal price = new BigDecimal(split[2]);
                boolean isElectric = Boolean.parseBoolean(split[3]);
                String model = split[4];
                list.add(new Car(regNumber, price, brand, isElectric, model));
            }
        }

        return list;
    }
}
