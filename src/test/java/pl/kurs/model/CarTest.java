package pl.kurs.model;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kurs.exceptions.TheGarageDoesNotAllowParkingLPGCars;
import pl.kurs.exceptions.TheGarageIsFull;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarTest {
    private Car car;
    private Garage garage;
    private Garage garage2;

    @BeforeEach
    public void init() {
        garage = new Garage(1, "Łęgska", false);
        garage2 = new Garage(1,"Zielna",false);
        car = new Car("Mercedes", "CLS", "petrol", garage);
    }

    @Test
    public void shouldThrowTheGarageIsFullException() {
        Exception e = assertThrows(TheGarageIsFull.class, () -> {
            new Car("Audi", "RS5", "petrol", garage);
        });
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(e).isInstanceOf(TheGarageIsFull.class);
        softAssertions.assertAll();
    }
    @Test
    public void shouldThrowTheGarageDoesNotAllowParkingLPGCarsException() {
        Exception e = assertThrows(TheGarageDoesNotAllowParkingLPGCars.class, () -> {
            new Car("Audi", "RS5", "lpg", garage2);
        });
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(e).isInstanceOf(TheGarageDoesNotAllowParkingLPGCars.class);
        softAssertions.assertAll();
    }
}
