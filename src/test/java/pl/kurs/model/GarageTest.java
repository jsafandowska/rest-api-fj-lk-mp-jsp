package pl.kurs.model;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GarageTest {

    private Garage garage;
    private Car car1;
    private Car car2;

    @BeforeEach
    public void init() {
        garage = new Garage(2, "Jabłoniowa", false);
        car1 = new Car("Audi", "A5", "petrol");
        car2 = new Car("Audi", "A3", "petrol");
    }
    @Test
    void addCar() {
        garage.addCar(car1);
        garage.addCar(car2);
        assertEquals(2, garage.getCars().size());
    }
    @Test
    public void shouldThrowNewIllegalStateExceptionWhenAddingNotAllowedLPGCar() {
        Exception e = assertThrows(IllegalStateException.class, () -> garage.addCar(new Car("BMW", "M3", "LPG")));
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(e.getClass().equals(IllegalStateException.class));
        softAssertions.assertThat(e.getMessage().equals("GARAGE_NOT_ACCEPT_LPG"));
        softAssertions.assertAll();
    }
    @Test
    public void shouldThrowNewIllegalStateArgumentExceptionWhenAddingCarToFullGarage() {
        garage.addCar(car1);
        garage.addCar(new Car("XXX", "Y", "petrol"));
        Exception e = assertThrows(IllegalStateException.class, () -> garage.addCar(new Car("XYZ", "Z", "petrol")));
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(e.getClass().equals(IllegalStateException.class));
        softAssertions.assertThat(e.getMessage().equals("GARAGE_IS_FULL"));
        softAssertions.assertAll();
    }
    @Test
    public void shouldDeleteCarWhenIsAlreadyInTheGarage() {
        garage.addCar(car1);
        garage.deleteCar(car1);
        assertFalse(garage.getCars().contains(car1));
    }
    @Test
    public void shouldThrowNowIllegalExceptionWhenCarIsNotInTheGarage() {
        Exception e = assertThrows(IllegalStateException.class, () -> garage.deleteCar(car1));
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(e.getClass().equals(IllegalStateException.class));
        softAssertions.assertThat(e.getMessage().equals("CAR_NOT_IN_THE_GARAGE"));
        softAssertions.assertAll();
    }
}
