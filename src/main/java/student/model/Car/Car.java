package student.model.Car;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a car available for rental.
 * <p>
 * Each car has a registration number, a daily rental price,
 * a brand, and a flag indicating whether it is electric.
 * </p>
 */
public class Car {
    private String regNumber;
    private BigDecimal rentalPricePerDay;
    private Brand brand;
    private boolean isElectric;

    /**
     * Constructs a new {@code Car} instance with the specified attributes.
     *
     * @param regNumber         the registration number of the car
     * @param rentalPricePerDay the rental price per day in {@link BigDecimal}
     * @param brand             the brand of the car
     * @param isElectric        {@code true} if the car is electric; {@code false} otherwise
     */
    public Car(String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean isElectric) {
        this.regNumber = regNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.brand = brand;
        this.isElectric = isElectric;
    }

    /**
     * Returns the registration number of the car.
     *
     * @return the car's registration number
     */
    public String getRegNumber() {
        return regNumber;
    }

    /**
     * Sets the registration number of the car.
     *
     * @param regNumber the new registration number
     */
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    /**
     * Returns the rental price per day for the car.
     *
     * @return the daily rental price
     */
    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    /**
     * Sets the rental price per day for the car.
     *
     * @param rentalPricePerDay the new daily rental price
     */
    public void setRentalPricePerDay(BigDecimal rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    /**
     * Returns the brand of the car.
     *
     * @return the {@link Brand} of the car
     */
    public Brand getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the car.
     *
     * @param brand the new car brand
     */
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    /**
     * Returns whether the car is electric.
     *
     * @return {@code true} if electric, otherwise {@code false}
     */
    public boolean isElectric() {
        return isElectric;
    }

    /**
     * Sets whether the car is electric.
     *
     * @param electric {@code true} to mark as electric, otherwise {@code false}
     */
    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    /**
     * Returns a string representation of the car.
     *
     * @return a string containing car details
     */
    @Override
    public String toString() {
        return "Car{" +
                "regNumber='" + regNumber + '\'' +
                ", rentalPricePerDay=" + rentalPricePerDay +
                ", brand=" + brand +
                ", isElectric=" + isElectric +
                '}';
    }

    /**
     * Checks if this car is equal to another object.
     * Two cars are considered equal if all their attributes are equal.
     *
     * @param o the object to compare with
     * @return {@code true} if equal, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;
        return isElectric == car.isElectric &&
                Objects.equals(regNumber, car.regNumber) &&
                Objects.equals(rentalPricePerDay, car.rentalPricePerDay) &&
                brand == car.brand;
    }

    /**
     * Returns the hash code for this car.
     *
     * @return the hash code computed from car attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(regNumber, rentalPricePerDay, brand, isElectric);
    }
}
