package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.service.CarIdGenerator;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class CarControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private List<Car> cars;
    @Autowired
    private ObjectMapper obj;
    @SpyBean
    private CarIdGenerator idGenerator;


    @Test
    public void shouldReturnSingleCar() throws Exception {
        Car car = new Car(idGenerator.getId(), "Audi", "RS5", "petrol");
        cars.add(car);
//        cars.add(new Car(idGenerator.getId(), "Audi", "RS5", "petrol"));
        postman.perform(get("/api/v1/cars/"+car.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("Audi"))
                .andExpect(jsonPath("$.model").value("RS5"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));
    }


    @Test
    public void shouldAddCar() throws Exception {
        CreateCarCommand command = new CreateCarCommand("Audi", "RS5", "petrol");
        String json = obj.writeValueAsString(command);

        postman.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Audi"))
                .andExpect(jsonPath("$.model").value("RS5"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));

        Car recentlyAdded = cars.get(cars.size() - 1);
        Assertions.assertEquals("Audi", recentlyAdded.getBrand());
        Assertions.assertEquals("RS5", recentlyAdded.getModel());
        Assertions.assertEquals("petrol", recentlyAdded.getFuelType());
        Assertions.assertTrue(recentlyAdded.getId() > 0);

        Mockito.verify(idGenerator, Mockito.times(1)).getId();
    }

    //
    @Test
    public void shouldDeleteCar() throws Exception {

        Car carToDelete = new Car(idGenerator.getId(), "Mercedes", "C63", "petrol");
        cars.add(carToDelete);

        postman.perform(delete("/api/v1/cars/" + carToDelete.getId()))
                .andExpect(status().isNoContent());

        boolean carExists = cars.stream().anyMatch(x -> x.getId() == carToDelete.getId());
        Assertions.assertFalse(carExists, "The car should be deleted from the list");
    }

    @Test
    public void shouldEditCar() throws Exception {
        Car carToDelete = new Car(idGenerator.getId(), "Mercedes", "C63", "petrol");
        cars.add(carToDelete);

        CreateCarCommand command = new CreateCarCommand("BMW", "M6", "petrol");
        String json = obj.writeValueAsString(command);

        postman.perform(put("/api/v1/cars/" + carToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.model").value("M6"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));

        Car createdCar = cars.stream().filter(x -> x.getId() == carToDelete.getId()).findFirst().orElse(null);
        Assertions.assertNotNull(createdCar, "The car should exist in the list");
        Assertions.assertEquals("BMW", createdCar.getBrand());
        Assertions.assertEquals("M6", createdCar.getModel());
        Assertions.assertEquals("petrol", createdCar.getFuelType());
    }

    @Test
    public void shouldEditCarPartially() throws Exception {
        Car carToDelete = new Car(idGenerator.getId(), "Mercedes", "C63", "petrol");
        cars.add(carToDelete);

        CreateCarCommand command = new CreateCarCommand(null, "M6", null);
        String json = obj.writeValueAsString(command);

        postman.perform(patch("/api/v1/cars/" + carToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Mercedes"))
                .andExpect(jsonPath("$.model").value("M6"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));

        Car createdCar = cars.stream().filter(x -> x.getId() == carToDelete.getId()).findFirst().orElse(null);
        Assertions.assertNotNull(createdCar, "The car should exist in the list");
        Assertions.assertEquals("Mercedes", createdCar.getBrand());
        Assertions.assertEquals("M6", createdCar.getModel());
        Assertions.assertEquals("petrol", createdCar.getFuelType());

    }
}
