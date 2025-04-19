package student.model.Car;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a car available for rental in the Car Rental System.
 * <p>
 * A {@code Car} contains key attributes including registration number,
 * daily rental price, brand, model, and whether it is electric.
 * </p>
 */
public class Car {

    /**
     * The unique registration number of the car.
     */
    private String regNumber;

    /**
     * The rental price of the car per day.
     */
    private BigDecimal rentalPricePerDay;

    /**
     * The brand of the car (e.g., TESLA, AUDI).
     */
    private Brand brand;

    /**
     * Indicates whether the car is electric.
     */
    private boolean isElectric;

    /**
     * The model name of the car.
     */
    private String model;

    /**
     * Constructs a new {@code Car} with the specified attributes.
     *
     * @param regNumber         The unique registration number.
     * @param rentalPricePerDay The rental price per day.
     * @param brand             The brand of the car.
     * @param isElectric        {@code true} if the car is electric, {@code false} otherwise.
     * @param model             The model name.
     */
    public Car(String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean isElectric, String model) {
        this.regNumber = regNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.brand = brand;
        this.isElectric = isElectric;
        this.model = model;
    }

    /**
     * Returns the registration number of the car.
     *
     * @return the registration number
     */
    public String getRegNumber() {
        return regNumber;
    }

    /**
     * Returns the daily rental price of the car.
     *
     * @return the rental price per day
     */
    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    /**
     * Returns the brand of the car.
     *
     * @return the car brand
     */
    public Brand getBrand() {
        return brand;
    }

    /**
     * Returns whether the car is electric.
     *
     * @return {@code true} if electric, {@code false} otherwise
     */
    public boolean isElectric() {
        return isElectric;
    }

    /**
     * Returns the model name of the car.
     *
     * @return the model name
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model name of the car.
     *
     * @param model the new model name
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Compares this car to another object for equality based on all fields.
     *
     * @param o the object to compare
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
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

    /**
     * Computes the hash code based on the car's fields.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(regNumber, rentalPricePerDay, brand, isElectric, model);
    }

    /**
     * Returns a string representation of the car in a human-readable format.
     *
     * @return a string describing the car
     */
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
