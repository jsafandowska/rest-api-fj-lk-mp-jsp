package pl.kurs.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.Main;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Book;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.service.BookIdGenerator;
import pl.kurs.service.CarIdGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private ObjectMapper objectMapper;

    @SpyBean
    private CarIdGenerator carIdGenerator;

    @Test
    public void shouldReturnSingleCar() throws Exception {
        Car car = new Car(carIdGenerator.getId(), "BMW", "M5", "petrol");
        cars.add(car);

        postman.perform(get("/api/v1/cars/"+car.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.model").value("M5"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));
    }

    @Test
    public void shouldAddCar() throws Exception {
        CreateCarCommand command = new CreateCarCommand("BMW", "M5", "petrol");
        String json = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.model").value("M5"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));

        Car recentlyAdded = cars.get(cars.size() - 1);
        Assertions.assertEquals("BMW", recentlyAdded.getBrand());
        Assertions.assertEquals("M5", recentlyAdded.getModel());
        Assertions.assertEquals("petrol", recentlyAdded.getFuelType());
        Assertions.assertTrue(recentlyAdded.getId() > 0);
        Mockito.verify(carIdGenerator, Mockito.times(1)).getId();
    }

    @Test
    public void shouldFindCar() throws Exception {
        Car car = new Car(carIdGenerator.getId(), "BMW", "M5", "petrol");
        cars.add(car);

        postman.perform(get("/api/v1/cars/"+car.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.model").value("M5"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        Car car = new Car(carIdGenerator.getId(), "BMW", "M5", "petrol");
        cars.add(car);
        postman.perform(delete("/api/v1/cars/"+car.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(cars.stream().anyMatch(c -> c.getId() == car.getId()));
    }

    @Test
    public void shouldEditCar() throws Exception {
        Car car = new Car(carIdGenerator.getId(), "BMW", "M5", "petrol");
        cars.add(car);
        EditCarCommand command = new EditCarCommand("Mercedes", "E63", "petrol");
        String json = objectMapper.writeValueAsString(command);

        postman.perform(put("/api/v1/cars/"+car.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("Mercedes"))
                .andExpect(jsonPath("$.model").value("E63"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));

        Car updatedCar = cars.stream().filter(c -> c.getId() == car.getId()).findFirst().orElseThrow(CarNotFoundException::new);
        Assertions.assertEquals("Mercedes", updatedCar.getBrand());
        Assertions.assertEquals("E63", updatedCar.getModel());
        Assertions.assertEquals("petrol", updatedCar.getFuelType());
    }

    @Test
    public void shouldPartiallyEditCar() throws Exception {
        Car car = new Car(carIdGenerator.getId(), "BMW", "M5", "petrol");
        cars.add(car);
        EditCarCommand command = new EditCarCommand(null, "M3", null);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/cars/"+car.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.model").value("M3"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));
        Car updatedCar = cars.stream().filter(c -> c.getId() == car.getId()).findFirst().orElseThrow(CarNotFoundException::new);
        Assertions.assertEquals("BMW", updatedCar.getBrand());
        Assertions.assertEquals("M3", updatedCar.getModel());
        Assertions.assertEquals("petrol", updatedCar.getFuelType());

    }
}