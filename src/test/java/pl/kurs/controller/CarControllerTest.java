package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class CarControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper obj;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    public void init() {
        carRepository.deleteAll();
    }

    @Test
    public void shouldReturnSingleCar() throws Exception {
        Car car = carRepository.saveAndFlush(new Car("Audi", "RS5", "petrol"));
        int id = car.getId();
        postman.perform(get("/api/v1/cars/" + id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.brand").value("Audi"))
               .andExpect(jsonPath("$.model").value("RS5"))
               .andExpect(jsonPath("$.fuelType").value("petrol"));
    }

    @Test
    public void shouldReturnAllCars() throws Exception {
//        carRepository.deleteAll();
        Car car1 = carRepository.saveAndFlush(new Car("BMW", "M3", "petrol"));
        Car car2 = carRepository.saveAndFlush(new Car("BMW", "M2", "petrol"));

        postman.perform(get("/api/v1/cars"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(car1.getId()))
               .andExpect(jsonPath("$[0].brand").value("BMW"))
               .andExpect(jsonPath("$[0].model").value("M3"))
               .andExpect(jsonPath("$[0].fuelType").value("petrol"))
               .andExpect(jsonPath("$[1].id").value(car2.getId()))
               .andExpect(jsonPath("$[1].brand").value("BMW"))
               .andExpect(jsonPath("$[1].model").value("M2"))
               .andExpect(jsonPath("$[1].fuelType").value("petrol"));
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
        Assertions.assertTrue(recentlyAdded.getId() > 0);
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        Car carToDelete = carRepository.saveAndFlush(new Car("Mercedes", "C63", "petrol"));
        int id = carToDelete.getId();

        postman.perform(delete("/api/v1/cars/" + id))
               .andExpect(status().isNoContent());

        boolean carExists = carRepository.findById(id).isPresent();
        Assertions.assertFalse(carExists, "The car should be deleted from the repository");
    }

    @Test
    public void shouldEditCar() throws Exception {
        Car carToEdit = carRepository.saveAndFlush(new Car("Mercedes", "C63", "petrol"));
        int id = carToEdit.getId();

        CreateCarCommand command = new CreateCarCommand("BMW", "M6", "petrol");
        String json = obj.writeValueAsString(command);

        postman.perform(put("/api/v1/cars/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.brand").value("BMW"))
               .andExpect(jsonPath("$.model").value("M6"))
               .andExpect(jsonPath("$.fuelType").value("petrol"));

        Car editedCar = carRepository.findById(id).get();
        Assertions.assertEquals("BMW", editedCar.getBrand());
        Assertions.assertEquals("M6", editedCar.getModel());
        Assertions.assertEquals("petrol", editedCar.getFuelType());
    }

    @Test
    public void shouldEditCarPartially() throws Exception {
        Car carToEdit = carRepository.saveAndFlush(new Car("Mercedes", "C63", "petrol"));
        int id = carToEdit.getId();

        CreateCarCommand command = new CreateCarCommand(null, "M6", null);
        String json = obj.writeValueAsString(command);

        postman.perform(patch("/api/v1/cars/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.brand").value("Mercedes"))
               .andExpect(jsonPath("$.model").value("M6"))
               .andExpect(jsonPath("$.fuelType").value("petrol"));

        Car editedCar = carRepository.findById(id).get();
        Assertions.assertEquals("Mercedes", editedCar.getBrand());
        Assertions.assertEquals("M6", editedCar.getModel());
        Assertions.assertEquals("petrol", editedCar.getFuelType());
    }
}

