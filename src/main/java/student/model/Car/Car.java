package student.model.Car;

import java.math.BigDecimal;
import java.util.Objects;

public class Car {
    private String regNumber;
    private BigDecimal rentalPricePerDay;
    private Brand brand;
    private boolean isElectric;
    private String model;

    public Car(String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean isElectric, String model) {
        this.regNumber = regNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.brand = brand;
        this.isElectric = isElectric;
        this.model = model;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public Brand getBrand() {
        return brand;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return isElectric == car.isElectric &&
                Objects.equals(regNumber, car.regNumber) &&
                Objects.equals(rentalPricePerDay, car.rentalPricePerDay) &&
                brand == car.brand &&
                Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, rentalPricePerDay, brand, isElectric, model);
    }

    @Override
    public String toString() {
        return String.format("%s %s [%s] - $%s/day (%s)",
                brand,
                model,
                regNumber,
                rentalPricePerDay,
                isElectric ? "Electric" : "Gas"
        );
    }

}
