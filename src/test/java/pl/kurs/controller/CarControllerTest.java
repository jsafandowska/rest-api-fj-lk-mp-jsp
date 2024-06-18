package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.repository.CarRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class CarControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper obj;
    @Autowired
    private CarRepository carRepository;

    @Test
    public void shouldReturnSingleCar() throws Exception {
        Car car = carRepository.saveAndFlush(new Car("Audi", "RS5", "petrol"));
        postman.perform(get("/api/v1/cars/" + car.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.brand").value("Audi"))
                .andExpect(jsonPath("$.model").value("RS5"))
                .andExpect(jsonPath("$.fuelType").value("petrol"));
    }

    @Test
    public void shouldThrowExceptionWhenCarNotFound() throws Exception {
        int nonExistentCarId = 999;
        postman.perform(get("/api/v1/cars/" + nonExistentCarId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddCar() throws Exception {
        CreateCarCommand command = new CreateCarCommand("Audi", "RS5", "petrol");
        String json = obj.writeValueAsString(command);
        String responseString = postman.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Audi"))
                .andExpect(jsonPath("$.model").value("RS5"))
                .andExpect(jsonPath("$.fuelType").value("petrol"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Car saved = obj.readValue(responseString, Car.class);
        Car recentlyAdded = carRepository.findById(saved.getId()).get();
        Assertions.assertEquals("Audi", recentlyAdded.getBrand());
        Assertions.assertEquals("RS5", recentlyAdded.getModel());
        Assertions.assertEquals("petrol", recentlyAdded.getFuelType());
        Assertions.assertEquals(saved.getId(), recentlyAdded.getId());
        Assertions.assertTrue(recentlyAdded.getId() > 0);
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        Car carToDelete = carRepository.saveAndFlush(new Car("Mercedes", "C63", "petrol"));
        postman.perform(delete("/api/v1/cars/" + carToDelete.getId()))
                .andExpect(status().isNoContent());
        boolean carExists = carRepository.existsById(carToDelete.getId());
        Assertions.assertFalse(carExists, "The car should be deleted from the list");
    }

    @Test
    public void shouldEditCar() throws Exception {
        Car carToEdit = carRepository.saveAndFlush(new Car("Mercedes", "C63", "petrol"));
        CreateCarCommand command = new CreateCarCommand("BMW", "M6", "petrol");
        String json = obj.writeValueAsString(command);
        String responseString = postman.perform(put("/api/v1/cars/" + carToEdit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.model").value("M6"))
                .andExpect(jsonPath("$.fuelType").value("petrol"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Car saved = obj.readValue(responseString, Car.class);
        Car recentlyAdded = carRepository.findById(saved.getId()).get();
        Assertions.assertNotNull(recentlyAdded, "The car should exist in the list");
        Assertions.assertEquals("BMW", recentlyAdded.getBrand());
        Assertions.assertEquals("M6", recentlyAdded.getModel());
        Assertions.assertEquals("petrol", recentlyAdded.getFuelType());
        Assertions.assertTrue(recentlyAdded.getId() > 0);
    }

    @Test
    public void shouldEditCarPartially() throws Exception {
        Car carToDelete = carRepository.saveAndFlush(new Car("Mercedes", "C63", "petrol"));
        CreateCarCommand command = new CreateCarCommand(null, "M6", null);
        String json = obj.writeValueAsString(command);
        String responseString = postman.perform(patch("/api/v1/cars/" + carToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Mercedes"))
                .andExpect(jsonPath("$.model").value("M6"))
                .andExpect(jsonPath("$.fuelType").value("petrol"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Car saved = obj.readValue(responseString, Car.class);
        Car recentlyAdded = carRepository.findById(saved.getId()).get();
        Assertions.assertNotNull(recentlyAdded, "The car should exist in the list");
        Assertions.assertEquals(saved.getId(), recentlyAdded.getId());
        Assertions.assertEquals("Mercedes", recentlyAdded.getBrand());
        Assertions.assertEquals("M6", recentlyAdded.getModel());
        Assertions.assertEquals("petrol", recentlyAdded.getFuelType());

    }
}
